package game.objects.enemies;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import game.movement.BoardBlockUpdater;
import game.movement.MovementConfiguration;
import game.movement.Point;
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

        initMovementConfiguration(enemyConfiguration);
        this.setVisible(true);
        this.setFriendly(false);
        this.rotateGameObject(movementDirection);
        this.objectType = enemyConfiguration.getObjectType();
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

        for (GameObject object : objectsFollowingThis) {
            object.deleteObject();
        }

        for (GameObject object : objectOrbitingThis) {
            object.deleteObject();
        }

        for (Enemy enemy : followingEnemies) {
            enemy.deleteObject();
        }


        this.visible = false;
    }


}