package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.BountyHunter;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.ClubAccess;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.CompoundWealth;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.TreasureHunter;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.util.OnScreenText;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.TimerHolder;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundManager;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.BoonSelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
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

public class BoonSelectionBoard extends JPanel implements TimerHolder {
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllersManager controllers = ConnectedControllersManager.getInstance();
    private MenuCursor menuCursor;

    private List<GUIComponent> firstColumn = new ArrayList<>();
    private List<GUIComponent> secondColumn = new ArrayList<>();
    private List<GUIComponent> thirdColumn = new ArrayList<>();
    private List<GUIComponent> fourthColumn = new ArrayList<>();
    private List<GUIComponent> fifthColumn = new ArrayList<>();
    private List<GUIComponent> sixthColumn = new ArrayList<>();
    private List<List<GUIComponent>> grid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();

    private GUIComponent selectBoonsTitle;

    private GUIComponent firstColumnBackgroundCard;
    private GUIComponent thirdColumnBackgroundCard;
    private GUIComponent fifthColumnBackgroundCard;

    private GUIComponent returnToSelectionBackgroundCard;
    private GUIComponent startGameBackgroundCard;
    private GUITextCollection selectClassButton;
    private GUITextCollection startGameButton;
    private GUITextCollection emeraldsAvailableText;

    private GUIComponent descriptionBackgroundCard;
    private GUIComponent descriptionBox;

    private GUITextCollection selectNepotism;
    private GUIComponent upgradeNepotism;

    private GUITextCollection selectClubAccess;
    private GUIComponent upgradeClubAccess;
    private GUITextCollection selectCompoundInterest;
    private GUIComponent upgradeCompoundInterest;
    private GUITextCollection selectBountyHunter;
    private GUIComponent upgradeBountyHunter;
    private GUITextCollection selectTreasureHunter;
    private GUIComponent upgradeTreasureHunter;

    private GUIComponent utilityColumnSelectionIndicator;
    private GUITextCollection utilityColumnTitleText;
    private GUIComponent defensiveColumnSelectionIndicator;
    private GUITextCollection defenseColumnTitleText;
    private GUIComponent offensiveColumnSelectionIndicator;
    private GUITextCollection offenseColumnTitleText;

    private GUITextCollection offenseWIPText;
    private GUITextCollection defenseWIPText;


    private Timer timer;
    private ControllerInputReader controllerInputReader;
    private int textPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
    private int selectedColumn = 0;
    private int selectedRow = 0;
    private boolean initializedMenuObjects = false;

    private DescriptionInfo currentDescription;
    private boolean shouldDrawBoonDescription;

    private class DescriptionInfo {
        String title = null;
        String cost = null;
        String descriptionText = null;
    }

