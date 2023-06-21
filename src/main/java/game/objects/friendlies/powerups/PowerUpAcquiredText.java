package game.objects.friendlies.powerups;

public class PowerUpAcquiredText {

	private String text;
	private float transparencyValue;
	private int xCoordinate;
	private int yCoordinate;
	private PowerUps powerUpType;
	
	public PowerUpAcquiredText(int xCoordinate, int yCoordinate, PowerUps powerUpType) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.powerUpType = powerUpType;
		this.transparencyValue = 1;
		getTextByPowerUpType();
	}
	
	
	private void getTextByPowerUpType() {
		switch(this.powerUpType) {
		case DOUBLE_SHOT:
			this.text = "Double Shot";
			break;
		case HEALTH_AND_SHIELD_RESTORE:
			this.text = "Health & Shield Restore";
			break;
		case INCREASED_MOVEMENT_SPEED:
			this.text = "Increased Movement Speed";
			break;
		case INCREASED_NORMAL_ATTACK_SPEED:
			this.text = "Increased Attack Speed";
			break;
		case INCREASED_NORMAL_DAMAGE:
			this.text = "Increased Attack Damage";
			break;
		case INCREASED_SPECIAL_ATTACK_SPEED:
			this.text = "Increased Special Attack Speed";
			break;
		case INCREASED_SPECIAL_DAMAGE:
			this.text = "Increased Special Attack Damage";
			break;
		case TRIPLE_SHOT:
			this.text = "Triple Shot";
			break;
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
}
