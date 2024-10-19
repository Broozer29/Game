package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.managers.AnimationManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Tazer extends Enemy {

    private List<Direction> missileDirections = new ArrayList<>();
    private Direction randomDirection = null;

    public Tazer (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.attackSpeed = 6f;
        this.damage = 15;
        initDirectionFromRotation();
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
    }


    public void fireAction () {
        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }

        boolean fired = false;
        // Check if the attack cooldown has been reached
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
        && allowedToFire) {
            if(randomDirection == null) {
                randomDirection = selectRandomMissileDirection();
            }
            this.rotateGameObjectTowards(randomDirection, false);
            this.allowedVisualsToRotate = false;

            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile(randomDirection);
                this.isAttacking = false;
                lastAttackTime = currentTime; // Update the last attack time after firing
                randomDirection = null;
                fired = true;
            }
        }

        if(fired){
            this.allowedVisualsToRotate = true;
        }
    }


    private void shootMissile (Direction randomDirection) {
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate, MissileEnums.TazerProjectile.getImageType()
                ,1);



        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.TazerProjectile;
        BouncingPathFinder missilePathFinder = new BouncingPathFinder();
        missilePathFinder.setMaxBounces(3);
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                2,2, missilePathFinder, movementPatternSize, randomDirection
        );

        this.rotateGameObjectTowards(randomDirection, false);
        this.allowedVisualsToRotate = false;

        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Tazer Projectile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
//        missile.getDestructionAnimation().setFrameDelay(1);
//        missile.getDestructionAnimation().setAnimationScale(2f);
        missile.setOwnerOrCreator(this);
        missile.getAnimation().setAnimationScale(1f);
        missile.setAllowedVisualsToRotate(false);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.resetMovementPath();

        this.missileManager.addExistingMissile(missile);
    }


    private void initDirectionFromRotation() {
        switch (this.movementConfiguration.getRotation()) {
            case DOWN:
                missileDirections.add(Direction.LEFT_DOWN);
//                missileDirections.add(Direction.DOWN);
                missileDirections.add(Direction.RIGHT_DOWN);
                break;
            case LEFT:
            case LEFT_DOWN:
            case LEFT_UP:
                missileDirections.add(Direction.LEFT_DOWN);
//                missileDirections.add(Direction.LEFT);
                missileDirections.add(Direction.LEFT_UP);
                break;
            case RIGHT:
            case RIGHT_DOWN:
            case RIGHT_UP:
                missileDirections.add(Direction.RIGHT_UP);
//                missileDirections.add(Direction.RIGHT);
                missileDirections.add(Direction.RIGHT_DOWN);
                break;
            case UP:
                missileDirections.add(Direction.LEFT_UP);
//                missileDirections.add(Direction.UP);
                missileDirections.add(Direction.RIGHT_UP);
                break;
            default:
                missileDirections.add(Direction.LEFT);
                break;

        }
    }

    private Direction selectRandomMissileDirection(){
        return missileDirections.get(random.nextInt(missileDirections.size()));
    }
}