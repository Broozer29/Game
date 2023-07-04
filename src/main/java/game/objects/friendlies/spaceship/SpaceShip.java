package game.objects.friendlies.spaceship;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.PlayerStats;
import data.TemporaryGameSettings;
import data.image.ImageEnums;
import game.managers.AnimationManager;
import game.managers.MovementInitiator;
import game.objects.Explosion;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class SpaceShip extends Sprite {

	private int directionx;
	private int directiony;

	private float currentShieldRegenDelayFrame;

	private boolean isEngineBoosted;

	private PlayerStats playerStats = PlayerStats.getInstance();
	private TemporaryGameSettings powerUpEffects = TemporaryGameSettings.getInstance();

	private List<SpriteAnimation> playerFollowingAnimations = new ArrayList<SpriteAnimation>();
	private List<Explosion> playerFollowingExplosions = new ArrayList<Explosion>();
	private List<SpaceShipRegularGun> spaceShipGuns = new ArrayList<SpaceShipRegularGun>();
	private List<SpaceShipSpecialGun> spaceShipSpecialGuns = new ArrayList<SpaceShipSpecialGun>();
	public List<SpecialAttack> playerFollowingSpecialAttacks = new ArrayList<SpecialAttack>();

	public SpaceShip() {
		super(DataClass.getInstance().getWindowWidth() / 10, DataClass.getInstance().getWindowHeight() / 2, 1);
		playerStats = PlayerStats.getInstance();
		powerUpEffects = TemporaryGameSettings.getInstance();
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
		isEngineBoosted = false;
		playerStats.initDefaultSettings();
		powerUpEffects.initDefaultSettings();
		SpaceShipRegularGun gun = new SpaceShipRegularGun();
		this.spaceShipGuns.add(gun);
		SpaceShipSpecialGun specialGun = new SpaceShipSpecialGun();
		this.spaceShipSpecialGuns.add(specialGun);
		
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
		for (SpaceShipRegularGun gun : spaceShipGuns) {
			gun.updateFrameCount();
		}
		
		for(SpaceShipSpecialGun specialGun : spaceShipSpecialGuns) {
			specialGun.updateFrameCount();
		}
		this.currentShieldRegenDelayFrame++;
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
			MovementInitiator.getInstance().movePlayerFollowingExplosion(explosion, this);
		}
		
		for (SpecialAttack attack : playerFollowingSpecialAttacks) {
			MovementInitiator.getInstance().movePlayerFollowingSpecialAttack(attack, this);
		}

		xCoordinate += directionx;
		yCoordinate += directiony;

		playerStats.setHomingRectangleYCoordinate((int) (yCoordinate - (height * playerStats.getHomingRectangleResizeScale())));
		playerStats.setHomingRectangleXCoordinate((int) (xCoordinate - (width * playerStats.getHomingRectangleResizeScale())));
		playerStats.setHomingRectangleWidth((int) (width * (playerStats.getHomingRectangleResizeScale() * 2)));
		playerStats.setHomingRectangleHeight((int) (height * (playerStats.getHomingRectangleResizeScale() * 2.25)));
		
		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		
		if (playerStats.getExhaustAnimation() != null) {
			playerStats.getExhaustAnimation().setX(this.getCenterXCoordinate() - (this.getWidth()));
			playerStats.getExhaustAnimation()
					.setY(this.getCenterYCoordinate() - (playerStats.getExhaustAnimation().getHeight() / 2) + 5);
		}

	}

	// Launch a missile from the center point of the spaceship
	private void fire() throws UnsupportedAudioFileException, IOException {
		for (SpaceShipRegularGun gun : spaceShipGuns) {
			gun.fire(this.xCoordinate, this.yCoordinate, this.getWidth(), this.getHeight(), playerStats.getAttackType());
		}
	}

	private void fireSpecialAttack() throws UnsupportedAudioFileException, IOException {
		for(SpaceShipSpecialGun gun : spaceShipSpecialGuns) {
			gun.fire(this.xCoordinate, this.yCoordinate, this.getWidth(), this.getHeight(), playerStats.getPlayerSpecialAttackType());
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
					directionx = -playerStats.getCurrentMovementSpeed(isEngineBoosted);
					break;
				case (KeyEvent.VK_D):
				case (KeyEvent.VK_RIGHT):
					directionx = playerStats.getCurrentMovementSpeed(isEngineBoosted);
					break;
				case (KeyEvent.VK_W):
				case (KeyEvent.VK_UP):
					directiony = -playerStats.getCurrentMovementSpeed(isEngineBoosted);
					break;
				case (KeyEvent.VK_S):
				case (KeyEvent.VK_DOWN):
					directiony = playerStats.getCurrentMovementSpeed(isEngineBoosted);
					break;
				case (KeyEvent.VK_Q):
				case (KeyEvent.VK_ENTER):
					try {
						fireSpecialAttack();
					} catch (UnsupportedAudioFileException | IOException e1) {
						e1.printStackTrace();
					}
					break;
				case (KeyEvent.VK_SHIFT):
				case (KeyEvent.VK_E):
					isEngineBoosted = true;
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
			isEngineBoosted = false;
			swapExhaust(playerStats.getEngineType());
		}
	}

	public void repairHealth(float healAmount) {
		playerStats.changeHitPoints(healAmount);
	}

	public void repairShields(float healAmount) {
		playerStats.changeShieldHitpoints(healAmount);
	}

}