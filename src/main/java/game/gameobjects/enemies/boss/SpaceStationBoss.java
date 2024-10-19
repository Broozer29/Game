package game.gameobjects.enemies.boss;

import VisualAndAudioData.image.ImageRotator;
import com.badlogic.gdx.Game;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gamestate.GameStateInfo;
import game.movement.MovementConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

public class SpaceStationBoss extends Enemy {


    private double lastTimeSinceRotated;
    private double secondsDelayForRotation = 0.05;
    private GameStateInfo gameStateInfo = null;

    public SpaceStationBoss (SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);
        gameStateInfo = GameStateInfo.getInstance();
        lastTimeSinceRotated = gameStateInfo.getGameSeconds();
    }

    public SpaceStationBoss (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        gameStateInfo = GameStateInfo.getInstance();
        lastTimeSinceRotated = gameStateInfo.getGameSeconds();
    }

    @Override
    public void fireAction () {
        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            this.allowedToMove = false;
        }

        if (lastTimeSinceRotated + secondsDelayForRotation <= gameStateInfo.getGameSeconds()) {
//            int currentCenterXCoordinate = super.getCenterXCoordinate();
//            int currentCenterYCoordinate = super.getCenterYCoordinate();
//
//            this.rotationAngle = increaseRotationAngle(rotationAngle);
//            this.image = ImageRotator.getInstance().rotate(this.originalImage, rotationAngle, false);
//            lastTimeSinceRotated = gameStateInfo.getGameSeconds();
//
//            setCenterCoordinates(currentCenterXCoordinate, currentCenterYCoordinate);
        }
    }

    private double increaseRotationAngle (double rotationAngle) {
        double newAngle = rotationAngle + 1;
        if (newAngle > 360) {
            newAngle = 0;
        }

        return newAngle;
    }

}
