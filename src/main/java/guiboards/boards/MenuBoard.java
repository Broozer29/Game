package guiboards.boards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import controllerInput.ConnectedControllers;
import controllerInput.ControllerInputEnums;
import controllerInput.ControllerInputReader;
import game.managers.AnimationManager;
import game.objects.background.BackgroundManager;
import game.objects.background.BackgroundObject;
import VisualAndAudioData.DataClass;
import guiboards.MenuCursor;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.MenuObjectCollection;
import guiboards.boardEnums.MenuObjectEnums;
import guiboards.MenuObjectPart;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;

public class MenuBoard extends JPanel implements ActionListener {
    private DataClass data = DataClass.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllers controllers = ConnectedControllers.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();
    private List<MenuObjectCollection> firstColumn = new ArrayList<MenuObjectCollection>();
    private List<MenuObjectCollection> secondColumn = new ArrayList<MenuObjectCollection>();
    private List<MenuObjectCollection> thirdColumn = new ArrayList<MenuObjectCollection>();
    private List<List<MenuObjectCollection>> grid = new ArrayList<>();

    private List<MenuObjectCollection> offTheGridObjects = new ArrayList<MenuObjectCollection>();

    private MenuCursor menuCursor;
    private MenuObjectCollection startGameTile;

    private Timer timer;
    private MenuObjectCollection titleImage;

    private MenuObjectCollection wasdExplanation;
    private MenuObjectCollection shiftExplanation;
    private MenuObjectCollection attackExplanation;
    private MenuObjectCollection specialAttackExplanation;

    private MenuObjectCollection startGameCard;
    private MenuObjectCollection selectTalentSelectionBoard;
    private MenuObjectCollection openShopBoard;

    private boolean foundControllerBool = false;
    private boolean initializedMenuObjects = false;
    private MenuObjectCollection foundController;
    private ControllerInputReader controllerInputReader;

    private int selectedRow = 0;
    private int selectedColumn = 0;

    public MenuBoard () {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        controllers.initController();
        if (controllers.getFirstController() != null) {
            foundControllerBool = true;
            controllerInputReader = controllers.getFirstController();
        }

        initMenuTiles();
        timer = new Timer(16, e -> repaint());
        timer.start();
    }

    // Initialize all starter pointers
    private void initMenuTiles () {

        // With functionality:
        float imageScale = 1;
        float textScale = 1;
        this.startGameTile = new MenuObjectCollection(150, (boardHeight / 2), textScale, "START GAME", MenuObjectEnums.Text,
                MenuFunctionEnums.Start_Game);

        int initCursorX = startGameTile.getXCoordinate();
        int initCursorY = startGameTile.getYCoordinate();
        this.menuCursor = new MenuCursor(initCursorX, initCursorY, imageScale);
        menuCursor.setXCoordinate(startGameTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));

        this.titleImage = new MenuObjectCollection((boardWidth / 2) - 357, (boardHeight / 2) - 300, imageScale, null,
                MenuObjectEnums.Title_Image, MenuFunctionEnums.NONE);

        // Without functionality:

        int xCoordinate = boardWidth / 100;
        int yCoordinate = boardHeight / 2 + 300;
        this.wasdExplanation = new MenuObjectCollection(xCoordinate, yCoordinate + 0, textScale,
                "MOVEMENT = WASD OR ARROWS OR JOYSTICK", MenuObjectEnums.Text, MenuFunctionEnums.NONE);
        this.shiftExplanation = new MenuObjectCollection(xCoordinate, yCoordinate + 20, textScale, "SHIFT = SPEED BOOST",
                MenuObjectEnums.Text, MenuFunctionEnums.NONE);

        this.attackExplanation = new MenuObjectCollection(xCoordinate, yCoordinate + 40, textScale, "SPACEBAR = NORMAL ATTACK",
                MenuObjectEnums.Text, MenuFunctionEnums.NONE);

