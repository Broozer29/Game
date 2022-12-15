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
	
	//Enemy combat stats
	protected float hitPoints;
	protected float maxHitPoints;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected boolean hasAttack;
	
	//Enemy movement/direction
	protected int movementSpeed;
	protected int currentBoardBlock;
	protected String rotation;
	protected String direction;
	protected Trajectory trajectory = new Trajectory();
	protected int angleModuloDivider;
	
	//Enemy miscellanious attributes
	protected String enemyType;
	protected String deathSound;
	protected boolean showHealthBar;
	protected List<Integer> boardBlockSpeeds = new ArrayList<Integer>();
	protected Animation exhaustAnimation;

	public Enemy(int x, int y, String direction, String enemyType) {
		super(x, y);
		this.enemyType = enemyType;
		this.direction = direction;
		this.currentBoardBlock = 8;
	}
	
	protected void setExhaustanimation(String imageType) {
		this.exhaustAnimation = new Animation(xCoordinate, yCoordinate, imageType, true);
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

		if (direction.equals("Up")) {
			if (yCoordinate <= 0) {
				this.setVisible(false);
			}
		} else if (direction.equals("Down")) {
			if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
		} else if (direction.equals("Left")) {
			if (xCoordinate < 0) {
				this.setVisible(false);
			}
		} else if (direction.equals("Right")) {
			if (xCoordinate > DataClass.getInstance().getWindowWidth()) {
				this.setVisible(false);
			}
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

	// Required for the trajectory to determine the length of the distance travelled
	public int getAdditionalXSteps() {
		return Math.abs(DataClass.getInstance().getWindowWidth() - xCoordinate + width);
	}

	// Required for the trajectory to determine the length of the distance travelled
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
		rotateImage(rotation);
	}
}
