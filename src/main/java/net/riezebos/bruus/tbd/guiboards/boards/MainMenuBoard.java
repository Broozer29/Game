package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.controllerInput.ControllerNotices;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveManager;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.util.OnScreenText;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.TimerHolder;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundManager;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.MenuBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.guiboards.guicomponents.PlaylistWindow;
import net.riezebos.bruus.tbd.guiboards.guicomponents.SettingsWindow;
import net.riezebos.bruus.tbd.guiboards.guicomponents.ShopkeeperWindow;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenuBoard extends JPanel implements TimerHolder {
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();

    private List<GUIComponent> firstColumn = new ArrayList<>();
    private List<GUIComponent> secondColumn = new ArrayList<>();
    private List<GUIComponent> thirdColumn = new ArrayList<>();
    private List<List<GUIComponent>> grid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();
    private MenuCursor menuCursor;
    private GUITextCollection selectClassBoard;
    private GUIComponent startGameBackgroundCard;
    private GUITextCollection selectMusicOptionText;
    private GUIComponent selectMusicOptionBackgroundCard;
    private GUITextCollection openShopButton;
    private GUITextCollection continueSaveFile;
    private GUITextCollection shopkeeperButton;
    private GUITextCollection playlistButton;
    private GUITextCollection settingsButton;
    private GUITextCollection selectMacOSMediaPlayerButton;
    private GUITextCollection selectDefaultMusicButton;
    private GUIComponent titleImage;

    private GUITextCollection closeGameButton;

    private Timer timer;
    private boolean initializedMenuObjects = false;

    private int selectedRow = 0;
    private int selectedColumn = 0;

    public MainMenuBoard() {
        logDiagnostic("MainMenuBoard constructor: Starting...");
        setFocusable(true);
        setBackground(Color.BLACK);
        logDiagnostic("MainMenuBoard constructor: Setting preferred size...");
        setPreferredSize(new Dimension(DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));

        logDiagnostic("MainMenuBoard constructor: Checking controllers...");

        logDiagnostic("MainMenuBoard constructor: Calling initMenuTiles()...");
        initMenuTiles();
        logDiagnostic("MainMenuBoard constructor: Creating timer...");
        timer = new Timer(GameState.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight() + 5));
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

    public void recreateWindow() {
        if (initializedMenuObjects) {
            SettingsWindow.getInstance().closeSilently();
            ShopkeeperWindow.getInstance().closeSilently();
            PlaylistWindow.getInstance().closeSilently(); // stops a preview and resumes the menu music where it was
            createCardAndButtons();
            animationManager.resetManager();

            menuCursor = MenuBoardCreator.createMenuCursor(selectClassBoard.getComponents().get(0));
            menuCursor.setSelectedMenuTile(selectClassBoard.getComponents().get(0));
            selectedColumn = 0;
            selectedRow = 0;

            buildOffTheGridObjects();
            grid.clear();
            grid.add(firstColumn);
            grid.add(secondColumn);
            grid.add(thirdColumn);
            recreateList(); // Fill the columns
            updateCursor();
        }
    }

    // Everything that is drawn but never selected, built in one place in drawing order:
    // what is added later is drawn on top, so the card comes before the letters on it
    private void buildOffTheGridObjects() {
        offTheGridObjects.clear();

        offTheGridObjects.add(startGameBackgroundCard);
//        offTheGridObjects.add(selectMusicOptionBackgroundCard);
        offTheGridObjects.add(titleImage);
        offTheGridObjects.add(menuCursor);
//        offTheGridObjects.addAll(selectMusicOptionText.getComponents());

        addLettersToOffTheGrid();
    }

    // Creates the card and the buttons that are shown, each one below the last shown button, and makes the card as high as they need
    private void createCardAndButtons() {
        boolean showContinue = SaveManager.getInstance().doesSaveFileExist();
        boolean showShop = DevTestSettings.enableDirectShopAccess;
        int buttonCount = 5 + (showContinue ? 1 : 0) + (showShop ? 1 : 0); // SELECT CLASS, ARMORY, PLAYLIST, SETTINGS and CLOSE GAME are always shown

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
        shopkeeperButton = MenuBoardCreator.shopkeeperButton(lastButton);
        playlistButton = MenuBoardCreator.playlistButton(shopkeeperButton);
        settingsButton = MenuBoardCreator.settingsButton(playlistButton);
        closeGameButton = MenuBoardCreator.testingButton(settingsButton);
    }

    // Recreate the tilesList that gets drawn by drawComponents
    private void recreateList() {
        firstColumn.clear();
        secondColumn.clear();
        thirdColumn.clear();
        addTilesToColumns();
    }

    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    // The other letters of each shown button are drawn but never selected, so they are added once per window, not on every recreateList
    private void addLettersToOffTheGrid() {
        addAllButFirstComponent(selectClassBoard);
        if (SaveManager.getInstance().doesSaveFileExist()) {
            addAllButFirstComponent(continueSaveFile);
        }
        if (DevTestSettings.enableDirectShopAccess) {
            addAllButFirstComponent(openShopButton);
        }
        addAllButFirstComponent(shopkeeperButton);
        addAllButFirstComponent(playlistButton);
        addAllButFirstComponent(settingsButton);
        addAllButFirstComponent(closeGameButton);
    }

    private void addTilesToColumns() {
        addToGrid(firstColumn, selectClassBoard.getComponents().get(0), 0, 0);

        if (SaveManager.getInstance().doesSaveFileExist()) {
            addToGrid(firstColumn, continueSaveFile.getComponents().get(0), 0, 0);
        }

        if(DevTestSettings.enableDirectShopAccess) {
            addToGrid(firstColumn, openShopButton.getComponents().get(0), 0, 0);
        }

        addToGrid(firstColumn, shopkeeperButton.getComponents().get(0), 0, 0);

        addToGrid(firstColumn, playlistButton.getComponents().get(0), 0, 0);

        addToGrid(firstColumn, settingsButton.getComponents().get(0), 0, 0);

        addToGrid(firstColumn, closeGameButton.getComponents().get(0), 0, 0);

//        secondColumn.add(selectDefaultMusicButton.getComponents().get(0));
//        addAllButFirstComponent(selectDefaultMusicButton);

//        secondColumn.add(selectMacOSMediaPlayerButton.getComponents().get(0));
//        addAllButFirstComponent(selectMacOSMediaPlayerButton);
    }

    private void addToGrid(List<GUIComponent> gridList, GUIComponent component, int column, int row) {
        component.setColumn(column);
        component.setRow(row);
        gridList.add(component);
    }


    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile() {
        grid.get(selectedRow).get(selectedColumn).activateComponent();
        if (grid.get(selectedRow).get(selectedColumn).getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            timer.stop();
        }

    }

    private void previousMenuTile() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

        do {
            selectedRow--;
            if (selectedRow < 0) {
                selectedRow = grid.size() - 1; // Wrap around to the bottom row
            }
        } while (grid.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!grid.get(selectedRow).isEmpty() && selectedColumn >= grid.get(selectedRow).size()) {
            selectedColumn = grid.get(selectedRow).size() - 1;
        }
        updateCursor();
    }

    // Go one menu tile downwards
    private void nextMenuTile() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

        do {
            selectedRow++;
            if (selectedRow >= grid.size()) {
                selectedRow = 0; // Wrap around to the top row
            }
        } while (grid.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!grid.get(selectedRow).isEmpty() && selectedColumn >= grid.get(selectedRow).size()) {
            selectedColumn = grid.get(selectedRow).size() - 1;
        }
        updateCursor();
    }

    // Check if the grid is empty
    private boolean isGridEmpty() {
        for (List<GUIComponent> row : grid) {
            if (!row.isEmpty()) {
                return false; // Return false as soon as a non-empty row is found
            }
        }
        return true; // If no non-empty rows are found, the grid is empty
    }

    // Go one menu tile to the left
    private void previousMenuColumn() {
        selectedColumn--;
        if (selectedColumn < 0) {
            selectedColumn = grid.get(selectedRow).size() - 1; // Wrap around to the rightmost column
        }
        updateCursor();
    }

    // Go one menu tile to the right
    private void nextMenuColumn() {
        selectedColumn++;
        if (selectedColumn >= grid.get(selectedRow).size()) {
            selectedColumn = 0; // Wrap around to the leftmost column
        }
        updateCursor();
    }

    // Update the cursor's position and selected menu tile
    private void updateCursor() {
        if (grid.get(selectedRow).isEmpty()) { // Check if the selected row is empty
            menuCursor.setSelectedMenuTile(null); // You might need to decide how you want to handle this situation in
            // your application
        } else {
            GUIComponent selectedTile = grid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedTile);
            menuCursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + menuCursor.getYDistanceModification());
            menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
        }
    }

    // Shows the settings page over the main menu; when it closes, SETTINGS is selected again
    public void openSettings() {
        SettingsWindow.getInstance().open(() -> {
            selectedRow = 0;
            selectedColumn = firstColumn.indexOf(settingsButton.getComponents().get(0));
            updateCursor();
            MenuInput.getInstance().startScreen();
        });
    }

    // Shows the SHOPKEEPER page over the main menu; when it closes, SHOPKEEPER is selected again
    public void openShopkeeper() {
        ShopkeeperWindow.getInstance().open(() -> {
            selectedRow = 0;
            selectedColumn = firstColumn.indexOf(shopkeeperButton.getComponents().get(0));
            updateCursor();
            MenuInput.getInstance().startScreen();
        });
    }

    // Shows the PLAYLIST page over the main menu; when it closes, PLAYLIST is selected again
    public void openPlaylist() {
        PlaylistWindow.getInstance().open(() -> {
            selectedRow = 0;
            selectedColumn = firstColumn.indexOf(playlistButton.getComponents().get(0));
            updateCursor();
            MenuInput.getInstance().startScreen();
        });
    }

    public void closeGame() {
        System.exit(1);
    }

    // Acts on what MenuInput reports: keys and the main controller, one set of rules for every menu screen
    public void executeMenuInput() {
        if (SettingsWindow.getInstance().isOpen()) {
            SettingsWindow.getInstance().handleInput(MenuInput.getInstance().poll()); // The page takes the input, the menu does not move
            return;
        }
        if (ShopkeeperWindow.getInstance().isOpen()) {
            ShopkeeperWindow.getInstance().handleInput(MenuInput.getInstance().poll()); // The page takes the input, the menu does not move
            return;
        }
        if (PlaylistWindow.getInstance().isOpen()) {
            PlaylistWindow.getInstance().handleInput(MenuInput.getInstance().poll()); // The page takes the input, the menu does not move
            return;
        }
        boolean needsUpdate = false;
        for (MenuAction action : MenuInput.getInstance().poll()) {
            switch (action) {
                case LEFT:
                    previousMenuTile();
                    needsUpdate = true;
                    break;
                case RIGHT:
                    nextMenuTile();
                    needsUpdate = true;
                    break;
                case UP:
                    previousMenuColumn();
                    needsUpdate = true;
                    break;
                case DOWN:
                    nextMenuColumn();
                    needsUpdate = true;
                    break;
                case CONFIRM:
                    selectMenuTile();
                    recreateList();
                    return; // selecting can change the screen
            }
        }

        if (needsUpdate) {
            recreateList(); // Update the GUI only if there was an action that requires it
        }
    }

    /*-----------------------------End of navigation methods--------------------------*/

    /*---------------------------Drawing methods-------------------------------*/
    @Override
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create(); // Create a copy to avoid modifying the original graphics context

            try {
                // Draws all background objects
                for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
                    drawImage(g2d, bgObject);
                }

                for (SpriteAnimation animation : animationManager.getLowerAnimations()) {
                    drawAnimation(g2d, animation);
                }

                drawObjects(g2d);

                for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
                    drawAnimation(g2d, animation);
                }

                for (OnScreenText text : OnScreenTextManager.getInstance().getOnScreenTexts()) {
                    drawText(g2d, text);
                }

                ControllerNotices.getInstance().draw(g2d);
            } finally {
                g2d.dispose(); // Ensure resources are released
            }

            animationManager.updateGameTick();
            backgroundManager.updateGameTick();
            Toolkit.getDefaultToolkit().sync();

            // readControllerState();
            executeMenuInput();
        } catch (Exception ex) {
            try {
                java.io.FileWriter fw = new java.io.FileWriter("error_log.txt", true);
                java.io.PrintWriter pw = new java.io.PrintWriter(fw);
                pw.println("=== Error at " + java.time.LocalDateTime.now() + " ===");
                ex.printStackTrace(pw);
                pw.println();
                pw.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            System.exit(1);
        }
    }


    private void drawObjects(Graphics2D g) {
        for (GUIComponent component : offTheGridObjects) {
            if (component != null) {
                drawGUIComponent(g, component);
            }
        }


        for (List<GUIComponent> list : grid) {
            for (GUIComponent component : list) {
                drawGUIComponent(g, component);
            }
        }

        if (SettingsWindow.getInstance().isOpen()) {
            for (GUIComponent component : SettingsWindow.getInstance().getComponents()) {
                drawGUIComponent(g, component);
            }
        }

        if (ShopkeeperWindow.getInstance().isOpen()) {
            for (GUIComponent component : ShopkeeperWindow.getInstance().getComponents()) {
                drawGUIComponent(g, component);
            }
            ShopkeeperWindow.getInstance().draw(g);
        }

        if (PlaylistWindow.getInstance().isOpen()) {
            for (GUIComponent component : PlaylistWindow.getInstance().getComponents()) {
                drawGUIComponent(g, component);
            }
            PlaylistWindow.getInstance().draw(g);
        }
    }

    private void drawGUIComponent(Graphics2D g, GUIComponent component) {
        g.drawImage(component.getImage(), component.getXCoordinate(), component.getYCoordinate(), this);
    }

    private void drawImage(Graphics g, Sprite sprite) {
        if (sprite.getImage() != null) {
            g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
        }
    }

    private void drawAnimation(Graphics2D g, SpriteAnimation animation) {
        if (animation.getCurrentFrameImage(false) != null) {
            g.drawImage(animation.getCurrentFrameImage(true), animation.getXCoordinate(), animation.getYCoordinate(), this);
        }
    }

    private void drawText(Graphics2D g, OnScreenText text) {
        // Ensure that transparency value is within the appropriate bounds.
        float transparency = Math.max(0, Math.min(1, text.getTransparencyValue()));
        Color originalColor = g.getColor(); // store the original color
        Font originalFont = g.getFont();

        // Set the color with the specified transparency.
        Color colorWithTransparency = new Color(
                text.getColor().getRed(),
                text.getColor().getGreen(),
                text.getColor().getBlue(),
                (int) (transparency * 255) // alpha value must be between 0 and 255
        );

//        g.setColor(new Color(1.0f, 1.0f, 1.0f, transparency)); // White with transparency
        g.setColor(colorWithTransparency);
        g.setFont(new Font("Helvetica", Font.PLAIN, text.getFontSize()));
        // Draw the text at the current coordinates.
        g.drawString(text.getText(), text.getXCoordinate(), text.getYCoordinate());

        // Update the Y coordinate of the text to make it scroll upwards.
        text.setYCoordinate(text.getYCoordinate() - 1);

        // Decrease the transparency for the next draw
        text.setTransparency(transparency - text.getTransparancyStepSize()); // decrease transparency

        g.setColor(originalColor); // restore the original color
        g.setFont(originalFont);
    }

    /*------------------------------End of Drawing methods-------------------------------*/


    public void continueRun() {
        BoardManager boardManager = BoardManager.getInstance();
        SaveManager.getInstance().loadSaveFile();

        if (Objects.requireNonNull(GameState.getInstance().getGameState()) == GameStatusEnums.Shopping) {
            ShopManager.getInstance().setRowsUnlockedByDifficulty(LevelManager.getInstance().getDifficultyScore());
            if (!ShopManager.getInstance().hasContents()) {
                //The save has no (readable) shop, so this counts as entering the shop
                ShopManager.getInstance().enterShop();
                SaveManager.getInstance().exportCurrentSave();
            }
            boardManager.getShopBoard().initShopBoardGUIComponents();
            boardManager.openShopWindow();
        } else {
            boardManager.initGame();
        }
    }

    public Timer getTimer() {
        return this.timer;
    }

}