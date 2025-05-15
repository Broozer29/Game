package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SpawnProjectileOnDeath implements EffectInterface {

    private static Random random = new Random();
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectActivationType;
    private int amountOfMissiles;
    private ImageEnums missileImage;
    private MissileEnums missileType;
    private boolean hasActivated = false;
    private ImageEnums missileImpactImage;

    public SpawnProjectileOnDeath (EffectActivationTypes effectActivationType, EffectIdentifiers effectIdentifier,
                                   int amountOfProjectiles, MissileEnums missileType, ImageEnums missileImage, ImageEnums missileImpactImage) {
        this.effectActivationType = effectActivationType;
        this.effectIdentifier = effectIdentifier;
        this.amountOfMissiles = amountOfProjectiles;
        this.missileType = missileType;
        this.missileImage = missileImage;
        this.missileImpactImage = missileImpactImage;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !hasActivated) {

            for (int i = 0; i < amountOfMissiles; i++) {
                Missile missile = createMissile(gameObject);
                missile.setOwnerOrCreator(gameObject);
                missile.setDestructable(true);
                missile.setDamageable(true);
                missile.setMaxHitPoints(50);
                missile.setCurrentHitpoints(50);

                if (missileImage.equals(ImageEnums.MutaliskMissile)) {
                    missile.getAnimation().setFrameDelay(6);
                    missile.setScale(1.4f);
                    missile.setKnockbackStrength(6);
                }
                MissileManager.getInstance().addExistingMissile(missile);
            }
            hasActivated = true;
        }
    }



    private Missile createMissile (GameObject gameObject) {
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

        StraightLinePathFinder straightLinePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                2, 2, straightLinePathFinder, MovementPatternSize.SMALL, Direction.LEFT
        );
        movementConfiguration.setDestination(getRandomPoint(gameObject, 200, 200));

        return MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
    }

    private Point getRandomPoint(GameObject selectedObject, int minDistance, int maxDistance) {
        int xCoordinate = selectedObject.getCenterXCoordinate();
        int yCoordinate = selectedObject.getCenterYCoordinate();

        double angle = random.nextDouble() * 2 * Math.PI; // Random angle in radians
        int distance = minDistance + random.nextInt(maxDistance - minDistance + 1); // Random distance within range

        int newX = xCoordinate + (int) (Math.cos(angle) * distance);
        int newY = yCoordinate + (int) (Math.sin(angle) * distance);

        return new Point(newX, newY);
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
        return this.effectActivationType;
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
