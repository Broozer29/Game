package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.util.OnScreenText;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.TimerHolder;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundManager;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.DifficultySelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class DifficultySelectionBoard extends JPanel implements TimerHolder {

    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ControllerManager controllers = ControllerManager.getInstance();


    private GUIComponent chooseDifficultyText;
    private GUIComponent startGameButtonBackgroundCard;
    private GUITextCollection startGameButton;
    private GUIComponent returnToMenuBackgroundCard;
    private GUITextCollection returnToMenuButton;

    private GUIComponent selectTribeBackgroundCard;
    private GUITextCollection selectTribe;
    private GUIComponent selectMiniBossBackgroundCard;
    private GUITextCollection selectMiniboss;

    private GUIComponent selectPirates;
    private GUIComponent selectZerg;
    private GUIComponent selectRoyalGuard;

    private GUIComponent selectNoMiniBoss;
    private GUIComponent selectOneMiniBoss;
    private GUIComponent selectTwoMiniBoss;

    private GUIComponent totalDifficultyBackgroundCard;
    private GUITextCollection totalDifficultyIcon;
    private GUITextCollection totalDifficultyText;

    private GUIComponent descriptionBoxBackgroundCard;

    private List<GUIComponent> firstColumn = new ArrayList<>(); //back to class selection, select pirates
    private List<GUIComponent> secondColumn = new ArrayList<>(); //select zerg
    private List<GUIComponent> thirdColumn = new ArrayList<>(); //select royal gyard
    private List<GUIComponent> fourthColumn = new ArrayList<>(); //select 0 mini bosses
    private List<GUIComponent> fifthColumn = new ArrayList<>(); //select 1 mini bosses
    private List<GUIComponent> sixthColumn = new ArrayList<>(); //select 2 mini bosses, start game


    private List<List<GUIComponent>> grid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();
    private MenuCursor menuCursor;
    private Timer timer;
    private ControllerInputReader controllerInputReader;
    private int selectedRow = 0;
    private int selectedColumn = 0;
    private boolean initializedMenuObjects = false;


    public DifficultySelectionBoard() {
        addKeyListener(new DifficultySelectionBoard.KeyInputReader());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));

        if (controllers.getPrimaryController() != null) {
            controllerInputReader = controllers.getPrimaryController();
        }

        initMenuTiles();
        timer = new Timer(GameState.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight() + 5));
        timer.start();
    }

    public void initMenuTiles() {
        chooseDifficultyText = DifficultySelectionBoardCreator.createChoosDifficultyText();
        startGameButtonBackgroundCard = DifficultySelectionBoardCreator.createStartGameButtonBackground();
        startGameButton = DifficultySelectionBoardCreator.createStartGameButton(startGameButtonBackgroundCard);

        selectTribeBackgroundCard = DifficultySelectionBoardCreator.createDifficultySelectionBackgroundCard();
        selectTribe = DifficultySelectionBoardCreator.createTitleText(selectTribeBackgroundCard, "ENEMY TRIBE");

        selectPirates = DifficultySelectionBoardCreator.createPirateSelection(selectTribeBackgroundCard);
        selectZerg = DifficultySelectionBoardCreator.createZergSelection(selectTribeBackgroundCard);
        selectRoyalGuard = DifficultySelectionBoardCreator.createRoyalGuardSelection(selectTribeBackgroundCard);

        selectMiniBossBackgroundCard = DifficultySelectionBoardCreator.createMiniBossSelectionBackgroundCard();
        selectMiniboss = DifficultySelectionBoardCreator.createTitleText(selectMiniBossBackgroundCard, "MINI BOSSES");

        selectNoMiniBoss = DifficultySelectionBoardCreator.createNoMiniBossSelection(selectMiniBossBackgroundCard);
        selectOneMiniBoss = DifficultySelectionBoardCreator.createOneMiniBossSelection(selectMiniBossBackgroundCard);
        selectTwoMiniBoss = DifficultySelectionBoardCreator.createTwoMiniBossSelection(selectMiniBossBackgroundCard);

        totalDifficultyBackgroundCard = DifficultySelectionBoardCreator.createTotalDifficultyBackgroundCard();
        totalDifficultyText = DifficultySelectionBoardCreator.createTitleText(totalDifficultyBackgroundCard, "SELECTED DIFFICULTY");
        totalDifficultyIcon = DifficultySelectionBoardCreator.createNextLevelDifficultyIcon(totalDifficultyBackgroundCard);

        descriptionBoxBackgroundCard = DifficultySelectionBoardCreator.createDescriptionBoxBackgroundCard();

        returnToMenuBackgroundCard = DifficultySelectionBoardCreator.createReturnToMainMenuBackgroundCard();
        returnToMenuButton = DifficultySelectionBoardCreator.createReturn(returnToMenuBackgroundCard);
        menuCursor = DifficultySelectionBoardCreator.createCursor(selectPirates);
        initializedMenuObjects = true;

        updateSelectedDifficultyIcons();
    }

    public void recreateWindow() {
        if (initializedMenuObjects) {
            lastMoveTime = System.currentTimeMillis();
            //Clear all existing columns/rows/grid then re-add them
            recreateList();
            selectedColumn = 0;
            selectedRow = 0;
            updateCursor();
        }
    }

    private void recreateList() {
        firstColumn.clear();
        secondColumn.clear();
        thirdColumn.clear();
        fourthColumn.clear();
        fifthColumn.clear();
        sixthColumn.clear();

        grid.clear();
        offTheGridObjects.clear();
        updateSelectedDifficultyIcons();

        totalDifficultyIcon = DifficultySelectionBoardCreator.createNextLevelDifficultyIcon(totalDifficultyBackgroundCard);

        offTheGridObjects.add(chooseDifficultyText);
        offTheGridObjects.add(startGameButtonBackgroundCard);
        offTheGridObjects.add(selectTribeBackgroundCard);
        offTheGridObjects.add(selectMiniBossBackgroundCard);
        offTheGridObjects.add(returnToMenuBackgroundCard);
        offTheGridObjects.addAll(selectTribe.getComponents());
        offTheGridObjects.addAll(selectMiniboss.getComponents());
        offTheGridObjects.add(totalDifficultyBackgroundCard);
        offTheGridObjects.addAll(totalDifficultyIcon.getComponents());
        offTheGridObjects.addAll(totalDifficultyText.getComponents());
        offTheGridObjects.add(descriptionBoxBackgroundCard);


        List<GUIComponent> firstRow = new ArrayList<>();
        firstRow.add(selectPirates);
        firstRow.add(selectZerg);
        firstRow.add(selectRoyalGuard);
        firstRow.add(selectNoMiniBoss);
        firstRow.add(selectOneMiniBoss);
        firstRow.add(selectTwoMiniBoss);


        List<GUIComponent> secondRow = new ArrayList<>();
        secondRow.add(returnToMenuButton.getComponents().get(0));
        addAllButFirstComponent(returnToMenuButton);
        secondRow.add(startGameButton.getComponents().get(0));
        addAllButFirstComponent(startGameButton);
        grid.add(firstRow);
        grid.add(secondRow);
        updateCursor();
    }

    private void addToGrid(List<GUIComponent> gridList, GUIComponent component, int column, int row) {
        //Crosswired for this board, temporary fix of a bigger problem also present in ShopBoard
        component.setColumn(row);
        component.setRow(column);
        gridList.add(component);
    }

    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    public void updateSelectedDifficultyIcons() {
        if (initializedMenuObjects) {
            ShopBoardCreator.updateDifficultyIconsToDifficulty(LevelManager.getInstance().getCurrentLevelDifficulty(),
                    selectPirates, selectZerg, selectRoyalGuard);

            ShopBoardCreator.updateMiniBossIconsToSelection(LevelManager.getInstance().getCurrentMiniBossConfig(),
                    selectNoMiniBoss, selectOneMiniBoss, selectTwoMiniBoss);
        }
    }

    private void drawClassDescriptionText(Graphics2D g) {
        drawClassDescriptionText(g, descriptionBoxBackgroundCard, menuCursor.getSelectedMenuTile().getDescriptionOfComponent());
    }


    private void drawClassDescriptionText(Graphics2D g, GUIComponent backgroundCard, String description) {
        int boxWidth = backgroundCard.getWidth();
        int boxHeight = backgroundCard.getHeight();

        int horizontalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int verticalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int maxTextWidth = boxWidth - (horizontalPadding * 2);
        String textFont = DataClass.getInstance().getTextFont();

        int descriptionX = backgroundCard.getXCoordinate() + horizontalPadding;
        int descriptionY = backgroundCard.getYCoordinate() + verticalPadding;

        if (description != null) {
            g.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));
            drawDescriptionText(g, description, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
        }
    }

    private void drawDescriptionText(Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight() + 2;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        g2d.setColor(color);

        for (String word : words) {
            // If adding the new word exceeds the maximum line width, draw the line and start a new one
            if (metrics.stringWidth(line + word) > maxWidth) {
                g2d.drawString(line.toString(), x, y);
                line = new StringBuilder(word).append(" ");
                y += lineHeight;
            } else {
                // Append the word to the current line
                line.append(word).append(" ");
            }
        }

        // Draw the remaining text
        if (line.length() > 0) {
            g2d.drawString(line.toString(), x, y);
        }
    }


    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile() {
        grid.get(selectedRow).get(selectedColumn).activateComponent();
        if (grid.get(selectedRow).get(selectedColumn).getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            timer.stop();
        }
    }


    private void navigateLeft() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }
        selectedColumn--;
        if (selectedColumn < 0) {
            selectedColumn = grid.get(selectedRow).size() - 1; // Wrap around to the rightmost column
        }

    }

    // Go one menu tile right
    private void navigateRight() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        selectedColumn++;
        if (selectedColumn >= grid.get(selectedRow).size()) {
            selectedColumn = 0; // Wrap around to the leftmost column
        }
    }

    // Go one menu tile to the left
    private void navigateUp() {
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
    }

    // Go one menu tile to the right
    private void navigateDown() {
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

    // Update the cursor's position and selected menu tile
    private void updateCursor() {
        if (grid.get(selectedRow).isEmpty()) { // Check if the selected row is empty
            menuCursor.setSelectedMenuTile(null); // Need to decide how you want to handle this situation
        } else {
            GUIComponent selectedTile = grid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedTile);
            menuCursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + menuCursor.getYDistanceModification());
            menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
        }
    }

    private class KeyInputReader extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            boolean needsUpdate = false;
            switch (key) {
                case (KeyEvent.VK_ENTER):
                    selectMenuTile();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_A):
                    navigateLeft();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_D):
                    navigateRight();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_W):
                    navigateUp();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_S):
                    navigateDown();
                    needsUpdate = true;
                    break;
            }

            if (needsUpdate) {
                recreateList();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case (KeyEvent.VK_ENTER):
                    break;
                case (KeyEvent.VK_A):
                    break;
                case (KeyEvent.VK_D):
                    break;
                case (KeyEvent.VK_W):
                    break;
                case (KeyEvent.VK_S):
                    break;
            }
        }
    }

    private long lastMoveTime = 0;

    public void executeControllerInput() {
        if (controllers.getPrimaryController() != null) {
            boolean needsUpdate = false;
            controllerInputReader = controllers.getPrimaryController();
            controllerInputReader.pollController();
            long currentTime = System.currentTimeMillis();

            // Left and right navigation
            if (currentTime - lastMoveTime > DataClass.CONTROLLER_INPUT_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT)) {
                    // Menu option to the left
                    navigateLeft();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT)) {
                    // Menu option to the right
                    navigateRight();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }

                // Up and down navigation
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP)) {
                    // Menu option upwards
                    navigateUp();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN)) {
                    // Menu option downwards
                    navigateDown();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }

                if (controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
                    // Select menu option
                    selectMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            if (currentTime - lastMoveTime > DataClass.CONTROLLER_INPUT_COOLDOWN &&
                    controllerInputReader.isInputActive(ControllerInputEnums.SPECIAL_ATTACK)) {
                // Select menu option
                BoardManager.getInstance().switchScreen(BoardManager.ScreenType.CLASS_SELECTION);
                needsUpdate = true;
                lastMoveTime = currentTime;
            }

            if (needsUpdate) {
                recreateList(); // Update the GUI only if there was an action that requires it
            }
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
                drawClassDescriptionText(g2d);
                drawImage(g2d, menuCursor);

                for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
                    drawAnimation(g2d, animation);
                }

                for (OnScreenText text : OnScreenTextManager.getInstance().getOnScreenTexts()) {
                    drawText(g2d, text);
                }
            } finally {
                g2d.dispose(); // Ensure resources are released
            }

            animationManager.updateGameTick();
            backgroundManager.updateGameTick();
            Toolkit.getDefaultToolkit().sync();

            // readControllerState();
            executeControllerInput();
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

    public Timer getTimer() {
        return this.timer;
    }
}
