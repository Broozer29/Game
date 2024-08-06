package guiboards.boards;

import javax.swing.JPanel;
import javax.swing.Timer;

import VisualAndAudioData.image.ImageEnums;
import controllerInput.ConnectedControllers;
import controllerInput.ControllerInputEnums;
import controllerInput.ControllerInputReader;
import game.gamestate.GameStateInfo;
import game.items.Item;
import game.items.PlayerInventory;
import game.items.enums.ItemEnums;
import game.managers.AnimationManager;
import game.managers.ShopManager;
import game.gameobjects.background.BackgroundManager;
import game.gameobjects.background.BackgroundObject;
import VisualAndAudioData.DataClass;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.boardEnums.MenuObjectEnums;
import guiboards.boardcreators.ShopBoardCreator;
import guiboards.guicomponents.*;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

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
    private ConnectedControllers controllers = ConnectedControllers.getInstance();
    private ShopManager shopManager = ShopManager.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();

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
        createWindow();

        if (controllers.getFirstController() != null) {
            controllerInputReader = controllers.getFirstController();
        }

        timer = new Timer(GameStateInfo.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));
        timer.start();
    }

    public void createWindow () {
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
        //

        shortSong = shopBoardCreator.createShortSongSelection(songLengthBackgroundCard);
        mediumSong = shopBoardCreator.createMediumSongSelection(songLengthBackgroundCard);
        longSong = shopBoardCreator.createLongSelection(songLengthBackgroundCard);
        lengthSelectionText = shopBoardCreator.createSongSelectionText(songLengthBackgroundCard, longSong);
        nextLevelDifficultyIcon = shopBoardCreator.createNextLevelDifficultyIcon(nextLevelDifficultyBackground);
        fourthRow.add(shortSong);
        fourthRow.add(mediumSong);
        fourthRow.add(longSong);
        offTheGridObjects.addAll(lengthSelectionText.getComponents());
        offTheGridObjects.add(rerollBackgroundCard);

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

        // Add all the created items to respective rows or lists

        // Rebuild the UI elements list
        recreateList();
    }

    private void addAllButFirstComponent(GUITextCollection textCollection){
        for(int i = 1; i < textCollection.getComponents().size(); i++){
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }


    private void recreateList () {
        grid.clear();
        offTheGridObjects.clear();
        playerInventoryMenuObjects.clear();


//        fifthRow.add(returnToMainMenu.getComponents().get(0));
        addAllButFirstComponent(returnToMainMenu);
//        fifthRow.add(nextLevelButton.getComponents().get(0));
        addAllButFirstComponent(nextLevelButton);
//        fifthRow.add(playerInventoryButton.getComponents().get(0));
        addAllButFirstComponent(playerInventoryButton);

        if(!thirdRow.contains(rerollButton)) {
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

    public void rerollShop(){
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

        int backgroundXPadding = 50;
        int backgroundYPadding = 30;

        int maxWidth = inventoryBackgroundCard.getWidth() - backgroundXPadding;
        int maxHeight = inventoryBackgroundCard.getHeight() - backgroundYPadding;
        int scale = 1;
        MenuObjectEnums objectEnum = MenuObjectEnums.ItemIcon;

        // Variables to control the layout within the inventory card
        int padding = 10; // Space between items
        int itemSize = 64; // Assuming each item icon is 64x64 pixels
        int itemsPerRow = maxWidth / (itemSize + padding);
        int xCoordinate = xCoordinateStart + padding + backgroundXPadding;
        int yCoordinate = yCoordinateStart + padding + backgroundYPadding;

        // Loop over each item in the inventory
        for (ItemEnums itemEnum : itemMap.keySet()) {
            // Create a menu object for the item
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinateStart);
            spriteConfiguration.setScale(scale);
            spriteConfiguration.setImageType(itemMap.get(itemEnum).getItemName().getItemIcon());

            GUIComponent itemComponent = new DisplayOnly(spriteConfiguration);
            playerInventoryMenuObjects.add(itemComponent);

            int quantity = itemMap.get(itemEnum).getQuantity();
            String quantityStr = String.valueOf(quantity);

            // Calculate the position for the quantity indicator
            int quantityX = xCoordinate + itemSize - 25; // Adjust for proper placement
            int quantityY = yCoordinate + itemSize - 25; // Adjust for proper placement

            // Create a MenuObjectPart for each digit in the quantity
            for (int i = 0; i < quantityStr.length(); i++) {
                char digit = quantityStr.charAt(i);
                ImageEnums digitImage = ImageEnums.getImageEnumForDigit(digit);
                SpriteConfiguration digitSpriteConfig = new SpriteConfiguration();
                digitSpriteConfig.setxCoordinate(quantityX + (i * 8)); // Adjust spacing between digits
                digitSpriteConfig.setyCoordinate(quantityY);
                digitSpriteConfig.setScale(scale);
                digitSpriteConfig.setImageType(digitImage);


                GUIComponent itemAmount = new DisplayOnly(digitSpriteConfig);
                playerInventoryMenuObjects.add(itemAmount);
            }

            // Update xCoordinate, wrapping and resetting yCoordinate as needed
            if ((xCoordinate - xCoordinateStart) / (itemSize + padding) >= itemsPerRow - 1) {
                // Move down to the next row and reset xCoordinate
                yCoordinate += itemSize + padding;
                xCoordinate = xCoordinateStart + padding;
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

    private void updateDescriptionBox (Graphics2D g2d) {
        GUIComponent selectedTile = menuCursor.getSelectedMenuTile();
        int boxWidth = descriptionRowsBackgroundCard.getWidth();
        g2d.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        g2d.setColor(Color.white);

        int horizontalPadding = 40;
        int verticalPadding = 40;
        int maxTextWidth = boxWidth - (horizontalPadding * 2);

        if (selectedTile != null) {
            String text = null;
            int descriptionX = itemDescription.getXCoordinate() + horizontalPadding;
            int descriptionY = itemDescription.getYCoordinate() + verticalPadding;

            if(selectedTile instanceof ShopItem){
                text = ((ShopItem) selectedTile).getMenuItemInformation().getItemDescription();
            } else {
                text = selectedTile.getDescriptionOfComponent();
            }

            if (text != null) {
                drawText(g2d, text, descriptionX, descriptionY, maxTextWidth);
            }
        }
    }

    private void drawText (Graphics2D g2d, String text, int x, int y, int maxWidth) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

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
        // TODO Auto-generated method stub

    }

    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile () {
//        if(selectedRow >= grid.size()){
//            selectedRow = grid.size() - 1;
//            return;
//        }
//        if(selectedColumn >= grid.get(selectedRow).size()){
//            selectedColumn = grid.get(selectedRow).size() - 1;
//            return;
//        }
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
            menuCursor.setY(selectedComponent.getYCoordinate());
            menuCursor.setX(selectedComponent.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
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

    public void executeControllerInput () {
        if (controllers.getFirstController() != null) {
            boolean needsUpdate = false;
            controllerInputReader.pollController();
            long currentTime = System.currentTimeMillis();

            // Left and right navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_QUICK)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_SLOW)) {
                    //Gaat naar boven
                    // Menu option to the left
                    previousMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_QUICK)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_SLOW)) {
                    //Gaat nar beneden
                    // Menu option to the right
                    nextMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            // Up and down navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_QUICK)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_SLOW)) {
                    //Gaat naar links
                    // Menu option upwards
                    previousMenuColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_QUICK)) {
                    // Menu option downwards
                    //Gaat naar rechts
                    nextMenuColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                // Fire action
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
                if (component instanceof ShopItem) {
                    drawItemsInShop(g, (ShopItem) component);
                }
                drawGUIComponent(g, component);
            }
        }

        if(showInventory){
            for(GUIComponent component : playerInventoryMenuObjects){
                if(component != null){
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

        g.drawImage(object.getImage(), xCoordinate, yCoordinate, this);

        switch (object.getMenuItemInformation().getItem().getItemRarity()) {
            case Common -> {
                g.setColor(Color.white);
            }
            case Rare -> {
                g.setColor(Color.green);
            }
            case Legendary -> {
                g.setColor(Color.red);
            }
            case Locked -> {
                g.setColor(Color.GRAY);
            }
        }

        if (object.getMenuItemInformation().isAvailable()) {
            g.setFont(new Font("Lucida Grande", Font.BOLD, 10));
            g.drawString(object.getMenuItemInformation().getItem().getItemName(),
                    xCoordinate,
                    yCoordinate + object.getHeight() + 10);

            g.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
            g.drawString(object.getMenuItemInformation().getItemRarity().toString()
                    , xCoordinate
                    , yCoordinate + object.getHeight() + 22);

            g.drawString("$" + object.getMenuItemInformation().getCost()
                    , xCoordinate
                    , yCoordinate + object.getHeight() + 34);
        } else {
            g.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
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