package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.friendlies.powerups.PowerUpAcquiredText;

public class OnScreenTextManager {

	private static OnScreenTextManager instance = new OnScreenTextManager();
	private List<PowerUpAcquiredText> powerUpTexts = new ArrayList<PowerUpAcquiredText>();

	private OnScreenTextManager() {

	}

	public static OnScreenTextManager getInstance() {
		return instance;
	}
	
	
	public void updateGameTick() {
		removeInvisibleText();
	}
	
	private void removeInvisibleText() {
		for (int i = 0; i < powerUpTexts.size(); i++){
			PowerUpAcquiredText text = powerUpTexts.get(i);
			if(text.getTransparencyValue() < 0.05) {
				powerUpTexts.remove(i);
			}
		}
	}

	public void addPowerUpText(PowerUpAcquiredText text) {
		if (!powerUpTexts.contains(text)) {
			powerUpTexts.add(text);
		}
	}
	

	public List<PowerUpAcquiredText> getPowerUpTexts() {
		return powerUpTexts;
	}

}
