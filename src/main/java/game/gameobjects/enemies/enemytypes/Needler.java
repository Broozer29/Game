package game.gameobjects.enemies.enemytypes;

import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.StraightLinePathFinder;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.spaceship.SpaceShip;
import game.util.CollisionDetector;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

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
            Point destination = new Point(
                    spaceship.getCenterXCoordinate() - this.getWidth() / 2,
                    spaceship.getCenterYCoordinate() - this.getHeight() / 2);
            this.movementConfiguration.setDestination(destination);
            this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
            this.allowedVisualsToRotate = true;
            this.rotateObjectTowardsPoint(destination, false);
            activated = true;
        }

    }

}