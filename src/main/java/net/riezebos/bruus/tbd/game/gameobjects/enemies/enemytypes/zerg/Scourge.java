package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
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
    private int firstDetectionRange = 240;

    public Scourge (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale);
        this.detonateOnCollision = true;
        this.knockbackStrength = 10 + EnemyManager.getInstance().getEnemyDifficultyModifier();
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.15f);
        this.firstDetectionRange = 240 + Math.round(EnemyManager.getInstance().getEnemyDifficultyModifier() * 10f);
    }

    @Override
    public void fireAction () {
        if (activatedSecondTime) {
            return;
        }

        if (!activatedFirstTime && isCloseEnough(firstDetectionRange)) {
            activateScourge();
            activatedFirstTime = true;
            return;
        }

        if (activatedFirstTime && !activatedSecondTime && isCloseEnough(140)) {
            activateScourge();
            activatedSecondTime = true;
        }
    }

    private void activateScourge () {
        OnScreenTextManager.getInstance().addText("!", this.getCenterXCoordinate(), this.getCenterYCoordinate());
        // Adjust movement speed.
        if (this.movementConfiguration.getMovementSpeed() < 1) {
            this.movementConfiguration.setMovementSpeed(1);
        } else {
            this.movementConfiguration.setMovementSpeed(Math.min(this.movementConfiguration.getMovementSpeed() * 1.5f, 5));
        }

        // Align towards the player's current position.
        SpaceShip spaceship = PlayerManager.getInstance().getClosestSpaceShip(this);
        Point destination = new Point(
                spaceship.getCenterXCoordinate() - this.getWidth() / 2,
                spaceship.getCenterYCoordinate() - this.getHeight() / 2);
        this.movementConfiguration.setDestination(destination);
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());
        this.allowedVisualsToRotate = true;
        this.rotateObjectTowardsPoint(destination, false);
    }


    private boolean isCloseEnough (int range) {
        return CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), range);
    }


}
