package gameObjectes;

import Data.DataClass;

public class Enemy extends Sprite {

	private float hitPoints;
	private String enemyType;

	public Enemy(int x, int y, String enemyType) {
		super(x, y);
		this.enemyType = enemyType;
		initEnemy();
	}

	public void takeDamage(float damageTaken) {
		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
			this.setVisible(false);
		}
	}

	private void initEnemy() {
		if (this.enemyType.equals("Alien")) {
			this.hitPoints = 35;
		}
	}
	
	public void move() {
		if (xCoordinate < 0) {
			xCoordinate = DataClass.getInstance().getWindowWidth();
		}
		xCoordinate -= 1;
	}

}
