package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FinalBossOrbitMissile extends Missile {

    private GameObject target;
    private static final int orbitStage = 0;
    private static final int moveBackStage = 1;
    private static final int chargeToTargetStage = 2;
    private static final int finished = 3;

    private int currentStage = 0;
    private double timeLastStepFinished = 0;
    private double timeToOrbit = 2;
    private double timeToMoveBackSlightly = 0.35f;
    private double timeAllowedToCharge = 0.5f;

    public FinalBossOrbitMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
        initDestructionAnimation(missileConfiguration, movementConfiguration);
        this.isDamageable = false;
        this.isDestructable = true;
        this.currentStage = orbitStage;
        this.allowedVisualsToRotate = false;
    }

    private void initDestructionAnimation(MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        if (missileConfiguration.getDestructionType() != null) {
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);

            if (this.destructionAnimation.getImageEnum().equals(ImageEnums.LaserBulletDestruction)) {
                this.destructionAnimation.rotateAnimation(movementConfiguration.getRotation(), false);
                this.destructionAnimation.setFrameDelay(1);
            }
        }
    }


    public void missileAction() {
        if(this.currentStage == orbitStage){
            if(GameState.getInstance().getGameSeconds() - timeLastStepFinished > timeToOrbit){
                currentStage = moveBackStage;
                timeLastStepFinished = GameState.getInstance().getGameSeconds();
                chargeAwayFromTarget();
            }
        }

        if(currentStage == moveBackStage){
            if(GameState.getInstance().getGameSeconds() - timeLastStepFinished > timeToMoveBackSlightly){
                currentStage = chargeToTargetStage;
                timeLastStepFinished = GameState.getInstance().getGameSeconds();
                chargeToTarget();
            }
        }

        if(currentStage == chargeToTargetStage){
            if(GameState.getInstance().getGameSeconds() - timeLastStepFinished > timeAllowedToCharge){
                this.setTransparancyAlpha(true, 1, -0.04f);
                currentStage = finished;
            }
        }

    }

    private void chargeAwayFromTarget(){
        float thisCenterXCoordinate = this.getCenterXCoordinate();
        float thisCenterYCoordinate = this.getCenterYCoordinate();

        float targetCenterXCoordinate = target.getCenterXCoordinate();
        float targetCenterYCoordinate = target.getCenterYCoordinate();

        // Calculate the angle between this missile and the target
        float deltaX = thisCenterXCoordinate - targetCenterXCoordinate;
        float deltaY = thisCenterYCoordinate - targetCenterYCoordinate;
        double angle = Math.atan2(deltaY, deltaX);

        // Calculate a point 100 pixels away from the target using the calculated angle
        float distance = 200;
        float newX = (targetCenterXCoordinate + (float)(Math.cos(angle) * distance)) - this.getWidth() / 2;
        float newY = (targetCenterYCoordinate + (float)(Math.sin(angle) * distance)) - this.getHeight() / 2;

        Point point = new Point(newX, newY);

        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setMovementSpeed(2f); //todo placeholder speed, should be variable if this is to be reused
        this.movementConfiguration.setDestination(point);
        this.movementConfiguration.setDirection(Direction.RIGHT);
        this.movementConfiguration.setCurrentLocation(new Point(thisCenterXCoordinate, thisCenterYCoordinate));
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());

    }

    private void chargeToTarget(){
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setMovementSpeed(7); //todo placeholder speed, should be variable if this is to be reused
        this.movementConfiguration.setDestination(target.getCurrentCenterLocation());
        this.movementConfiguration.setDirection(Direction.RIGHT);
        this.movementConfiguration.setCurrentLocation(new Point(this.getCenterXCoordinate(), this.getCenterYCoordinate()));
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
    }

    public void setTimeLastStepFinished(double timeLastStepFinished) {
        this.timeLastStepFinished = timeLastStepFinished;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }
}
