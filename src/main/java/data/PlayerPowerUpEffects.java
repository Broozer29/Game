package data;

public class PlayerPowerUpEffects {
	boolean doubleShotActive;
	boolean tripleShotActive;

	private static PlayerPowerUpEffects instance = new PlayerPowerUpEffects();

	private PlayerPowerUpEffects() {
		initDefaultSettings();
	}

	public void initDefaultSettings() {
		doubleShotActive = false;
		tripleShotActive = false;
	}

	public static PlayerPowerUpEffects getInstance() {
		return instance;
	}

	public boolean getDoubleShotActive() {
		return doubleShotActive;
	}

	public void setDoubleShotActive(boolean doubleShotActive) {
		this.doubleShotActive = doubleShotActive;
	}

	public boolean getTripleShotActive() {
		return tripleShotActive;
	}

	public void setTripleShotActive(boolean tripleShotActive) {
		this.tripleShotActive = tripleShotActive;
	}

}