        this.specialAttackExplanation = new MenuObjectCollection(xCoordinate, yCoordinate + 60, textScale,
                "Q AND ENTER = SPECIAL ATTACK", MenuObjectEnums.Text, MenuFunctionEnums.NONE);

        this.startGameCard = new MenuObjectCollection(startGameTile.getXCoordinate() - 120, startGameTile.getYCoordinate() - 50,
                1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
        this.startGameCard.getMenuImages().get(0).setImageDimensions(400, 250);


        this.selectTalentSelectionBoard = new MenuObjectCollection(startGameTile.getXCoordinate(),
                startGameTile.getYCoordinate() + 50, 1, "SELECT TALENTS TEST BOARD", MenuObjectEnums.Text,
                MenuFunctionEnums.Select_Talent_Selection_Board);

        this.openShopBoard = new MenuObjectCollection(selectTalentSelectionBoard.getXCoordinate(),
                selectTalentSelectionBoard.getYCoordinate() + 50, 1, "OPEN SHOP",
                MenuObjectEnums.Text, MenuFunctionEnums.Open_Shop_Window);

        if (!foundControllerBool) {
            this.foundController = new MenuObjectCollection(100, 100, (float) 1.5,
                    "NO CONTROLLER COULD BE FOUND. MAKE SURE THE CONTROLLER IS CONNECTED AND ON THEN RESTART",
                    MenuObjectEnums.Text, MenuFunctionEnums.NONE);
        } else {
            this.foundController = new MenuObjectCollection(boardWidth / 2 - 100, 100, (float) 1.5, "FOUND A CONTROLLER",
                    MenuObjectEnums.Text, MenuFunctionEnums.NONE);
        }

        initializedMenuObjects = true;
    }

    public void recreateWindow () {
        if (initializedMenuObjects) {
            animationManager.resetManager();
            firstColumn.clear();
            secondColumn.clear();
            thirdColumn.clear();
            offTheGridObjects.clear();


            addTilesToColumns();

            offTheGridObjects.add(wasdExplanation);
            offTheGridObjects.add(shiftExplanation);
            offTheGridObjects.add(attackExplanation);
            offTheGridObjects.add(specialAttackExplanation);
            offTheGridObjects.add(startGameCard);

            offTheGridObjects.add(titleImage);
//            offTheGridObjects.add(selectNormalAttackCard);
//            offTheGridObjects.add(selectSpecialAttackCard);
//            offTheGridObjects.add(selectNormalAttackTitle);
//            offTheGridObjects.add(selectSpecialAttackTitle);
//
//            offTheGridObjects.add(selectNormalAttackIcon);
//            offTheGridObjects.add(selectSpecialAttackIcon);

            offTheGridObjects.add(menuCursor);

//            AnimationManager.getInstance()
//                    .addUpperAnimation(selectNormalAttackIconHighlight.getMenuImages().get(0).getAnimation());
//            AnimationManager.getInstance()
//                    .addUpperAnimation(selectSpecialAttackIconHighlight.getMenuImages().get(0).getAnimation());

            offTheGridObjects.add(foundController);
            this.menuCursor.setSelectedMenuTile(startGameTile);

            grid.add(firstColumn);
            grid.add(secondColumn);
            grid.add(thirdColumn);
        }
    }

    // Recreate the tilesList that gets drawn by drawComponents
    private void recreateList () {
        firstColumn.clear();
        secondColumn.clear();
        thirdColumn.clear();
        addTilesToColumns();

    }

    private void addTilesToColumns () {
        //Move start game tile to selectLevelBoard
        addTileToFirstColumn(startGameTile);
        addTileToFirstColumn(selectTalentSelectionBoard);
        addTileToFirstColumn(openShopBoard);
//        addTileToSecondColumn(selectLaserbeam);
//        addTileToSecondColumn(selectRocketLauncher);
//        addTileToThirdColumn(selectEMP);
    }

