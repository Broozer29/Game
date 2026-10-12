package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopContents;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopRoller;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopSlot;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.items.WisdomBall;
import net.riezebos.bruus.tbd.game.items.items.util.WisdomBallRollManager;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.GUIComponentItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ShopBoard extends MenuBoard {

    private List<GUIComponent> regularGridFirstRow = new ArrayList<>();
    private List<GUIComponent> regularGridSecondRow = new ArrayList<>();
    private List<GUIComponent> regularGridThirdRow = new ArrayList<>();
    private List<GUIComponent> regularGridFourthRow = new ArrayList<>();
    private List<GUIComponent> regularGridFifthRow = new ArrayList<>();
    private final MenuGrid regularMenuGrid = new MenuGrid();


    private List<GUIComponent> inventoryGridFirstRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridSecondRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridThirdRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridFourthRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridFifthRow = new ArrayList<>();
    private List<GUIComponent> inventoryGridSixthRow = new ArrayList<>();
    private final MenuGrid inventoryMenuGrid = new MenuGrid();

    private List<GUIComponent> allInventoryGUIComponents = new ArrayList<>();
    // The grid the cursor is in; the inventory grid is not in grids (it is drawn only while the inventory is open)
    private MenuGrid activeGrid = regularMenuGrid;
    // Set by every input that changes the screen; onInputHandled rebuilds only then (BACK on a closed inventory changes nothing)
    private boolean rebuildNeeded = false;

    private List<GUIComponent> popGUIComponentList = new ArrayList<>();

    private DisplayOnly itemRowsBackgroundCard;
    private DisplayOnly songLengthBackgroundCard;
    private DisplayOnly songDifficultyBackgroundCard;
    private DisplayOnly descriptionBackgroundCard;
    private DisplayOnly inventoryBackgroundCard;
    private DisplayOnly nextLevelDifficultyBackground;
    private DisplayOnly returnToMainMenuBackgroundCard;
    private DisplayOnly inventoryButtonBackgroundCard;
    private DisplayOnly startNextLevelBackgroundCard;

    private GUIComponent selectPirateTribe;
    private GUIComponent selectZergTribe;
    private GUIComponent selectRoyalGuard;
    private GUIComponent easyMiniBossConfig;
    private GUIComponent mediumMiniBossConfig;
    private GUIComponent hardMiniBossConfig;
    private GUITextCollection tribeSelectionText;
    private GUITextCollection lengthSelectionText;
    private GUITextCollection nextLevelDifficultyIcon;
    private GUITextCollection additionalTextCollection;


    private GUITextCollection returnToMainMenu;
    private GUITextCollection nextLevelButton;
    private GUITextCollection playerInventoryButton;
    private GUITextCollection nextLevelModifiersTextCollection;
    private GUIComponent itemDescription;
    private GUIComponent rerollButton;
    private GUITextCollection rerollCostText;
    private GUITextCollection rerollCost;
    private GUIComponent rerollBackgroundCard;
    private GUIComponent freezeButton;
    private GUITextCollection freezeText;
    private GUIComponent freezeBackgroundCard;


    private GUITextCollection moneyIcon;
    private boolean showInventory;
    private ShopBoardCreator shopBoardCreator;

    private static class DescriptionInfo {
        String title = null;
        String cost = null;
        String descriptionText = null;
        Color rarityColor = null;
    }

    private DescriptionInfo currentDescriptionInfo = null;


    public ShopBoard() {
        grids.add(regularMenuGrid);
        showInventory = false;
        shopBoardCreator = new ShopBoardCreator();

        getTimer().start();
    }

    @Override
    public void recreateWindow() {
        for (List<GUIComponent> row : regularMenuGrid.getRows()) {
            for (GUIComponent component : row) {
                component.setVisible(false);
            }
            row.clear();
        }

        allInventoryGUIComponents.clear();
        regularMenuGrid.clear();
        for (GUIComponent component : offTheGridObjects) {
            component.setVisible(false);
        }
        offTheGridObjects.clear();


        // Initialize background cards first since they are dependencies
        itemRowsBackgroundCard = shopBoardCreator.createShopItemsBackground();
        songDifficultyBackgroundCard = shopBoardCreator.createSongDifficultyBackgroundCard(itemRowsBackgroundCard);
        songLengthBackgroundCard = shopBoardCreator.createSongLengthBackgroundCard(itemRowsBackgroundCard);

        inventoryBackgroundCard = shopBoardCreator.createInventoryBackgroundCard();
        rerollBackgroundCard = shopBoardCreator.createRerollBackgroundCard(itemRowsBackgroundCard);
        descriptionBackgroundCard = shopBoardCreator.createDescriptionRowsBackgroundCard(rerollBackgroundCard);
        nextLevelDifficultyBackground = shopBoardCreator.createShowNextLevelDifficultyBackground(songLengthBackgroundCard);
        nextLevelModifiersTextCollection = shopBoardCreator.createLevelModifiersText(songLengthBackgroundCard);
        // Create items from the shop contents
        buildItemRows();

        // Create difficulty and song length settings using the respective background cards
        selectPirateTribe = shopBoardCreator.createSelectPirateTribe(songDifficultyBackgroundCard);
        selectZergTribe = shopBoardCreator.createSelectZergTribe(songDifficultyBackgroundCard);
        selectRoyalGuard = shopBoardCreator.createSelectRoyalGuard(songDifficultyBackgroundCard);
        tribeSelectionText = shopBoardCreator.createSelectDifficultyText(songDifficultyBackgroundCard, selectRoyalGuard);


        if (!LevelManager.getInstance().isNextLevelABossLevel()) {
            regularGridFourthRow.add(selectPirateTribe);
        }
        regularGridFourthRow.add(selectZergTribe); //This gets set to red wings if its a boss level so always add it
        if (!LevelManager.getInstance().isNextLevelABossLevel()) {
            regularGridFourthRow.add(selectRoyalGuard);
        }

        offTheGridObjects.addAll(tribeSelectionText.getComponents());
        offTheGridObjects.addAll(nextLevelModifiersTextCollection.getComponents());

        if (!LevelManager.getInstance().isNextLevelABossLevel()) {
            easyMiniBossConfig = shopBoardCreator.createNoMiniBossSelection(songLengthBackgroundCard);
        }
        mediumMiniBossConfig = shopBoardCreator.createOneMinibossSelection(songLengthBackgroundCard); //This gets set to red wings if its a boss level so always add it
        if (!LevelManager.getInstance().isNextLevelABossLevel()) {
            hardMiniBossConfig = shopBoardCreator.createTwoMiniBossSelection(songLengthBackgroundCard);
        }


        songLengthBackgroundCard = shopBoardCreator.createSongLengthBackgroundCard(itemRowsBackgroundCard); //why is this needed broken???
        lengthSelectionText = shopBoardCreator.createSongSelectionText(songLengthBackgroundCard, mediumMiniBossConfig);

        if (!LevelManager.getInstance().isNextLevelABossLevel()) {
            regularGridFourthRow.add(easyMiniBossConfig);
        }

        regularGridFourthRow.add(mediumMiniBossConfig);
        if (!LevelManager.getInstance().isNextLevelABossLevel()) {
            regularGridFourthRow.add(hardMiniBossConfig);
        }
        offTheGridObjects.addAll(lengthSelectionText.getComponents());


        offTheGridObjects.add(rerollBackgroundCard);
        rerollButton = shopBoardCreator.createRerollButton(rerollBackgroundCard);
        freezeBackgroundCard = shopBoardCreator.createFreezeBackgroundCard(rerollBackgroundCard);
        freezeButton = shopBoardCreator.createFreezeButton(freezeBackgroundCard);
        freezeText = shopBoardCreator.createFreezeText(freezeBackgroundCard);
        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);

        // Setup buttons and cursor
        returnToMainMenuBackgroundCard = shopBoardCreator.createReturnToMainMenuBackgroundCard(nextLevelDifficultyBackground);
        returnToMainMenu = shopBoardCreator.createReturnToMainMenu(returnToMainMenuBackgroundCard);

        startNextLevelBackgroundCard = shopBoardCreator.createStartNextLevelBackgroundCard(nextLevelDifficultyBackground);
        nextLevelButton = shopBoardCreator.createStartNextLevelButton(startNextLevelBackgroundCard);

        inventoryButtonBackgroundCard = shopBoardCreator.createInventoryButtonBackgroundCard(nextLevelDifficultyBackground);
        playerInventoryButton = shopBoardCreator.createPlayerInventoryButton(inventoryButtonBackgroundCard);

        rerollCostText = shopBoardCreator.createRerollText(rerollBackgroundCard);
        rerollCost = shopBoardCreator.createRerollCostText(rerollBackgroundCard);

        itemDescription = shopBoardCreator.createDescriptionBox(descriptionBackgroundCard);
        menuCursor = shopBoardCreator.createCursor(returnToMainMenu);

        regularMenuGrid.reset();
        inventoryMenuGrid.reset();

        // Money and Difficulty indicators
        moneyIcon = shopBoardCreator.createMoneyObject(itemRowsBackgroundCard);
//        offTheGridObjects.addAll(moneyIcon.getComponents());

        popGUIComponentList.removeIf(component -> !component.isVisible());


        createInventoryGrid();
        // Rebuild the UI elements list
        recreateList();

    }


    //Required by VIP tickets to change the "FREE" to an actual price
    public void remakeShopRerollText() {
        if (rerollBackgroundCard == null) {
            return; //We aren't in the shop nor has the shop been visisted
        }

        rerollCost = shopBoardCreator.createRerollCostText(rerollBackgroundCard);
    }

    //Helper method to add all text collection components that are intended to be displayonly
    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    public void updateSelectedDifficultyIcons() {
        if (selectPirateTribe == null || easyMiniBossConfig == null) {
            return; //none of the components have been created yet, so we can't update the icons yet
        }
        ShopBoardCreator.updateDifficultyIconsToDifficulty(LevelManager.getInstance().getLastSelectedDifficulty(),
                selectPirateTribe, selectZergTribe, selectRoyalGuard);

        ShopBoardCreator.updateMiniBossIconsToSelection(LevelManager.getInstance().getLastSelectedMiniBossConfig(),
                easyMiniBossConfig, mediumMiniBossConfig, hardMiniBossConfig);

        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);
    }


    private void recreateList() {
        regularMenuGrid.clear();
        offTheGridObjects.clear();

        updateSelectedDifficultyIcons();

        if (!regularGridFirstRow.contains(rerollButton)) {
            regularGridFirstRow.add(rerollButton);
//            regularGridFirstRow.add(rerollButton);
        }
        if (!regularGridFirstRow.contains(freezeButton)) {
            regularGridFirstRow.add(freezeButton);
        }

        regularGridFifthRow.clear();

        regularMenuGrid.addRow(regularGridFirstRow);
        regularMenuGrid.addRow(regularGridSecondRow);
        regularMenuGrid.addRow(regularGridThirdRow);
        regularMenuGrid.addRow(regularGridFourthRow);
        regularMenuGrid.addRow(regularGridFifthRow);

        offTheGridObjects.add(returnToMainMenuBackgroundCard);
        offTheGridObjects.add(inventoryButtonBackgroundCard);
        offTheGridObjects.add(startNextLevelBackgroundCard);


//        addToGrid(regularGridFifthRow, returnToMainMenu.getComponents().get(0), 4, 0);
        regularGridFifthRow.add(returnToMainMenu.getComponents().get(0));
        addAllButFirstComponent(returnToMainMenu);

//        addToGrid(regularGridFifthRow, playerInventoryButton.getComponents().get(0), 4, 1);
        regularGridFifthRow.add(playerInventoryButton.getComponents().get(0));
        addAllButFirstComponent(playerInventoryButton);

//        addToGrid(regularGridFifthRow, nextLevelButton.getComponents().get(0), 4, 2);
        regularGridFifthRow.add(nextLevelButton.getComponents().get(0));
        addAllButFirstComponent(nextLevelButton);

//        regularGridFirstRow.add(rerollButton);


        moneyIcon = shopBoardCreator.createMoneyObject(itemRowsBackgroundCard);


        offTheGridObjects.add(nextLevelDifficultyBackground);
        offTheGridObjects.add(itemRowsBackgroundCard);
        offTheGridObjects.add(descriptionBackgroundCard);
        offTheGridObjects.addAll(nextLevelModifiersTextCollection.getComponents());

//        if (!AudioManager.getInstance().isMusicControlledByThirdPartyApp()) {
        offTheGridObjects.add(songLengthBackgroundCard);
        offTheGridObjects.addAll(lengthSelectionText.getComponents());
//        }


        offTheGridObjects.add(songDifficultyBackgroundCard);
        offTheGridObjects.addAll(moneyIcon.getComponents());
        offTheGridObjects.addAll(nextLevelDifficultyIcon.getComponents());

        offTheGridObjects.addAll(tribeSelectionText.getComponents());
        offTheGridObjects.add(rerollBackgroundCard);
        offTheGridObjects.addAll(rerollCostText.getComponents());
        offTheGridObjects.addAll(rerollCost.getComponents());
        offTheGridObjects.add(freezeBackgroundCard);
        offTheGridObjects.addAll(freezeText.getComponents());

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)) {
            additionalTextCollection = shopBoardCreator.createProtossCapacityText(itemRowsBackgroundCard);
            offTheGridObjects.addAll(additionalTextCollection.getComponents());
        }

        if (this.showInventory) {
            createInventoryGrid();
            populateInventoryGridWithItems();
        }

        updateCursor();
    }


    private void createInventoryGrid() {
        allInventoryGUIComponents.clear();

        for (List<GUIComponent> listInGrid : inventoryMenuGrid.getRows()) {
            listInGrid.clear(); //Clear all existing rows before repopulating them in createPlayerInventory()
        }

        inventoryMenuGrid.clear();
        inventoryGridSixthRow.clear();
        inventoryGridSixthRow.add(playerInventoryButton.getComponents().get(0));
        inventoryMenuGrid.addRow(inventoryGridFirstRow);
        inventoryMenuGrid.addRow(inventoryGridSecondRow);
        inventoryMenuGrid.addRow(inventoryGridThirdRow);
        inventoryMenuGrid.addRow(inventoryGridFourthRow);
        inventoryMenuGrid.addRow(inventoryGridFifthRow);
        inventoryMenuGrid.addRow(inventoryGridSixthRow);

        allInventoryGUIComponents.add(inventoryBackgroundCard);
    }

    private void buildItemRows() {
        ShopContents contents = ShopManager.getInstance().getContents();
        regularGridFirstRow = shopBoardCreator.createRowOfItems(0, contents);
        regularGridSecondRow = shopBoardCreator.createRowOfItems(1, contents);
        regularGridThirdRow = shopBoardCreator.createRowOfItems(2, contents);
    }

    public void rerollShop() {
        ShopContents contents = ShopManager.getInstance().getContents();
        ShopRoller.rollShop(contents);
        contents.setFrozen(false); //Refreshing rerolls every slot and unfreezes the shop

        List<ShopSlot> changedSlots = new ArrayList<>();
        WisdomBall wisdomball = (WisdomBall) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.WisdomBall);

        if (wisdomball != null) {
            if (wisdomball.shouldActivate()) {
                changedSlots = WisdomBallRollManager.getInstance().applyWisdomBallRoll(contents);
            }
        }

        buildItemRows();

        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();
        for (List<GUIComponent> row : List.of(regularGridFirstRow, regularGridSecondRow, regularGridThirdRow)) {
            for (GUIComponent component : row) {
                if (changedSlots.contains(((ShopItem) component).getSlot())) {
                    itemsToShowAnimAt.add(component);
                }
            }
        }
        if (!itemsToShowAnimAt.isEmpty()) {
            playWisdomBallAnimation(itemsToShowAnimAt);
        }
    }

    private void populateInventoryGridWithItems() {
        Map<ItemEnums, Item> itemMap = PlayerInventory.getInstance().getItems();
        int xCoordinateStart = inventoryBackgroundCard.getXCoordinate();
        int yCoordinateStart = inventoryBackgroundCard.getYCoordinate();

        int backgroundXPadding = Math.round(60 * DataClass.getInstance().getResolutionFactor());
        int backgroundYPadding = Math.round(60 * DataClass.getInstance().getResolutionFactor());

        int padding = Math.round(15 * DataClass.getInstance().getResolutionFactor());
        ; // Space between items
        int itemSize = ShopBoardCreator.shopItemIconDimensions; //set the icon dimensions

        int maxWidth = inventoryBackgroundCard.getWidth() - backgroundXPadding - padding;
        int maxHeight = inventoryBackgroundCard.getHeight() - backgroundYPadding - padding;
        float scale = 1 * DataClass.getInstance().getResolutionFactor();

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
            int quantityX = xCoordinate + itemSize - Math.round(25 * DataClass.getInstance().getResolutionFactor()); // Adjust for proper placement
            int quantityY = yCoordinate + itemSize - Math.round(10 * DataClass.getInstance().getResolutionFactor()); // Adjust for proper placement

            GUITextCollection itemAmount = new GUITextCollection(quantityX, quantityY, String.valueOf(quantity));
            itemAmount.setScale(1.5f * DataClass.getInstance().getResolutionFactor());
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
    private void addInventoryItemComponentToCorrespondingRow(int row, GUIComponent itemComponent) {
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
    private DescriptionInfo prepareDescriptionText(GUIComponent selectedTileHelper) {
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


    /*------------------------Navigation methods--------------------------------*/

    @Override
    protected void onConfirm() {
        activeGrid.getSelected().activateComponent();
        // Read again: activating the inventory button switches the grid
        if (activeGrid.getSelected().getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            getTimer().stop();
        }
        rebuildNeeded = true;
    }

    @Override
    protected void onBack() {
        if (showInventory) {
            setShowInventory(false);
            rebuildNeeded = true;
        }
    }

    @Override
    protected void onInputHandled() {
        if (rebuildNeeded) {
            rebuildNeeded = false;
            recreateList();
        }
    }

    @Override
    protected MenuGrid activeGrid() {
        return activeGrid;
    }

    @Override
    protected void onDirection(MenuAction action) {
        rebuildNeeded = true;
        if (action == MenuAction.RIGHT && jumpToRerollButton()) {
            return;
        }
        super.onDirection(action);
    }

    private boolean jumpToRerollButton() {
        // The cursor tile is the one from the start of the poll, as before
        GUIComponent cursorTile = menuCursor.getSelectedMenuTile();
        if (cursorTile.equals(this.regularGridFirstRow.get(7))  //The last item of the first row
                || cursorTile.equals(this.regularGridSecondRow.get(7))  //The last item of the second row
                || cursorTile.equals(this.regularGridThirdRow.get(7))) { //The last item of the third row
            regularMenuGrid.select(rerollButton);
            return true;
        }
        return false;
    }

    // Update the cursor's position and selected menu tile
    private void updateCursor() {
        activeGrid.placeCursor(menuCursor);
        GUIComponent selectedComponent = activeGrid.getSelected();
        if (selectedComponent != null) {
            currentDescriptionInfo = prepareDescriptionText(selectedComponent);
        }
    }

    /*-----------------------------End of navigation methods--------------------------*/

    /*---------------------------Drawing methods-------------------------------*/
    @Override
    protected boolean drawsOnScreenTexts() {
        return false;
    }

    @Override
    protected void drawScreenContent(Graphics2D g) {
        //Show the inventory grid if it's open
        if (showInventory) {
            for (GUIComponent component : allInventoryGUIComponents) {
                drawGUIComponent(g, component);
            }
        }
    }

    @Override
    protected void drawScreenOverlay(Graphics2D g) {
        for (GUIComponent component : popGUIComponentList) {
            if (component != null && component.isVisible()) {
                drawGUIComponent(g, component);
                component.setYCoordinate(component.getYCoordinate() - 1);
            }
        }
        drawDescriptionInfo(g, currentDescriptionInfo);
    }


    //Draws the description of the currently hovered option, called every game tick and has room for performance optimization
    private static int horizontalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
    private static int verticalPadding = Math.round(50 * DataClass.getInstance().getResolutionFactor());

    private void drawDescriptionInfo(Graphics2D g2d, DescriptionInfo descriptionInfo) {
        int boxWidth = descriptionBackgroundCard.getWidth();
        int boxHeight = descriptionBackgroundCard.getHeight();
        String textFont = DataClass.getInstance().getTextFont();
        int maxTextWidth = boxWidth - (horizontalPadding * 2);

        int descriptionX = itemDescription.getXCoordinate() + horizontalPadding;
        int descriptionY = itemDescription.getYCoordinate() + verticalPadding;


        if (descriptionInfo.title != null) {
            g2d.setFont(new Font(textFont, Font.BOLD, Math.round(22 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));  // Larger font for the title
            drawDescriptionText(g2d, descriptionInfo.title, descriptionX, descriptionY, maxTextWidth, descriptionInfo.rarityColor);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            descriptionY += titleMetrics.getHeight() + Math.round(10 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor));  // Spacing after the title
        }

        // Set font and draw the description if available
        if (descriptionInfo.descriptionText != null) {
            g2d.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));  // Default font for description
            drawDescriptionText(g2d, descriptionInfo.descriptionText, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
            FontMetrics descriptionMetrics = g2d.getFontMetrics();
            descriptionY += descriptionMetrics.getHeight() * ((descriptionInfo.descriptionText.length() / maxTextWidth) + 1); // Estimate line height
        }

        // Set font and draw the cost if available
        if (descriptionInfo.cost != null) {
            g2d.setFont(new Font(textFont, Font.ITALIC, Math.round(16 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));  // Slightly smaller italic font for the cost
            FontMetrics costMetrics = g2d.getFontMetrics();
            int costY = itemDescription.getYCoordinate() + boxHeight - verticalPadding - costMetrics.getHeight();
            drawDescriptionText(g2d, descriptionInfo.cost, descriptionX, costY, maxTextWidth, Color.WHITE);
        }
    }

    //Helper method to centralize the actual drawing on the screen
    private void drawDescriptionText(Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight() + 2;
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


    @Override
    protected void drawGUIComponent(Graphics2D g, GUIComponent component) {
        if (component instanceof ShopItem shopItem) {
            drawItemsInShop(g, shopItem);
        }
        if (component.getImage() != null) {
            // Save the original composite
            Composite originalComposite = g.getComposite();

            float alpha = component.getTransparancyAlpha();
            if (component == freezeButton) {
                if (isShopFrozen()) {
                    drawFrostGlow(g, component.getXCoordinate(), component.getYCoordinate(), component.getWidth(), component.getHeight(), 6);
                } else {
                    alpha *= 0.65f; //The icon is dimmed until the shop is frozen
                }
            }

            // Set the alpha composite
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Draw the image
            g.drawImage(component.getImage(), component.getXCoordinate(), component.getYCoordinate(), this);

            // Reset to the original composite
            g.setComposite(originalComposite);
        }
    }

    private boolean isShopFrozen() {
        ShopContents contents = ShopManager.getInstance().getContents();
        return contents != null && contents.isFrozen();
    }

    // An ice-blue border with a soft glow outside and inside it, around the given rectangle
    private void drawFrostGlow(Graphics2D g, int x, int y, int width, int height, int glowSize) {
        Stroke originalStroke = g.getStroke();
        Color originalColor = g.getColor();
        Shape originalClip = g.getClip();
        Object originalAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Outer glow: wide, faint strokes that get narrower and stronger towards the border
        for (int i = glowSize; i >= 1; i--) {
            g.setStroke(new BasicStroke(i * 2));
            g.setColor(new Color(127, 220, 255, 90 / glowSize));
            g.drawRoundRect(x - 1, y - 1, width + 2, height + 2, 8, 8);
        }

        //Inner glow, kept inside the rectangle
        g.clipRect(x, y, width, height);
        for (int i = glowSize / 2; i >= 1; i--) {
            g.setStroke(new BasicStroke(i * 2));
            g.setColor(new Color(127, 220, 255, 70 / glowSize));
            g.drawRoundRect(x, y, width, height, 8, 8);
        }
        g.setClip(originalClip);

        //The border
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(127, 220, 255));
        g.drawRoundRect(x, y, width, height, 8, 8);

        g.setStroke(originalStroke);
        g.setColor(originalColor);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, originalAntialiasing);
    }

    private void drawItemsInShop(Graphics2D g, ShopItem object) {

        int xCoordinate = object.getXCoordinate();
        int yCoordinate = object.getYCoordinate();
        String textFont = DataClass.getInstance().getTextFont();
        g.drawImage(object.getImage(), xCoordinate, yCoordinate, this);
        if (isShopFrozen() && object.getShopItemInformation().isAvailable()) {
            int framePadding = Math.round(3 * DataClass.getInstance().getResolutionFactor());
            drawFrostGlow(g, xCoordinate - framePadding, yCoordinate - framePadding,
                    object.getWidth() + framePadding * 2, object.getHeight() + framePadding * 2, 5);
        }
        g.setColor(object.getShopItemInformation().getItemRarity().getColor());

        if (object.getShopItemInformation().isAvailable()) {
            g.setFont(new Font(textFont, Font.BOLD, Math.round(10 * DataClass.getInstance().getResolutionFactor())));
            g.drawString(object.getShopItemInformation().getItem().getItemName(),
                    xCoordinate,
                    yCoordinate + object.getHeight() + Math.round(10 * DataClass.getInstance().getResolutionFactor()));

            g.setFont(new Font(textFont, Font.PLAIN, Math.round(10 * DataClass.getInstance().getResolutionFactor())));
            g.drawString(object.getShopItemInformation().getItemRarity().toString()
                    , xCoordinate
                    , yCoordinate + object.getHeight() + Math.round(22 * DataClass.getInstance().getResolutionFactor()));


            boolean isFree = object.getShopItemInformation().getCost() <= 0.001f;
            String cost = isFree ? "Free" : "$" + Math.round(object.getShopItemInformation().getCost());
            if (isFree) {
                g.setFont(new Font(textFont, Font.BOLD, Math.round(14 * DataClass.getInstance().getResolutionFactor())));
                g.setColor(ItemRarityEnums.Rare.getColor());
            }
            g.drawString(cost
                    , xCoordinate
                    , yCoordinate + object.getHeight() + Math.round(34 * DataClass.getInstance().getResolutionFactor()));
        } else {
            g.setFont(new Font(textFont, Font.PLAIN, Math.round(10 * DataClass.getInstance().getResolutionFactor())));
            g.drawString("Unavailable!",
                    xCoordinate,
                    yCoordinate + object.getHeight() + Math.round(10 * DataClass.getInstance().getResolutionFactor()));
        }
    }

    /*------------------------------End of Drawing methods-------------------------------*/

    public boolean isShowInventory() {
        return showInventory;
    }

    public void setShowInventory(boolean showInventory) {
        this.showInventory = showInventory;

        if (this.showInventory) {
            activeGrid = inventoryMenuGrid;
            inventoryMenuGrid.select(playerInventoryButton.getComponents().get(0));
        } else {
            activeGrid = regularMenuGrid;
            regularMenuGrid.select(playerInventoryButton.getComponents().get(0));
        }
        updateCursor();  //update the cursor because we move it
    }

    public void addGUIAnimation(GUIComponent incomingComponent) {
        if (!popGUIComponentList.isEmpty()) {
            GUIComponent lowestComponent = popGUIComponentList.stream()
                    .filter(Sprite::isVisible)
                    .min(Comparator.comparingInt(GUIComponent::getYCoordinate))
                    .orElse(null);

            if (lowestComponent != null) {
                incomingComponent.setYCoordinate(lowestComponent.getYCoordinate() - incomingComponent.getHeight());
            }
        }
        popGUIComponentList.add(incomingComponent);
    }

    public void playWisdomBallAnimation(List<GUIComponent> itemsToShowAnimAt) {
        //todo play audio cue


        for (GUIComponent item : itemsToShowAnimAt) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(item.getXCoordinate());
            spriteConfiguration.setyCoordinate(item.getYCoordinate());
            spriteConfiguration.setScale(1 * DataClass.getInstance().getResolutionFactor());
            spriteConfiguration.setImageType(ImageEnums.SelectNewClassAnimation); //placeholder


            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            spriteAnimation.setCenterCoordinates(item.getCenterXCoordinate(), item.getCenterYCoordinate());

            animationManager.addUpperAnimation(spriteAnimation);
        }
    }
}