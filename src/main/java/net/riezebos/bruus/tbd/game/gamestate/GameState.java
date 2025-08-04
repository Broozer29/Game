package net.riezebos.bruus.tbd.game.gamestate;

import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveFile;

public class GameState {

    private static GameState instance = new GameState();
    private GameStatusEnums gameState;
    private GameMode gameMode = GameMode.Default;

    private double lastPause;

    private int DELAY = 0;

    private double gameSeconds;
    private double levelStartTime;
    private int stagesCompleted;
    private long gameTicksExecuted;


    private float difficultyCoefficient;
    private int monsterLevel;
    private float initialOffset;
    private int bossesDefeated;


    private GameState() {
        resetGameState();
    }

    public static GameState getInstance () {
        return instance;
    }

    public void resetGameState () {
        setGameState(GameStatusEnums.Waiting);
        setDELAY(15);
        this.gameTicksExecuted = 0;
        this.gameSeconds = 0;
        this.stagesCompleted = 0;
        this.monsterLevel = 0;
        this.initialOffset = (1 / 0.33f);
        this.bossesDefeated = 0;
    }


    private int testingVariableBonus = 0;

    public void updateDifficultyCoefficient () {
        float playerFactor = 1;
        float baseTimeFactor = 0.0606f; // Base factor for time, at LevelManager difficulty 2
        float maxTimeFactor = 0.125f; // Define the maximum time factor for LevelManager difficulty 6
        float stageFactor = (float) Math.pow(1.05, stagesCompleted); // Exponential growth for each stage completed


        float songDifficultyModifier = LevelManager.getInstance().getCurrentLevelDifficultyScore(); // This should be obtained from the LevelManager and ranges between 2 and 6 (inclusive)

        // Scale the time factor based on the level difficulty
        float timeFactor = baseTimeFactor + (maxTimeFactor - baseTimeFactor) * ((songDifficultyModifier - 2) / (6 - 2));

        double timeInMinutes = gameSeconds / 60.0f; // Convert seconds to minutes

        if (testingVariableBonus > 0) {
            System.out.println("Adding additional difficulty coefficient in GameStateInfo 66");
        }

        difficultyCoefficient = (float) ((playerFactor + timeInMinutes * timeFactor) * stageFactor) + testingVariableBonus;
        updateMonsterLevel();
    }


    private void updateMonsterLevel () {
        // Apply the formula and subtract the offset to ensure starting level is 1
        monsterLevel = (int) Math.round(1 + ((difficultyCoefficient / 0.33) - initialOffset));

        // Ensure monsterLevel never goes below 1
        monsterLevel = Math.max(1, monsterLevel);
    }


    public GameStatusEnums getGameState () {
        return gameState;
    }

    public boolean isAllowedToPause(){
        return (this.gameSeconds -1) > lastPause;
    }

    private void setLastPause(){
        this.lastPause = gameSeconds;
    }

    public void setGameState (GameStatusEnums ingame) {
        this.gameState = ingame;

        if(ingame == GameStatusEnums.Paused) {
            this.setLastPause();
        }
    }

    public int getDELAY () {
        return DELAY;
    }

    public void setDELAY (int dELAY) {
        DELAY = dELAY;
    }

    public double getGameSeconds () {
        return gameSeconds;
    }

    public double getLevelStartTime() {
        return levelStartTime;
    }

    public void setLevelStartTime(double levelStartTime) {
        this.levelStartTime = levelStartTime;
    }

    public void addGameTicks (long gameTick) {
        this.gameTicksExecuted += gameTick;
        updateGameTimeByExecutedGameTicks();
    }

    private void updateGameTimeByExecutedGameTicks () {
        this.gameSeconds = (double) (gameTicksExecuted * getDELAY()) / 1000.0;
    }

    public int getStagesCompleted () {
        return stagesCompleted;
    }

    public void setStagesCompleted (int stagesCompleted) {
        this.stagesCompleted = stagesCompleted;
    }

    public float getDifficultyCoefficient () {
        if(difficultyCoefficient <= 0){
            updateDifficultyCoefficient();
        }
        return difficultyCoefficient;
    }

    public int getMonsterLevel () {
        return monsterLevel;
    }

    public void setMonsterLevel (int monsterLevel) {
        this.monsterLevel = monsterLevel;
    }

    public int getBossesDefeated () {
        return bossesDefeated;
    }

    public void setBossesDefeated (int bossesDefeated) {
        this.bossesDefeated = bossesDefeated;
    }


    public void setGameSeconds(double gameSeconds) {
        this.gameSeconds = gameSeconds;
    }

    public void setDifficultyCoefficient(float difficultyCoefficient) {
        this.difficultyCoefficient = difficultyCoefficient;
    }

    public long getGameTicksExecuted() {
        return gameTicksExecuted;
    }

    public void setGameTicksExecuted(long gameTicksExecuted) {
        this.gameTicksExecuted = gameTicksExecuted;
    }

    public void loadInSaveFile(SaveFile saveFile){
        resetGameState();
        this.stagesCompleted = saveFile.getStagesCompleted();
        this.bossesDefeated = saveFile.getBossesDefeated();
        this.difficultyCoefficient = saveFile.getDifficultyCoefficient();
        this.gameTicksExecuted = saveFile.getGameTicksExecuted();
        this.gameMode = saveFile.getGameModes();
        this.lastPause = 0;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}

