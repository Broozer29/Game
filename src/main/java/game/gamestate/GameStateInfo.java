package game.gamestate;

import VisualAndAudioData.audio.AudioPositionCalculator;
import VisualAndAudioData.audio.CustomAudioClip;

public class GameStateInfo {

    private static GameStateInfo instance = new GameStateInfo();
    private GameStatusEnums gameState;

    private SpawningMechanic spawningMechanic;

    private int DELAY = 0;

    private double gameSeconds;
    private int stagesCompleted;

    private long gameTicksExecuted;

    private float musicSeconds = 0;
    private float maxMusicSeconds = 0;

    private float difficultyCoefficient;

    private GameStateInfo () {
        initializeGame();
    }

    public static GameStateInfo getInstance () {
        return instance;
    }

    public void initializeGame () {
        setGameState(GameStatusEnums.Waiting);
        setDELAY(15);
        setMusicSeconds(0);
        this.gameTicksExecuted = 0;
        this.gameSeconds = 0;
        this.spawningMechanic = SpawningMechanic.Director;
        this.stagesCompleted = 0;
    }

    public GameStatusEnums getGameState () {
        return gameState;
    }

    public void setGameState (GameStatusEnums ingame) {
        this.gameState = ingame;
    }

    public int getDELAY () {
        return DELAY;
    }

    public void setDELAY (int dELAY) {
        DELAY = dELAY;
    }

    public float getMusicSeconds () {
        return musicSeconds;
    }

    public void setMusicSeconds (float musicSeconds) {
        this.musicSeconds = musicSeconds;
    }

    public void setMaxMusicSeconds (CustomAudioClip music) {
        this.maxMusicSeconds = AudioPositionCalculator.getInstance().getPlaybackTimeInSeconds(music.getClip(), (long) music.getFrameLength());
    }

    public float getMaxMusicSeconds () {
        //No song loaded or found, so testing purposes
        if (this.maxMusicSeconds == 0) {
            return 50000000;
        }
        return this.maxMusicSeconds;
    }

    public double getGameSeconds () {
        return gameSeconds;
    }

    public long getGameTicksExecuted () {
        return gameTicksExecuted;
    }

    public void setMaxMusicSeconds (float maxMusicSeconds) {
        this.maxMusicSeconds = maxMusicSeconds;
    }

    public void addGameTicks(long gameTick){
        this.gameTicksExecuted += gameTick;
        updateGameTimeByExecutedGameTicks();
    }

    private void updateGameTimeByExecutedGameTicks() {
        this.gameSeconds = (double)(gameTicksExecuted * getDELAY()) / 1000.0;
    }

    public SpawningMechanic getSpawningMechanic () {
        return spawningMechanic;
    }

    public void setSpawningMechanic (SpawningMechanic spawningMechanic) {
        this.spawningMechanic = spawningMechanic;
    }

    public int getStagesCompleted () {
        return stagesCompleted;
    }

    public void setStagesCompleted (int stagesCompleted) {
        this.stagesCompleted = stagesCompleted;
    }

    public float getDifficultyCoefficient () {
        return difficultyCoefficient;
    }

    public void updateDifficultyCoefficient() {
        float playerFactor = 1;
        float timeFactor = 0.0506f; // The factor for time, increase value to increase difficulty scaling
        float difficultyValue = 1; // This can be a static value or change based on game settings
        float stageFactor = (float) Math.pow(1.15, stagesCompleted); // Exponential growth for each stage completed

        double timeInMinutes = gameSeconds / 60.0f; // Convert seconds to minutes

        difficultyCoefficient = (float) ((playerFactor + timeInMinutes * timeFactor) * stageFactor);
    }
}
