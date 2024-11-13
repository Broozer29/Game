package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.game.gameobjects.background.BackgroundManager;
import net.riezebos.bruus.tbd.game.gameobjects.background.BackgroundObject;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopBoard extends JPanel implements ActionListener {

    private DataClass data = DataClass.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllersManager controllers = ConnectedControllersManager.getInstance();
    private ShopManager shopManager = ShopManager.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();
    private AudioManager audioManager = AudioManager.getInstance();

    private List<GUIComponent> firstRow = new ArrayList<>();
    private List<GUIComponent> secondRow = new ArrayList<>();
    private List<GUIComponent> thirdRow = new ArrayList<>();
    private List<GUIComponent> fourthRow = new ArrayList<>();
    private List<GUIComponent> fifthRow = new ArrayList<>();
    private List<List<GUIComponent>> grid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();

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
    private List<GUIComponent> playerInventoryMenuObjects = new ArrayList<>();
    private Timer timer;
    private int selectedRow = 0;
    private int selectedColumn = 0;
    private ControllerInputReader controllerInputReader;
    private boolean showInventory;
    private ShopBoardCreator shopBoardCreator;


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

    public void createWindow () {
        for(List<GUIComponent> row: grid){
            for(GUIComponent component: row){
                component.setVisible(false);
            }
            row.clear();
        }
        grid.clear();
        for(GUIComponent component : offTheGridObjects){
            component.setVisible(false);
        }
        offTheGridObjects.clear();
        playerInventoryMenuObjects.clear();


        // Initialize background cards first since they are dependencies
        itemRowsBackgroundCard = shopBoardCreator.createItemRowsBackgroundCard();
        songDifficultyBackgroundCard = shopBoardCreator.createSongDifficultyBackgroundCard();
        songLengthBackgroundCard = shopBoardCreator.createSongLengthBackgroundCard();
        descriptionRowsBackgroundCard = shopBoardCreator.createDescriptionRowsBackgroundCard();
        inventoryBackgroundCard = shopBoardCreator.createInventoryBackgroundCard();
        nextLevelDifficultyBackground = shopBoardCreator.createNextLevelDifficultyBackground();
        rerollBackgroundCard = shopBoardCreator.createRerollBackgroundCard(nextLevelDifficultyBackground);

        // Create other UI components
        firstRow = shopBoardCreator.createNewFirstRowOfItems();
        secondRow = shopBoardCreator.createNewSecondRowOfItems();
        thirdRow = shopBoardCreator.createNewThirdRowOfItems();


        // Create difficulty and song length settings using the respective background cards
        selectEasyDifficulty = shopBoardCreator.createSelectEasyDifficulty(songDifficultyBackgroundCard);
        selectMediumDifficulty = shopBoardCreator.createSelectMediumDifficulty(songDifficultyBackgroundCard);
        selectHardDifficulty = shopBoardCreator.createSelectHardDifficulty(songDifficultyBackgroundCard);
        difficultySelectionText = shopBoardCreator.createSelectDifficultyText(songDifficultyBackgroundCard, selectHardDifficulty);


        fourthRow.add(selectEasyDifficulty);
        fourthRow.add(selectMediumDifficulty);
        fourthRow.add(selectHardDifficulty);
        offTheGridObjects.addAll(difficultySelectionText.getComponents());

        shortSong = shopBoardCreator.createShortSongSelection(songLengthBackgroundCard);
        mediumSong = shopBoardCreator.createMediumSongSelection(songLengthBackgroundCard);
        longSong = shopBoardCreator.createLongSelection(songLengthBackgroundCard);
        lengthSelectionText = shopBoardCreator.createSongSelectionText(songLengthBackgroundCard, longSong);

        if(AudioManager.getInstance().getMusicMediaPlayer().equals(MusicMediaPlayer.Default)) {
            fourthRow.add(shortSong);
            fourthRow.add(mediumSong);
            fourthRow.add(longSong);
        } else {
            lengthSelectionText.setStartingXCoordinate(songLengthBackgroundCard.getXCoordinate() + Math.round(songLengthBackgroundCard.getWidth() * 0.05f));
            lengthSelectionText.setText("MUSIC DETERMINED BY ITUNES OR SPOTIFY");
        }

        offTheGridObjects.addAll(lengthSelectionText.getComponents());
        offTheGridObjects.add(rerollBackgroundCard);

        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);

        // Setup buttons and cursor
        returnToMainMenu = shopBoardCreator.createReturnToMainMenu();
        nextLevelButton = shopBoardCreator.createStartNextLevelButton();
        playerInventoryButton = shopBoardCreator.createPlayerInventoryButton();
        fifthRow.add(returnToMainMenu.getComponents().get(0));
        addAllButFirstComponent(returnToMainMenu);
        fifthRow.add(nextLevelButton.getComponents().get(0));
        addAllButFirstComponent(nextLevelButton);
        fifthRow.add(playerInventoryButton.getComponents().get(0));
        addAllButFirstComponent(playerInventoryButton);
        rerollButton = shopBoardCreator.createRerollButton(rerollBackgroundCard);
        thirdRow.add(rerollButton);
        rerollCostText = shopBoardCreator.createRerollCostText(rerollBackgroundCard);
        offTheGridObjects.addAll(rerollCostText.getComponents());

        itemDescription = shopBoardCreator.createDescriptionBox();


        menuCursor = shopBoardCreator.createCursor(returnToMainMenu);

        // Money and Difficulty indicators
        moneyIcon = shopBoardCreator.createMoneyObject(nextLevelDifficultyBackground);
        offTheGridObjects.addAll(moneyIcon.getComponents());

        // Rebuild the UI elements list
        recreateList();
    }

    public void remakeShopRerollText () {
        rerollCostText = shopBoardCreator.createRerollCostText(rerollBackgroundCard);
    }

    private void addAllButFirstComponent (GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    private void updateSelectedDifficultyIcons(){
        shopBoardCreator.updateDifficultyIconsToDifficulty(LevelManager.getInstance().getCurrentLevelDifficulty(),
                selectEasyDifficulty, selectMediumDifficulty, selectHardDifficulty);
        shopBoardCreator.updateLengthIconsToLength(LevelManager.getInstance().getCurrentLevelLength(),
                shortSong, mediumSong, longSong);
    }


    private void recreateList () {
        grid.clear();
        offTheGridObjects.clear();
        playerInventoryMenuObjects.clear();

        updateSelectedDifficultyIcons();
        addAllButFirstComponent(returnToMainMenu);
        addAllButFirstComponent(nextLevelButton);
        addAllButFirstComponent(playerInventoryButton);

        if (!thirdRow.contains(rerollButton)) {
            thirdRow.add(rerollButton);
        }


        grid.add(firstRow);
        grid.add(secondRow);
        grid.add(thirdRow);
        grid.add(fourthRow);
        grid.add(fifthRow);


        updateNextLevelDifficultyIcon();
        updateMoneyIcon();

        offTheGridObjects.add(nextLevelDifficultyBackground);
        offTheGridObjects.add(itemRowsBackgroundCard);
        offTheGridObjects.add(descriptionRowsBackgroundCard);
        offTheGridObjects.add(songLengthBackgroundCard);
        offTheGridObjects.add(songDifficultyBackgroundCard);
        offTheGridObjects.addAll(moneyIcon.getComponents());
        offTheGridObjects.addAll(nextLevelDifficultyIcon.getComponents());
        offTheGridObjects.addAll(lengthSelectionText.getComponents());
        offTheGridObjects.addAll(difficultySelectionText.getComponents());
        offTheGridObjects.add(rerollBackgroundCard);
        offTheGridObjects.addAll(rerollCostText.getComponents());


        if (this.showInventory) {
            playerInventoryMenuObjects.add(inventoryBackgroundCard);
            createPlayerInventory();
        }


        updateCursor();
    }

    public void rerollShop () {
        firstRow = shopBoardCreator.createNewFirstRowOfItems();
        secondRow = shopBoardCreator.createNewSecondRowOfItems();
        thirdRow = shopBoardCreator.createNewThirdRowOfItems();
    }


    private void updateNextLevelDifficultyIcon () {
        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);
    }

    private void updateMoneyIcon () {
        moneyIcon = shopBoardCreator.createMoneyObject(nextLevelDifficultyBackground);
    }


    private void createPlayerInventory () {
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

        // Loop over each item in the inventory
        for (ItemEnums itemEnum : itemMap.keySet()) {
            // Create a menu object for the item
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinate);
            spriteConfiguration.setScale(scale);
            spriteConfiguration.setImageType(itemMap.get(itemEnum).getItemName().getItemIcon());

            GUIComponent itemComponent = new DisplayOnly(spriteConfiguration);
            itemComponent.setImageDimensions(itemSize, itemSize);
            playerInventoryMenuObjects.add(itemComponent);

            int quantity = itemMap.get(itemEnum).getQuantity();

            // Calculate the position for the quantity indicator
            int quantityX = xCoordinate + itemSize - 25; // Adjust for proper placement
            int quantityY = yCoordinate + itemSize - 10; // Adjust for proper placement

            GUITextCollection itemAmount = new GUITextCollection(quantityX, quantityY, String.valueOf(quantity));
            itemAmount.setScale(1.5f);
            playerInventoryMenuObjects.addAll(itemAmount.getComponents());

            // Update xCoordinate, wrapping and resetting yCoordinate as needed
            if ((xCoordinate - xCoordinateStart) / (itemSize + padding) >= itemsPerRow - 1) {
                // Move down to the next row and reset xCoordinate
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

    private void updateDescriptionBox(Graphics2D g2d) {
        GUIComponent selectedTile = menuCursor.getSelectedMenuTile();
        int boxWidth = descriptionRowsBackgroundCard.getWidth();
        int boxHeight = descriptionRowsBackgroundCard.getHeight();

        int horizontalPadding = 40;
        int verticalPadding = 40;
        int maxTextWidth = boxWidth - (horizontalPadding * 2);
        String textFont = DataClass.getInstance().getTextFont();
        if (selectedTile != null) {
            String text = null;
            String itemTitle = null;
            String itemCost = null;
            Color itemRarityColor = null;

            int descriptionX = itemDescription.getXCoordinate() + horizontalPadding;
            int descriptionY = itemDescription.getYCoordinate() + verticalPadding;

            if (selectedTile instanceof ShopItem shopItem) {
                itemTitle = shopItem.getShopItemInformation().getItem().getItemName();
                itemCost = "Costs: " + Math.round(shopItem.getShopItemInformation().getCost()) + " minerals. You have: " + Math.round(PlayerInventory.getInstance().getCashMoney());
                text = shopItem.getShopItemInformation().getItemDescription();
                itemRarityColor = shopItem.getShopItemInformation().getItemRarity().getColor();
            } else {
                text = selectedTile.getDescriptionOfComponent();
            }

            // Draw the item title with a larger font size
            if (itemTitle != null) {
                g2d.setFont(new Font(textFont, Font.BOLD, 24));  // Larger font for the title
                drawDescriptionText(g2d, itemTitle, descriptionX, descriptionY, maxTextWidth, itemRarityColor);
                FontMetrics titleMetrics = g2d.getFontMetrics();
                descriptionY += titleMetrics.getHeight() + 10;  // Spacing after the title
            }

            // Draw the description with the default font size
            if (text != null) {
                g2d.setFont(new Font(textFont, Font.PLAIN, 20));  // Default font for description
                drawDescriptionText(g2d, text, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
                FontMetrics descriptionMetrics = g2d.getFontMetrics();
                descriptionY += descriptionMetrics.getHeight() * ((text.length() / maxTextWidth) + 1); // Estimate line height
            }

            // Draw the cost at the bottom of the description box
            if (itemCost != null) {
                g2d.setFont(new Font(textFont, Font.ITALIC, 18));  // Slightly smaller italic font for the cost
                FontMetrics costMetrics = g2d.getFontMetrics();
                int costY = itemDescription.getYCoordinate() + boxHeight - verticalPadding - costMetrics.getHeight();
                drawDescriptionText(g2d, itemCost, descriptionX, costY, maxTextWidth, Color.WHITE);
            }
        }
    }

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



    public Timer getTimer () {
        return timer;
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        //The timer is responsible for redrawing and polling input from a controller
    }

    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile () {
        grid.get(selectedRow).get(selectedColumn).activateComponent();
        if (grid.get(selectedRow).get(selectedColumn).getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
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
    private void nextMenuTile () {
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
    private boolean isGridEmpty () {
        for (List<GUIComponent> row : grid) {
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
            selectedColumn = grid.get(selectedRow).size() - 1; // Wrap around to the rightmost column
        }
        updateCursor();
    }

    // Go one menu tile to the right
    private void nextMenuColumn () {
        selectedColumn++;
        if (selectedColumn >= grid.get(selectedRow).size()) {
            selectedColumn = 0; // Wrap around to the leftmost column
        }
        updateCursor();
    }

    // Update the cursor's position and selected menu tile
    private void updateCursor () {
        if (grid.get(selectedRow).isEmpty()) {
            menuCursor.setSelectedMenuTile(null);
        } else {
            GUIComponent selectedComponent = grid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedComponent);
            menuCursor.setYCoordinate(selectedComponent.getYCoordinate());
            menuCursor.setXCoordinate(selectedComponent.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
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
                    previousMenuColumn();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_D):
                    nextMenuColumn();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_W):
                    previousMenuTile();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_S):
                    nextMenuTile();
                    needsUpdate = true;
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
    private static final long MOVE_COOLDOWN = 350; // milliseconds

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
        updateDescriptionBox(g2d);

        for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
            drawAnimation(g2d, animation);
        }
        animationManager.updateGameTick();
        backgroundManager.updateGameTick();
        Toolkit.getDefaultToolkit().sync();

        // Reading controller input
        executeControllerInput();
    }


    private void drawObjects (Graphics2D g) {
        for (GUIComponent component : offTheGridObjects) {
            if (component != null) {
                drawGUIComponent(g, component);
            }
        }


        for (List<GUIComponent> list : grid) {
            for (GUIComponent component : list) {
                if (component instanceof ShopItem shopItem) {
                    drawItemsInShop(g, shopItem);
                }
                drawGUIComponent(g, component);
            }
        }

        if (showInventory) {
            for (GUIComponent component : playerInventoryMenuObjects) {
                if (component != null) {
                    drawGUIComponent(g, component);
                }
            }
        }

        drawGUIComponent(g, menuCursor);
    }


    private void drawGUIComponent (Graphics2D g, GUIComponent component) {
        g.drawImage(component.getImage(), component.getXCoordinate(), component.getYCoordinate(), this);
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

    }
}