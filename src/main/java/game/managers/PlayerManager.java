package game.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.objects.Explosion;
import game.objects.friendlies.spaceship.SpaceShip;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import gamedata.GameStateInfo;
import gamedata.GameStatusEnums;
import gamedata.PlayerStats;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import visual.objects.SpriteAnimation;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private GameStateInfo gameState = GameStateInfo.getInstance();
	private PlayerStats playerStats = PlayerStats.getInstance();
	private SpaceShip spaceship;
	private List<Integer> playerCoordinatesList = new ArrayList<Integer>();

	private PlayerManager() {
		initSpaceShip();
	}

	public static PlayerManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		initSpaceShip();
	}

	public void updateGameTick() {
		updateSpaceShipMovement();
		checkPlayerHealth();
		spaceship.updateGameTick();
	}
	
	//Removes the temporary buffs from a spaceship for the next level
	public void resetSpaceshipForNextLevel() {
		this.spaceship.resetSpaceshipFollowingObjects();
	}
	
	public SpaceShip getSpaceship() {
		return this.spaceship;
	}

	private void checkPlayerHealth() {
		if (playerStats.getHitpoints() <= 0 && gameState.getGameState() == GameStatusEnums.Playing) {
			gameState.setGameState(GameStatusEnums.Dying);
			spaceship.setVisible(false);
		}
	}

	private void updateSpaceShipMovement() {
		if (spaceship.isVisible()) {
			spaceship.move();
			spaceship.updateCurrentBoardBlock();
		}
	}

	private void initSpaceShip() {
		this.spaceship = new SpaceShip();
	
	}

	public void startDyingScene() {
		if (playerStats.getHitpoints() <= 0 && gameState.getGameState() == GameStatusEnums.Dying) {
			if (!animationManager.getUpperAnimations().contains(spaceship.getDeathAnimation())) {
				animationManager.getUpperAnimations().add(spaceship.getDeathAnimation());
				animationManager.getLowerAnimations().remove(spaceship.getExhaustAnimation());

				if (AudioManager.getInstance().getBackgroundMusic() != null) {
					AudioManager.getInstance().getBackgroundMusic().stopClip();
				}
				
				try {
					AudioManager.getInstance().playMusicAudio(AudioEnums.Destroyed_Explosion);
				} catch (UnsupportedAudioFileException | IOException e) {
					e.printStackTrace();
				}
			}

			if (spaceship.getDeathAnimation().getCurrentFrame() >= spaceship.getDeathAnimation().getTotalFrames()) {
				gameState.setGameState(GameStatusEnums.Dead);
			}
		}
	}

	public List<Integer> getNearestFriendlyHomingCoordinates() {
		playerCoordinatesList.set(0, spaceship.getCenterXCoordinate());
		playerCoordinatesList.set(1, spaceship.getCenterYCoordinate());
		return playerCoordinatesList;
	}

}