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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import controllerInput.ConnectedControllers;
import controllerInput.ControllerInputEnums;
import controllerInput.ControllerInputReader;
import game.managers.AnimationManager;
import game.gameobjects.background.BackgroundManager;
import game.gameobjects.background.BackgroundObject;
import VisualAndAudioData.DataClass;
import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.boardcreators.MenuBoardCreator;
import guiboards.guicomponents.GUIComponent;
import guiboards.guicomponents.GUITextCollection;
import guiboards.guicomponents.MenuCursor;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;

public class MenuBoard extends JPanel implements ActionListener {
    private DataClass data = DataClass.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllers controllers = ConnectedControllers.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();

    private List<GUIComponent> firstColumn = new ArrayList<>();
    private List<GUIComponent> secondColumn = new ArrayList<>();
    private List<GUIComponent> thirdColumn = new ArrayList<>();
    private List<List<GUIComponent>> grid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();
    private MenuCursor menuCursor;
    private GUITextCollection startGameButton;
    private List<GUITextCollection> controlExplanations;
    private GUIComponent startGameBackgroundCard;
    private GUITextCollection openShopButton;
    private GUIComponent titleImage;
    private GUITextCollection foundController;
    private Timer timer;
    private boolean foundControllerBool = false;
    private boolean initializedMenuObjects = false;
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
        timer = new Timer(16, e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));
        timer.start();
    }

    // Initialize all starter pointers
    private void initMenuTiles () {
        // With functionality:
        titleImage = MenuBoardCreator.createTitleImage();
        controlExplanations = MenuBoardCreator.createControlsExplanations();
        startGameBackgroundCard = MenuBoardCreator.startGameBackgroundCard();
        startGameButton = MenuBoardCreator.createStartGameButton(startGameBackgroundCard);
        menuCursor = MenuBoardCreator.createMenuCursor(startGameButton.getComponents().get(0));
        openShopButton = MenuBoardCreator.openShopButton(startGameButton);
        foundController = MenuBoardCreator.foundControllerText(foundControllerBool, titleImage);


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

            for (GUITextCollection explanation : controlExplanations) {
                for (GUIComponent component : explanation.getComponents()) {
                    offTheGridObjects.add(component);
                }
            }

            offTheGridObjects.add(startGameBackgroundCard);
            offTheGridObjects.add(titleImage);
            offTheGridObjects.add(menuCursor);


            offTheGridObjects.addAll(foundController.getComponents());
            this.menuCursor.setSelectedMenuTile(startGameButton.getComponents().get(0));

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

    private void addAllButFirstComponent (GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    private void addTilesToColumns () {
        //Move start game tile to selectLevelBoard
        firstColumn.add(startGameButton.getComponents().get(0));
        addAllButFirstComponent(startGameButton);
        firstColumn.add(openShopButton.getComponents().get(0));
        addAllButFirstComponent(openShopButton);

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
        if (grid.get(selectedRow).isEmpty()) { // Check if the selected row is empty
            menuCursor.setSelectedMenuTile(null); // You might need to decide how you want to handle this situation in
            // your application
        } else {
            GUIComponent selectedTile = grid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedTile);
            menuCursor.setYCoordinate(selectedTile.getYCoordinate() - menuCursor.getHeight() / 2);
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

    private int inputDelay;
    private static final long MOVE_COOLDOWN = 50; // 50 milliseconds

    public void executeControllerInput () {
        if (controllers.getFirstController() != null) {
            controllerInputReader.pollController();

            // Left and right navigation
            if (inputDelay >= MOVE_COOLDOWN) {
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_LEFT_QUICK)) {
                    // Menu option to the left
                    previousMenuTile();
                    inputDelay = 0;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_RIGHT_QUICK)) {
                    // Menu option to the right
                    nextMenuTile();
                    inputDelay = 0;
                }

                // Up and down navigation
                if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_UP_QUICK)) {
                    // Menu option upwards
                    previousMenuColumn();
                    inputDelay = 0;
                } else if (controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_SLOW)
                        || controllerInputReader.isInputActive(ControllerInputEnums.MOVE_DOWN_QUICK)) {
                    // Menu option downwards
                    nextMenuColumn();
                    inputDelay = 0;
                }

                if (controllerInputReader.isInputActive(ControllerInputEnums.FIRE)) {
                    // Select menu option
                    selectMenuTile();
                    inputDelay = 0;
                }
            }
        }
    }


    public void resetLastMoveTime () {
        inputDelay = 0;
    }

    /*-----------------------------End of navigation methods--------------------------*/

    /*---------------------------Drawing methods-------------------------------*/
    @Override
    public void paintComponent (Graphics g) {
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
        } finally {
            g2d.dispose(); // Ensure resources are released
        }

        animationManager.updateGameTick();
        backgroundManager.updateGameTick();
        Toolkit.getDefaultToolkit().sync();

        // readControllerState();
        executeControllerInput();
        inputDelay++;
    }

    private void drawObjects (Graphics2D g) {
        recreateList(); //Shouldn't be called every loop, is simply waste of resources but negligable for now
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

    private void drawGUIComponent (Graphics2D g, GUIComponent component) {
        g.drawImage(component.getImage(), component.getXCoordinate(), component.getYCoordinate(), this);
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