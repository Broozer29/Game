package gameObjectes;

import java.util.Random;

import Data.DataClass;
import gameManagers.MissileManager;

public class Enemy extends Sprite {

	private float hitPoints;
	private String enemyType;
	private float attackSpeedFrameCount;
	private float currentAttackSpeedFrameCount = 0;
	private Random random = new Random();

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
			this.attackSpeedFrameCount = 60;
			loadImage("Alien");
		}
	}

	public void move() {
		if (xCoordinate < 0) {
			xCoordinate = DataClass.getInstance().getWindowWidth();
		}
		xCoordinate -= 1;
	}

	public void fireAction() {
		if (currentAttackSpeedFrameCount == attackSpeedFrameCount) {
			MissileManager missileManager = MissileManager.getInstance();

			missileManager.addEnemyMissile(this.xCoordinate, this.yCoordinate + calculateRandomWeaponHeightOffset(),
					"AlienDefault");
			currentAttackSpeedFrameCount = 0;
		}
		if (currentAttackSpeedFrameCount < attackSpeedFrameCount) {
			this.currentAttackSpeedFrameCount++;
		}

	}

	private int calculateRandomWeaponHeightOffset() {
		int upOrDown = random.nextInt((1 - 0) + 1) + 1;
		int yOffset = random.nextInt(((this.getHeight() / 2) - 0) + 1) + 0;
		if (upOrDown == 1) {
			return yOffset;
		} else {
			return -yOffset;
		}
	}

}
