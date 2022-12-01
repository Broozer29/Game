package game.objects.enemies;

import java.util.Random;

import data.DataClass;
import data.movement.Trajectory;
import game.managers.AnimationManager;
import game.managers.MissileManager;
import image.objects.Sprite;

public class Enemy extends Sprite {

	protected MissileManager missileManager = MissileManager.getInstance();
	protected AnimationManager animationManager = AnimationManager.getInstance();
	protected float hitPoints;
	protected float maxHitPoints;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected Random random = new Random();
	protected int movementSpeed;
	protected int currentBoardBlock;
	protected String rotation;
	protected boolean hasAttack;
	protected String direction;
	protected Trajectory trajectory = new Trajectory();
	protected String deathSound;
	protected boolean showHealthBar;

	public Enemy(int x, int y, String direction) {
		super(x, y);
		this.direction = direction;
		this.currentBoardBlock = 8;
	}

	// Called when there is collision between friendly missile and enemy
	public void takeDamage(float damageTaken) {
		if (animationManager == null) {
			animationManager = AnimationManager.getInstance();
		}
		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
			animationManager.addDestroyedExplosion(xCoordinate, yCoordinate);
			this.setVisible(false);
		}
	}

	public float getCurrentHitpoints() {
		return this.hitPoints;
	}

	public float getMaxHitpoints() {
		return this.maxHitPoints;
	}

	public String getEnemyDirection() {
		return this.direction;
	}

	public int getMovementSpeed() {
		return this.movementSpeed;
	}
	
	public boolean showhealthBar() {
		return this.showHealthBar;
	}
	
	public String getDeathSound() {
		return this.deathSound;
	}

	public boolean getHasAttack() {
		return this.hasAttack;
	}

	public int getBoardBlockNumber() {
		return this.currentBoardBlock;
	}

	//Required for the trajectory to determine the length of the distance travelled
	public int getAdditionalXSteps() {
		return Math.abs(DataClass.getInstance().getWindowWidth() - xCoordinate);
	}

	//Required for the trajectory to determine the length of the distance travelled
	public int getAdditionalYSteps() {
		switch (direction) {
		case ("Up"):
			return Math.abs(DataClass.getInstance().getWindowHeight() - yCoordinate);
		case ("Down"):
			return Math.abs(DataClass.getInstance().getWindowHeight() + Math.abs(yCoordinate));
		}
		return 0;
	}

	protected void setRotation(String rotation) {
		this.rotation = rotation;
		switch (rotation) {
		case ("Up"):
			rotateImage(Math.PI * 1.5);
			break;
		case ("Down"):
			rotateImage(Math.PI * 0.5);
			break;
		case ("Left"):
			rotateImage(0);
			break;
		case ("Right"):
			rotateImage(Math.PI);
			break;
		}
	}
}
