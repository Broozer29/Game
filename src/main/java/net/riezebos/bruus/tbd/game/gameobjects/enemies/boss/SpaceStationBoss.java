package net.riezebos.bruus.tbd.game.gameobjects.enemies.boss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;


public class SpaceStationBoss extends Enemy {

    public SpaceStationBoss (SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);
    }


    @Override
    public void fireAction () {
        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            this.allowedToMove = false;
        }

//        OnScreenTextManager.getInstance().addText("" + this.animation.getCurrentFrame(), this.getCenterXCoordinate(), this.getCenterYCoordinate());

    }

    private double increaseRotationAngle (double rotationAngle) {
        double newAngle = rotationAngle + 1;
        if (newAngle > 360) {
            newAngle = 0;
        }

        return newAngle;
    }

}
