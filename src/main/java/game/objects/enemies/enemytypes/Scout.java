package game.objects.enemies.enemytypes;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import game.objects.missiles.MissileCreator;
import game.objects.missiles.MissileTypeEnums;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Scout extends Enemy {
    public Scout (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(EnemyEnums.Scout.getDestructionImageEnum());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale / 1.5f);
        this.attackSpeed = this.enemyType.getAttackSpeed();
        this.damage = MissileTypeEnums.LaserBullet.getDamage();
    }

    @Override
    public void fireAction(){
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            if (!chargingUpAttackAnimation.isPlaying()) {
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile();
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }
    }

    private void shootMissile () {
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate, ImageEnums.LaserBullet
                , this.scale / 2);


        //Create missile movement attributes and create a movement configuration
        MissileTypeEnums missileType = MissileTypeEnums.LaserBullet;
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                missileType.getxMovementSpeed(), missileType.getyMovementSpeed(), missilePathFinder, movementPatternSize, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Scout Laser Bullet";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
//        missile.setAllowedVisualsToRotate(true);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.resetMovementPath();

        this.missileManager.addExistingMissile(missile);

    }
}