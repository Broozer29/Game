package game.objects.enemies.enemytypes;

import game.managers.OnScreenTextManager;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.StraightLinePathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.player.PlayerManager;
import game.objects.player.spaceship.SpaceShip;
import game.util.CollisionDetector;
import game.util.OnScreenText;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class Needler extends Enemy {
    public Needler (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(EnemyEnums.Needler.getDestructionImageEnum());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale / 1.5f);
        this.damage = 50;
        this.detonateOnCollision = true;
    }

    @Override
    public void fireAction(){
//        SpaceShip spaceShip = PlayerManager.getInstance().getSpaceship();
//        Rectangle spaceShipBounds = spaceShip.getBounds();
//        int spaceShipCenterXCoordinate = spaceShip.getCenterXCoordinate();
//        int spaceShipCenterYCoordinate = spaceShip.getCenterYCoordinate();
//        int spaceShipWidth = spaceShip.getWidth();
//        int spaceShipHeight = spaceShip.getHeight();
//
//
//        int thisHeight = this.height;
//        int thisWidth = this.width;
//        int thisX = this.getCenterXCoordinate();
//        int thisY = this.getCenterYCoordinate();
//        Rectangle thisBounds = this.getBounds();

        if(CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getSpaceship(), 125)){
            activateNeedler();
        }

    }


    private boolean activated = false;
    private void activateNeedler(){
//        OnScreenTextManager.getInstance().addText("Activate Needler", this.getCenterXCoordinate(), this.getCenterYCoordinate());

        if(!activated) {
            if (this.movementConfiguration.getXMovementSpeed() < 1) {
                this.movementConfiguration.setXMovementSpeed(1);
            } else {
                this.movementConfiguration.setXMovementSpeed(this.movementConfiguration.getXMovementSpeed() * 2);
            }

            if (this.movementConfiguration.getYMovementSpeed() < 1) {
                this.movementConfiguration.setYMovementSpeed(1);
            } else {
                this.movementConfiguration.setYMovementSpeed(this.movementConfiguration.getYMovementSpeed() * 2);
            }


            SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
            Point destination = new Point(spaceship.getCenterXCoordinate(), spaceship.getCenterYCoordinate());
            this.movementConfiguration.setDestination(destination);
            this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
//            this.allowedVisualsToRotate = true;
            this.rotateObjectTowardsPoint(destination, false);
            activated = true;
        }

    }

}
