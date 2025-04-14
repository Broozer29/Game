package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Bomba extends Enemy {

    private List<Direction> missileDirections = new ArrayList<Direction>();

    public Bomba (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

        //Specialized behaviour configuration stuff
        this.initDirectionFromRotation();
        this.damage = 15;
        this.attackSpeed = 7;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
    }

    public void fireAction () {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            updateChargingAttackAnimationCoordination();
            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile();
                this.isAttacking = false;
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }
    }

    private void shootMissile () {
        for (Direction direction : missileDirections) {
            //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
            SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                    ImageEnums.Bomba_Missile, 0.25f);

            float movementSpeed = 2f;


            //Create missile movement attributes and create a movement configuration
            MissileEnums missileType = MissileEnums.BombaProjectile;
            PathFinder missilePathFinder = new RegularPathFinder();
            MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
            MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                    movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, direction
            );


            //Create remaining missile attributes and a missile configuration
            boolean isFriendly = false;
            int maxHitPoints = 35;
            int maxShields = 0;
            AudioEnums deathSound = null;
            boolean allowedToDealDamage = true;
            String objectType = "Bomba Missile";

            MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                    deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false,
                    true, true, false);


            //Create the missile and finalize the creation process, then add it to the manager and consequently the game
            Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
            missile.setOwnerOrCreator(this);
            missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
            missile.rotateGameObjectTowards(missile.getMovementConfiguration().getDestination().getX(), missile.getMovementConfiguration().getDestination().getY(), true);
            missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
            missile.resetMovementPath();
            missile.setAllowedVisualsToRotate(false);
            this.missileManager.addExistingMissile(missile);
        }
    }

    private void initDirectionFromRotation () {
        switch (this.movementConfiguration.getRotation()) {
            case DOWN:
                missileDirections.add(Direction.LEFT_DOWN);
                missileDirections.add(Direction.DOWN);
                missileDirections.add(Direction.RIGHT_DOWN);
                break;
            case LEFT:
            case LEFT_DOWN:
            case LEFT_UP:
                missileDirections.add(Direction.LEFT_DOWN);
                missileDirections.add(Direction.LEFT);
                missileDirections.add(Direction.LEFT_UP);
                break;
            case NONE:
                missileDirections.add(Direction.LEFT);
                break;
            case RIGHT:
            case RIGHT_DOWN:
            case RIGHT_UP:
                missileDirections.add(Direction.RIGHT_UP);
                missileDirections.add(Direction.RIGHT);
                missileDirections.add(Direction.RIGHT_DOWN);
                break;
            case UP:
                missileDirections.add(Direction.LEFT_UP);
                missileDirections.add(Direction.UP);
                missileDirections.add(Direction.RIGHT_UP);
                break;
            default:
                missileDirections.add(Direction.LEFT);
                break;

        }
    }
}