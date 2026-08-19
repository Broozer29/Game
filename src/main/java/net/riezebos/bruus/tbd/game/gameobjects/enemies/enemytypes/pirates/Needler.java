package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.pirates;

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

public class Needler extends Enemy {
    private int rangeThreshold = 125;
    public Needler (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(this.scale / 1.5f);
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.2f);
        this.damage = 13;
        this.detonateOnCollision = true;
        this.knockbackStrength = 10 + EnemyManager.getInstance().getEnemyDifficultyModifier();
        this.hasAttack = false;
        this.rangeThreshold = Math.round(125 + (EnemyManager.getInstance().getEnemyDifficultyModifier() * 7.5f));
        this.moveSpeedBoost = 2 + (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.055f);
    }

    @Override
    public void fireAction(){
        if(!activated && CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), rangeThreshold)){
            activateNeedler();
        }

    }


    private boolean activated = false;
    private float moveSpeedBoost = 2f;
    private void activateNeedler(){
        if(!activated) {
            OnScreenTextManager.getInstance().addText("!", this.getCenterXCoordinate(), this.getCenterYCoordinate(), 25);
            if (this.movementConfiguration.getMovementSpeed() < 1) {
                this.movementConfiguration.setMovementSpeed(1);
            } else {
                this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getMovementSpeed() * moveSpeedBoost);
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
