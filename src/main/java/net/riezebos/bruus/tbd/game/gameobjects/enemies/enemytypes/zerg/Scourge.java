package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

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

public class Scourge extends Enemy {

    private boolean activatedFirstTime = false;
    private boolean activatedSecondTime = false;

    public Scourge (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale);
        this.damage = 15;
        this.detonateOnCollision = true;
        this.knockbackStrength = 10;
    }

    @Override
    public void fireAction () {
        if (activatedSecondTime) {
            return;
        }

        if (!activatedFirstTime && isCloseEnough(200)) {
            activateScourge();
            activatedFirstTime = true;
            return;
        }

        if (activatedFirstTime && !activatedSecondTime && isCloseEnough(125)) {
            activateScourge();
            activatedSecondTime = true;
        }
    }

    private void activateScourge () {
        OnScreenTextManager.getInstance().addText("!", this.getCenterXCoordinate(), this.getCenterYCoordinate());
        // Adjust movement speed.
        if (this.movementConfiguration.getXMovementSpeed() < 1) {
            this.movementConfiguration.setXMovementSpeed(1);
        } else {
            this.movementConfiguration.setXMovementSpeed(Math.min(this.movementConfiguration.getXMovementSpeed() * 1.5f, 5));
        }

        if (this.movementConfiguration.getYMovementSpeed() < 1) {
            this.movementConfiguration.setYMovementSpeed(1);
        } else {
            this.movementConfiguration.setYMovementSpeed(Math.min(this.movementConfiguration.getXMovementSpeed() * 1.5f, 5));
        }

        // Align towards the player's current position.
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point destination = new Point(
                spaceship.getCenterXCoordinate() - this.getWidth() / 2,
                spaceship.getCenterYCoordinate() - this.getHeight() / 2);
        this.movementConfiguration.setDestination(destination);
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
        this.allowedVisualsToRotate = true;
        this.rotateObjectTowardsPoint(destination, false);
    }


    private boolean isCloseEnough (int range) {
        return CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getSpaceship(), range);
    }


}
