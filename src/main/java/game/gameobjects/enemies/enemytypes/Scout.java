package game.gameobjects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.MovementPatternSize;
import game.movement.pathfinders.HoverPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.missiles.Missile;
import game.gameobjects.missiles.MissileConfiguration;
import game.gameobjects.missiles.MissileCreator;
import game.gameobjects.missiles.MissileEnums;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Scout extends Enemy {
    public Scout (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale / 1.5f);
        this.attackSpeed = 5;
        this.damage = 10;
        this.detonateOnCollision = false;
        this.knockbackStrength = 5;


        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
            HoverPathFinder pathFinder = (HoverPathFinder) this.movementConfiguration.getPathFinder();
            movementConfiguration.setBoardBlockToHoverIn(7);
            pathFinder.setShouldDecreaseBoardBlock(true);
            pathFinder.setDecreaseBoardBlockAmountBy(2);
        }
    }

    @Override
    public void fireAction(){
        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder){
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }


        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (allowedToFire && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
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
        MissileEnums missileType = MissileEnums.ScoutLaserBullet;

        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate, missileType.getImageType()
                , this.scale);

        int movementSpeed = 3;

        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed,movementSpeed, missilePathFinder, movementPatternSize, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 0;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Scout Laser Bullet";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
//        missile.setSpeedsUp(true);
//        missile.setAllowedVisualsToRotate(true);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.rotateGameObjectTowards(missile.getMovementConfiguration().getDestination().getX(), missile.getMovementConfiguration().getDestination().getY(), true);
        missile.setCenterXCoordinate(chargingUpAttackAnimation.getCenterXCoordinate());
        missile.setCenterYCoordinate(chargingUpAttackAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false);
        missile.resetMovementPath();

        this.missileManager.addExistingMissile(missile);
    }
}
