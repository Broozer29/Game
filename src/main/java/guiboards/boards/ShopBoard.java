package guiboards.boards;

import javax.sound.sampled.UnsupportedAudioFileException;
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
import game.objects.background.BackgroundManager;
import game.objects.background.BackgroundObject;
import VisualAndAudioData.DataClass;
import game.spawner.enums.LevelDifficulty;
import game.spawner.enums.LevelLength;
import guiboards.MenuCursor;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.MenuObjectCollection;
import guiboards.boardEnums.MenuObjectEnums;
import guiboards.MenuObjectPart;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopBoard extends JPanel implements ActionListener {

    private DataClass data = DataClass.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllers controllers = ConnectedControllers.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();
    private List<MenuObjectCollection> firstRow = new ArrayList<MenuObjectCollection>();
    private List<MenuObjectCollection> secondRow = new ArrayList<MenuObjectCollection>();
    private List<MenuObjectCollection> thirdRow = new ArrayList<MenuObjectCollection>();
    private List<MenuObjectCollection> fourthRow = new ArrayList<MenuObjectCollection>();
    private List<MenuObjectCollection> fifthRow = new ArrayList<MenuObjectCollection>();
    private List<List<MenuObjectCollection>> grid = new ArrayList<>();

    private List<MenuObjectCollection> offTheGridObjects = new ArrayList<MenuObjectCollection>();

    private List<MenuObjectCollection> playerInventoryMenuObjects = new ArrayList<>();

    private MenuObjectCollection itemRowsBackgroundCard;
    private MenuObjectCollection songSelectionBackgroundCard;
    private MenuObjectCollection relicBackgroundCard;
    private MenuObjectCollection descriptionRowsBackgroundCard;
    private MenuObjectCollection itemDescription;


    private Timer timer;
    private int selectedRow = 0;
    private int selectedColumn = 0;

    private MenuCursor menuCursor;
    private MenuObjectCollection returnToMainMenu;
    private MenuObjectCollection nextLevelButton;

    private MenuObjectCollection inventoryBackgroundCard;
    private MenuObjectCollection playerInventoryCollection;
    private MenuObjectCollection playerInventoryButton;

    private ControllerInputReader controllerInputReader;
    private boolean initializedMenuObjects = false;


    private float imageScale = 1;
    private float textScale = 1;
    private int itemWidth = 100; // assuming each MenuObject will be 100 pixels wide
    private int itemHeight = 100; // assuming each MenuObject will be 100 pixels tall
    private int horizontalSpacing = 35; // space between items horizontally
    private int verticalSpacing = 60; // space between items vertically
    private int horizontalScreenDistance = 75;
    private int verticalScreenDistance = 20;

    private boolean showInventory;

    private int fourthRowY = 3 * (itemHeight + verticalSpacing) + verticalSpacing;


    public ShopBoard () {
        addKeyListener(new TAdapter());
        setFocusable(true);
        showInventory = false;
        createWindow();

        if (controllers.getFirstController() != null) {
            controllerInputReader = controllers.getFirstController();
        }

        timer = new Timer(GameStateInfo.getInstance().getDELAY(), e -> repaint());
        timer.start();
    }

    public void createWindow () {
        animationManager.resetManager();
        firstRow.clear();
        secondRow.clear();
        thirdRow.clear();
        fourthRow.clear();
        fifthRow.clear();
        offTheGridObjects.clear();


        createFirstRowOfItems();
        createSecondRowOfItems();
        createThirdRowOfItems();
        createSongDifficultySection();
        createSongLengthSection();
        createDescriptionBox();
        initBackgroundCards();


        this.menuCursor.setSelectedMenuTile(returnToMainMenu);
        recreateList();
    }

    private void recreateList () {
        grid.clear();
        offTheGridObjects.clear();
        playerInventoryMenuObjects.clear();
        addTileToFifthRow(returnToMainMenu);
        addTileToFifthRow(nextLevelButton);
        addTileToFifthRow(playerInventoryButton);

        grid.add(firstRow);
        grid.add(secondRow);
        grid.add(thirdRow);
        grid.add(fourthRow);
        grid.add(fifthRow);

        offTheGridObjects.add(itemRowsBackgroundCard);
        offTheGridObjects.add(descriptionRowsBackgroundCard);
        offTheGridObjects.add(songSelectionBackgroundCard);
        offTheGridObjects.add(relicBackgroundCard);
        offTheGridObjects.add(itemDescription);

        if (this.showInventory) {
            playerInventoryMenuObjects.add(inventoryBackgroundCard);
            playerInventoryMenuObjects.add(playerInventoryCollection);
            createPlayerInventory();
        }

//        if (showInventory) {
//            offTheGridObjects.add(inventoryBackgroundCard);
//            offTheGridObjects.add(playerInventoryCollection);
//        }

        updateCursor();
    }

    private void initBackgroundCards () {
        float widthRatio = 1250 / 1440f;
        float heightRatio = 550f / 875f;

        int cardWidth = (int) (boardWidth * widthRatio);
        int cardHeight = (int) (boardHeight * heightRatio);

        itemRowsBackgroundCard = new MenuObjectCollection(-20, -20,
                1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);

        itemRowsBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);

        widthRatio = 450 / 1440f;
        heightRatio = 200f / 875f;
        cardWidth = (int) (boardWidth * widthRatio);
        cardHeight = (int) (boardHeight * heightRatio);

        relicBackgroundCard = new MenuObjectCollection(30, fourthRowY - 20,
                1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);

        relicBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);

        float xCoordPaddingRatio = 50 / 1440f;
        cardWidth = (int) (boardWidth * widthRatio);
        cardHeight = (int) (boardHeight * heightRatio);
        int xCoordPadding = (int) (boardWidth * xCoordPaddingRatio);
        int xCoord = (3 * (itemWidth + horizontalSpacing)) + (horizontalScreenDistance * 2);
        songSelectionBackgroundCard = new MenuObjectCollection(xCoord - xCoordPadding, fourthRowY - 20,
                1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);

        songSelectionBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);


        widthRatio = 400 / 1440f;
        heightRatio = 300 / 875f;

        cardWidth = (int) (boardWidth * widthRatio);
        cardHeight = (int) (boardHeight * heightRatio);
        xCoord = boardWidth / 2 + (boardWidth / 5);
        descriptionRowsBackgroundCard = new MenuObjectCollection(xCoord, fourthRowY - 20,
                1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);

        descriptionRowsBackgroundCard.getMenuImages().get(0).setImageDimensions(cardWidth, cardHeight);

        this.returnToMainMenu = new MenuObjectCollection(100, boardHeight - 80, textScale, "RETURN TO MAIN MENU", MenuObjectEnums.Text,
                MenuFunctionEnums.Return_To_Main_Menu);

        this.nextLevelButton = new MenuObjectCollection(500, boardHeight - 80, textScale, "START NEXT LEVEL", MenuObjectEnums.Text,
                MenuFunctionEnums.Start_Game);

        this.playerInventoryButton = new MenuObjectCollection(800, boardHeight - 80, textScale
                , "OPEN INVENTORY", MenuObjectEnums.Text, MenuFunctionEnums.Open_Inventory);

        widthRatio = 800 / 1440f;
        heightRatio = 500 / 875f;

        inventoryBackgroundCard = new MenuObjectCollection(boardWidth / 6, boardHeight / 2 - (boardHeight / 4),
                1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        inventoryBackgroundCard.getMenuImages().get(0).setImageDimensions((int) (boardWidth * widthRatio), (int) (boardHeight * heightRatio));

        int initCursorX = returnToMainMenu.getXCoordinate();
        int initCursorY = returnToMainMenu.getYCoordinate();

        this.menuCursor = new MenuCursor(initCursorX, initCursorY, imageScale);
        menuCursor.setXCoordinate(returnToMainMenu.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
    }

    private void createPlayerInventory () {
        Map<ItemEnums, Item> itemMap = PlayerInventory.getInstance().getItems();
        int xCoordinateStart = inventoryBackgroundCard.getXCoordinate();
        int yCoordinateStart = inventoryBackgroundCard.getYCoordinate();

        int backgroundXPadding = 50;
        int backgroundYPadding = 30;

        int maxWidth = inventoryBackgroundCard.getMenuImages().get(0).getWidth() - backgroundXPadding;
        int maxHeight = inventoryBackgroundCard.getMenuImages().get(0).getHeight() - backgroundYPadding;
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
            MenuObjectCollection menuObjectCollection = new MenuObjectCollection(
                    xCoordinate, yCoordinate, scale, "Dummy text", objectEnum, MenuFunctionEnums.NONE);
            menuObjectCollection.changeImage(itemMap.get(itemEnum).getItemName().getItemIcon());
            menuObjectCollection.getMenuImages().get(0).setImageDimensions(50, 50);
            // Add it to the list
            playerInventoryMenuObjects.add(menuObjectCollection);


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

                MenuObjectPart digitPart = new MenuObjectPart(digitSpriteConfig);
                menuObjectCollection.addMenuPart(digitPart);
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

    private void createFirstRowOfItems () {
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = verticalScreenDistance;
            MenuObjectEnums type = (i == 6 || i == 7) ? MenuObjectEnums.RareItem : MenuObjectEnums.CommonItem; // If index is 6 or 7, it's a rare item
            MenuObjectCollection item = new MenuObjectCollection(x, y, imageScale, "Item " + (i + 1), type, MenuFunctionEnums.PurchaseItem);
            firstRow.add(item);
        }
    }

    private void createSecondRowOfItems () {
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = (1 * (itemHeight + verticalSpacing)) + verticalScreenDistance;
            MenuObjectEnums type = (i == 6 || i == 7) ? MenuObjectEnums.RareItem : MenuObjectEnums.CommonItem; // If index is 6 or 7, it's a rare item
            MenuObjectCollection item = new MenuObjectCollection(x, y, imageScale, "Item " + (i + 11), type, MenuFunctionEnums.PurchaseItem);
            secondRow.add(item);
        }
    }

    private void createThirdRowOfItems () {
        for (int i = 0; i < 8; i++) {
            int x = (i * (itemWidth + horizontalSpacing)) + horizontalScreenDistance;
            int y = (2 * (itemHeight + verticalSpacing)) + verticalScreenDistance;
            MenuObjectEnums type = (i == 6 || i == 7) ? MenuObjectEnums.RareItem : MenuObjectEnums.CommonItem; // If index is 6 or 7, it's a rare item
            MenuObjectCollection item = new MenuObjectCollection(x, y, imageScale, "Item " + (i + 21), type, MenuFunctionEnums.PurchaseItem);
            thirdRow.add(item);
        }
    }


    LevelDifficulty[] difficulties = {LevelDifficulty.Easy, LevelDifficulty.Medium, LevelDifficulty.Hard};

    private void createSongDifficultySection () {
        //Below is the old way of doing it
        for (int i = 0; i < 3; i++) {
            int x = i * (itemWidth + horizontalSpacing) + horizontalScreenDistance;
            MenuObjectCollection difficulty = new MenuObjectCollection(x, fourthRowY, imageScale, difficulties[i].toString().toUpperCase(), MenuObjectEnums.Text, MenuFunctionEnums.SelectSongDifficulty);
            difficulty.setLevelDifficulty(difficulties[i]);
            fourthRow.add(difficulty);
        }
    }

    LevelLength[] levelLengths = {LevelLength.Short, LevelLength.Medium, LevelLength.Long};

    private void createSongLengthSection () {
        int xCoord = (3 * (itemWidth + horizontalSpacing)) + (horizontalScreenDistance * 2);
        // Assuming there are 3 songs
        for (int i = 0; i < 3; i++) {
            // Calculate x to be positioned after the last special item
            int x = xCoord + (i * (itemWidth + horizontalSpacing));
            MenuObjectCollection songLength = new MenuObjectCollection(x, fourthRowY, imageScale, levelLengths[i].toString().toUpperCase(), MenuObjectEnums.Text, MenuFunctionEnums.SelectSongLength);
            songLength.setLevelLength(levelLengths[i]);
            fourthRow.add(songLength);
        }


    }

    private void createDescriptionBox () {
        int y = fourthRowY;
        int x = boardWidth / 2 + (boardWidth / 5);
        itemDescription = new MenuObjectCollection(x, y, imageScale, "", MenuObjectEnums.Text, MenuFunctionEnums.NONE);
    }


    private void updateDescriptionBox (Graphics2D g2d) {
        MenuObjectCollection selectedTile = menuCursor.getSelectedMenuTile();
        int boxWidth = descriptionRowsBackgroundCard.getMenuImages().get(0).getWidth();
        g2d.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        g2d.setColor(Color.white);
        // Set padding inside the description box
        int horizontalPadding = 40;
        int verticalPadding = 40;

        int maxTextWidth = boxWidth - (horizontalPadding * 2);

        if (selectedTile != null && selectedTile.getMenuItemInformation() != null) {
            String text = selectedTile.getMenuItemInformation().getItemDescription();
            int descriptionX = itemDescription.getXCoordinate() + horizontalPadding;
            int descriptionY = itemDescription.getYCoordinate() + verticalPadding;

            FontMetrics metrics = g2d.getFontMetrics();
            int lineHeight = metrics.getHeight();

            // Split the text into words
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                // If adding the new word exceeds the maximum line width, draw the line and start a new one
                if (metrics.stringWidth(line.toString() + word) > maxTextWidth) {
                    g2d.drawString(line.toString(), descriptionX, descriptionY);
                    line = new StringBuilder(word).append(" ");
                    descriptionY += lineHeight;
                } else {
                    // Append the word to the current line
                    line.append(word).append(" ");
                }
            }

            // Draw the remaining text
            if (line.length() > 0) {
                g2d.drawString(line.toString(), descriptionX, descriptionY);
            }
        }
    }


    private void addTileToFirstRow (MenuObjectCollection menuTile) {
        firstRow.add(menuTile);
    }

    private void addTileToSecondRow (MenuObjectCollection menuTile) {
        secondRow.add(menuTile);
    }

    private void addTileToThirdRow (MenuObjectCollection menuTile) {
        thirdRow.add(menuTile);
    }

    private void addTileToFourthRow (MenuObjectCollection menuObject) {
        fourthRow.add(menuObject);
    }

    private void addTileToFifthRow (MenuObjectCollection menuTile) {
        if (!fifthRow.contains(menuTile)) {
            fifthRow.add(menuTile);
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
        try {
            grid.get(selectedRow).get(selectedColumn).menuTileAction();
            if (grid.get(selectedRow).get(selectedColumn).getMenuFunction() == MenuFunctionEnums.Start_Game) {
                timer.stop();
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
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
        for (List<MenuObjectCollection> row : grid) {
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
            MenuObjectCollection selectedTile = grid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedTile);
            menuCursor.setYCoordinate(selectedTile.getYCoordinate());
            menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
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
    private static final long MOVE_COOLDOWN = 200; // 500 milliseconds

    public void executeControllerInput () {
        if (controllers.getFirstController() != null) {
            boolean needsUpdate = false;
            controllerInputReader.pollController();
            long currentTime = System.currentTimeMillis();
            System.out.println("THIS IS NOT YET REVERSED FOR CONTROLLER, GONNA HAVE TO FIX IT");

            // Left and right navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_QUICK)) {
                    // Menu option to the left
                    previousMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_QUICK)) {
                    // Menu option to the right
                    nextMenuTile();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            // Up and down navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_QUICK)) {
                    // Menu option upwards
                    previousMenuColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_QUICK)) {
                    // Menu option downwards
                    nextMenuColumn();
                    needsUpdate = true;
                    lastMoveTime = currentTime;
                }
            }

            // Fire action
            if (controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
                // Select menu option
                selectMenuTile();
                needsUpdate = true;
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
        for (MenuObjectCollection object : offTheGridObjects) {
            if (object != null) {
                drawMenuObject(g, object);
            }
        }

        for (List<MenuObjectCollection> list : grid) {
            for (MenuObjectCollection object : list) {
                if (object != null) {
                    if (object.getMenuObjectType().equals(MenuObjectEnums.CommonItem)
                            || object.getMenuObjectType().equals(MenuObjectEnums.RareItem)
                            || object.getMenuObjectType().equals(MenuObjectEnums.LegendaryItem)) {
                        drawItemsInShop(g, object);
                    }
                    drawMenuObject(g, object);
                }
            }
        }

        if (showInventory) {
            for (MenuObjectCollection object : playerInventoryMenuObjects) {
                if (object != null) {
                    drawMenuObject(g, object);
                }
            }

        }

        g.drawString("Money available: " + PlayerInventory.getInstance().getCashMoney(),
                itemRowsBackgroundCard.getXCoordinate() + itemRowsBackgroundCard.getMenuImages().get(0).getWidth() + 10,
                itemRowsBackgroundCard.getYCoordinate() + 30);
        g.drawImage(menuCursor.getMenuImages().get(0).getImage(), menuCursor.getXCoordinate(), menuCursor.getYCoordinate(), this);

    }

    private void drawMenuObject (Graphics2D g, MenuObjectCollection object) {
        if (object.getMenuObjectType() == MenuObjectEnums.Text) {
            for (MenuObjectPart letter : object.getMenuTextImages()) {
                g.drawImage(letter.getImage(), letter.getXCoordinate(), letter.getYCoordinate(), this);
            }
        } else {
            for (MenuObjectPart image : object.getMenuImages()) {
                g.drawImage(image.getImage(), object.getXCoordinate(), object.getYCoordinate(),
                        this);
            }
        }
    }

    private void drawItemsInShop (Graphics g, MenuObjectCollection object) {

        int xCoordinate = object.getMenuImages().get(0).getXCoordinate();
        int yCoordinate = object.getMenuImages().get(0).getYCoordinate();

        g.drawImage(object.getMenuImages().get(0).getImage(), xCoordinate, yCoordinate, this);

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
        }

        if (object.getMenuItemInformation().isAvailable()) {
            g.setFont(new Font("Lucida Grande", Font.BOLD, 10));
            g.drawString(object.getMenuItemInformation().getItem().getItemName(),
                    xCoordinate,
                    yCoordinate + object.getMenuImages().get(0).getHeight() + 10);

            g.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
            g.drawString(object.getMenuItemInformation().getItemRarity().toString()
                    , xCoordinate
                    , yCoordinate + object.getMenuImages().get(0).getHeight() + 22);

            g.drawString("$" + object.getMenuItemInformation().getCost()
                    , xCoordinate
                    , yCoordinate + object.getMenuImages().get(0).getHeight() + 34);
        } else {
            g.drawString("Sold!",
                    xCoordinate,
                    yCoordinate + object.getMenuImages().get(0).getHeight() + 10);
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