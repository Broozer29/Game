package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.util.OnScreenText;

public class OnScreenTextManager {

	private static OnScreenTextManager instance = new OnScreenTextManager();
	private List<OnScreenText> powerUpTexts = new ArrayList<OnScreenText>();
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
			if(powerUpTexts.get(i).getTransparencyValue() < 0.05) {
				powerUpTexts.remove(i);
			}
		}
	}

	public void addPowerUpText(OnScreenText text) {
		if (!powerUpTexts.contains(text)) {
			powerUpTexts.add(text);
		}
	}
	

	public List<OnScreenText> getPowerUpTexts() {
		return powerUpTexts;
	}

	public void resetManager() {
		for(OnScreenText text : powerUpTexts){
			text.setTransparency(0.00f);
		}

		removeInvisibleText();

		powerUpTexts = new ArrayList<OnScreenText>();
	}

}