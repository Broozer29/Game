package game.objects.enemies;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioManager;

public class Enemy extends GameObject {

    protected MissileManager missileManager = MissileManager.getInstance();
    protected AnimationManager animationManager = AnimationManager.getInstance();
    protected AudioManager audioManager = AudioManager.getInstance();
    protected Random random = new Random();

    protected EnemyStatus enemyStatus;
    protected EnemyEnums enemyType;

    protected List<Enemy> followingEnemies = new LinkedList<Enemy>(); //inherit from gameobject

    public Enemy (int x, int y, Point destination, Direction rotation, EnemyEnums enemyType, float scale,
                  PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
        super(x, y, scale);
        this.enemyType = enemyType;
        this.currentLocation = new Point(x, y);
        updateCurrentBoardBlock();
        this.lastBoardBlock = currentBoardBlock;
        initMovementConfiguration(currentLocation, destination, pathFinder, rotation, xMovementSpeed, yMovementSpeed);
        this.setFriendly(false);
        this.enemyStatus = EnemyStatus.IDLE;
    }

    //inherit from gameobject
    private void initMovementConfiguration (Point currentLocation, Point destination, PathFinder pathFinder,
                                            Direction rotation, int xMovementSpeed, int yMovementSpeed) {
        movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setCurrentLocation(currentLocation);
        movementConfiguration.setDestination(destination);
        movementConfiguration.setRotation(rotation);
        movementConfiguration.setXMovementSpeed(xMovementSpeed);
        movementConfiguration.setYMovementSpeed(yMovementSpeed);
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