package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.HashSet;
import java.util.Set;

public class SpawnProjectileOnDeath implements EffectInterface {

    private EffectIdentifiers effectIdentifier;

    private int amountOfMissiles;
    private ImageEnums missileImage;
    private MissileEnums missileType;
    private boolean hasActivated = false;
    private ImageEnums missileImpactImage;

    public SpawnProjectileOnDeath (EffectActivationTypes effectActivationType, EffectIdentifiers effectIdentifier,
                                   int amountOfProjectiles, MissileEnums missileType, ImageEnums missileImage, ImageEnums missileImpactImage) {
        this.effectIdentifier = effectIdentifier;
        this.amountOfMissiles = amountOfProjectiles;
        this.missileType = missileType;
        this.missileImage = missileImage;
        this.missileImpactImage = missileImpactImage;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !hasActivated) {
            Set<Direction> usedDirections = new HashSet<>();

            for (int i = 0; i < amountOfMissiles; i++) {
                Direction direction;
                int attempts = 0;
                do {
                    direction = Direction.getRandomDiagonalDirection();
                    attempts++;
                } while (usedDirections.contains(direction) && attempts < 8);

                usedDirections.add(direction);

                Missile missile = createMissile(gameObject, direction);
                missile.setOwnerOrCreator(gameObject);
                missile.setDestructable(true);

                if (missileImage.equals(ImageEnums.MutaliskMissile)) {
                    missile.getAnimation().setFrameDelay(6);
                    missile.setScale(1.2f);
                }
                MissileManager.getInstance().addExistingMissile(missile);
            }
            hasActivated = true;
        }
    }



    private Missile createMissile (GameObject gameObject, Direction direction) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(gameObject.getScale());
        spriteConfiguration.setImageType(this.missileImage);

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(
                this.missileType, 40, 0, null, gameObject.getDamage(), missileImpactImage,
                gameObject.isFriendly(), true, "Spawn On Death missile " + missileType, missileType.isUsesBoxCollision(), false
                , true, false
        );

        BouncingPathFinder bouncingPathFinder = new BouncingPathFinder();
        bouncingPathFinder.setMaxBounces(1);

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                2, 2, bouncingPathFinder, MovementPatternSize.SMALL, direction
        );

        return MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return EffectActivationTypes.OnObjectDeath;
    }

    @Override
    public void resetDuration () {
        //Not needed
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
//Not needed
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return this.effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject) {

    }

    @Override
    public EffectInterface copy () {
        return null;
    }

    public ImageEnums getMissileImage () {
        return missileImage;
    }

    public void setMissileImage (ImageEnums missileImage) {
        this.missileImage = missileImage;
    }

    public MissileEnums getMissileType () {
        return missileType;
    }

    public void setMissileType (MissileEnums missileType) {
        this.missileType = missileType;
    }
}
