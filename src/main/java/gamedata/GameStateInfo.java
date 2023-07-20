package gamedata;

public class GameStateInfo {

	private static GameStateInfo instance = new GameStateInfo();
	private GameStatusEnums gameState;
	private int DELAY = 0;
	private float musicSeconds = 0;
	
	private GameStateInfo() {
		initializeGame();
	}
	
	public static GameStateInfo getInstance() {
		// TODO Auto-generated method stub
		return instance ;
	}
	
	public void initializeGame() {
		setGameState(GameStatusEnums.Waiting);
		setDELAY(15);
		setMusicSeconds(0);
	}

	public GameStatusEnums getGameState() {
		return gameState;
	}

	public void setGameState(GameStatusEnums ingame) {
		this.gameState = ingame;
	}

	public int getDELAY() {
		return DELAY;
	}

	public void setDELAY(int dELAY) {
		DELAY = dELAY;
	}

	public float getMusicSeconds() {
		return musicSeconds;
	}

	public void setMusicSeconds(float musicSeconds) {
		this.musicSeconds = musicSeconds;
	}
}
