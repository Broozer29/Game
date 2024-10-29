package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ConnectedControllers;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.game.gameobjects.background.BackgroundManager;
import net.riezebos.bruus.tbd.game.gameobjects.background.BackgroundObject;
import net.riezebos.bruus.tbd.visuals.objects.AnimationManager;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.util.OnScreenText;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.MenuBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.visuals.data.DataClass;
import net.riezebos.bruus.tbd.visuals.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visuals.data.audio.CustomAudioClip;
import net.riezebos.bruus.tbd.visuals.objects.Sprite;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuBoard extends JPanel implements ActionListener {
    private DataClass data = DataClass.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllers controllers = ConnectedControllers.getInstance();
    private final int boardWidth = data.getWindowWidth();
    private final int boardHeight = data.getWindowHeight();
    private AudioManager audioManager = AudioManager.getInstance();

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
    private GUITextCollection selectMacOSMediaPlayerButton;
    private GUITextCollection selectDefaultMusicButton;
    private GUIComponent titleImage;
    private GUITextCollection foundController;
    private Timer timer;
    private boolean foundControllerBool = false;
    private boolean initializedMenuObjects = false;
    private ControllerInputReader controllerInputReader;

    private int selectedRow = 0;
    private int selectedColumn = 0;
    private boolean recreateList = false;

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
        selectMacOSMediaPlayerButton = MenuBoardCreator.selectMacOSMediaPlayer(openShopButton);
        selectDefaultMusicButton = MenuBoardCreator.selectDefaultPlayer(selectMacOSMediaPlayerButton);

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
            recreateList();
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

        firstColumn.add(selectMacOSMediaPlayerButton.getComponents().get(0));
        addAllButFirstComponent(selectMacOSMediaPlayerButton);

        firstColumn.add(selectDefaultMusicButton.getComponents().get(0));
        addAllButFirstComponent(selectDefaultMusicButton);

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
            boolean needsUpdate = false;
            switch (key) {
                case (KeyEvent.VK_ENTER):
                    selectMenuTile();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_A):
                    previousMenuTile();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_D):
                    nextMenuTile();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_W):
                    previousMenuColumn();
                    needsUpdate = true;
                    break;
                case (KeyEvent.VK_S):
                    nextMenuColumn();
                    needsUpdate = true;
                    break;
            }

            if(needsUpdate){
                recreateList();
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

                // Up and down navigation
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
        } finally {
            g2d.dispose(); // Ensure resources are released
        }

        animationManager.updateGameTick();
        backgroundManager.updateGameTick();
        Toolkit.getDefaultToolkit().sync();

        // readControllerState();
        executeControllerInput();

        try {
            restreamLoopingMusicIfFinished();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void restreamLoopingMusicIfFinished() throws UnsupportedAudioFileException, IOException {
        if(audioManager == null){
            audioManager = AudioManager.getInstance();
        }
        CustomAudioClip backGroundMusicCustomAudioclip = audioManager.getBackGroundMusicCustomAudioclip();
        if(backGroundMusicCustomAudioclip == null){
            return;
        }
        if (backGroundMusicCustomAudioclip.getCurrentSecondsInPlayback() >= backGroundMusicCustomAudioclip.getTotalSecondsInPlayback() &&
                audioManager.getCurrentSong().shouldBeStreamed() &&
                backGroundMusicCustomAudioclip.isLoop()) {
            audioManager.playDefaultBackgroundMusic(audioManager.getCurrentSong(), true);
        }
    }

    private void drawObjects (Graphics2D g) {
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

    private void drawText (Graphics2D g, OnScreenText text) {
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

    @Override
    public void actionPerformed (ActionEvent e) {
        // TODO Auto-generated method stub

    }

    public Timer getTimer () {
        return this.timer;
    }

}