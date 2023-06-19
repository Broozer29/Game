package game.objects.friendlies.friendlyobjects;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.PlayerPowerUpEffects;
import data.PlayerStats;
import data.audio.AudioEnums;
import data.image.enums.ImageEnums;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.managers.ExplosionManager;
import game.managers.FriendlyObjectManager;
import game.movement.Direction;
import game.objects.Explosion;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class SpaceShip extends Sprite {

	private int directionx;
	private int directiony;

	private float currentSpecialAttackFrame;
	private float currentShieldRegenDelayFrame;


	private PlayerStats playerStats = PlayerStats.getInstance();
	private PlayerPowerUpEffects powerUpEffects = PlayerPowerUpEffects.getInstance();
	

	private List<SpriteAnimation> playerFollowingAnimations = new ArrayList<SpriteAnimation>();
	private List<Explosion> playerFollowingExplosions = new ArrayList<Explosion>();
	private List<SpaceShipRegularGun> spaceShipGuns = new ArrayList<SpaceShipRegularGun>();

	public SpaceShip() {
		super(DataClass.getInstance().getWindowWidth() / 10, DataClass.getInstance().getWindowHeight() / 2, 1);
		playerStats = PlayerStats.getInstance();
		powerUpEffects = PlayerPowerUpEffects.getInstance();
		loadImage(playerStats.getSpaceShipImage());
		setExhaustAnimation(playerStats.getExhaustImage());
		initShip();
	}

	protected void setExhaustAnimation(ImageEnums imageType) {
		SpriteAnimation exhaustAnimation = new SpriteAnimation(xCoordinate, yCoordinate, imageType, true, scale);
		playerStats.setExhaustAnimation(exhaustAnimation);
		playerStats.setCurrentExhaust(imageType);
	}

	// Called when managers need to be reset.
	public void resetSpaceship() {
		initShip();
	}

	private void initShip() {
		directionx = 0;
		directiony = 0;
		currentShieldRegenDelayFrame = 0;
		currentSpecialAttackFrame = 100;
		playerStats.initDefaultSettings();
		powerUpEffects.initDefaultSettings();
		SpaceShipRegularGun gun = new SpaceShipRegularGun();
		this.spaceShipGuns.add(gun);
	}

	public void addShieldDamageAnimation() {
		if (playerFollowingAnimations.size() < 10) {
			SpriteAnimation shieldAnimation = new SpriteAnimation(this.xCoordinate, this.yCoordinate,
					ImageEnums.Default_Player_Shield_Damage, false, 1);

			shieldAnimation.setFrameDelay(2);

			int yDist = 30;
			int xDist = 10;
			shieldAnimation.addXOffset(-xDist);
			shieldAnimation.addYOffset(-yDist);
			playerFollowingAnimations.add(shieldAnimation);

			shieldAnimation.setX(this.xCoordinate);
			shieldAnimation.setY(this.yCoordinate);
			AnimationManager.getInstance().addUpperAnimation(shieldAnimation);

		}

	}

	private void cycleFollowingAnimations() {
		for (int i = 0; i < playerFollowingAnimations.size(); i++) {
			if (!playerFollowingAnimations.get(i).isVisible()) {
				playerFollowingAnimations.remove(i);
			}
		}
		for (int i = 0; i < playerFollowingExplosions.size(); i++) {
			if (!playerFollowingExplosions.get(i).isVisible()) {
				playerFollowingExplosions.remove(i);
			}
		}
	}

	public void takeHitpointDamage(float damage) {
		float shieldPiercingDamage = playerStats.getShieldHitpoints() - damage;
		if (shieldPiercingDamage < 0) {
			playerStats.changeHitPoints(shieldPiercingDamage);
			playerStats.setShieldHitpoints(0);
		} else {
			playerStats.changeShieldHitpoints(-damage);
			addShieldDamageAnimation();
		}

		this.currentShieldRegenDelayFrame = 0;

	}

	public void updateGameTick() {
		for(SpaceShipRegularGun gun : spaceShipGuns) {
			gun.updateFrameCount();
		}
		this.currentShieldRegenDelayFrame++;
		this.currentSpecialAttackFrame++;
		cycleFollowingAnimations();

		if (currentShieldRegenDelayFrame >= playerStats.getShieldRegenDelay()) {
			if (playerStats.getShieldHitpoints() < playerStats.getMaxShieldHitPoints()) {
				repairShields((float) 0.4);
				repairHealth((float) (0.4));
			}
		}
	}

	// Moves the spaceship
	public void move() {
		for (SpriteAnimation anim : playerFollowingAnimations) {
			anim.setX(xCoordinate);
			anim.setY(yCoordinate);
		}

		for (Explosion explosion : playerFollowingExplosions) {
			explosion.setX(xCoordinate);
			explosion.setY(yCoordinate);
			explosion.getAnimation().setX(xCoordinate);
			explosion.getAnimation().setY(yCoordinate);
		}

		xCoordinate += directionx;
		yCoordinate += directiony;

		float homingRectangleResizeScale = playerStats.getHomingRectangleResizeScale();
		playerStats.setHomingRectangleYCoordinate((int) (yCoordinate - (height * homingRectangleResizeScale)));
		playerStats.setHomingRectangleXCoordinate((int) (xCoordinate - (width * homingRectangleResizeScale)));
		playerStats.setHomingRectangleWidth((int) (width * (homingRectangleResizeScale * 2)));
		playerStats.setHomingRectangleHeight((int) (height * (homingRectangleResizeScale * 2.25)));

		if (playerStats.getExhaustAnimation() != null) {
			playerStats.getExhaustAnimation().setX(this.getCenterXCoordinate() - (this.getWidth()));
			playerStats.getExhaustAnimation()
					.setY(this.getCenterYCoordinate() - (playerStats.getExhaustAnimation().getHeight() / 2) + 5);
		}

	}

	// Launch a missile from the center point of the spaceship
	private void fire() throws UnsupportedAudioFileException, IOException {
		for (SpaceShipRegularGun gun : spaceShipGuns) {
			gun.fire(this.xCoordinate, this.yCoordinate, this.getWidth(), this.getHeight());
		}
	}

	private void fireSpecialAttack() {
		if (currentSpecialAttackFrame >= playerStats.getSpecialAttackSpeed()) {
			SpriteAnimation specialAttackAnimation = new SpriteAnimation(this.getCenterXCoordinate(),
					this.getCenterYCoordinate(), playerStats.getPlayerEMPType(), false, 1);
			specialAttackAnimation.setFrameDelay(4);
			
			
			Explosion specialAttack = new Explosion(this.getCenterXCoordinate(), this.getCenterYCoordinate(), 1, specialAttackAnimation, 3, true);
			specialAttack.addXOffset(-(specialAttackAnimation.getWidth() / 4));
			specialAttack.addYOffset(-(specialAttackAnimation.getHeight() / 2));

			specialAttackAnimation.addXOffset(-(specialAttackAnimation.getWidth() / 4));
			specialAttackAnimation.addYOffset(-(specialAttackAnimation.getHeight() / 2));

			try {
				AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
				playerFollowingExplosions.add(specialAttack);
				ExplosionManager.getInstance().addExistingExplosion(specialAttack);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			this.currentSpecialAttackFrame = 0;
		}
	}

	private void swapExhaust(ImageEnums newEngineType) {
		if (!newEngineType.equals(playerStats.getCurrentExhaust())) {
			float scale = 1;
			int xOffset = 0;
			if (playerStats.getCurrentExhaust().equals(ImageEnums.Default_Player_Engine_Boosted)) {
				scale = 1;
				xOffset = 0;
			} else {
				scale = 1;
				xOffset = -30;
			}
			playerStats.getExhaustAnimation().changeImagetype(newEngineType);
			playerStats.getExhaustAnimation().setAnimationScale(scale);
			playerStats.getExhaustAnimation().addXOffset(xOffset);
			playerStats.setCurrentExhaust(newEngineType);
		}
	}

	private final Set<Integer> pressedKeys = new HashSet<>();

	public synchronized void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());
		if (!pressedKeys.isEmpty()) {
			for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
				switch (it.next()) {
				case (KeyEvent.VK_SPACE):
					try {
						fire();
					} catch (UnsupportedAudioFileException | IOException e1) {
						e1.printStackTrace();
					}
					break;
				case (KeyEvent.VK_A):
				case (KeyEvent.VK_LEFT):
					directionx = -playerStats.getMovementSpeed();
					break;
				case (KeyEvent.VK_D):
				case (KeyEvent.VK_RIGHT):
					directionx = playerStats.getMovementSpeed();
					break;
				case (KeyEvent.VK_W):
				case (KeyEvent.VK_UP):
					directiony = -playerStats.getMovementSpeed();
					break;
				case (KeyEvent.VK_S):
				case (KeyEvent.VK_DOWN):
					directiony = playerStats.getMovementSpeed();
					break;
				case (KeyEvent.VK_Q):
				case (KeyEvent.VK_ENTER):
					fireSpecialAttack();
					break;
				case (KeyEvent.VK_SHIFT):
				case (KeyEvent.VK_E):
					playerStats.setMovementSpeed(8);
					swapExhaust(playerStats.getBoostedEngineType());
					break;
				}
			}
		}
	}

	public synchronized void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_Q) {
		}
		if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
			directionx = 0;
		}
		if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			directionx = 0;
		}
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			directiony = 0;
		}
		if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			directiony = 0;
		}
		if (key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_E) {
			playerStats.setMovementSpeed(4);
			swapExhaust(playerStats.getDefaultEngineType());
		}
	}

	public void repairHealth(float healAmount) {
		playerStats.changeHitPoints(healAmount);
	}

	public void repairShields(float healAmount) {
		playerStats.changeShieldHitpoints(healAmount);
	}

}