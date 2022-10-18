package gameObjectes;

public class Missile extends Sprite {

	private final int BOARD_WIDTH = 1280;
	private final int MISSILE_SPEED = 2;
	private float missileDamage = (float) 7.5;

	public Missile(int x, int y) {
		super(x, y);
		initMissile();
	}

	private void initMissile() {
		loadImage("laserbeam");
		getImageDimensions();
	}

	public void move() {

		xCoordinate += MISSILE_SPEED;

		if (xCoordinate > BOARD_WIDTH) {
			visible = false;
		}
	}
	
	public float getMissileDamage() {
		return this.missileDamage;
	}
}