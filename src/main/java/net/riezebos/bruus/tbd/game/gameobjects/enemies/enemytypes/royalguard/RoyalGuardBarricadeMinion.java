package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;


/*

        to do's
                - royal barricade moet deze minions spawnen en bijhouden. Als de barricade sterft, deze minion ook
        - deze minion vliegt rondom de parent barricade
        - wanneer een speler dichtbij genoeg is, schiet los van de barricade en ga regelrecht af op de speler

 */
public class RoyalGuardBarricadeMinion extends Enemy {
    private boolean activated = false;
    private boolean warmingUp = true;
    private double spawnedTime = 0;
    private boolean increasedInSpeed = false;
    private int detectionRange = 145;
    private boolean isAttached = true;

    public RoyalGuardBarricadeMinion(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.damage = 10;
        this.attackSpeed = 100000; //not used
        this.knockbackStrength = 12;
        this.detonateOnCollision = true;
        spawnedTime = GameState.getInstance().getGameSeconds();
    }


    @Override
    public void fireAction() {

        if(spawnedTime + 1 < GameState.getInstance().getGameSeconds()){
            warmingUp = false; //1 sec grace period after spawning
        }

        if (this.ownerOrCreator != null && (this.ownerOrCreator.getCurrentHitpoints() <= 0 || !this.ownerOrCreator.isVisible())) {
            this.takeDamage(99999);
        }

        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            updateMovementPath();
        }

        if (!activated && CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), detectionRange)) {
            engageTarget();
        }
    }


    private int minDistanceFromParent = 40;
    private int maxDistanceFromParent = 200;

    public void updateMovementPath() {
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
        this.setAllowedVisualsToRotate(true);
        this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint(this.ownerOrCreator, minDistanceFromParent, maxDistanceFromParent));
    }

    private void engageTarget() {
        if (!activated) {
            OnScreenTextManager.getInstance().addText("!", this.getCenterXCoordinate(), this.getCenterYCoordinate(), 25);
            if(!increasedInSpeed) {
                if (this.movementConfiguration.getMovementSpeed() < 1) {
                    this.movementConfiguration.setMovementSpeed(1);
                } else {
                    this.movementConfiguration.setMovementSpeed(Math.min(this.movementConfiguration.getMovementSpeed() * 2.25f, 3.5f));
                }
            }

            SpaceShip spaceship = PlayerManager.getInstance().getClosestSpaceShip(this);
            Point destination = new Point(
                    spaceship.getCenterXCoordinate() - this.getWidth() / 2,
                    spaceship.getCenterYCoordinate() - this.getHeight() / 2);
            this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
            this.movementConfiguration.resetMovementPath();
            this.movementConfiguration.setDestination(destination);
            this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
            this.allowedVisualsToRotate = true;
            this.rotateObjectTowardsPoint(destination, false);
            activated = true;
            increasedInSpeed = true;
            warmingUp = false;
            isAttached = false;
        }
    }

    public void throwAtTarget(GameObject target) {
        OnScreenTextManager.getInstance().addText("!", this.getCenterXCoordinate(), this.getCenterYCoordinate(), 25);
        if (this.movementConfiguration.getMovementSpeed() < 1) {
            this.movementConfiguration.setMovementSpeed(1);
        } else {
            this.movementConfiguration.setMovementSpeed(Math.min(this.movementConfiguration.getMovementSpeed() * 2.25f, 3.5f));
        }

        Point destination = new Point(
                target.getCenterXCoordinate() - this.getWidth() / 2,
                target.getCenterYCoordinate() - this.getHeight() / 2);
        this.movementConfiguration.setDestination(destination);
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
        this.allowedVisualsToRotate = true;
        this.rotateObjectTowardsPoint(destination, false);
        increasedInSpeed = true;
        warmingUp = false;
        isAttached = false;
    }

    public boolean isWarmingUp() {
        return warmingUp;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean attached) {
        isAttached = attached;
    }
}
