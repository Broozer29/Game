package game.objects.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.DataClass;
import data.movement.Trajectory;
import game.managers.AnimationManager;
import game.managers.MissileManager;
import image.objects.Animation;
import image.objects.Sprite;

public class Enemy extends Sprite {

	protected MissileManager missileManager = MissileManager.getInstance();
	protected AnimationManager animationManager = AnimationManager.getInstance();
	protected Random random = new Random();

	// Enemy combat stats
	protected float hitPoints;
	protected float maxHitPoints;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected boolean hasAttack;

	// Enemy movement/direction
	protected int movementSpeed;
	protected int currentBoardBlock;
	protected String rotation;
	protected String direction;
	protected Trajectory trajectory;
	protected int angleModuloDivider;

	// Enemy miscellanious attributes
	protected String enemyType;
	protected String deathSound;
	protected boolean showHealthBar;
	protected List<Integer> boardBlockSpeeds = new ArrayList<Integer>();
	protected Animation exhaustAnimation;
	protected Animation deathAnimation;

	public Enemy(int x, int y, String direction, String enemyType, float scale) {
		super(x, y, scale);
		this.enemyType = enemyType;
		this.direction = direction;
		this.currentBoardBlock = 8;
	}

	protected void setExhaustanimation(String imageType) {
		this.exhaustAnimation = new Animation(xCoordinate, yCoordinate, imageType, true, scale);
	}

	protected void setDeathAnimation(String imageType) {
		this.deathAnimation = new Animation(xCoordinate, yCoordinate, imageType, false, scale);
	}

	// Called when there is collision between friendly missile and enemy
	public void takeDamage(float damageTaken) {
		if (animationManager == null) {
			animationManager = AnimationManager.getInstance();
		}
		this.hitPoints -= damageTaken;
		if (this.hitPoints <= 0) {
			this.deathAnimation.setX(this.getCenterXCoordinate() - (deathAnimation.getWidth() / 2));
			this.deathAnimation.setY(this.getCenterYCoordinate() - (deathAnimation.getHeight() / 2));
			animationManager.addDestroyedExplosion(deathAnimation);
			this.setVisible(false);
		}
	}

	public void updateBoardBlock() {
		int boardBlockSize = DataClass.getInstance().getWindowWidth() / 8;
		if (xCoordinate >= 0 && xCoordinate <= (boardBlockSize * 1)) {
			this.movementSpeed = boardBlockSpeeds.get(0);
		} else if (xCoordinate >= (boardBlockSize * 1) && xCoordinate <= (boardBlockSize * 2)) {
			this.movementSpeed = boardBlockSpeeds.get(1);
		} else if (xCoordinate >= (boardBlockSize * 2) && xCoordinate <= (boardBlockSize * 3)) {
			this.movementSpeed = boardBlockSpeeds.get(2);
		} else if (xCoordinate >= (boardBlockSize * 3) && xCoordinate <= (boardBlockSize * 4)) {
			this.movementSpeed = boardBlockSpeeds.get(3);
		} else if (xCoordinate >= (boardBlockSize * 4) && xCoordinate <= (boardBlockSize * 5)) {
			this.movementSpeed = boardBlockSpeeds.get(4);
		} else if (xCoordinate >= (boardBlockSize * 5) && xCoordinate <= (boardBlockSize * 6)) {
			this.movementSpeed = boardBlockSpeeds.get(5);
		} else if (xCoordinate >= (boardBlockSize * 6) && xCoordinate <= (boardBlockSize * 7)) {
			this.movementSpeed = boardBlockSpeeds.get(6);
		} else if (xCoordinate >= (boardBlockSize * 7) && xCoordinate <= (boardBlockSize * 8)) {
			this.movementSpeed = boardBlockSpeeds.get(7);
		} else if (xCoordinate > boardBlockSize * 8) {
			this.movementSpeed = boardBlockSpeeds.get(7);
		}
		this.trajectory.updateMovementSpeed(movementSpeed);
	}

	// Called every loop to move the enemy
	public void move() {
		List<Integer> newCoordsList = trajectory.getPathCoordinates(xCoordinate, yCoordinate);
		xCoordinate = newCoordsList.get(0);
		yCoordinate = newCoordsList.get(1);
		if (direction.contains("Up")) {
			if (yCoordinate <= 0) {
				this.setVisible(false);
			}
		} else if (direction.contains("Down")) {
			if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
		} else if (direction.contains("Left")) {
			if (xCoordinate < 0) {
				this.setVisible(false);
			}
		} else if (direction.contains("Right")) {
			if (xCoordinate > DataClass.getInstance().getWindowWidth()) {
				this.setVisible(false);
			}
		}
		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() + (this.getWidth() / 2));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2));
		}
	}

	// Random offset for the origin of the missile the enemy shoots
	protected int calculateRandomWeaponHeightOffset() {
		int upOrDown = random.nextInt((1 - 0) + 1) + 1;
		int yOffset = random.nextInt(((this.getHeight() / 2) - 0) + 1) + 0;
		if (upOrDown == 1) {
			return yOffset;
		} else {
			return -yOffset;
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

	public Animation getExhaustAnimation() {
		return this.exhaustAnimation;
	}

	public String getEnemyType() {
		return this.enemyType;
	}

	public int getAngleModuloDivider() {
		return this.angleModuloDivider;
	}

	public Animation getDestroyedAnimation() {
		return this.deathAnimation;
	}
	
	public void updateTrajectory() {
		switch(trajectory.getTrajectoryType()) {
		case("Regular"):
			trajectory.updateRegularPath();
			break;
		case("Homing"):
			trajectory.updateEnemyHomingPaths(this);
			break;
		}
	}

	protected int getTotalTravelDistance() {
		if (direction.contains("Up") || direction.contains("Down")) {
			return DataClass.getInstance().getWindowHeight() + getAdditionalYSteps();
		} else
			return DataClass.getInstance().getWindowWidth() + getAdditionalXSteps();
	}

	// Required for the trajectory to determine the length of the distance travelled
	protected int getAdditionalXSteps() {
		//Somehow comes up short 
//		return Math.abs(DataClass.getInstance().getWindowWidth() - (xCoordinate + width));
		return Math.abs(DataClass.getInstance().getWindowWidth());
	}

	// Required for the trajectory to determine the length of the distance travelled
	protected int getAdditionalYSteps() {
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
		rotateImage(rotation);
	}
}
