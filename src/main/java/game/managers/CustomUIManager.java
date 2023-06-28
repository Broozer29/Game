package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import game.UI.UIObject;

public class CustomUIManager {

	private int healthBarWidth = 120;
	private int healthBarHeight = 20;
	private int healthFrameWidth = 120;
	private int healthFrameHeight = 15;
	private UIObject healthFrame;
	private UIObject healthBar;

	private UIObject shieldFrame;
	private UIObject shieldBar;
	private static CustomUIManager instance = new CustomUIManager();

	private CustomUIManager() {

	}

	public static CustomUIManager getInstance() {
		return instance;
	}

	public void createGameBoardGUI() {
		createHealthBar();
		createShieldBar();
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

}