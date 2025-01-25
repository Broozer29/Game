package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.LinkedList;

public class PlayerManager {

    private static PlayerManager instance = new PlayerManager();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();
    private SpaceShip spaceship;

    private PlayerManager () {
//		initSpaceShip();
    }

    public static PlayerManager getInstance () {
        return instance;
    }

    // Called when a game instance needs to be deleted and the manager needs to be
    // reset.
    public void resetManager () {
        if (spaceship != null) {
            for(EffectInterface effectInterface : spaceship.getEffects()){
                effectInterface.removeEffect(spaceship);
            }
            spaceship.deleteObject();
            spaceship = null;
        }
    }

    public void createSpaceShip () {
        initSpaceShip();
    }

    public void updateGameTick () {
        updateSpaceShipMovement();
        checkPlayerHealth();
        spaceship.updateGameTick();
    }

    public SpaceShip getSpaceship () {
        if (this.spaceship == null) {
            initSpaceShip();
        }
        return this.spaceship;
    }

    private void checkPlayerHealth () {
        if (PlayerInventory.getInstance().getItemByName(ItemEnums.EmergencyRepairBot) != null) {
            PlayerInventory.getInstance().getItemByName(ItemEnums.EmergencyRepairBot).applyEffectToObject(spaceship);
        }
        if (spaceship.getCurrentHitpoints() <= 0 && gameState.getGameState() == GameStatusEnums.Playing) {
            gameState.setGameState(GameStatusEnums.Dying);
            spaceship.setVisible(false);
        }
    }

    private void updateSpaceShipMovement () {
        if (spaceship.isVisible()) {
            spaceship.move();
        }
    }

    private void initSpaceShip () {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(DataClass.getInstance().getWindowWidth() / 10);
        spriteConfiguration.setyCoordinate(DataClass.getInstance().getWindowHeight() / 2);
        spriteConfiguration.setScale(0.6f);
        spriteConfiguration.setImageType(ImageEnums.Player_Spaceship_Model_3);
        this.spaceship = new SpaceShip(spriteConfiguration);
    }

    public void startDyingScene () {
        if (spaceship.getCurrentHitpoints() <= 0 && gameState.getGameState() == GameStatusEnums.Dying) {
            spaceship.setImmune(true); //Ignore enemies and missiles whilst exploding
            if (!animationManager.getUpperAnimations().contains(spaceship.getDestructionAnimation())) {
                animationManager.getUpperAnimations().add(spaceship.getDestructionAnimation());
                animationManager.getLowerAnimations().remove(spaceship.getExhaustAnimation());

                AudioManager.getInstance().stopMusicAudio();
                AudioManager.getInstance().addAudio(AudioEnums.Destroyed_Explosion);
            }

            if (spaceship.getDestructionAnimation().getCurrentFrame() >= spaceship.getDestructionAnimation().getTotalFrames()) {
                gameState.setGameState(GameStatusEnums.Dead);

                PlayerInventory.getInstance().resetInventory();
            }
        }
    }

    public LinkedList<Integer> getNearestFriendlyHomingCoordinates () {
        LinkedList<Integer> playerCoordinatesList = new LinkedList<>();
        playerCoordinatesList.add(0, spaceship.getCenterXCoordinate());
        playerCoordinatesList.add(1, spaceship.getCenterYCoordinate());
        return playerCoordinatesList;
    }

}