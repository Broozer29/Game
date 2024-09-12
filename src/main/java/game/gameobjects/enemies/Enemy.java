package game.gameobjects.enemies;

import java.util.Random;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.enums.EnemyCategory;
import game.gameobjects.player.PlayerManager;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatsTracker;
import game.movement.PathFinderEnums;
import game.movement.pathfinders.DestinationPathFinder;
import game.util.BoardBlockUpdater;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.gameobjects.GameObject;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.missiles.MissileManager;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class Enemy extends GameObject {

    protected MissileManager missileManager = MissileManager.getInstance();
    protected Random random = new Random();
    protected boolean allowedToFire;
    protected boolean isAttacking;

    protected EnemyEnums enemyType;
    protected PathFinderEnums missileTypePathFinders;
    protected double lastAttackTime = 0.0;

    protected boolean detonateOnCollision;

    public Enemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, movementConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
        configureEnemy(enemyConfiguration);
        initChargingUpAnimation(spriteConfiguration);
    }

    public Enemy (SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, movementConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
        configureEnemy(enemyConfiguration);
        initChargingUpAnimation(spriteAnimationConfigurationion.getSpriteConfiguration());

    }

    private void configureEnemy (EnemyConfiguration enemyConfiguration) {
        this.enemyType = enemyConfiguration.getEnemyType();
        this.maxHitPoints = enemyConfiguration.getMaxHitPoints();
        this.currentHitpoints = maxHitPoints;
        this.maxShieldPoints = enemyConfiguration.getMaxShields();
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
        this.isAttacking = false;
        modifyStatsBasedOnLevel();
        this.setVisible(true);
        this.setFriendly(false);
        this.rotateGameObjectTowards(movementRotation, true);
        this.objectType = enemyConfiguration.getObjectType();
        this.allowedToFire = true;
    }

    private void modifyStatsBasedOnLevel () {
        int enemyLevel = GameStateInfo.getInstance().getMonsterLevel();
        float difficultyCoeff = GameStateInfo.getInstance().getDifficultyCoefficient();

        if (enemyLevel > 1) {
            this.maxHitPoints *= (float) Math.pow(1.15, enemyLevel);
            this.currentHitpoints = maxHitPoints;
            this.maxShieldPoints *= (float) Math.pow(1.15, enemyLevel);
            this.currentShieldPoints = maxShieldPoints;
            this.damage *= (float) Math.pow(1.15, enemyLevel);
            // XP on death is multiplied by 50% of difficultyCoeff
            this.xpOnDeath *= (float) (1 + (0.5 * difficultyCoeff));
            // Cash money worth is multiplied by 50% of difficultyCoeff
            this.cashMoneyWorth *= (float) (1 + (0.5 * difficultyCoeff));
        }
    }

    private void initChargingUpAnimation (SpriteConfiguration spriteConfiguration) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Charging);
        this.chargingUpAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    public void triggerClassSpecificOnDeathTriggers () {
        if(!this.enemyType.getEnemyCategory().equals(EnemyCategory.Summon)) {
            GameStatsTracker.getInstance().addEnemyKilled(1);
        }
    }

    public EnemyEnums getEnemyType () {
        return this.enemyType;
    }

    public void fireAction () {
        // This could contain default behaviour but SHOULD be overriden by specific enemytype
        // classes.
    }

    public void rotateAfterMovement () {
        updateChargingAttackAnimationCoordination();
        if (!this.allowedVisualsToRotate) {
            return;
        }

        // Check for specific missile type pathfinders
        if (this.getMissileTypePathFinders() == PathFinderEnums.Homing
                || this.getMissileTypePathFinders() == PathFinderEnums.StraightLine) {
            // Rotate towards the player, assuming these are only used for enemies that aim
            Point point = new Point(
                    PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                    PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
            rotateObjectTowardsPoint(point, true);
            updateChargingAttackAnimationCoordination();
            return;
        }

        if (this.getEnemyType().equals(EnemyEnums.Alien_Bomb)) {
            if (this.getOwnerOrCreator() != null) {
                this.rotateGameObjectTowards(this.getOwnerOrCreator().getMovementConfiguration().getRotation(), true);
                this.setAllowedVisualsToRotate(false);
            }
            return;
        }


        if (!movementConfiguration.getCurrentPath().getWaypoints().isEmpty()) {
            rotateObjectTowardsDestination(true);
            setAllowedVisualsToRotate(false);
        } else if (!(movementConfiguration.getPathFinder() instanceof DestinationPathFinder)) {
                System.out.println("I've encountered an empty waypoints, this shouldnt happen outside explicit testing -> Enemy.java, line 145");
                setAllowedVisualsToRotate(false);

        }
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
                this.currentHitpoints > 1 &&
                !this.destructionAnimation.isPlaying()) {
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
        this.ownerOrCreator = null;
        this.visible = false;
    }

    public SpriteAnimation getChargingUpAttackAnimation () {
        return chargingUpAttackAnimation;
    }

    public void setChargingUpAttackAnimation (SpriteAnimation chargingUpAttackAnimation) {
        this.chargingUpAttackAnimation = chargingUpAttackAnimation;
    }

    public PathFinderEnums getMissileTypePathFinders () {
        return missileTypePathFinders;
    }

    public void setMissileTypePathFinders (PathFinderEnums missileTypePathFinders) {
        this.missileTypePathFinders = missileTypePathFinders;
    }

    public boolean isAllowedToFire () {
        return allowedToFire;
    }

    public void setAllowedToFire (boolean allowedToFire) {
        this.allowedToFire = allowedToFire;
    }

    public double getLastAttackTime () {
        return lastAttackTime;
    }

    public void setLastAttackTime (double lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public boolean isAttacking () {
        return isAttacking;
    }

    public void setAttacking (boolean attacking) {
        isAttacking = attacking;
    }
}