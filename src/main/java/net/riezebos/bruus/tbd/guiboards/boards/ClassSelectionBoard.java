package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ConnectedControllersManager;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputEnums;
import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.util.OnScreenText;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.TimerHolder;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundManager;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ClassSelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.guiboards.util.ClassDescription;
import net.riezebos.bruus.tbd.guiboards.util.WeaponDescription;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClassSelectionBoard extends JPanel implements TimerHolder {

    private AudioManager audioManager = AudioManager.getInstance();
    private BackgroundManager backgroundManager = BackgroundManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private ConnectedControllersManager controllers = ConnectedControllersManager.getInstance();

    /*Board Components*/
    private GUIComponent classSelectionBackgroundCard;
    private GUIComponent classDescriptionBackgroundCard;
    private GUIComponent primaryWeaponExplanationBackgroundCard;
    private GUIComponent primaryWeaponIcon;
    private GUIComponent secondarySkillWeaponExplanationBackgroundCard;
    private GUIComponent secondaryWeaponIcon;
    private GUIComponent classSelectionTitleImage;
    private WeaponDescription primaryWeaponDescription;
    private WeaponDescription secondarySkillDescription;
    private GUITextCollection classSelectionSelectText;
    private GUITextCollection selectCaptainTextButton;
    private GUITextCollection selectFireFighterTextButton;

    private GUIComponent startGameButtonBackgroundCard;
    private GUITextCollection startGameButton;
    private GUIComponent returnToMenuBackgroundCard;
    private GUITextCollection returnToMenuButton;
    /*Board Components*/

    private List<GUIComponent> firstColumn = new ArrayList<>();
    private List<GUIComponent> secondColumn = new ArrayList<>();

    private List<List<GUIComponent>> grid = new ArrayList<>();
    private List<GUIComponent> offTheGridObjects = new ArrayList<>();
    private MenuCursor menuCursor;
    private Timer timer;
    private ControllerInputReader controllerInputReader;
    private int selectedRow = 0;
    private int selectedColumn = 0;
    private boolean initializedMenuObjects = false;


    public ClassSelectionBoard () {
        addKeyListener(new KeyInputReader());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed (MouseEvent e) {
                System.out.println("Mouse clicked at: X=" + e.getX() + ", Y=" + e.getY());
            }
        });

        if (controllers.getFirstController() != null) {
            controllerInputReader = controllers.getFirstController();
        }

        initMenuTiles();
        timer = new Timer(GameStateInfo.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));
        timer.start();
    }

    private void initMenuTiles () {
        classDescriptionBackgroundCard = ClassSelectionBoardCreator.createClassDescriptionBackgroundCard();

        primaryWeaponExplanationBackgroundCard = ClassSelectionBoardCreator.createPrimaryWeaponDescriptionBackgroundCard();
        primaryWeaponIcon = ClassSelectionBoardCreator.createPrimaryWeaponDescriptionIcon(primaryWeaponExplanationBackgroundCard, lastHoveredOtion);

        secondarySkillWeaponExplanationBackgroundCard = ClassSelectionBoardCreator.createSecondarySkillDescriptionBackgroundCard();
        secondaryWeaponIcon = ClassSelectionBoardCreator.createSecondaryWeaponDescriptionIcon(secondarySkillWeaponExplanationBackgroundCard, lastHoveredOtion);

        classSelectionTitleImage = ClassSelectionBoardCreator.createSelectClassImage();

        returnToMenuBackgroundCard = ClassSelectionBoardCreator.createReturnToMainMenuBackgroundCard();
        returnToMenuButton = ClassSelectionBoardCreator.createReturnToMainMenu(returnToMenuBackgroundCard);


        classSelectionBackgroundCard = ClassSelectionBoardCreator.createSelectShipBackgroundCard();
        classSelectionSelectText = ClassSelectionBoardCreator.createSelectClassText(classSelectionBackgroundCard);
        selectCaptainTextButton = ClassSelectionBoardCreator.createSelectCaptain(classSelectionBackgroundCard);
        selectFireFighterTextButton = ClassSelectionBoardCreator.createSelectFireFighter(classSelectionBackgroundCard);

        startGameButtonBackgroundCard = ClassSelectionBoardCreator.createStartGameButtonBackgroundCard();
        startGameButton = ClassSelectionBoardCreator.createStartGameButton(startGameButtonBackgroundCard);
        menuCursor = ClassSelectionBoardCreator.createCursor(returnToMenuButton);
        initializedMenuObjects = true;
    }

    public void recreateWindow () {
        if (initializedMenuObjects) {
            lastMoveTime = System.currentTimeMillis();
            //Clear all existing columns/rows/grid then re-add them
            recreateList();
            selectedColumn = 0;
            selectedRow = 0;
            updateCursor();
        }
    }

    private void recreateList () {
        firstColumn.clear();
        secondColumn.clear();
        grid.clear();
        offTheGridObjects.clear();

        offTheGridObjects.add(startGameButtonBackgroundCard);
        offTheGridObjects.add(returnToMenuBackgroundCard);
        offTheGridObjects.add(classSelectionTitleImage);
        offTheGridObjects.add(classSelectionBackgroundCard);
        offTheGridObjects.add(classDescriptionBackgroundCard);
        offTheGridObjects.add(primaryWeaponExplanationBackgroundCard);
        offTheGridObjects.add(primaryWeaponIcon);
        offTheGridObjects.add(secondarySkillWeaponExplanationBackgroundCard);
        offTheGridObjects.add(secondaryWeaponIcon);
        offTheGridObjects.addAll(classSelectionSelectText.getComponents());


        addToGrid(firstColumn, selectCaptainTextButton.getComponents().get(0), 0,0);
        addAllButFirstComponent(selectCaptainTextButton);

        addToGrid(firstColumn, selectFireFighterTextButton.getComponents().get(0), 0,1);
        addAllButFirstComponent(selectFireFighterTextButton);

        addToGrid(firstColumn, returnToMenuButton.getComponents().get(0), 0,2);
        addAllButFirstComponent(returnToMenuButton);


        addToGrid(secondColumn, startGameButton.getComponents().get(0), 1,0);
        addAllButFirstComponent(startGameButton);

        offTheGridObjects.add(menuCursor);

        grid.add(firstColumn);
        grid.add(secondColumn);

        updateDescriptionBoxes();
        updateCursor();
    }

    private void addToGrid(List<GUIComponent> gridList, GUIComponent component, int column, int row) {
        //Crosswired for this board, temporary fix of a bigger problem also present in ShopBoard
        component.setColumn(row);
        component.setRow(column);
        gridList.add(component);
    }

    private void addAllButFirstComponent (GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }

    }


    private PlayerClass lastHoveredOtion = PlayerStats.getInstance().getPlayerClass();

    public void updateDescriptionBoxes() {
        GUIComponent component = grid.get(selectedRow).get(selectedColumn);

        if (component.equals(selectCaptainTextButton.getComponents().get(0))) {
            lastHoveredOtion = PlayerClass.Captain;
            setWeaponIcons(PlayerClass.Captain);
        } else if (component.equals(selectFireFighterTextButton.getComponents().get(0))) {
            lastHoveredOtion = PlayerClass.FireFighter;
            setWeaponIcons(PlayerClass.FireFighter);
        } else {
            lastHoveredOtion = PlayerStats.getInstance().getPlayerClass();
            setWeaponIcons(lastHoveredOtion);
        }

        secondarySkillDescription = ClassSelectionBoardCreator.createSecondarySkillDescription(lastHoveredOtion);
        primaryWeaponDescription = ClassSelectionBoardCreator.createPrimaryWeaponDescription(lastHoveredOtion);
    }

    // Method to set the appropriate weapon icons based on the PlayerClass
    private void setWeaponIcons(PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                primaryWeaponIcon.setNewImage(ImageEnums.Starcraft2ConcentratedLaser);
                secondaryWeaponIcon.setNewImage(ImageEnums.Starcraft2_Electric_Field);
                break;
            case FireFighter:
                primaryWeaponIcon.setNewImage(ImageEnums.Starcraft2FireBatWeapon);
                secondaryWeaponIcon.setNewImage(ImageEnums.Starcraft2_Fire_Hardened_Shields);
                break;
            default:
                // Handle default case, if needed (e.g., if PlayerClass is unexpected)
                break;
        }
    }

    private void drawClassDescriptionText (Graphics2D g) {
        drawClassDescriptionText(g, classDescriptionBackgroundCard);
    }


    private void drawClassDescriptionText (Graphics2D g, GUIComponent backgroundCard) {
        int boxWidth = backgroundCard.getWidth();
        int boxHeight = backgroundCard.getHeight();

        int horizontalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int verticalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int maxTextWidth = boxWidth - (horizontalPadding * 2);
        String textFont = DataClass.getInstance().getTextFont();
        ClassDescription classDescription = ClassDescription.getInstance(lastHoveredOtion);

        if (classDescription != null) {
            String desciption = classDescription.getDescription();
            String title = classDescription.getTitle();
            String attackSpeed = "Attacks every: " + classDescription.getBaseAttackSpeed() + " seconds";
            String attackDamage = "Base damage: " + Math.round(classDescription.getBaseDamage());

            int descriptionX = backgroundCard.getXCoordinate() + horizontalPadding;
            int descriptionY = backgroundCard.getYCoordinate() + verticalPadding;


            if (title != null) {
                g.setFont(new Font(textFont, Font.BOLD, Math.round(20 * DataClass.getInstance().getResolutionFactor())));  // Larger font for the title
                drawDescriptionText(g, title, descriptionX, descriptionY, maxTextWidth, Color.GREEN);
                FontMetrics titleMetrics = g.getFontMetrics();
                descriptionY += titleMetrics.getHeight() + 10;
            }

            if (desciption != null) {
                g.setFont(new Font(textFont, Font.PLAIN, Math.round(14 * DataClass.getInstance().getResolutionFactor())));
                drawDescriptionText(g, desciption, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
                FontMetrics descriptionMetrics = g.getFontMetrics();
                descriptionY += descriptionMetrics.getHeight() * ((desciption.length() / maxTextWidth) + 1); //Doesn't properly increase the Y coordinates for subsequent texts, for some reason
            }

            int tempHeight = 0;
            if (attackSpeed != null) {
                g.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * DataClass.getInstance().getResolutionFactor())));
                FontMetrics descriptionMetrics = g.getFontMetrics();
                int attackSpeedY = backgroundCard.getYCoordinate() + boxHeight - verticalPadding - descriptionMetrics.getHeight();
                tempHeight = descriptionMetrics.getHeight();
                drawDescriptionText(g, attackSpeed, descriptionX, attackSpeedY, maxTextWidth, Color.ORANGE);
            }

            if (attackDamage != null) {
                g.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * DataClass.getInstance().getResolutionFactor())));
                FontMetrics descriptionMetrics = g.getFontMetrics();
                int attackDamageY = backgroundCard.getYCoordinate() + boxHeight - verticalPadding - descriptionMetrics.getHeight() - Math.round(tempHeight * 1.5f);
                drawDescriptionText(g, attackDamage, descriptionX, attackDamageY, maxTextWidth, Color.ORANGE);
            }
        }
    }


    private void drawDescriptionBoxText (Graphics2D g) {
        drawAbilityDescriptionText(g, primaryWeaponExplanationBackgroundCard, primaryWeaponIcon, primaryWeaponDescription);
        drawAbilityDescriptionText(g, secondarySkillWeaponExplanationBackgroundCard, secondaryWeaponIcon, secondarySkillDescription);
    }


    private void drawAbilityDescriptionText (Graphics2D g, GUIComponent backgroundCard, GUIComponent weaponIcon, WeaponDescription weaponDescription) {
        int boxWidth = backgroundCard.getWidth();
        int boxHeight = backgroundCard.getHeight();

        int horizontalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int verticalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int maxTextWidth = boxWidth - (horizontalPadding * 3);
        String textFont = DataClass.getInstance().getTextFont();

        if (weaponDescription != null) {
            String abilityDescription = weaponDescription.getDescription();
            String abilityName = weaponDescription.getName();

            int descriptionX = weaponIcon.getXCoordinate() + weaponIcon.getWidth() + (horizontalPadding / 2);
            int descriptionY = weaponIcon.getYCoordinate();

            if (abilityName != null) {
                g.setFont(new Font(textFont, Font.BOLD, Math.round(18 * DataClass.getInstance().getResolutionFactor())));  // Larger font for the title
                drawDescriptionText(g, abilityName, descriptionX, descriptionY, maxTextWidth, Color.GREEN);
                FontMetrics titleMetrics = g.getFontMetrics();
                descriptionY += titleMetrics.getHeight() + Math.round(10 * DataClass.getInstance().getResolutionFactor());  // Spacing after the title
            }

            if (abilityDescription != null) {
                g.setFont(new Font(textFont, Font.PLAIN, Math.round(14 * DataClass.getInstance().getResolutionFactor())));  // Default font for description
                drawDescriptionText(g, abilityDescription, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
                FontMetrics descriptionMetrics = g.getFontMetrics();
                descriptionY += descriptionMetrics.getHeight() * ((abilityDescription.length() / maxTextWidth) + 1); // Estimate line height
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


    /*------------------------Navigation methods--------------------------------*/

    // Activate the functionality of the specific menutile
    private void selectMenuTile () {
        grid.get(selectedRow).get(selectedColumn).activateComponent();
        if (grid.get(selectedRow).get(selectedColumn).getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            timer.stop();
        }
    }

    private void navigateLeft() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }
        if (handleLeftOrRightNavigationOverwrites()) {
            return; //We overwrote the navigation
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

    }

    // Go one menu tile right
    private void navigateRight() {
        if (isGridEmpty()) {
            return; // Do nothing if the grid is empty
        }

        if (handleLeftOrRightNavigationOverwrites()) {
            return; //We overwrote the navigation
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
    }


    private boolean handleLeftOrRightNavigationOverwrites() {
        //If overwritten a navigation, return true, if not return false
        if (menuCursor.getSelectedMenuTile().equals(this.returnToMenuButton.getComponents().get(0))) {
            selectedColumn = startGameButton.getComponents().get(0).getColumn();
            selectedRow = startGameButton.getComponents().get(0).getRow();
            return true;
        }

        if (menuCursor.getSelectedMenuTile().equals(this.startGameButton.getComponents().get(0))) {
            selectedColumn = returnToMenuButton.getComponents().get(0).getColumn();
            selectedRow = returnToMenuButton.getComponents().get(0).getRow();
            return true;
        }

        return false;
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
    private void navigateUp() {
        selectedColumn--;
        if (selectedColumn < 0) {
            selectedColumn = grid.get(selectedRow).size() - 1; // Wrap around to the rightmost column
        }
    }

    // Go one menu tile to the right
    private void navigateDown() {
        selectedColumn++;
        if (selectedColumn >= grid.get(selectedRow).size()) {
            selectedColumn = 0; // Wrap around to the leftmost column
        }
    }

    // Update the cursor's position and selected menu tile
    private void updateCursor () {
        if (grid.get(selectedRow).isEmpty()) { // Check if the selected row is empty
            menuCursor.setSelectedMenuTile(null); // Need to decide how you want to handle this situation
        } else {
            GUIComponent selectedTile = grid.get(selectedRow).get(selectedColumn);
            menuCursor.setSelectedMenuTile(selectedTile);
            menuCursor.setYCoordinate(selectedTile.getYCoordinate() - menuCursor.getHeight() / 2);
            menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
        }
    }

    private class KeyInputReader extends KeyAdapter {

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
            drawDescriptionBoxText(g2d);
            drawClassDescriptionText(g2d);

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

    public Timer getTimer () {
        return this.timer;
    }

    public void recreateCursor () {
        menuCursor = ClassSelectionBoardCreator.createCursor(returnToMenuButton);
        updateCursor();
    }

    public void addCursorAnimation(){
        animationManager.addUpperAnimation(ClassSelectionBoardCreator.createCursorAnimation(menuCursor));
        audioManager.addAudio(AudioEnums.ItemAcquired);
    }
}
