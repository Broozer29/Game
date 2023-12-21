package game.objects.enemies;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import game.objects.missiles.MissileManager;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class Enemy extends GameObject {

    protected MissileManager missileManager = MissileManager.getInstance();
    protected Random random = new Random();

    protected EnemyEnums enemyType;

    protected List<Enemy> followingEnemies = new LinkedList<Enemy>(); //inherit from gameobject

//    public Enemy (int x, int y, Point destination, Direction rotation, EnemyEnums enemyType, float scale,
//                  PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
//        super(x, y, scale);
//        this.enemyType = enemyType;
//        this.currentLocation = new Point(x, y);
//        updateCurrentBoardBlock();
//        this.lastBoardBlock = currentBoardBlock;
//        initMovementConfiguration(currentLocation, destination, pathFinder, rotation, xMovementSpeed, yMovementSpeed);
//        this.setFriendly(false);
//        this.allowedToDealDamage = true;
//    }

    public Enemy(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration){
        super(spriteConfiguration);
        configureEnemy(enemyConfiguration);
    }

    public Enemy(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration){
        super(spriteAnimationConfigurationion);
        configureEnemy(enemyConfiguration);
    }

    private void configureEnemy(EnemyConfiguration enemyConfiguration){
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
        updateCurrentBoardBlock();
        this.lastBoardBlock = currentBoardBlock;
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
    }


    // Random offset for the origin of the missile the enemy shoots

    public EnemyEnums getEnemyType () {
        return this.enemyType;
    }

    public void fireAction () {
        // This could contain default behaviour but SHOULD be overriden by specific enemytype
        // classes.
    }


}