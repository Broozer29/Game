package net.riezebos.bruus.tbd.guiboards;

import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.discordconnector.DiscordConnector;
import net.riezebos.bruus.tbd.game.UI.GameBoardCreator;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.guiboards.boards.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class BoardManager extends JFrame {

    private DataClass data = DataClass.getInstance();
    private static BoardManager instance = new BoardManager();
    private AudioManager audioManager = AudioManager.getInstance();

    private JPanel currentBoard = null;
    private JPanel loadingScreen = null;

    // Single instance cache for each board type
    private MainMenuBoard mainMenuBoard;
    private GameBoard gameBoard;
    private ShopBoard shopBoard;
    private ClassSelectionBoard classSelectionBoard;
    private DifficultySelectionBoard difficultySelectionBoard;
    private BoonSelectionBoard boonSelectionBoard;

    public enum ScreenType {
        MAIN_MENU, GAME, SHOP, UPGRADE_SELECTION, CLASS_SELECTION, DIFFICULTY_SELECTION
    }

    private Map<ScreenType, Runnable> screenActions = new EnumMap<>(ScreenType.class);


    private BoardManager() {
        setResizable(false);
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create and display loading screen
        loadingScreen = createLoadingScreen();
        add(loadingScreen);
    }

    private JPanel createLoadingScreen() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                Font font = new Font("Monospaced", Font.BOLD, 48);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                String text = "Loading...";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(text, x, y);
            }
        };
        panel.setBackground(Color.BLACK);
        return panel;
    }

    public void finishInitialization() {
        // Now that window is visible, get actual dimensions
        data.setWindowWidth(getWidth());
        data.setWindowHeight(getHeight());

        // Define actions for each screen
        screenActions.put(ScreenType.MAIN_MENU, () -> {
            playMenuMusic();
            mainMenuBoard.recreateWindow();
            mainMenuBoard.getTimer().restart();
            ControllerManager.getInstance().setControllerSensitive(false);
            DiscordConnector.getInstance().setStatus("Just booted up", "Main Menu");
        });

        screenActions.put(ScreenType.UPGRADE_SELECTION, () -> {
            boonSelectionBoard.recreateWindow();
            boonSelectionBoard.getTimer().restart();
            ControllerManager.getInstance().setControllerSensitive(false);
        });
        screenActions.put(ScreenType.GAME, () -> {
            stopMusic();
            GameBoardCreator.getInstance().resetManager();
            gameBoard.startGame();
            ControllerManager.getInstance().setControllerSensitive(true);
            DiscordConnector.getInstance().setStatus(PlayerStats.getInstance().getPlayerClass() + ", on stage " + GameState.getInstance().getStagesCompleted(), "Fighting");
        });

        screenActions.put(ScreenType.SHOP, () -> {
            playShopMenuMusic();
            PlayerInventory.getInstance().setCashMoney(Math.round(PlayerInventory.getInstance().getCashMoney()));
            shopBoard.initShopBoardGUIComponents();
            shopBoard.getTimer().restart();
            ControllerManager.getInstance().setControllerSensitive(false);
            DiscordConnector.getInstance().setStatus("Shopping with " + Math.round(PlayerInventory.getInstance().getCashMoney()) + " minerals.", "Shop");
        });

        screenActions.put(ScreenType.CLASS_SELECTION, () -> {
            classSelectionBoard.initMenuTiles(); //Kinda sloppy, recreates ALL components when only a set amount need to be remade but its a quick fix
            classSelectionBoard.recreateWindow();
            classSelectionBoard.getTimer().restart();
            ControllerManager.getInstance().setControllerSensitive(false);
            DiscordConnector.getInstance().setStatus("Deciding a class", "Class Selection");
        });

        screenActions.put(ScreenType.DIFFICULTY_SELECTION, () -> {
            difficultySelectionBoard.initMenuTiles();
            difficultySelectionBoard.recreateWindow();
            difficultySelectionBoard.getTimer().restart();
            ControllerManager.getInstance().setControllerSensitive(false);
            DiscordConnector.getInstance().setStatus("Choosing difficulty as a " + PlayerStats.getInstance().getPlayerClass() + ".", "Class Selection");
        });

        // Remove loading screen
        remove(loadingScreen);
        loadingScreen = null;
    }

    public static BoardManager getInstance() {
        return instance;
    }


    private void playMenuMusic() {
//        if(audioManager.getBackGroundMusicCustomAudioclip() != null && !audioManager.getBackGroundMusicCustomAudioclip().getAudioType().equals(AudioEnums.mainmenu)) {
        audioManager.stopMusicAudio();
        audioManager.playDefaultBackgroundMusicForALevel(AudioEnums.mainmenu, true);
//        }

    }

    private void playShopMenuMusic() {
        audioManager.stopMusicAudio();
        audioManager.playDefaultBackgroundMusicForALevel(AudioEnums.Lemmino_Firecracker, true);
    }

    private void stopMusic() {
        audioManager.stopMusicAudio();
    }

    public void switchScreen(ScreenType screenType) {
        logDiagnostic("switchScreen() called for: " + screenType);
        // Stop timer on current screen
        if (currentBoard != null) {
            if (currentBoard instanceof TimerHolder) {
                ((TimerHolder) currentBoard).getTimer().stop();
            }
            remove(currentBoard);
            currentBoard = null; // Allow garbage collection
        }

        logDiagnostic("Getting window dimensions...");
        data.setWindowWidth(getWidth());
        data.setWindowHeight(getHeight());

        // Create new board instance
        logDiagnostic("Creating board for type: " + screenType);
        JPanel newBoard = createBoardForType(screenType);
        logDiagnostic("Board created, adding to frame...");
        currentBoard = newBoard;
        add(currentBoard);
        logDiagnostic("Revalidating and repainting...");
        currentBoard.revalidate();
        currentBoard.requestFocus();
        repaint();

        // Execute specific screen actions
        logDiagnostic("Executing screen actions for: " + screenType);
        Runnable action = screenActions.get(screenType);
        if (action != null) {
            action.run();
        }
        logDiagnostic("switchScreen() completed for: " + screenType);
    }

    private void logDiagnostic(String message) {
        System.out.println(message);
        try {
            java.io.FileWriter fw = new java.io.FileWriter("startup_log.txt", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);
            pw.println("[" + java.time.LocalDateTime.now() + "] " + message);
            pw.close();
        } catch (java.io.IOException e) {
            // Silently fail if can't write to log
        }
    }

    private JPanel createBoardForType(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                if (mainMenuBoard == null) {
                    mainMenuBoard = new MainMenuBoard();
                }
                return mainMenuBoard;
            case GAME:
                if (gameBoard == null) {
                    gameBoard = new GameBoard();
                }
                return gameBoard;
            case SHOP:
                if (shopBoard == null) {
                    shopBoard = new ShopBoard();
                }
                return shopBoard;
            case CLASS_SELECTION:
                if (classSelectionBoard == null) {
                    classSelectionBoard = new ClassSelectionBoard();
                }
                return classSelectionBoard;
            case UPGRADE_SELECTION:
                if (boonSelectionBoard == null) {
                    boonSelectionBoard = new BoonSelectionBoard();
                }
                return boonSelectionBoard;
            case DIFFICULTY_SELECTION:
                if (difficultySelectionBoard == null) {
                    difficultySelectionBoard = new DifficultySelectionBoard();
                }
                return difficultySelectionBoard;
            default:
                throw new IllegalArgumentException("Unknown screen type: " + type);
        }
    }

    public void initMainMenu() {
        if (gameBoard != null) {
            gameBoard.resetGame();
        }
        switchScreen(ScreenType.MAIN_MENU);
    }

    public void initGame() {
        switchScreen(ScreenType.GAME);
    }

    public void openShopWindow() {
        switchScreen(ScreenType.SHOP);
    }

    public void openBoonSelectionScreen() {
        switchScreen(ScreenType.UPGRADE_SELECTION);
    }

    public void menuToClassSelection() {
        switchScreen(ScreenType.CLASS_SELECTION);
    }
    public void openDifficultyScreen() {
        switchScreen(ScreenType.DIFFICULTY_SELECTION);
    }

    public ClassSelectionBoard getClassSelectionBoard() {
        return classSelectionBoard;
    }

    public ShopBoard getShopBoard() {
        if (shopBoard == null) {
            shopBoard = new ShopBoard();
        }
        return shopBoard;
    }

    public BoonSelectionBoard getUpgradeSelectionBoard() {
        if (boonSelectionBoard == null) {
            boonSelectionBoard = new BoonSelectionBoard();
        }
        return boonSelectionBoard;
    }

    public GameBoard getGameBoard() {
        if (gameBoard == null) {
            gameBoard = new GameBoard();
        }
        return gameBoard;
    }

    public MainMenuBoard getMainMenuBoard() {
        if (mainMenuBoard == null) {
            mainMenuBoard = new MainMenuBoard();
        }
        return mainMenuBoard;
    }

    public DifficultySelectionBoard getDifficultySelectionBoard() {
        if (difficultySelectionBoard == null) {
            difficultySelectionBoard = new DifficultySelectionBoard();
        }
        return difficultySelectionBoard;
    }
}
