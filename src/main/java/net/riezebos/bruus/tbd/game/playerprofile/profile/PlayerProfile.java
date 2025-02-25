package net.riezebos.bruus.tbd.game.playerprofile.profile;

public class PlayerProfile {
    private int enemyKilledCounter;
    private int highestStageCompleted;
    private int bossesKilledCounter;
    private int emeralds = 100;

    private int nepotismLevel = 1;


    public PlayerProfile() {

    }


    public int getEnemyKilledCounter() {
        return enemyKilledCounter;
    }

    public void setEnemyKilledCounter(int enemyKilledCounter) {
        this.enemyKilledCounter = enemyKilledCounter;
    }

    public void addEnemyKilledCounter(int amount){
        this.enemyKilledCounter += amount;
    }

    public int getHighestStageCompleted() {
        return highestStageCompleted;
    }

    public void setHighestStageCompleted(int highestStageCompleted) {
        this.highestStageCompleted = highestStageCompleted;
    }

    public int getBossesKilledCounter() {
        return bossesKilledCounter;
    }

    public void setBossesKilledCounter(int bossesKilledCounter) {
        this.bossesKilledCounter = bossesKilledCounter;
    }

    public void addBossesKilledCounter(int amount){
        this.bossesKilledCounter += amount;
    }

    public void addEmeralds(int amount){
        this.emeralds += amount;
    }

    public int getEmeralds() {
        return emeralds;
    }

    public void setEmeralds(int emeralds) {
        this.emeralds = emeralds;
    }

    public int getNepotismLevel() {
        return nepotismLevel;
    }

    public void setNepotismLevel(int nepotismLevel) {
        this.nepotismLevel = nepotismLevel;
    }
}