    public BoonSelectionBoard() {
        addKeyListener(new BoonSelectionBoard.KeyInputReader());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));

        if (controllers.getFirstController() != null) {
            controllerInputReader = controllers.getFirstController();
        }

        initMenuTiles();
        timer = new Timer(GameState.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));
        timer.start();
    }

    private void initMenuTiles() {
        selectBoonsTitle = BoonSelectionBoardCreator.createSelectUpgradesTitleCard();
        firstColumnBackgroundCard = BoonSelectionBoardCreator.createFirstColumnBackgroundCard();
        thirdColumnBackgroundCard = BoonSelectionBoardCreator.createThirdColumnBackgroundCard();
        fifthColumnBackgroundCard = BoonSelectionBoardCreator.createFifthColumnBackgroundCard();
        returnToSelectionBackgroundCard = BoonSelectionBoardCreator.createReturnToSelectionBackgroundCard();
        startGameBackgroundCard = BoonSelectionBoardCreator.createStartGameBackgroundCard();

        emeraldsAvailableText = BoonSelectionBoardCreator.createEmeraldText();
        descriptionBackgroundCard = BoonSelectionBoardCreator.createDescriptionBackgroundCard();
        descriptionBox = BoonSelectionBoardCreator.createDescriptionComponent(descriptionBackgroundCard);

        selectClassButton = BoonSelectionBoardCreator.createReturnToClassSelectionButton(returnToSelectionBackgroundCard);
        startGameButton = BoonSelectionBoardCreator.createStartGameButton(startGameBackgroundCard);


        recreateBoonsComponents();

        utilityColumnSelectionIndicator = BoonSelectionBoardCreator.createUpgradeSelectedCheckmark();
        utilityColumnTitleText = BoonSelectionBoardCreator.createFirstColumnTitle(firstColumnBackgroundCard);
        defensiveColumnSelectionIndicator = BoonSelectionBoardCreator.createUpgradeSelectedCheckmark();
        defenseColumnTitleText = BoonSelectionBoardCreator.createThirdColumnTitle(thirdColumnBackgroundCard);
        defenseWIPText = BoonSelectionBoardCreator.createWorkInProgressText(thirdColumnBackgroundCard);
        offensiveColumnSelectionIndicator = BoonSelectionBoardCreator.createUpgradeSelectedCheckmark();
        offenseColumnTitleText = BoonSelectionBoardCreator.createFifthColumnTitle(fifthColumnBackgroundCard);
        offenseWIPText = BoonSelectionBoardCreator.createWorkInProgressText(fifthColumnBackgroundCard);

        menuCursor = BoonSelectionBoardCreator.createCursor(selectNepotism.getComponents().get(0));
        initializedMenuObjects = true;
        recreateWindow();
    }

    public void recreateWindow() {
        if (initializedMenuObjects) {
            lastMoveTime = System.currentTimeMillis();
            menuCursor = BoonSelectionBoardCreator.createCursor(selectNepotism.getComponents().get(0));
            //Clear all existing columns/rows/grid then re-add them
            recreateList();
            selectedRow = 0;
            selectedColumn = 0;
            updateCursor();
        }
    }

    public void recreateEmeraldText() {
        emeraldsAvailableText = BoonSelectionBoardCreator.createEmeraldText();
    }

    public void recreateList() {
        firstColumn.clear();
        secondColumn.clear();
        thirdColumn.clear();
        fourthColumn.clear();
        fifthColumn.clear();
        sixthColumn.clear();
        grid.clear();
        offTheGridObjects.clear();

        grid.add(firstColumn);
        grid.add(secondColumn);
        grid.add(thirdColumn);
        grid.add(fourthColumn);
        grid.add(fifthColumn);
        grid.add(sixthColumn);

        offTheGridObjects.add(selectBoonsTitle);
        offTheGridObjects.add(firstColumnBackgroundCard);
        offTheGridObjects.add(thirdColumnBackgroundCard);
        offTheGridObjects.add(fifthColumnBackgroundCard);
        offTheGridObjects.add(returnToSelectionBackgroundCard);
        offTheGridObjects.add(startGameBackgroundCard);
        offTheGridObjects.add(descriptionBackgroundCard);

        recreateBoonsComponents();

        offTheGridObjects.addAll(emeraldsAvailableText.getComponents());
        offTheGridObjects.addAll(utilityColumnTitleText.getComponents());
        offTheGridObjects.addAll(defenseColumnTitleText.getComponents());
        offTheGridObjects.addAll(offenseColumnTitleText.getComponents());
        offTheGridObjects.addAll(offenseWIPText.getComponents());
        offTheGridObjects.addAll(defenseWIPText.getComponents());


        addToGrid(firstColumn, selectNepotism.getComponents().get(0), 0, 0);
//        firstColumn.add(selectNepotism.getComponents().get(0));
        addAllButFirstComponent(selectNepotism);
        addToGrid(secondColumn, upgradeNepotism, 1, 0);


        addToGrid(firstColumn, selectClubAccess.getComponents().get(0), 0, 1);
        addAllButFirstComponent(selectClubAccess);
        addToGrid(secondColumn, upgradeClubAccess, 1, 1);

        addToGrid(firstColumn, selectCompoundInterest.getComponents().get(0), 0, 2);
        addAllButFirstComponent(selectCompoundInterest);
        addToGrid(secondColumn, upgradeCompoundInterest, 1, 2);

        addToGrid(firstColumn, selectBountyHunter.getComponents().get(0), 0, 3);
        addAllButFirstComponent(selectBountyHunter);
        addToGrid(secondColumn, upgradeBountyHunter, 1, 3);

        addToGrid(firstColumn, selectTreasureHunter.getComponents().get(0), 0, 4);
        addAllButFirstComponent(selectTreasureHunter);
        addToGrid(secondColumn, upgradeTreasureHunter, 1, 4);

        addToGrid(firstColumn, selectClassButton.getComponents().get(0), 0, 5);
        addAllButFirstComponent(selectClassButton);

        addToGrid(sixthColumn, startGameButton.getComponents().get(0), 5, 0);
        addAllButFirstComponent(startGameButton);

        offTheGridObjects.add(menuCursor);

        updateCheckmarkLocationsToSelectedBoons();

        if (showUtilityCheckmark) {
            offTheGridObjects.add(utilityColumnSelectionIndicator);
        }

        updateCursor();
    }

    private void recreateBoonsComponents() {
        selectNepotism = BoonSelectionBoardCreator.createNepotismSelectionButton(firstColumnBackgroundCard);
        upgradeNepotism = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectNepotism, MenuFunctionEnums.UpgradeNepotism);

        selectClubAccess = BoonSelectionBoardCreator.createOtherBoonButtons(selectNepotism.getComponents().get(0), ClubAccess.getInstance());
        upgradeClubAccess = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectClubAccess, MenuFunctionEnums.UpgradeClubAccess);

        selectCompoundInterest = BoonSelectionBoardCreator.createOtherBoonButtons(selectClubAccess.getComponents().get(0), CompoundWealth.getInstance());
        upgradeCompoundInterest = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectCompoundInterest, MenuFunctionEnums.UpgradeCompoundWealth);

        selectBountyHunter = BoonSelectionBoardCreator.createOtherBoonButtons(selectCompoundInterest.getComponents().get(0), BountyHunter.getInstance());
        upgradeBountyHunter = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectBountyHunter, MenuFunctionEnums.UpgradeBountyHunter);

        selectTreasureHunter = BoonSelectionBoardCreator.createOtherBoonButtons(selectBountyHunter.getComponents().get(0), TreasureHunter.getInstance());
        upgradeTreasureHunter = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectTreasureHunter, MenuFunctionEnums.UpgradeTreasureHunter);

        emeraldsAvailableText = BoonSelectionBoardCreator.createEmeraldText();
    }

    //Helper method to add all text collection components that are intended to be displayonly
    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    private void addToGrid(List<GUIComponent> gridList, GUIComponent component, int column, int row) {
        component.setColumn(column);
        component.setRow(row);
        gridList.add(component);
    }

    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile() {
        grid.get(selectedColumn).get(selectedRow).activateComponent();
        if (grid.get(selectedColumn).get(selectedRow).getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            timer.stop();
        }
    }

    //Move a tile LEFT
    private void previousColumn() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        if (handleLeftOrRightNavigationOverwrites()) {
            return; //We overwrote the navigation
        }

        int originalRow = selectedColumn; // Keep track of the starting row to avoid infinite loop

        do {
            selectedColumn--;
            if (selectedColumn < 0) {
                selectedColumn = grid.size() - 1; // Wrap around to the bottom row
            }
        } while (grid.get(selectedColumn).isEmpty() && selectedColumn != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!grid.get(selectedColumn).isEmpty() && selectedRow >= grid.get(selectedColumn).size()) {
            selectedRow = grid.get(selectedColumn).size() - 1;
        }
    }

    // Go one menu tile RIGHT
    private void nextColumn() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        if (handleLeftOrRightNavigationOverwrites()) {
            return; //We overwrote the navigation
        }

        int originalRow = selectedColumn; // Keep track of the starting row to avoid infinite loop

        do {
            selectedColumn++;
            if (selectedColumn >= grid.size()) {
                selectedColumn = 0; // Wrap around to the top row
            }
        } while (grid.get(selectedColumn).isEmpty() && selectedColumn != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!grid.get(selectedColumn).isEmpty() && selectedRow >= grid.get(selectedColumn).size()) {
            selectedRow = grid.get(selectedColumn).size() - 1;
        }
    }


    private boolean handleLeftOrRightNavigationOverwrites() {
        //If overwritten a navigation, return true, if not return false
        if (menuCursor.getSelectedMenuTile().equals(this.selectClassButton.getComponents().get(0))) {
            selectedColumn = startGameButton.getComponents().get(0).getColumn();
            selectedRow = startGameButton.getComponents().get(0).getRow();
            return true;
        }

        if (menuCursor.getSelectedMenuTile().equals(this.startGameButton.getComponents().get(0))) {
            selectedColumn = selectClassButton.getComponents().get(0).getColumn();
            selectedRow = selectClassButton.getComponents().get(0).getRow();
            return true;
        }

        return false;
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

    // Go one menu tile down
    private void previousRow() {
        selectedRow--;
        if (selectedRow < 0) {
            selectedRow = grid.get(selectedColumn).size() - 1;
        }
    }

    // Go one menu tile up
    private void nextRow() {
        selectedRow++;
        if (selectedRow >= grid.get(selectedColumn).size()) {
            selectedRow = 0;
        }
    }

    // Update the cursor's position and selected menu tile
    private void updateCursor() {
        if (grid.get(selectedColumn).isEmpty()) { // Check if the selected row is empty
            menuCursor.setSelectedMenuTile(null); // Need to decide how you want to handle this situation
        } else {
            GUIComponent selectedTile = grid.get(selectedColumn).get(selectedRow);
            menuCursor.setSelectedMenuTile(selectedTile);
            menuCursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + menuCursor.getYDistanceModification());
            menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
            currentDescription = prepareDescriptionText(selectedTile);
        }
    }

    private DescriptionInfo prepareDescriptionText(GUIComponent selectedTileHelper) {
        DescriptionInfo descriptionInfo = new DescriptionInfo();
        if (selectedTileHelper.getMenuFunctionality() != null) {
            Boon selectedBoon = BoonSelectionBoardCreator.getBoonByMenuFunctionality(selectedTileHelper.getMenuFunctionality());
            if (selectedBoon != null) {
                if (selectedBoon.isUnlocked()) {
                    descriptionInfo.descriptionText = selectedBoon.getBoonDescription();
                    descriptionInfo.title = selectedBoon.getBoonName() + " rank: " + selectedBoon.getCurrentLevel();
                } else {
                    descriptionInfo.descriptionText = selectedBoon.getBoonUnlockCondition();
                    descriptionInfo.title = "LOCKED";
                }

                if (!selectedBoon.isUnlocked()) {
                    descriptionInfo.cost = "";
                } else if (selectedBoon.canUpgradeFurther()) {
                    descriptionInfo.cost = "Costs: " + selectedBoon.getBoonUpgradeCost() + " emerald to upgrade. You currently have: " + PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds();
                } else {
                    descriptionInfo.cost = "Max rank reached";
                }
                shouldDrawBoonDescription = true;
            } else { //not a boon selected
                descriptionInfo.descriptionText = selectedTileHelper.getDescriptionOfComponent();
                shouldDrawBoonDescription = false;
            }
        }

        return descriptionInfo;
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
                    previousColumn();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_D):
                    nextColumn();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_W):
                    previousRow();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_S):
                    nextRow();
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
    private static final long MOVE_COOLDOWN = 350; // milliseconds

    public void executeControllerInput() {
        if (controllers.getFirstController() != null) {
            boolean needsUpdate = false;
            controllerInputReader.pollController();
            long currentTime = System.currentTimeMillis();

            // Left and right navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT)) {
                    // Menu option to the left
                    previousColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT)) {
                    // Menu option to the right
                    nextColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }

                // Up and down navigation
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP)) {
                    // Menu option upwards
                    previousRow();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN)) {
                    // Menu option downwards
                    nextRow();
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

            if (needsUpdate) {
                recreateList(); // Update the GUI only if there was an action that requires it
            }
        }
    }

    /*-----------------------------End of navigation methods--------------------------*/

    /*---------------------------Drawing methods-------------------------------*/
    @Override
    public void paintComponent(Graphics g) {
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
            drawDescriptionInfo(g2d, currentDescription);

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

    }

    private void drawDescriptionInfo(Graphics2D g2d, DescriptionInfo descriptionInfo) {
        int boxWidth = descriptionBackgroundCard.getWidth();
        int boxHeight = descriptionBackgroundCard.getHeight();
        String textFont = DataClass.getInstance().getTextFont();
        int maxTextWidth = boxWidth - (textPadding * 2);

        int descriptionX = descriptionBox.getXCoordinate() + textPadding;
        int descriptionY = descriptionBox.getYCoordinate() + textPadding;


        if (shouldDrawBoonDescription && descriptionInfo.title != null) {
            g2d.setFont(new Font(textFont, Font.BOLD, Math.round(22 * DataClass.getInstance().getResolutionFactor())));  // Larger font for the title
            drawDescriptionText(g2d, descriptionInfo.title, descriptionX, descriptionY, maxTextWidth, Color.GREEN);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            descriptionY += titleMetrics.getHeight() * ((descriptionInfo.descriptionText.length() / maxTextWidth) + 2);  // Spacing after the title
        }


        // Set font and draw the description if available
        if (descriptionInfo.descriptionText != null) {
            g2d.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * DataClass.getInstance().getResolutionFactor())));  // Default font for description
            drawDescriptionText(g2d, descriptionInfo.descriptionText, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
            FontMetrics descriptionMetrics = g2d.getFontMetrics();
            descriptionY += descriptionMetrics.getHeight() * ((descriptionInfo.descriptionText.length() / maxTextWidth) + 1); // Estimate line height
        }

        if (shouldDrawBoonDescription && descriptionInfo.cost != null) {
            g2d.setFont(new Font(textFont, Font.ITALIC, Math.round(16 * DataClass.getInstance().getResolutionFactor())));  // Slightly smaller italic font for the cost
            FontMetrics costMetrics = g2d.getFontMetrics();
            int costY = descriptionBox.getYCoordinate() + boxHeight - textPadding - costMetrics.getHeight();
            drawDescriptionText(g2d, descriptionInfo.cost, descriptionX, costY, maxTextWidth, Color.WHITE);
        }
    }

    private void drawDescriptionText(Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        g2d.setColor(color);

        for (String word : words) {
            // If adding the new word exceeds the maximum line width, draw the line and start a new one
            if (metrics.stringWidth(line.toString() + word) > maxWidth) {
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

    private void updateCheckmarkLocationsToSelectedBoons() {
        placeUtilityColumnCheckmark();
    }

    private boolean showUtilityCheckmark = false;

    private void placeUtilityColumnCheckmark() {
        Boon utilityBoon = BoonManager.getInstance().getUtilityBoon();

        if (utilityBoon == null) {
            showUtilityCheckmark = false;
            return; //Nothing selected, so we dont set it
        }

        BoonEnums boonEnums = utilityBoon.getBoonEnum();
        int checkMarkXCoordinate = 0;
        int checkMarkYCoordinate = 0;
        GUITextCollection selectedTextCollection = null;

        switch (boonEnums) {
            case NEPOTISM -> {
                selectedTextCollection = selectNepotism;
            }
            case CLUB_ACCESS -> {
                selectedTextCollection = selectClubAccess;
            }
            case BOUNTY_HUNTER -> {
                selectedTextCollection = selectBountyHunter;
            }
            case TREASURE_HUNTER -> {
                selectedTextCollection = selectTreasureHunter;
            }
            case COMPOUND_WEALTH -> {
                selectedTextCollection = selectCompoundInterest;
            }
            default -> {
                showUtilityCheckmark = false;
                return;
            }
        }


        checkMarkXCoordinate = selectedTextCollection.getStartingXCoordinate() + Math.round((selectedTextCollection.getWidth() / 2));
        checkMarkYCoordinate = selectedTextCollection.getComponents().get(0).getCenterYCoordinate();

        utilityColumnSelectionIndicator.setCenterCoordinates(checkMarkXCoordinate, checkMarkYCoordinate);
        showUtilityCheckmark = true;
    }

}
