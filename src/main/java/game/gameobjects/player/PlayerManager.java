package game.gameobjects.player;

import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.items.enums.ItemEnums;
import game.items.PlayerInventory;
import game.managers.AnimationManager;
import game.gameobjects.player.spaceship.SpaceShip;
import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private GameStateInfo gameState = GameStateInfo.getInstance();
	private SpaceShip spaceship;

	private PlayerManager() {
//		initSpaceShip();
	}

	public static PlayerManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		if(spaceship != null){
			spaceship.deleteObject();
			spaceship = null;
		}
	}

	public void createSpaceShip(){
		initSpaceShip();
	}

	public void updateGameTick() {
		updateSpaceShipMovement();
		checkPlayerHealth();
		spaceship.updateGameTick();
	}
	public SpaceShip getSpaceship() {
		if(this.spaceship == null){
			initSpaceShip();
		}
		return this.spaceship;
	}

	private void checkPlayerHealth() {
		if(PlayerInventory.getInstance().getItemByName(ItemEnums.EmergencyRepairBot) != null){
			PlayerInventory.getInstance().getItemByName(ItemEnums.EmergencyRepairBot).applyEffectToObject(spaceship);
		}
		if (spaceship.getCurrentHitpoints() <= 0 && gameState.getGameState() == GameStatusEnums.Playing) {
			gameState.setGameState(GameStatusEnums.Dying);
			spaceship.setVisible(false);
		}
	}

	private void updateSpaceShipMovement() {
		if (spaceship.isVisible()) {
			spaceship.move();
		}
	}

	private void initSpaceShip() {
		SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
		spriteConfiguration.setxCoordinate(DataClass.getInstance().getWindowWidth() / 10);
		spriteConfiguration.setyCoordinate(DataClass.getInstance().getWindowHeight() /2);
		spriteConfiguration.setScale(0.6f);
		spriteConfiguration.setImageType(ImageEnums.Player_Spaceship_Model_3);
		this.spaceship = new SpaceShip(spriteConfiguration);
	}

	public void startDyingScene() {
		if (spaceship.getCurrentHitpoints() <= 0 && gameState.getGameState() == GameStatusEnums.Dying) {
			if (!animationManager.getUpperAnimations().contains(spaceship.getDestructionAnimation())) {
				animationManager.getUpperAnimations().add(spaceship.getDestructionAnimation());
				animationManager.getLowerAnimations().remove(spaceship.getExhaustAnimation());

				AudioManager.getInstance().stopMusicAudio();
				
				try {
					AudioManager.getInstance().addAudio(AudioEnums.Destroyed_Explosion);
				} catch (UnsupportedAudioFileException | IOException e) {
					e.printStackTrace();
				}
			}

			if (spaceship.getDestructionAnimation().getCurrentFrame() >= spaceship.getDestructionAnimation().getTotalFrames()) {
				gameState.setGameState(GameStatusEnums.Dead);
				spaceship.setImmune(true); //Ignore enemies and missiles whilst exploding
				PlayerInventory.getInstance().resetInventory();
			}
		}
	}

	public LinkedList<Integer> getNearestFriendlyHomingCoordinates() {
		LinkedList<Integer> playerCoordinatesList = new LinkedList<>();
		playerCoordinatesList.add(0, spaceship.getCenterXCoordinate());
		playerCoordinatesList.add(1, spaceship.getCenterYCoordinate());
		return playerCoordinatesList;
	}

}