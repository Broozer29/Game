package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class Needler extends Enemy {
    public Needler (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale / 1.5f);
        this.damage = 15;
        this.detonateOnCollision = true;
        this.knockbackStrength = 10;
    }

    @Override
    public void fireAction(){
        if(!activated && CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), 125)){
            activateNeedler();
        }

    }


    private boolean activated = false;
    private void activateNeedler(){
        if(!activated) {
            OnScreenTextManager.getInstance().addText("!", this.getCenterXCoordinate(), this.getCenterYCoordinate(), 25);
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


            SpaceShip spaceship = PlayerManager.getInstance().getClosestSpaceShip(this);
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
