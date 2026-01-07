package net.riezebos.bruus.tbd.game.gamestate;

import net.riezebos.bruus.tbd.game.level.directors.GodRunDetector;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class GameStatsTracker {
    private static GameStatsTracker instance = new GameStatsTracker();
    private int enemiesKilled;// done
    private int enemiesSpawned;// done
    private double mineralsAcquired;// done

    private int amountOfEnemiesMissedThisRound; //Used to score the grade
    private int enemiesSpawnedThisRound;
    private int enemiesKilledThisRound;
    private double mineralsGainedThisRound;


    private GameStatsTracker () {

    }

    public static GameStatsTracker getInstance () {
        return instance;
    }

    public void resetGameStatsTracker () {
        enemiesKilled = 0;
        mineralsAcquired = 0;
        enemiesSpawned = 0;
        amountOfEnemiesMissedThisRound = 0;
    }

    public void resetStatsForNextRound () {
        amountOfEnemiesMissedThisRound = 0;
        enemiesKilledThisRound = 0;
        enemiesSpawnedThisRound = 0;
        mineralsGainedThisRound = 0;
    }

    public int getAmountOfEnemiesMissedThisRound () {
        amountOfEnemiesMissedThisRound = enemiesSpawnedThisRound - enemiesKilledThisRound;
        return amountOfEnemiesMissedThisRound;
    }

    public int getEnemiesKilledThisRound () {
        return this.enemiesKilledThisRound;
    }

    public int getEnemiesSpawnedThisRound () {
        return this.enemiesSpawnedThisRound;
    }


    public void addEnemySpawned (int amount) {
        enemiesSpawned += amount;
        enemiesSpawnedThisRound += amount;
    }

    public void addEnemyKilled (int amount) {
        enemiesKilled += amount;
        enemiesKilledThisRound += amount;
        GodRunDetector.getInstance().addEnemyKilled();
    }

    public void addMoneyAcquired (float amount) {
        mineralsAcquired += amount;
        mineralsGainedThisRound += amount;
    }


    public int getPercentageOfEnemiesKilledThisRound () {
        return Math.round(((float) enemiesKilledThisRound / enemiesSpawnedThisRound) * 100);
    }

    public ImageEnums getGradeImageTypeByCurrentRoundScore () {
        double percentage = 0;
        if (enemiesKilledThisRound > 0 && enemiesSpawnedThisRound > 0) {
            percentage = ((double) enemiesKilledThisRound / enemiesSpawnedThisRound) * 100;
        }

        if (percentage < 40) {
            return ImageEnums.GradeBronze;
        } else if (percentage >= 40 && percentage < 50) {
            return ImageEnums.GradeSilver;
        } else if (percentage >= 50 && percentage < 60) {
            return ImageEnums.GradeGold;
        } else if (percentage >= 60 && percentage < 70) {
            return ImageEnums.GradePlatinum;
        } else if (percentage >= 70 && percentage < 80) {
            return ImageEnums.GradeDiamond;
        } else if (percentage >= 80 && percentage < 90) {
            return ImageEnums.GradeMaster;
        } else if (percentage >= 90) {
            return ImageEnums.GradeGrandMaster;
        }

        return ImageEnums.Test_Image;
    }

    public int getEnemiesKilled () {
        return enemiesKilled;
    }

    public long getMineralsAcquired () {
        return Math.round(mineralsAcquired);
    }

    public long getMineralsGainedThisRound () {
        return Math.round(mineralsGainedThisRound);
    }

    public int getEnemiesSpawned () {
        return enemiesSpawned;
    }
}
