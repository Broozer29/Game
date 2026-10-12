package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.MenuBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuGrid;
import net.riezebos.bruus.tbd.guiboards.guicomponents.SettingsWindow;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class MainMenuBoard extends MenuBoard {
    private final MenuGrid grid = new MenuGrid();
    private GUITextCollection selectClassBoard;
    private GUIComponent startGameBackgroundCard;
    private GUITextCollection selectMusicOptionText;
    private GUIComponent selectMusicOptionBackgroundCard;
    private GUITextCollection openShopButton;
    private GUITextCollection continueSaveFile;
    private GUITextCollection settingsButton;
    private GUITextCollection selectMacOSMediaPlayerButton;
    private GUITextCollection selectDefaultMusicButton;
    private GUIComponent titleImage;

    private GUITextCollection closeGameButton;

    private boolean initializedMenuObjects = false;

    public MainMenuBoard() {
        logDiagnostic("MainMenuBoard constructor: Starting...");
        grids.add(grid);

        logDiagnostic("MainMenuBoard constructor: Calling initMenuTiles()...");
        initMenuTiles();
        // Don't start timer in constructor - let it be started explicitly via getTimer().restart()
        logDiagnostic("MainMenuBoard constructor: Completed");
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

    // Initialize all starter pointers
    private void initMenuTiles() {
        titleImage = MenuBoardCreator.createTitleImage();
        createCardAndButtons();

        selectMusicOptionBackgroundCard = MenuBoardCreator.selectMusicPlayerBackgroundCard(startGameBackgroundCard);
        selectMusicOptionText = MenuBoardCreator.selectMusicText(selectMusicOptionBackgroundCard);
        selectDefaultMusicButton = MenuBoardCreator.selectDefaultLocalMusicPlayer(selectMusicOptionBackgroundCard);
        selectMacOSMediaPlayerButton = MenuBoardCreator.selectMacOSItunesMediaPlayer(selectDefaultMusicButton);
        initializedMenuObjects = true;
    }

    @Override
    public void recreateWindow() {
        if (initializedMenuObjects) {
            SettingsWindow.getInstance().closeSilently();
            createCardAndButtons();
            animationManager.resetManager();

            menuCursor = MenuBoardCreator.createMenuCursor(selectClassBoard.getComponents().get(0));
            menuCursor.setSelectedMenuTile(selectClassBoard.getComponents().get(0));

            buildOffTheGridObjects();
            buildGrid();
            grid.reset();
            grid.placeCursor(menuCursor);
        }
    }

    // Everything that is drawn but never selected, built in one place in drawing order:
    // what is added later is drawn on top, so the card comes before the letters on it
    private void buildOffTheGridObjects() {
        offTheGridObjects.clear();

        offTheGridObjects.add(startGameBackgroundCard);
//        offTheGridObjects.add(selectMusicOptionBackgroundCard);
        offTheGridObjects.add(titleImage);
//        offTheGridObjects.addAll(selectMusicOptionText.getComponents());

        addLettersToOffTheGrid();
    }

    // Creates the card and the buttons that are shown, each one below the last shown button, and makes the card as high as they need
    private void createCardAndButtons() {
        boolean showContinue = SaveManager.getInstance().doesSaveFileExist();
        boolean showShop = DevTestSettings.enableDirectShopAccess;
        int buttonCount = 3 + (showContinue ? 1 : 0) + (showShop ? 1 : 0); // SELECT CLASS, SETTINGS and CLOSE GAME are always shown

        startGameBackgroundCard = MenuBoardCreator.startGameBackgroundCard(buttonCount);
        selectClassBoard = MenuBoardCreator.createStartGameButton(startGameBackgroundCard);
        GUITextCollection lastButton = selectClassBoard;
        continueSaveFile = MenuBoardCreator.continueSaveFileButton(lastButton);
        if (showContinue) {
            lastButton = continueSaveFile;
        }
        openShopButton = MenuBoardCreator.openShopButton(lastButton);
        if (showShop) {
            lastButton = openShopButton;
        }
        settingsButton = MenuBoardCreator.settingsButton(lastButton);
        closeGameButton = MenuBoardCreator.testingButton(settingsButton);
    }

    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    // The other letters of each shown button are drawn but never selected, so they are added once per window
    private void addLettersToOffTheGrid() {
        addAllButFirstComponent(selectClassBoard);
        if (SaveManager.getInstance().doesSaveFileExist()) {
            addAllButFirstComponent(continueSaveFile);
        }
        if (DevTestSettings.enableDirectShopAccess) {
            addAllButFirstComponent(openShopButton);
        }
        addAllButFirstComponent(settingsButton);
        addAllButFirstComponent(closeGameButton);
    }

    // One row per shown button, top to bottom
    private void buildGrid() {
        grid.clear();
        addRow(selectClassBoard);

        if (SaveManager.getInstance().doesSaveFileExist()) {
            addRow(continueSaveFile);
        }

        if (DevTestSettings.enableDirectShopAccess) {
            addRow(openShopButton);
        }

        addRow(settingsButton);

        addRow(closeGameButton);
    }

    private void addRow(GUITextCollection button) {
        List<GUIComponent> row = new ArrayList<>();
        row.add(button.getComponents().get(0));
        grid.addRow(row);
    }

    // Shows the settings page over the main menu; when it closes, SETTINGS is selected again
    public void openSettings() {
        SettingsWindow.getInstance().open(() -> {
            grid.select(settingsButton.getComponents().get(0));
            grid.placeCursor(menuCursor);
            MenuInput.getInstance().startScreen();
        });
    }

    public void closeGame() {
        System.exit(1);
    }

    /*------------------------Menu hooks--------------------------------*/

    // Activate the functionality of the specific menutile
    @Override
    protected void onConfirm() {
        GUIComponent selectedTile = grid.getSelected();
        selectedTile.activateComponent();
        if (selectedTile.getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            getTimer().stop();
        }
    }

    // Move the cursor to the selected tile
    @Override
    protected void onInputHandled() {
        grid.placeCursor(menuCursor);
    }

    // While the settings page is open it takes the input, the menu does not move
    @Override
    protected boolean handleHostedInput(List<MenuAction> actions) {
        if (!SettingsWindow.getInstance().isOpen()) {
            return false;
        }
        SettingsWindow.getInstance().handleInput(actions);
        return true;
    }

    // The settings page is drawn over the menu, after the cursor
    @Override
    protected void drawScreenOverlay(Graphics2D g) {
        if (SettingsWindow.getInstance().isOpen()) {
            for (GUIComponent component : SettingsWindow.getInstance().getComponents()) {
                drawGUIComponent(g, component);
            }
        }
    }
}
