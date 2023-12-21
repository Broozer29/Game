package game.objects.friendlies.powerups;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.CollisionDetector;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.objects.GameObject;
import game.utils.BoundsCalculator;
import gamedata.BoostsUpgradesAndBuffsSettings;
import gamedata.DataClass;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;

public class PowerUpManager {

    private static PowerUpManager instance = new PowerUpManager();
    private List<PowerUp> powerUpsOnTheField = new ArrayList<PowerUp>();

    private PowerUpManager () {
    }

    public static PowerUpManager getInstance () {
        return instance;
    }

    public void updateGameTick () {
        cyclePowerUps();
    }

    private void cyclePowerUps () {
        for (int i = 0; i < powerUpsOnTheField.size(); i++) {
            if (!powerUpsOnTheField.get(i).isVisible()) {
                powerUpsOnTheField.remove(i);
            } else {
                powerUpsOnTheField.get(i).move();
                checkPowerUpCollision(powerUpsOnTheField.get(i));
            }

        }
    }

    public List<PowerUp> getPowerUpsOnTheField () {
        return powerUpsOnTheField;
    }

    private void checkPowerUpCollision (PowerUp powerUp) {
        if (CollisionDetector.getInstance().detectCollision(powerUp, PlayerManager.getInstance().getSpaceship())) {
            handleCollision(powerUp);
        }
    }

    private void handleCollision (PowerUp powerUp) {
        powerUp.startPowerUpTimer();
        powerUp.setVisible(false);
        try {
            AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void resetManager () {
        powerUpsOnTheField = new ArrayList<PowerUp>();
    }

}