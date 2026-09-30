package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FinalBossSpreadMissile extends Missile {

    private GameObject target = null;
    private boolean gettingIntoPlace = true;
    private boolean movingToPlayer = false;
    private boolean finishedMovingToPlayer = false;
    private int rangeThreshold = 80;

    public FinalBossSpreadMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
        initDestructionAnimation(missileConfiguration, movementConfiguration);
        this.isDamageable = false;
        this.isDestructable = true;
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
        if(this.movementConfiguration.getPathFinder() instanceof DestinationPathFinder && this.getCurrentLocation().equals(this.movementConfiguration.getDestination())){
            gettingIntoPlace = false;
        }

        if(!gettingIntoPlace && !movingToPlayer && !finishedMovingToPlayer) {
            this.target = PlayerManager.getInstance().getClosestSpaceShip(this);
            redirectTowardsTarget(PlayerManager.getInstance().getClosestSpaceShip(this));
        }

        if(!finishedMovingToPlayer && movingToPlayer && GameState.getInstance().getGameTicksExecuted() % 3 == 0){
            redirectTowardsTarget(PlayerManager.getInstance().getClosestSpaceShip(this)); //reorient toward player
        }

        if(!finishedMovingToPlayer && movingToPlayer && target != null && CollisionDetector.getInstance().isNearby(this, target, rangeThreshold)){
            stopCharging();
        }

    }

    private void redirectTowardsTarget(GameObject target){
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setMovementSpeed(1.5f);
        this.movementConfiguration.setDestination(target.getCurrentCenterLocation());
        this.movementConfiguration.setDirection(Direction.RIGHT);
        this.movementConfiguration.setCurrentLocation(new Point(this.getCenterXCoordinate(), this.getCenterYCoordinate()));
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
        this.setAllowedToMove(true);
        movingToPlayer = true;
    }

    private void stopCharging(){
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setMovementSpeed(1.5f); //todo placeholder speed, should be variable if this is to be reused
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
        this.movementConfiguration.setDestination(target.getCurrentCenterLocation());
        this.movementConfiguration.setDirection(Direction.RIGHT);
        this.movementConfiguration.setCurrentLocation(new Point(this.getCenterXCoordinate(), this.getCenterYCoordinate()));
        movingToPlayer = false;
        finishedMovingToPlayer = true;
        this.setAllowedToMove(true);
    }
}
