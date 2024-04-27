package game.objects.powerups;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.objects.powerups.creation.PowerUpCreator;
import game.util.CollisionDetector;
import game.objects.player.PlayerManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;

public class PowerUpManager {

    private static PowerUpManager instance = new PowerUpManager();
    private List<PowerUp> powerUpsOnTheField = new ArrayList<PowerUp>();

    private boolean createPowerAtStartOfStage = false;

    private PowerUpManager () {
    }

    public static PowerUpManager getInstance () {
        return instance;
    }

    public void updateGameTick () {
        movePowerUps();
        removePowerUps();

        if (createPowerAtStartOfStage) {
            PowerUpCreator.getInstance().initializePowerUpSpawnTimers();
            createPowerAtStartOfStage = false;
        }
    }

    private void movePowerUps () {
        for (PowerUp powerUp : powerUpsOnTheField) {
            powerUp.move();
            checkPowerUpCollision(powerUp);
        }
    }

    private void removePowerUps () {
        for (int i = 0; i < powerUpsOnTheField.size(); i++) {
            if (!powerUpsOnTheField.get(i).isVisible()) {
                powerUpsOnTheField.remove(i);
                i--;
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
        powerUp.deleteObject();
        try {
            AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void resetManager () {
        for (PowerUp powerUp : powerUpsOnTheField) {
            powerUp.setVisible(false);
        }
        removePowerUps();
        powerUpsOnTheField = new ArrayList<PowerUp>();
    }

}