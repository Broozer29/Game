package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.UI.UIObject;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OnScreenTextManager {

	private static OnScreenTextManager instance = new OnScreenTextManager();
	private List<OnScreenText> onScreenTexts = new ArrayList<>();
	private OnScreenTextManager() {
	}

	public static OnScreenTextManager getInstance() {
		return instance;
	}
	
	
	public void updateGameTick() {
		removeInvisibleText();
	}
	
	private void removeInvisibleText() {
		for (int i = 0; i < onScreenTexts.size(); i++){
			if(onScreenTexts.get(i).getTransparencyValue() < 0.05) {
				onScreenTexts.remove(i);
			}
		}
	}

	public void addTextObject (OnScreenText text) {
		if (!onScreenTexts.contains(text)) {
			onScreenTexts.add(text);
		}
	}

	public void addText(String text){
		OnScreenText onScreenText = new OnScreenText(DataClass.getInstance().getWindowWidth() / 2,
				DataClass.getInstance().getWindowHeight() / 2,
				text);
		this.onScreenTexts.add(onScreenText);
	}

	public void addText(String text, int xCoordinate, int yCoordinate){
		OnScreenText onScreenText = new OnScreenText(xCoordinate, yCoordinate, text);
		this.onScreenTexts.add(onScreenText);
	}

	public void addText(String text, int xCoordinate, int yCoordinate, int fontSize){
		OnScreenText onScreenText = new OnScreenText(xCoordinate, yCoordinate, text);
		onScreenText.setFontSize(fontSize);
		this.onScreenTexts.add(onScreenText);
	}

	public void addMineralsGainedText(float amount){
		UIObject mineralIcon = GameUICreator.getInstance().getMineralIcon();
		int xCoordinate = mineralIcon.getXCoordinate() - Math.round(mineralIcon.getWidth() * 0.5f);
		int yCoordinate = mineralIcon.getYCoordinate() + Math.round(mineralIcon.getHeight() * 1.5f);

		OnScreenText onScreenText = new OnScreenText(xCoordinate, yCoordinate, "+" + Math.round(amount));
		this.onScreenTexts.add(onScreenText);
	}

	public void addDamageNumberText(float damageNumber, int xCoordinate, int yCoordinate, boolean isCrit){
		OnScreenText onScreenText = new OnScreenText(xCoordinate, yCoordinate, String.valueOf(Math.round(damageNumber)));
		onScreenText.setTransparancyStepSize(0.0175f);

		if(isCrit){
			onScreenText.setFontSize(16);
			onScreenText.setColor(Color.ORANGE);
		} else {
			onScreenText.setFontSize(14);
			onScreenText.setColor(Color.YELLOW);
		}
		this.onScreenTexts.add(onScreenText);
	}



	public List<OnScreenText> getOnScreenTexts () {
		return onScreenTexts;
	}

	public void resetManager() {
		for(OnScreenText text : onScreenTexts){
			text.setTransparency(0.00f);
		}

		removeInvisibleText();

		onScreenTexts = new ArrayList<>();
	}

}