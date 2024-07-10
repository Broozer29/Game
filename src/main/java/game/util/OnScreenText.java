package game.util;

import game.gameobjects.powerups.PowerUpEnums;

import java.awt.*;

public class OnScreenText {

	private String text;
	private float transparencyValue;
	private int xCoordinate;
	private int yCoordinate;
	private PowerUpEnums powerUpType;

	private float transparancyStepSize;
	private int fontSize;
	private Color color;

	public OnScreenText(int xCoordinate, int yCoordinate, PowerUpEnums powerUpType) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.powerUpType = powerUpType;
		this.transparencyValue = 1;
		this.transparancyStepSize = (float) 0.01;
		this.fontSize = 10;
		this.color = Color.WHITE;
		getTextByPowerUpType();
	}

	public OnScreenText(int xCoordinate, int yCoordinate, String message){
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.transparencyValue = 1;
		this.transparancyStepSize = (float) 0.01;
		this.text = message;
		this.fontSize = 10;
		this.color = Color.WHITE;
	}

	private void getTextByPowerUpType() {
		switch(this.powerUpType) {
		case DOUBLE_SHOT:
			this.text = "Double Shot";
			break;
		case HEALTH_AND_SHIELD_RESTORE:
			this.text = "Health & Shield Restore";
			break;
		case INCREASED_NORMAL_DAMAGE:
			this.text = "Increased Attack Damage";
			break;
		case TRIPLE_SHOT:
			this.text = "Triple Shot";
			break;
		case DUMMY_DO_NOT_USE:
			this.text = "Test text at: x =" + this.xCoordinate + " y = " + this.yCoordinate;
			break;
		case Guardian_Drone:
			this.text = "Homing Missile Guardian Bot";
		
		default:
			break;
		
		}
	}


	public String getText() {
		return this.text;
	}


	public float getTransparencyValue() {
		return transparencyValue;
	}


	public int getXCoordinate() {
		return xCoordinate;
	}
	
	public int getYCoordinate() {
		return yCoordinate;
	}
	
	public void setXCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	
	public void setYCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	public void setTransparency(float transparancy) {
		this.transparencyValue = transparancy;
	}

	public float getTransparancyStepSize () {
		return transparancyStepSize;
	}

	public void setTransparancyStepSize (float transparancyStepSize) {
		this.transparancyStepSize = transparancyStepSize;
	}

	public int getFontSize () {
		return fontSize;
	}

	public void setFontSize (int fontSize) {
		this.fontSize = fontSize;
	}

	public Color getColor () {
		return color;
	}

	public void setColor (Color color) {
		this.color = color;
	}
}