    private void addTileToFirstColumn (MenuObjectCollection menuTile) {
        firstColumn.add(menuTile);
    }

    private void addTileToSecondColumn (MenuObjectCollection menuTile) {
        secondColumn.add(menuTile);
    }

    private void addTileToThirdColumn (MenuObjectCollection menuTile) {
        thirdColumn.add(menuTile);
    }

//    private void updateActiveAttackIcons () {
//        switch (PlayerStats.getInstance().getAttackType()) {
//            case DefaultPlayerLaserbeam:
//            default:
//                selectNormalAttackIcon.changeImage(ImageEnums.Starcraft2_Pulse_Laser);
//                break;
//            case Rocket1:
//                selectNormalAttackIcon.changeImage(ImageEnums.Starcraft2_Dual_Rockets);
//                break;
//        }
//
//        switch (PlayerStats.getInstance().getPlayerSpecialAttackType()) {
//            case EMP:
//            default:
//                selectSpecialAttackIcon.changeImage(ImageEnums.Starcraft2_Electric_Field);
//                break;
//            case Rocket_Cluster:
//                break;
//
//        }
//    }

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
        if (grid.get(selectedRow).isEmpty()) { // Check if the selected row is empty
            menuCursor.setSelectedMenuTile(null); // You might need to decide how you want to handle this situation in
            // your application
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
            int key = e.getKeyCode();
            switch (key) {
                case (KeyEvent.VK_ENTER):
                    selectMenuTile();
                    break;
                case (KeyEvent.VK_A):
                    previousMenuTile();
                    break;
                case (KeyEvent.VK_D):
                    nextMenuTile();
                    break;
                case (KeyEvent.VK_W):
                    previousMenuColumn();
                    break;
                case (KeyEvent.VK_S):
                    nextMenuColumn();
                    break;
            }
            // Shoddy fix, maybe an updateGameTick? Atleast this way it doesnt constantly check for
            // new icons
//            updateActiveAttackIcons();
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

    // Component 4 = Y
    // Component 1 = B
    // Component 3 = X
    // Component 0 = A

    // Component 10 & 11 zijn de middelste 2 knoppies, die voor pauzeren

    // Component 6 = linker schouder
    // Component 7 = rechter schouder

    // Remaining components...

    private long lastMoveTime = 0;
    private static final long MOVE_COOLDOWN = 200; // 500 milliseconds

    public void executeControllerInput () {
        if (controllers.getFirstController() != null) {
            controllerInputReader.pollController();
            long currentTime = System.currentTimeMillis();

            // Left and right navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_QUICK)) {
                    // Menu option to the left
                    previousMenuTile();
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_QUICK)) {
                    // Menu option to the right
                    nextMenuTile();
                    lastMoveTime = currentTime;
                }
            }

            // Up and down navigation
            if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_QUICK)) {
                    // Menu option upwards
                    previousMenuColumn();
                    lastMoveTime = currentTime;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_QUICK)) {
                    // Menu option downwards
                    nextMenuColumn();
                    lastMoveTime = currentTime;
                }
            }

            // Fire action
            if (controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
                // Select menu option
                selectMenuTile();
//                updateActiveAttackIcons();
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

        for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
            drawAnimation(g2d, animation);
        }
        animationManager.updateGameTick();
        backgroundManager.updateGameTick();
        Toolkit.getDefaultToolkit().sync();

//		readControllerState();
        executeControllerInput();

    }

    private void drawObjects (Graphics2D g) {
        recreateList();
        for (MenuObjectCollection object : offTheGridObjects) {
            if (object != null) {
                drawMenuObject(g, object);
            }
        }

        for (List<MenuObjectCollection> list : grid) {
            for (MenuObjectCollection object : list) {
                if (object != null) {
                    drawMenuObject(g, object);
                }
            }
        }

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

    @Override
    public void actionPerformed (ActionEvent e) {
        // TODO Auto-generated method stub

    }

    public Timer getTimer () {
        return this.timer;
    }

}