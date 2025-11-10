package net.riezebos.bruus.tbd.game.level.directors;

import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

public class GodRunDetector {

    private static final GodRunDetector instance = new GodRunDetector();

    private int enemiesSpawned = 0;
    private int enemiesKilled = 0;
    private float enemyKillRatio = 0f;
    private double gameSecondsWithHighKillRatio = -1f;
    private int minimumSpawnsRequiredToActivate = 25;
    private int boardBlockTotal = 0;
    private int godRunScore = 0;
    private int lastGodRunScore = 0;
    private double lastGameSecondsGodRunScoreUpdated = 0;

    private GodRunDetector() {
    }

    public static GodRunDetector getInstance() {
        return instance;
    }

    public void resetGodRunDetector() {
        this.enemiesKilled = 0;
        this.enemiesSpawned = 0;
        this.enemyKillRatio = 0f;
        this.gameSecondsWithHighKillRatio = -1f; // stop tracking
        this.godRunScore = 0;
        this.boardBlockTotal = 0;
        this.lastGodRunScore = 0;
        this.lastGameSecondsGodRunScoreUpdated = GameState.getInstance().getGameSeconds();
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public float getEnemyKillRatio() {
        return enemyKillRatio;
    }

    public void addBoardBlock(int amount) {
        boardBlockTotal += amount;
    }

    public void addEnemySpawned() {
        enemiesSpawned++;
        recalcAndUpdateTracking();
    }

    public void addEnemyKilled() {
        enemiesKilled++;
        recalcAndUpdateTracking();
    }

    private void recalcAndUpdateTracking() {
        calculateKillRatio();
        startOrResetTracking();
    }

    private void calculateKillRatio() {
        if (enemiesSpawned <= 0) {
            enemyKillRatio = 0f;
        } else {
            enemyKillRatio = (float) enemiesKilled / (float) enemiesSpawned;
        }
    }

    private void startOrResetTracking() {
        if (enemyKillRatio > ratioThresholdWhenLowGodRunScore) {
            if (gameSecondsWithHighKillRatio < 0f) {
                gameSecondsWithHighKillRatio = GameState.getInstance().getGameSeconds();
            }
        } else {
            // Ratio dropped at/below threshold -> reset the sustained window
            gameSecondsWithHighKillRatio = -1f;
        }
    }

    private float enemyKillRatioRequiredWhenLowGodRunScore = 6f;
    private float enemyKillRatioRequiredWhenHighGodRunScore = 5f;
    private float ratioThresholdWhenLowGodRunScore = 0.75f;
    private float ratioThresholdWhenHighGodRunScore = 0.65f;
    private float sustainSecondsWhenLowGodrunScore = 7.5f;
    private float sustainSecondsWhenHighGodRunScore = 7.5f;

    public void updateGodRunStatus() {
        if (GameState.getInstance().getGameMode().equals(GameMode.Nightmare)) {
            this.godRunScore = GameState.getInstance().getStagesCompleted() > 0 ? 2 : 0; //maximum hardcoded for now
            this.lastGodRunScore = this.godRunScore;
            return;
        }


        if (enemiesSpawned < minimumSpawnsRequiredToActivate) godRunScore = 0;
        if (gameSecondsWithHighKillRatio < 0f) godRunScore = 0;

        double now = GameState.getInstance().getGameSeconds();
        double elapsed = now - gameSecondsWithHighKillRatio;

        if (enemyKillRatio > ((lastGodRunScore >= 1 ? ratioThresholdWhenHighGodRunScore : ratioThresholdWhenLowGodRunScore))
                && elapsed >= ((lastGodRunScore >= 1 ? sustainSecondsWhenHighGodRunScore : sustainSecondsWhenLowGodrunScore))) {
            godRunScore++;
        }

        //Tl;dr, if enough enemies are killed near the spawn positions and a high ratio of enemies has been killed, increase the godrunscore
        //If it is already high, change the ratio required so that it is easier to maintain a score of 2
        if (boardBlockTotal > 0 && ((float) boardBlockTotal / enemiesKilled)
                >= (lastGodRunScore >= 2 ? enemyKillRatioRequiredWhenHighGodRunScore : enemyKillRatioRequiredWhenLowGodRunScore)) {
            godRunScore++;
        }

        if (GameState.getInstance().getStagesCompleted() <= 3)
            godRunScore = 0; //if early game, never enable godrun modifications

        if (lastGodRunScore != godRunScore && now - lastGameSecondsGodRunScoreUpdated >= 15) { //only update the godrunscore a maximum of once every X seconds
            lastGodRunScore = godRunScore;
            lastGameSecondsGodRunScoreUpdated = now;
            AudioManager.getInstance().addAudio(AudioEnums.GodRunDetected);
//            OnScreenTextManager.getInstance().addText("GOD RUN DETECTED, SCORE: " + godRunScore);
        }

    }

    public int getGodRunScore() {
        return godRunScore;
    }
}
