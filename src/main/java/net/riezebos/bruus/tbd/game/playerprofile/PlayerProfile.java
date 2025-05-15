package net.riezebos.bruus.tbd.game.playerprofile;

public class PlayerProfile {
    private int enemyKilledCounter;
    private int highestStageCompleted;
    private int bossesKilledCounter;
    private boolean carrierUnlocked = false;
    private boolean fireFighterUnlocked = false;
    private int emeralds = 0;
    private int nepotismLevel = 0;
    private int clubAccessLevel = 0;
    private int compoundWealthLevel = 0;
    private int BountyHunterLevel = 0;
    private int treasureHunterLevel = 0;
    private int ThickHideLevel = 0;


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

    public int getClubAccessLevel() {
        return clubAccessLevel;
    }

    public void setClubAccessLevel(int clubAccessLevel) {
        this.clubAccessLevel = clubAccessLevel;
    }

    public int getCompoundWealthLevel() {
        return compoundWealthLevel;
    }

    public void setCompoundWealthLevel(int compoundWealthLevel) {
        this.compoundWealthLevel = compoundWealthLevel;
    }

    public int getBountyHunterLevel() {
        return BountyHunterLevel;
    }

    public void setBountyHunterLevel(int bountyHunterLevel) {
        BountyHunterLevel = bountyHunterLevel;
    }

    public int getTreasureHunterLevel() {
        return treasureHunterLevel;
    }

    public void setTreasureHunterLevel(int treasureHunterLevel) {
        this.treasureHunterLevel = treasureHunterLevel;
    }

    public boolean isCarrierUnlocked() {
        return carrierUnlocked;
    }

    public void setCarrierUnlocked(boolean carrierUnlocked) {
        this.carrierUnlocked = carrierUnlocked;
    }

    public boolean isFireFighterUnlocked() {
        return fireFighterUnlocked;
    }

    public void setFireFighterUnlocked(boolean fireFighterUnlocked) {
        this.fireFighterUnlocked = fireFighterUnlocked;
    }

    public int getThickHideLevel() {
        return ThickHideLevel;
    }

    public void setThickHideLevel(int thickHideLevel) {
        ThickHideLevel = thickHideLevel;
    }
}
