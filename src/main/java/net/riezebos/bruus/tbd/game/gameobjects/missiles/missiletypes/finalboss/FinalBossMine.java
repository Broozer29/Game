package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FinalBossMine extends Missile {

    private int rangeThreshold = 150;
    private boolean activated = false;
    private double gameTimeCharged = 0;
    private double timeLastSpedUp = 0;

    public FinalBossMine(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.setFrameDelay(5);
        this.isDamageable = true;
        this.isDestructable = false;
        this.allowedVisualsToRotate = false;
        this.timeLastSpedUp = GameState.getInstance().getGameSeconds();

        rangeThreshold = 150;

        if (missileConfiguration.getDestructionType() != null) {
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
            this.destructionAnimation.setAnimationScale(0.75f);
        }
    }

    public void missileAction() {
        if(this.movementConfiguration.getMovementSpeed() > 0.1f) {
            this.movementConfiguration.setMovementSpeed(this.getMovementConfiguration().getMovementSpeed() * 0.99f);
        } else if(this.isAllowedToMove()){
            this.setAllowedToMove(false);
            this.gameTimeCharged = GameState.getInstance().getGameSeconds();
        }


        //call this once every X ticks to save performance
        if (GameState.getInstance().getGameTicksExecuted() % 5 == 0) {
            if (!activated && CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), rangeThreshold)) {
                detonateMissile();
                this.activated = true;
            }

            if(!activated && GameState.getInstance().getGameSeconds() - timeLastSpedUp > 1.5f && this.animation.getFrameDelay() > 0) {
                int newFrameDelay = Math.max(0, this.animation.getFrameDelay() - 1);
                this.animation.setFrameDelay(newFrameDelay);
                timeLastSpedUp = GameState.getInstance().getGameSeconds();
            }

            if(!activated && this.animation.getFrameDelay() == 0 && GameState.getInstance().getGameSeconds() - timeLastSpedUp > 1.5f){
                detonateMissile();
            }
        }
    }

    public void detonateMissile() {
        createMissiles();
        this.setVisible(false);
    }

    private static int ANGLE_INCREMENT = 60;
    private void createMissiles() {
        for (int angle = 0; angle <= (360 - ANGLE_INCREMENT); angle += ANGLE_INCREMENT) {
            // Directly call shootMissiles using current angle
            shootMissiles(angle);
        }
    }


    @Override
    public void triggerOnDeathActions() {
        createMissiles();
    }

    @Override
    public boolean isShowHealthBar(){
        return false;
    }

    private void shootMissiles(double angleDegrees) {
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                ImageEnums.LaserBullet, 0.4f);


        float movementSpeed = 1.5f;
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                this.getOwnerOrCreator().getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);


        //Calculate the angle based on the current chargingAnimation. Because we want to fire from 4 directions, we also need to keep
        //track of the angle that the given chargingAnimation has in this method
        Point bulletOrigin = new Point(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        Point bulletDestination = calculateBulletDestination(angleDegrees, 400, this.getCenterXCoordinate(), this.getCenterYCoordinate());

        missile.resetMovementPath();

        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.getMovementConfiguration().setDestination(bulletDestination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(bulletOrigin.getX(), bulletOrigin.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Point calculateBulletDestination(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }
}