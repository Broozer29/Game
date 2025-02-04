package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.LinkedList;

public class PlayerManager {

    private static PlayerManager instance = new PlayerManager();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();
    private SpaceShip spaceship;
    private PerformanceLogger performanceLogger;

    private PlayerManager () {
        this.performanceLogger = new PerformanceLogger("Player Manager");
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
        performanceLogger.reset();
    }

    public void createSpaceShip () {
        initSpaceShip();
    }

    public void updateGameTick () {
//        PerformanceLoggerManager.timeAndLog(performanceLogger, "Total", () -> {
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Ship Movement", this::updateSpaceShipMovement);
//        updateSpaceShipMovement();
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Check Player Health", this::checkPlayerHealth);
//        checkPlayerHealth();
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Update SpaceShip", spaceship::updateGameTick);
//        });
    }



    public SpaceShip getSpaceship () {
        if (this.spaceship == null) {
            initSpaceShip();
        }
        return this.spaceship;
    }

    private void checkPlayerHealth () {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairBot) != null) {
            PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairBot).applyEffectToObject(spaceship);
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

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}