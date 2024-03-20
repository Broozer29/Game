package game.objects.enemies;

import java.util.Random;

import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.util.BoardBlockUpdater;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.GameObject;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.missiles.MissileManager;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Enemy extends GameObject {

    protected MissileManager missileManager = MissileManager.getInstance();
    protected Random random = new Random();

    protected EnemyEnums enemyType;

    public Enemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, movementConfiguration);
        configureEnemy(enemyConfiguration);
        initChargingUpAnimation(spriteConfiguration);
    }

    public Enemy (SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, movementConfiguration);
        configureEnemy(enemyConfiguration);
        initChargingUpAnimation(spriteAnimationConfigurationion.getSpriteConfiguration());
    }

    private void configureEnemy (EnemyConfiguration enemyConfiguration) {
        this.enemyType = enemyConfiguration.getEnemyType();
        this.maxHitPoints = enemyConfiguration.getMaxHitPoints();
        this.currentHitpoints = maxHitPoints;
        this.maxShieldPoints = enemyConfiguration.getMaxShields();
        this.attackSpeed = enemyConfiguration.getAttackSpeed();
        this.currentShieldPoints = maxShieldPoints;
        this.hasAttack = enemyConfiguration.isHasAttack();
        this.showHealthBar = enemyConfiguration.isShowHealthBar();
        this.deathSound = enemyConfiguration.getDeathSound();
        this.currentLocation = new Point(xCoordinate, yCoordinate);
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = enemyConfiguration.isBoxCollision();
        this.baseArmor = enemyConfiguration.getBaseArmor();
        this.cashMoneyWorth = enemyConfiguration.getCashMoneyWorth();
        this.xpOnDeath = enemyConfiguration.getXpOnDeath();
        modifyStatsBasedOnLevel();
        this.setVisible(true);
        this.setFriendly(false);
        this.rotateGameObjectTowards(movementRotation);
        this.objectType = enemyConfiguration.getObjectType();
    }

    private void modifyStatsBasedOnLevel () {
        int enemyLevel = GameStateInfo.getInstance().getMonsterLevel();
        float difficultyCoeff = GameStateInfo.getInstance().getDifficultyCoefficient();

        if (enemyLevel > 1) {
            this.maxHitPoints *= (float) Math.pow(1.20, enemyLevel);
            this.currentHitpoints = maxHitPoints;
            this.maxShieldPoints *= (float) Math.pow(1.20, enemyLevel);
            this.currentShieldPoints = maxShieldPoints;
            this.damage *= (float) Math.pow(1.20, enemyLevel);
            // XP on death is multiplied by 50% of difficultyCoeff
            this.xpOnDeath *= (float) (1 + 0.5 * difficultyCoeff);
            // Cash money worth is multiplied by 100% (double) of difficultyCoeff
            this.cashMoneyWorth *= (1 + difficultyCoeff);
        }
    }

    private void initChargingUpAnimation (SpriteConfiguration spriteConfiguration) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Charging);
        this.chargingUpAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }



    public EnemyEnums getEnemyType () {
        return this.enemyType;
    }

    public void fireAction () {
        // This could contain default behaviour but SHOULD be overriden by specific enemytype
        // classes.
    }

    public void onCreationEffects () {
        //Exist to be overriden
    }

    public void onDeathEffects () {
        //exist to be overriden
    }

    public void deleteObject () {
        if (this.animation != null) {
            this.animation.setVisible(false);
        }

        for (SpriteAnimation spriteAnimation : effectAnimations) {
            spriteAnimation.setVisible(false);
        }

        if (this.exhaustAnimation != null) {
            this.exhaustAnimation.setVisible(false);
        }

        if (this.shieldDamagedAnimation != null) {
            this.shieldDamagedAnimation.setVisible(false);
        }

        if (this.destructionAnimation != null &&
                this.currentHitpoints > 1) {
            this.destructionAnimation.setVisible(false);
        }

        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setVisible(false);
        }

        for (GameObject object : objectsFollowingThis) {
            object.deleteObject();
        }

        for (GameObject object : objectOrbitingThis) {
            object.deleteObject();
        }

        this.objectToFollow = null;

        if (movementConfiguration != null) {
            this.movementConfiguration.deleteConfiguration();
        }
        this.movementConfiguration = null;
        this.movementTracker = null;
        this.ownerOrCreator = null;
        this.visible = false;
    }

    public SpriteAnimation getChargingUpAttackAnimation () {
        return chargingUpAttackAnimation;
    }

    public void setChargingUpAttackAnimation (SpriteAnimation chargingUpAttackAnimation) {
        this.chargingUpAttackAnimation = chargingUpAttackAnimation;
    }
}