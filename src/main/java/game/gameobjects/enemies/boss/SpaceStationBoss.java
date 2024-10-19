package game.gameobjects.enemies.boss;

import VisualAndAudioData.image.ImageRotator;
import com.badlogic.gdx.Game;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gamestate.GameStateInfo;
import game.managers.OnScreenTextManager;
import game.movement.MovementConfiguration;
import game.util.OnScreenText;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;


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
