package game.managers;

import game.UI.UIObject;
import gamedata.PlayerStats;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

public class GameUIManager {

	private int healthBarWidth = 120;
	private int healthBarHeight = 20;
	private int healthFrameWidth = 120;
	private int healthFrameHeight = 15;
	private UIObject healthFrame;
	private UIObject healthBar;

	private UIObject shieldFrame;
	private UIObject shieldBar;
	
	private UIObject specialAttackFrame;
	private SpriteAnimation specialAttackHighlight;
	
	
	private static GameUIManager instance = new GameUIManager();

	private GameUIManager () {

	}

	public static GameUIManager getInstance() {
		return instance;
	}

	public void createGameBoardGUI() {
		createHealthBar();
		createShieldBar();
		createSpecialAttackUIObjects();
	}
	
	private void createSpecialAttackUIObjects() {
		ImageEnums frameType = null;
		switch(PlayerStats.getInstance().getPlayerSpecialAttackType()) {
		case EMP:
			frameType = ImageEnums.Starcraft2_Electric_Field;
			break;
		case Firewall:
			frameType = ImageEnums.Starcraft2_Firebat_Weapon;
			break;
		case Rocket_Cluster:
			break;
		default:
			break;
		
		}

		specialAttackFrame = new UIObject(150, 20, (float) 0.7, frameType);
		specialAttackHighlight = new SpriteAnimation(50, 30, ImageEnums.Highlight, true, (float) 0.7);
		specialAttackHighlight.setImageDimensions(specialAttackFrame.getWidth(), specialAttackFrame.getHeight());
		specialAttackHighlight.setX(specialAttackFrame.getXCoordinate() - 2);
		specialAttackHighlight.setY(specialAttackFrame.getYCoordinate() - 2);
	}

	private void createHealthBar() {
		healthFrame = new UIObject(0, 30, 1, ImageEnums.Frame);
		healthFrame.resizeToDimensions(healthFrameWidth, healthFrameHeight);
		healthBar = new UIObject(10, 30, 1, ImageEnums.Health_Bar);
		healthBar.resizeToDimensions(healthBarWidth, healthBarHeight);
	}

	private void createShieldBar() {
		shieldFrame = new UIObject(0, 60, 1, ImageEnums.Frame);
		shieldFrame.resizeToDimensions(healthFrameWidth, healthFrameHeight);
		shieldBar = new UIObject(10, 60, 1, ImageEnums.Shield_Bar);
		shieldBar.resizeToDimensions(healthBarWidth, healthBarHeight);
	}

	public int getHealthBarWidth() {
		return healthBarWidth;
	}

	public int getHealthBarHeight() {
		return healthBarHeight;
	}

	public UIObject getHealthFrame() {
		return healthFrame;
	}

	public UIObject getHealthBar() {
		return healthBar;
	}

	public UIObject getShieldFrame() {
		return shieldFrame;
	}

	public UIObject getShieldBar() {
		return shieldBar;
	}

	public void resetManager() {
		createHealthBar();
		createShieldBar();
	}

	public UIObject getSpecialAttackFrame() {
		return specialAttackFrame;
	}

	public SpriteAnimation getSpecialAttackHighlight() {
		return specialAttackHighlight;
	}

}