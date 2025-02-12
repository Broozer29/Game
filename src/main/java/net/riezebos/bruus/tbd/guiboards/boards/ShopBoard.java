package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.GUIComponentItemInformation;
import net.riezebos.bruus.tbd.guiboards.TimerHolder;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundManager;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopBoard extends JPanel implements TimerHolder {

    private DataClass data = DataClass.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllersManager controllers = ConnectedControllersManager.getInstance();
    private ShopManager shopManager = ShopManager.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();
    private AudioManager audioManager = AudioManager.getInstance();

    private List<GUIComponent> regularGridFirstRow = new ArrayList<>();
    private List<GUIComponent> regularGridSecondRow = new ArrayList<>();
    private List<GUIComponent> regularGridThirdRow = new ArrayList<>();
    private List<GUIComponent> regularGridFourthRow = new ArrayList<>();
    private List<GUIComponent> regularGridFifthRow = new ArrayList<>();
    private List<List<GUIComponent>> regularGrid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();


    private List<GUIComponent> inventoryGridFirstRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridSecondRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridThirdRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridFourthRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridFifthRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridSixthRow = new ArrayList<>();
    private List<List<GUIComponent>> inventoryGrid = new ArrayList<>();

    private List<GUIComponent> allInventoryGUIComponents = new ArrayList<>();
    private List<List<GUIComponent>> selectedGrid = regularGrid;

    private List<GUIComponent> contractHelperList = new ArrayList<>();

    private DisplayOnly itemRowsBackgroundCard;
    private DisplayOnly songLengthBackgroundCard;
    private DisplayOnly songDifficultyBackgroundCard;
    private DisplayOnly descriptionRowsBackgroundCard;
    private DisplayOnly inventoryBackgroundCard;
    private DisplayOnly nextLevelDifficultyBackground;

    private GUIComponent selectEasyDifficulty;
    private GUIComponent selectMediumDifficulty;
    private GUIComponent selectHardDifficulty;
    private GUIComponent shortSong;
    private GUIComponent mediumSong;
    private GUIComponent longSong;
    private GUITextCollection difficultySelectionText;
    private GUITextCollection lengthSelectionText;
    private GUITextCollection nextLevelDifficultyIcon;


    private MenuCursor menuCursor;
    private GUITextCollection returnToMainMenu;
    private GUITextCollection nextLevelButton;
    private GUITextCollection playerInventoryButton;
    private GUIComponent itemDescription;
    private GUIComponent rerollButton;
    private GUITextCollection rerollCostText;
    private GUIComponent rerollBackgroundCard;


    private GUITextCollection moneyIcon;
    private Timer timer;
    private int selectedRow = 0;
    private int selectedColumn = 0;
    private ControllerInputReader controllerInputReader;
    private boolean showInventory;
    private ShopBoardCreator shopBoardCreator;

    private class DescriptionInfo {
        String title = null;
        String cost = null;
        String descriptionText = null;
        Color rarityColor = null;
    }

    private DescriptionInfo currentDescriptionInfo = null;


    public ShopBoard () {
        addKeyListener(new TAdapter());
        setFocusable(true);
        showInventory = false;
        shopBoardCreator = new ShopBoardCreator(boardWidth, boardHeight, 100, 100, 35,
                60, 75, 20, 1, 1, shopManager);
        if (controllers.getFirstController() != null) {
            controllerInputReader = controllers.getFirstController();
        }

        timer = new Timer(GameStateInfo.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));
        timer.start();
    }

    public void initShopBoardGUIComponents () {
        for (List<GUIComponent> row : regularGrid) {
            for (GUIComponent component : row) {
                component.setVisible(false);
            }
            row.clear();
        }

        allInventoryGUIComponents.clear();
        regularGrid.clear();
        for (GUIComponent component : offTheGridObjects) {
            component.setVisible(false);
        }
        offTheGridObjects.clear();
        lastMoveTime = System.currentTimeMillis(); //To prevent the user from immediatly pressing another button after going to this screen


        // Initialize background cards first since they are dependencies
        itemRowsBackgroundCard = shopBoardCreator.createItemRowsBackgroundCard();
        songDifficultyBackgroundCard = shopBoardCreator.createSongDifficultyBackgroundCard();
        songLengthBackgroundCard = shopBoardCreator.createSongLengthBackgroundCard();
        descriptionRowsBackgroundCard = shopBoardCreator.createDescriptionRowsBackgroundCard();
        inventoryBackgroundCard = shopBoardCreator.createInventoryBackgroundCard();
        nextLevelDifficultyBackground = shopBoardCreator.createNextLevelDifficultyBackground();
        rerollBackgroundCard = shopBoardCreator.createRerollBackgroundCard(nextLevelDifficultyBackground);

        // Create items
        regularGridFirstRow = shopBoardCreator.createNewFirstRowOfItems();
        regularGridSecondRow = shopBoardCreator.createNewSecondRowOfItems();
        regularGridThirdRow = shopBoardCreator.createNewThirdRowOfItems();

        //Activate any contracts if there are any:
        shopManager.activateFinishedContracts();

        // Create difficulty and song length settings using the respective background cards
        selectEasyDifficulty = shopBoardCreator.createSelectEasyDifficulty(songDifficultyBackgroundCard);
        selectMediumDifficulty = shopBoardCreator.createSelectMediumDifficulty(songDifficultyBackgroundCard);
        selectHardDifficulty = shopBoardCreator.createSelectHardDifficulty(songDifficultyBackgroundCard);
        difficultySelectionText = shopBoardCreator.createSelectDifficultyText(songDifficultyBackgroundCard, selectHardDifficulty);


        regularGridFourthRow.add(selectEasyDifficulty);
        regularGridFourthRow.add(selectMediumDifficulty);
        regularGridFourthRow.add(selectHardDifficulty);
        offTheGridObjects.addAll(difficultySelectionText.getComponents());

        shortSong = shopBoardCreator.createShortSongSelection(songLengthBackgroundCard);
        mediumSong = shopBoardCreator.createMediumSongSelection(songLengthBackgroundCard);
        longSong = shopBoardCreator.createLongSelection(songLengthBackgroundCard);
        lengthSelectionText = shopBoardCreator.createSongSelectionText(songLengthBackgroundCard, longSong);

        if (!AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
            regularGridFourthRow.add(shortSong);
            regularGridFourthRow.add(mediumSong);
            regularGridFourthRow.add(longSong);
            offTheGridObjects.addAll(lengthSelectionText.getComponents());
        }
        offTheGridObjects.add(rerollBackgroundCard);

        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);

        // Setup buttons and cursor
        returnToMainMenu = shopBoardCreator.createReturnToMainMenu();
        nextLevelButton = shopBoardCreator.createStartNextLevelButton();
        playerInventoryButton = shopBoardCreator.createPlayerInventoryButton();
        regularGridFifthRow.add(returnToMainMenu.getComponents().get(0));
        addAllButFirstComponent(returnToMainMenu);
        regularGridFifthRow.add(nextLevelButton.getComponents().get(0));
        addAllButFirstComponent(nextLevelButton);
        regularGridFifthRow.add(playerInventoryButton.getComponents().get(0));
        addAllButFirstComponent(playerInventoryButton);
        rerollButton = shopBoardCreator.createRerollButton(rerollBackgroundCard);
        regularGridThirdRow.add(rerollButton);

        shopManager.resetFreeRerolls();
        rerollCostText = shopBoardCreator.createRerollCostText(rerollBackgroundCard);
        offTheGridObjects.addAll(rerollCostText.getComponents());

        itemDescription = shopBoardCreator.createDescriptionBox();
        menuCursor = shopBoardCreator.createCursor(returnToMainMenu);


        // Money and Difficulty indicators
        moneyIcon = shopBoardCreator.createMoneyObject(nextLevelDifficultyBackground);
        offTheGridObjects.addAll(moneyIcon.getComponents());

        contractHelperList.removeIf(component -> !component.isVisible());


        createInventoryGrid();
        // Rebuild the UI elements list
        recreateList();
    }

    //Required by VIP tickets to change the "FREE" to an actual price
    public void remakeShopRerollText () {
        if (rerollBackgroundCard == null) {
            return; //We aren't in the shop nor has the shop been visisted
        }

        rerollCostText = shopBoardCreator.createRerollCostText(rerollBackgroundCard);
    }

    //Helper method to add all text collection components that are intended to be displayonly
    private void addAllButFirstComponent (GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    private void updateSelectedDifficultyIcons () {
        shopBoardCreator.updateDifficultyIconsToDifficulty(LevelManager.getInstance().getCurrentLevelDifficulty(),
                selectEasyDifficulty, selectMediumDifficulty, selectHardDifficulty);
        if (!AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
            shopBoardCreator.updateLengthIconsToLength(LevelManager.getInstance().getCurrentLevelLength(),
                    shortSong, mediumSong, longSong);
        }
    }


    private void recreateList () {
        regularGrid.clear();
        offTheGridObjects.clear();

        updateSelectedDifficultyIcons();
        addAllButFirstComponent(returnToMainMenu);
        addAllButFirstComponent(nextLevelButton);
        addAllButFirstComponent(playerInventoryButton);

        if (!regularGridThirdRow.contains(rerollButton)) {
            regularGridThirdRow.add(rerollButton);
        }


        regularGrid.add(regularGridFirstRow);
        regularGrid.add(regularGridSecondRow);
        regularGrid.add(regularGridThirdRow);
        regularGrid.add(regularGridFourthRow);
        regularGrid.add(regularGridFifthRow);


        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);
        moneyIcon = shopBoardCreator.createMoneyObject(nextLevelDifficultyBackground);

        offTheGridObjects.add(nextLevelDifficultyBackground);
        offTheGridObjects.add(itemRowsBackgroundCard);
        offTheGridObjects.add(descriptionRowsBackgroundCard);

        if (!AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
            offTheGridObjects.add(songLengthBackgroundCard);
            offTheGridObjects.addAll(lengthSelectionText.getComponents());
        }


        offTheGridObjects.add(songDifficultyBackgroundCard);
        offTheGridObjects.addAll(moneyIcon.getComponents());
        offTheGridObjects.addAll(nextLevelDifficultyIcon.getComponents());

        offTheGridObjects.addAll(difficultySelectionText.getComponents());
        offTheGridObjects.add(rerollBackgroundCard);
        offTheGridObjects.addAll(rerollCostText.getComponents());

        if (this.showInventory) {
            createInventoryGrid();
            populateInventoryGridWithItems();
        }

        updateCursor();
    }

    private void createInventoryGrid () {
        allInventoryGUIComponents.clear();

        for (List<GUIComponent> listInGrid : inventoryGrid) {
            listInGrid.clear(); //Clear all existing rows before repopulating them in createPlayerInventory()
        }

        inventoryGrid.clear();
        inventoryGridSixthRow.clear();
        inventoryGridSixthRow.add(playerInventoryButton.getComponents().get(0));
        inventoryGrid.add(inventoryGridFirstRow);
        inventoryGrid.add(inventoryGridSecondRow);
        inventoryGrid.add(inventoryGridThirdRow);
        inventoryGrid.add(inventoryGridFourthRow);
        inventoryGrid.add(inventoryGridFifthRow);
        inventoryGrid.add(inventoryGridSixthRow);

        allInventoryGUIComponents.add(inventoryBackgroundCard);
    }

    public void rerollShop () {
        regularGridFirstRow = shopBoardCreator.createNewFirstRowOfItems();
        regularGridSecondRow = shopBoardCreator.createNewSecondRowOfItems();
        regularGridThirdRow = shopBoardCreator.createNewThirdRowOfItems();
    }

    private void populateInventoryGridWithItems () {
        Map<ItemEnums, Item> itemMap = PlayerInventory.getInstance().getItems();
        int xCoordinateStart = inventoryBackgroundCard.getXCoordinate();
        int yCoordinateStart = inventoryBackgroundCard.getYCoordinate();

        int backgroundXPadding = 60;
        int backgroundYPadding = 60;

        int padding = 15; // Space between items
        int itemSize = 80; //set the icon dimensions

        int maxWidth = inventoryBackgroundCard.getWidth() - backgroundXPadding - padding;
        int maxHeight = inventoryBackgroundCard.getHeight() - backgroundYPadding - padding;
        int scale = 1;

        // Variables to control the layout within the inventory card
        int itemsPerRow = maxWidth / (itemSize + padding);
        int xCoordinate = xCoordinateStart + padding + backgroundXPadding;
        int yCoordinate = yCoordinateStart + padding + backgroundYPadding;
        int currentRow = 1;

        // Loop over each item in the inventory
        for (ItemEnums itemEnum : itemMap.keySet()) {
            // Create a menu object for the item
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinate);
            spriteConfiguration.setScale(scale);
            spriteConfiguration.setImageType(itemMap.get(itemEnum).getItemEnum().getItemIcon());

            GUIComponent itemComponent = new DisplayOnly(spriteConfiguration);
            itemComponent.setImageDimensions(itemSize, itemSize);
            String itemDesc = ItemDescriptionRetriever.getDescriptionOfItem(itemEnum);

            GUIComponentItemInformation guiComponentItemInformation = new GUIComponentItemInformation(
                    itemEnum, itemEnum.getItemRarity(), itemDesc, true, itemEnum.getItemRarity().getItemCost()
            );
            itemComponent.setShopItemInformation(guiComponentItemInformation);


            int quantity = itemMap.get(itemEnum).getQuantity();

            // Calculate the position for the quantity indicator
            int quantityX = xCoordinate + itemSize - 25; // Adjust for proper placement
            int quantityY = yCoordinate + itemSize - 10; // Adjust for proper placement

            GUITextCollection itemAmount = new GUITextCollection(quantityX, quantityY, String.valueOf(quantity));
            itemAmount.setScale(1.5f);
            addInventoryItemComponentToCorrespondingRow(currentRow, itemComponent);
            allInventoryGUIComponents.addAll(itemAmount.getComponents());

            // Update xCoordinate, wrapping and resetting yCoordinate as needed
            if ((xCoordinate - xCoordinateStart) / (itemSize + padding) >= itemsPerRow - 1) {
                // Move down to the next row and reset xCoordinate
                currentRow++;
                yCoordinate += itemSize + padding;
                xCoordinate = xCoordinateStart + padding + backgroundXPadding;
                // If yCoordinate goes outside of the maxHeight, break out of the loop
                if (yCoordinate + itemSize > yCoordinateStart + maxHeight) {
                    break;
                }
            } else {
                // Move right to the next item position
                xCoordinate += itemSize + padding;
            }
        }
    }

    //Helper method for adding inventory gui components to the correct row on the inventorygrid
    private void addInventoryItemComponentToCorrespondingRow (int row, GUIComponent itemComponent) {
        if (itemComponent != null) {
            allInventoryGUIComponents.add(itemComponent);
        }
        switch (row) {
            case 1:
                inventoryGridFirstRow.add(itemComponent);
                break;
            case 2:
                inventoryGridSecondRow.add(itemComponent);
                break;
            case 3:
                inventoryGridThirdRow.add(itemComponent);
                break;
            case 4:
                inventoryGridFourthRow.add(itemComponent);
                break;
            case 5:
                inventoryGridFifthRow.add(itemComponent);
                break;
            default:
                OnScreenTextManager.getInstance().addText("Too many items to display in inventory",
                        DataClass.getInstance().getWindowWidth() / 2,
                        DataClass.getInstance().getWindowHeight() / 2);
                break;
        }
    }

    //Helper method for creating DescriptionInfo objects to centralize the creation of description box text
    private DescriptionInfo prepareDescriptionText (GUIComponent selectedTileHelper) {
        DescriptionInfo descriptionInfo = new DescriptionInfo();
        if (selectedTileHelper.getShopItemInformation() != null) {
            // If the item is a ShopItem, gather its information
            descriptionInfo.title = selectedTileHelper.getShopItemInformation().getItem().getItemName();
            descriptionInfo.descriptionText = selectedTileHelper.getShopItemInformation().getItemDescription();
            descriptionInfo.rarityColor = selectedTileHelper.getShopItemInformation().getItemRarity().getColor();

            if (!showInventory) {
                descriptionInfo.cost = "Costs: " + Math.round(selectedTileHelper.getShopItemInformation().getCost()) + " minerals. You have: " + Math.round(PlayerInventory.getInstance().getCashMoney());
            } else {
                //Replacing the itemcost with the amount the player has
                descriptionInfo.cost = "You have: " + PlayerInventory.getInstance().getItemFromInventoryIfExists(selectedTileHelper.getShopItemInformation().getItem()).getQuantity() + " of this item.";
            }

        } else {
            // For other GUI components, use the description directly
            descriptionInfo.descriptionText = selectedTileHelper.getDescriptionOfComponent();
        }

        return descriptionInfo;
    }


    public Timer getTimer () {
        return timer;
    }

    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile () {
        selectedGrid.get(selectedRow).get(selectedColumn).activateComponent();
        if (selectedGrid.get(selectedRow).get(selectedColumn).getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            timer.stop();
        }
    }

    private void previousMenuTile () {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

        do {
            selectedRow--;
            if (selectedRow < 0) {
                selectedRow = selectedGrid.size() - 1; // Wrap around to the bottom row
            }
        } while (selectedGrid.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!selectedGrid.get(selectedRow).isEmpty() && selectedColumn >= selectedGrid.get(selectedRow).size()) {
            selectedColumn = selectedGrid.get(selectedRow).size() - 1;
        }
        updateCursor();
    }

    // Go one menu tile downwards
    private void nextMenuTile () {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

        do {
            selectedRow++;
            if (selectedRow >= selectedGrid.size()) {
                selectedRow = 0; // Wrap around to the top row
            }
        } while (selectedGrid.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!selectedGrid.get(selectedRow).isEmpty() && selectedColumn >= selectedGrid.get(selectedRow).size()) {
            selectedColumn = selectedGrid.get(selectedRow).size() - 1;
        }
        updateCursor();
    }

    // Check if the grid is empty
    private boolean isGridEmpty () {
        for (List<GUIComponent> row : selectedGrid) {
            if (!row.isEmpty()) {
                return false; // Return false as soon as a non-empty row is found
            }
        }
        return true; // If no non-empty rows are found, the grid is empty
    }

    // Go one menu tile to the left
    private void previousMenuColumn () {
        selectedColumn--;
        if (selectedColumn < 0) {
            selectedColumn = selectedGrid.get(selectedRow).size() - 1; // Wrap around to the rightmost column
        }
        updateCursor();
    }

    // Go one menu tile to the right
    private void nextMenuColumn () {
        selectedColumn++;
        if (selectedColumn >= selectedGrid.get(selectedRow).size()) {
            selectedColumn = 0; // Wrap around to the leftmost column
        }
        updateCursor();
    }

    // Update the cursor's position and selected menu tile
    private void updateCursor () {
        if (selectedGrid.get(selectedRow).isEmpty()) {
            menuCursor.setSelectedMenuTile(null);
        } else {
            GUIComponent selectedComponent = selectedGrid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedComponent);
            menuCursor.setYCoordinate(selectedComponent.getYCoordinate());
            menuCursor.setXCoordinate(selectedComponent.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
            currentDescriptionInfo = prepareDescriptionText(selectedComponent);
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased (KeyEvent e) {
            //This is reversed in shopboard lmao
            int key = e.getKeyCode();
            boolean needsUpdate = false;
            switch (key) {
                case (KeyEvent.VK_ENTER):
                    selectMenuTile();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_A):
                    updateCursor();
                    previousMenuColumn();
                    needsUpdate = false;
                    break;
                case (KeyEvent.VK_D):
                    updateCursor();
                    nextMenuColumn();
                    needsUpdate = false;
                    break;
                case (KeyEvent.VK_W):
                    updateCursor();
                    previousMenuTile();
                    needsUpdate = false;
                    break;
                case (KeyEvent.VK_S):
                    nextMenuTile();
                    updateCursor();
                    needsUpdate = false;
                    break;
            }

            if (needsUpdate) {
                recreateList(); // Update the GUI only if there was an action that requires it
            }
        }

        @Override
        public void keyPressed (KeyEvent e) {
            //Shouldnt do anything, keyrelease activates input
        }
    }

    private long lastMoveTime = 0;
    private static final long MOVE_COOLDOWN = 300; // milliseconds

    public void executeControllerInput () {
        if (controllers.getFirstController() != null) {
            boolean needsUpdate = false;
            controllerInputReader.pollController();
            long currentTime = System.currentTimeMillis();

            // Left and right navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP)) {
                    //Gaat naar boven
                    // Menu option to the left
                    previousMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN)) {
                    //Gaat nar beneden
                    // Menu option to the right
                    nextMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            // Up and down navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT)) {
                    //Gaat naar links
                    // Menu option upwards
                    previousMenuColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT)) {
                    // Menu option downwards
                    //Gaat naar rechts
                    nextMenuColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            if (currentTime - lastMoveTime > MOVE_COOLDOWN &&
                    controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
                // Select menu option
                selectMenuTile();
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
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Draws all background objects
        for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
            drawImage(g2d, bgObject);
        }

        for (SpriteAnimation animation : animationManager.getLowerAnimations()) {
            drawAnimation(g2d, animation);
        }

        drawObjects(g2d);
        drawDescriptionInfo(g2d, currentDescriptionInfo);

        for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
            drawAnimation(g2d, animation);
        }
        animationManager.updateGameTick();
        backgroundManager.updateGameTick();
        Toolkit.getDefaultToolkit().sync();

        // Reading controller input
        executeControllerInput();
    }


    //Draws the description of the currently hovered option, called every game tick and has room for performance optimization
    private static int horizontalPadding = 40;
    private static int verticalPadding = 40;

    private void drawDescriptionInfo (Graphics2D g2d, DescriptionInfo descriptionInfo) {
        int boxWidth = descriptionRowsBackgroundCard.getWidth();
        int boxHeight = descriptionRowsBackgroundCard.getHeight();
        String textFont = DataClass.getInstance().getTextFont();
//        int horizontalPadding = 40;
//        int verticalPadding = 40;
        int maxTextWidth = boxWidth - (horizontalPadding * 2);

        int descriptionX = itemDescription.getXCoordinate() + horizontalPadding;
        int descriptionY = itemDescription.getYCoordinate() + verticalPadding;


        if (descriptionInfo.title != null) {
            g2d.setFont(new Font(textFont, Font.BOLD, 24));  // Larger font for the title
            drawDescriptionText(g2d, descriptionInfo.title, descriptionX, descriptionY, maxTextWidth, descriptionInfo.rarityColor);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            descriptionY += titleMetrics.getHeight() + 10;  // Spacing after the title
        }

        // Set font and draw the description if available
        if (descriptionInfo.descriptionText != null) {
            g2d.setFont(new Font(textFont, Font.PLAIN, 20));  // Default font for description
            drawDescriptionText(g2d, descriptionInfo.descriptionText, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
            FontMetrics descriptionMetrics = g2d.getFontMetrics();
            descriptionY += descriptionMetrics.getHeight() * ((descriptionInfo.descriptionText.length() / maxTextWidth) + 1); // Estimate line height
        }

        // Set font and draw the cost if available
        if (descriptionInfo.cost != null) {
            g2d.setFont(new Font(textFont, Font.ITALIC, 18));  // Slightly smaller italic font for the cost
            FontMetrics costMetrics = g2d.getFontMetrics();
            int costY = itemDescription.getYCoordinate() + boxHeight - verticalPadding - costMetrics.getHeight();
            drawDescriptionText(g2d, descriptionInfo.cost, descriptionX, costY, maxTextWidth, Color.WHITE);
        }
    }

    //Helper method to centralize the actual drawing on the screen
    private void drawDescriptionText (Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
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


    private void drawObjects (Graphics2D g) {
        for (GUIComponent component : offTheGridObjects) {
            if (component != null) {
                drawGUIComponent(g, component);
            }
        }


        //Always show the regular grid
        for (List<GUIComponent> list : regularGrid) {
            for (GUIComponent component : list) {
                if (component instanceof ShopItem shopItem) {
                    drawItemsInShop(g, shopItem);
                }
                drawGUIComponent(g, component);
            }
        }


        //Show the inventory grid if it's open
        if (showInventory) {
            for (GUIComponent component : allInventoryGUIComponents) {
                drawGUIComponent(g, component);
            }

//            drawGUIComponent(g, inventoryBackgroundCard);
//            for (List<GUIComponent> list : inventoryGrid) {
//                for (GUIComponent component : list) {
//                    drawGUIComponent(g, component);
//                }
//            }
        }

        for (GUIComponent component : contractHelperList) {
            if (component != null && component.isVisible()) {
                drawGUIComponent(g, component);
                component.setYCoordinate(component.getYCoordinate() - 1);
            }
        }

        drawGUIComponent(g, menuCursor);
    }


    private void drawGUIComponent (Graphics2D g, GUIComponent component) {
        if (component.getImage() != null) {
            // Save the original composite
            Composite originalComposite = g.getComposite();

            // Set the alpha composite
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, component.getTransparancyAlpha()));

            // Draw the image
            g.drawImage(component.getImage(), component.getXCoordinate(), component.getYCoordinate(), this);

            // Reset to the original composite
            g.setComposite(originalComposite);
        }
    }

    private void drawItemsInShop (Graphics g, ShopItem object) {

        int xCoordinate = object.getXCoordinate();
        int yCoordinate = object.getYCoordinate();
        String textFont = DataClass.getInstance().getTextFont();
        g.drawImage(object.getImage(), xCoordinate, yCoordinate, this);
        g.setColor(object.getShopItemInformation().getItemRarity().getColor());

        if (object.getShopItemInformation().isAvailable()) {
            g.setFont(new Font(textFont, Font.BOLD, 10));
            g.drawString(object.getShopItemInformation().getItem().getItemName(),
                    xCoordinate,
                    yCoordinate + object.getHeight() + 10);

            g.setFont(new Font(textFont, Font.PLAIN, 10));
            g.drawString(object.getShopItemInformation().getItemRarity().toString()
                    , xCoordinate
                    , yCoordinate + object.getHeight() + 22);

            g.drawString("$" + object.getShopItemInformation().getCost()
                    , xCoordinate
                    , yCoordinate + object.getHeight() + 34);
        } else {
            g.setFont(new Font(textFont, Font.PLAIN, 10));
            g.drawString("Unavailable!",
                    xCoordinate,
                    yCoordinate + object.getHeight() + 10);
        }
    }

    private void drawImage (Graphics g, Sprite sprite) {
        if (sprite.getImage() != null) {
            g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
        }
    }

    private void drawAnimation (Graphics2D g, SpriteAnimation animation) {
        if (animation.getCurrentFrameImage(false) != null) {
            g.drawImage(animation.getCurrentFrameImage(true), animation.getXCoordinate(), animation.getYCoordinate(), this);
        }
    }

    /*------------------------------End of Drawing methods-------------------------------*/

    public boolean isShowInventory () {
        return showInventory;
    }

    public void setShowInventory (boolean showInventory) {
        this.showInventory = showInventory;

        if (this.showInventory) {
            selectedGrid = inventoryGrid;
            selectedColumn = 0;
            selectedRow = 5;
            updateCursor(); //update the cursor because we move it
        } else {
            selectedGrid = regularGrid;
            selectedColumn = 2;
            selectedRow = 4;
            updateCursor();  //update the cursor because we move it
        }

    }

    public void addContractAnimation (GUIComponent component) {
        contractHelperList.add(component);
    }
}