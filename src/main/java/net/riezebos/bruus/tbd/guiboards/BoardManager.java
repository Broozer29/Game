package net.riezebos.bruus.tbd.guiboards;

import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.guiboards.boards.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

public class BoardManager extends JFrame {

    private DataClass data = DataClass.getInstance();
    private MainMenuBoard mainMenuBoard;
    private GameBoard gameBoard;
    private ShopBoard shopBoard;
    private ClassSelectionBoard classSelectionBoard;
    private BoonSelectionBoard boonSelectionBoard;
    private static BoardManager instance = new BoardManager();
    private AudioManager audioManager = AudioManager.getInstance();

    private JPanel currentBoard = null;

    public enum ScreenType {
        MAIN_MENU, GAME, SHOP, UPGRADE_SELECTION, CLASS_SELECTION
    }

    private Map<ScreenType, JPanel> screens = new EnumMap<>(ScreenType.class);
    private Map<ScreenType, Runnable> screenActions = new EnumMap<>(ScreenType.class);


    private BoardManager() {
        setResizable(false);
        setTitle("Alpha version 0.1");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(data.getWindowWidth(), data.getWindowHeight());

        // Initialize screens
        mainMenuBoard = new MainMenuBoard();
        gameBoard = new GameBoard();
        shopBoard = new ShopBoard();
        classSelectionBoard = new ClassSelectionBoard();
        boonSelectionBoard = new BoonSelectionBoard();

        screens.put(ScreenType.MAIN_MENU, mainMenuBoard);
        screens.put(ScreenType.GAME, gameBoard);
        screens.put(ScreenType.SHOP, shopBoard);
        screens.put(ScreenType.CLASS_SELECTION, classSelectionBoard);
        screens.put(ScreenType.UPGRADE_SELECTION, boonSelectionBoard);

        // Define actions for each screen
        screenActions.put(ScreenType.MAIN_MENU, () -> {
            playMenuMusic();
            mainMenuBoard.recreateWindow();
            mainMenuBoard.getTimer().restart();
            ConnectedControllersManager.getInstance().setControllerSensitive(false);
        });

        screenActions.put(ScreenType.UPGRADE_SELECTION, () -> {
            boonSelectionBoard.recreateWindow();
            boonSelectionBoard.getTimer().restart();
            ConnectedControllersManager.getInstance().setControllerSensitive(false);
        });
        screenActions.put(ScreenType.GAME, () -> {
            stopMusic();
            gameBoard.startGame();
            ConnectedControllersManager.getInstance().setControllerSensitive(true);
        });

        screenActions.put(ScreenType.SHOP, () -> {
            playShopMenuMusic();
            PlayerInventory.getInstance().setCashMoney(Math.round(PlayerInventory.getInstance().getCashMoney()));
            shopBoard.initShopBoardGUIComponents();
            shopBoard.getTimer().restart();
            ConnectedControllersManager.getInstance().setControllerSensitive(false);
        });

        screenActions.put(ScreenType.CLASS_SELECTION, () -> {
            classSelectionBoard.recreateWindow();
            classSelectionBoard.getTimer().restart();
            ConnectedControllersManager.getInstance().setControllerSensitive(false);
        });
    }

    public static BoardManager getInstance () {
        return instance;
    }


    private void playMenuMusic () {
//        if(audioManager.getBackGroundMusicCustomAudioclip() != null && !audioManager.getBackGroundMusicCustomAudioclip().getAudioType().equals(AudioEnums.mainmenu)) {
            audioManager.stopMusicAudio();
            audioManager.playDefaultBackgroundMusic(AudioEnums.mainmenu, true);
//        }

    }

    private void playShopMenuMusic () {
        audioManager.stopMusicAudio();
        audioManager.playDefaultBackgroundMusic(AudioEnums.Lemmino_Firecracker, true);
    }

    private void stopMusic(){
        audioManager.stopMusicAudio();
    }

    public void switchScreen(ScreenType screenType) {
        // Stop timers for all screens
        screens.values().forEach(screen -> {
            if (screen instanceof TimerHolder) {
                ((TimerHolder) screen).getTimer().stop();
            }
        });

        // Change screen
        JPanel newBoard = screens.get(screenType);
        if (currentBoard != null) {
            remove(currentBoard);
            currentBoard.revalidate();
        }
        currentBoard = newBoard;
        add(currentBoard);
        currentBoard.revalidate();
        currentBoard.requestFocus();
        repaint();

        // Execute specific screen actions
        Runnable action = screenActions.get(screenType);
        if (action != null) {
            action.run();
        }
    }

    public void initMainMenu() {
        gameBoard.resetGame();
        switchScreen(ScreenType.MAIN_MENU);
    }

    public void initGame() {
        switchScreen(ScreenType.GAME);
    }

    public void openShopWindow() {
        switchScreen(ScreenType.SHOP);
    }

    public void openUpgradeSelectionScreen() {
        switchScreen(ScreenType.UPGRADE_SELECTION);
    }

    public void menuToClassSelection() {
        switchScreen(ScreenType.CLASS_SELECTION);
    }

    public ClassSelectionBoard getClassSelectionBoard () {
        return classSelectionBoard;
    }

    public ShopBoard getShopBoard () {
        return shopBoard;
    }

    public BoonSelectionBoard getUpgradeSelectionBoard() {
        return boonSelectionBoard;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
