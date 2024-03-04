package game.objects.enemies;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.movement.BoardBlockUpdater;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
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

    protected List<Enemy> followingEnemies = new LinkedList<Enemy>(); //inherit from gameobject

    public Enemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
        super(spriteConfiguration);
        configureEnemy(enemyConfiguration);
        initChargingUpAnimation(spriteConfiguration);
    }

    public Enemy (SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration) {
        super(spriteAnimationConfigurationion);
        configureEnemy(enemyConfiguration);
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
        this.movementDirection = enemyConfiguration.getMovementDirection();
        this.currentLocation = new Point(xCoordinate, yCoordinate);
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = enemyConfiguration.isBoxCollision();
        this.baseArmor = enemyConfiguration.getBaseArmor();
        this.cashMoneyWorth = enemyConfiguration.getCashMoneyWorth();
        this.xpOnDeath = enemyConfiguration.getXpOnDeath();
        modifyStatsBasedOnLevel();
        initMovementConfiguration(enemyConfiguration);
        this.setVisible(true);
        this.setFriendly(false);
        this.rotateGameObjectTowards(movementDirection);
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


    //inherit from gameobject
    private void initMovementConfiguration (EnemyConfiguration enemyConfiguration) {
        PathFinder pathFinder = enemyConfiguration.getMovementPathFinder();
        movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setCurrentLocation(currentLocation);
        movementConfiguration.setDestination(pathFinder.calculateInitialEndpoint(currentLocation, movementDirection, false));
        movementConfiguration.setRotation(enemyConfiguration.getMovementDirection());
        movementConfiguration.setXMovementSpeed(enemyConfiguration.getxMovementSpeed());
        movementConfiguration.setYMovementSpeed(enemyConfiguration.getyMovementSpeed());
        movementConfiguration.setStepsTaken(0);
        movementConfiguration.setHasLock(true);

        if (pathFinder instanceof HomingPathFinder) {
            movementConfiguration.setTarget(((HomingPathFinder) pathFinder).getTarget(isFriendly(), this.xCoordinate, this.yCoordinate));
        }

        //hardcoded lol, should be in the config file, make a config file factory cause this getting out of hand a lil, too many variables for memory alone
        movementConfiguration.setDiamondWidth(enemyConfiguration.getMovementPatternSize().getDiamondWidth());
        movementConfiguration.setDiamondHeight(enemyConfiguration.getMovementPatternSize().getDiamondHeight());
        movementConfiguration.setStepsBeforeBounceInOtherDirection(enemyConfiguration.getMovementPatternSize().getStepsBeforeBounceInOtherDirection());

        movementConfiguration.setAngleStep(0.1);
        movementConfiguration.setCurveDistance(1);
        movementConfiguration.setRadius(5);
        movementConfiguration.setRadiusIncrement(enemyConfiguration.getMovementPatternSize().getRadiusIncrement());

        movementConfiguration.setPrimaryDirectionStepAmount(enemyConfiguration.getMovementPatternSize().getPrimaryDirectionStepAmount());
        movementConfiguration.setFirstDiagonalDirectionStepAmount(enemyConfiguration.getMovementPatternSize().getSecondaryDirectionStepAmount());
        movementConfiguration.setSecondDiagonalDirectionStepAmount(enemyConfiguration.getMovementPatternSize().getSecondaryDirectionStepAmount());

    }


    // Random offset for the origin of the missile the enemy shoots

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

        for (Enemy enemy : followingEnemies) {
            enemy.deleteObject();
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