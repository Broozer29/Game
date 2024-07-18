package game.gamestate;

import VisualAndAudioData.image.ImageEnums;

public class GameStatsTracker {
    private static GameStatsTracker instance = new GameStatsTracker();
    private double highestDamageDealt; // done
    private int enemiesKilled;// done
    private int enemiesSpawned;// done
    private double mineralsAcquired;// done
    private double totalDamageDealt;// done

    private int shotsFired; // done
    private int shotsHit; // done
    private double totalDamageTaken; //done

    private double damageTakenThisRound; //Done
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
        highestDamageDealt = 0;
        enemiesKilled = 0;
        mineralsAcquired = 0;
        enemiesSpawned = 0;
        totalDamageDealt = 0;
        shotsFired = 0;
        shotsHit = 0;
        totalDamageTaken = 0;
        damageTakenThisRound = 0;
        amountOfEnemiesMissedThisRound = 0;
    }

    public void resetStatsForNextRound () {
        damageTakenThisRound = 0;
        amountOfEnemiesMissedThisRound = 0;
        enemiesKilledThisRound = 0;
        enemiesSpawnedThisRound = 0;
        mineralsGainedThisRound = 0;
    }

    public int getAmountOfEnemiesMissedThisRound () {
        amountOfEnemiesMissedThisRound = enemiesSpawnedThisRound - enemiesKilledThisRound;
        return amountOfEnemiesMissedThisRound;
    }

    public long getDamageTakenThisRound () {
        return Math.round(damageTakenThisRound);
    }

    public void addShotFired (int amount) {
        shotsFired += amount;
    }

    public void addShotHit (int amount) {
        shotsHit += amount;
    }

    public int getEnemiesKilledThisRound () {
        return this.enemiesKilledThisRound;
    }

    public int getEnemiesSpawnedThisRound () {
        return this.enemiesSpawnedThisRound;
    }

    public int getAccuracy () {
        return Math.round(((float) shotsHit / shotsFired) * 100);
    }

    public void addDamageDealt (double damage) {
        totalDamageDealt += damage;
    }

    public long getTotalDamageDealt () {
        return Math.round(totalDamageDealt);
    }

    public void addEnemySpawned (int amount) {
        enemiesSpawned += amount;
        enemiesSpawnedThisRound += amount;
    }

    public void setHighestDamageDealt (double damageDealt) {
        if (damageDealt > highestDamageDealt) {
            highestDamageDealt = damageDealt;
        }
    }

    public void addEnemyKilled (int amount) {
        enemiesKilled += amount;
        enemiesKilledThisRound += amount;
    }

    public void addDamageTaken (double damage) {
        totalDamageTaken += damage;
        damageTakenThisRound += damage;
    }

    public long getTotalDamageTaken () {
        return Math.round(totalDamageTaken);
    }

    public void addMoneyAcquired (float amount) {
        mineralsAcquired += amount;
        mineralsGainedThisRound += amount;
    }

    public int getPercentageOfTotalSpawnedEnemiesKilled () {
        return Math.round(((float) enemiesKilled / enemiesSpawned) * 100);
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

    public long getHighestDamageDealt () {
        return Math.round(highestDamageDealt);
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

    public int getShotsFired () {
        return shotsFired;
    }

    public int getShotsHit () {
        return shotsHit;
    }

}
