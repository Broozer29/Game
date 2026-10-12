package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ClassSelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuGrid;
import net.riezebos.bruus.tbd.guiboards.util.ClassDescription;
import net.riezebos.bruus.tbd.guiboards.util.WeaponDescription;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClassSelectionBoard extends MenuBoard {

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
    private GUITextCollection selectCarrierTextButton;
    private GUITextCollection selectMutaliskTextButton;
    private GUIComponent startGameButtonBackgroundCard;
    private GUITextCollection startGameButton;
    private GUIComponent returnToMenuBackgroundCard;
    private GUITextCollection returnToMenuButton;
    /*Board Components*/

    private final MenuGrid grid = new MenuGrid();
    private boolean initializedMenuObjects = false;


    public ClassSelectionBoard() {
        grids.add(grid);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse clicked at: X=" + e.getX() + ", Y=" + e.getY());
            }
        });


        initMenuTiles();
        getTimer().start();
    }

    public void initMenuTiles() {
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
        selectCarrierTextButton = ClassSelectionBoardCreator.createSelectCarrier(classSelectionBackgroundCard);
        selectMutaliskTextButton = ClassSelectionBoardCreator.createSelectMutalisk(classSelectionBackgroundCard);

        startGameButtonBackgroundCard = ClassSelectionBoardCreator.createStartGameButtonBackground();
        startGameButton = ClassSelectionBoardCreator.createStartGameButton(startGameButtonBackgroundCard);


        menuCursor = ClassSelectionBoardCreator.createCursor(returnToMenuButton);
        initializedMenuObjects = true;
    }

    @Override
    public void recreateWindow() {
        if (initializedMenuObjects) {
            //Clear all existing columns/rows/grid then re-add them
            recreateList();
            grid.reset();
            grid.placeCursor(menuCursor);
        }
    }

    private void recreateList() {
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


        addRow(selectCaptainTextButton);
        addRow(selectFireFighterTextButton);
        addRow(selectCarrierTextButton);
        addRow(selectMutaliskTextButton);

        // Return and Start share the bottom row
        List<GUIComponent> bottomRow = new ArrayList<>();
        bottomRow.add(returnToMenuButton.getComponents().get(0));
        addAllButFirstComponent(returnToMenuButton);
        bottomRow.add(startGameButton.getComponents().get(0));
        addAllButFirstComponent(startGameButton);
        grid.addRow(bottomRow);

        updateDescriptionBoxes();
        grid.placeCursor(menuCursor);
    }

    private void addRow(GUITextCollection button) {
        List<GUIComponent> row = new ArrayList<>();
        row.add(button.getComponents().get(0));
        addAllButFirstComponent(button);
        grid.addRow(row);
    }

    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }

    }


    private PlayerClass lastHoveredOtion = PlayerStats.getInstance().getPlayerClass();

    public void updateDescriptionBoxes() {
        GUIComponent component = grid.getSelected();

        if (component.equals(selectCaptainTextButton.getComponents().get(0))) {
            lastHoveredOtion = PlayerClass.Captain;
            setWeaponIcons(PlayerClass.Captain);
        } else if (component.equals(selectFireFighterTextButton.getComponents().get(0))) {
            lastHoveredOtion = PlayerClass.FireFighter;
            setWeaponIcons(PlayerClass.FireFighter);
        } else if (component.equals(selectCarrierTextButton.getComponents().get(0))) {
            lastHoveredOtion = PlayerClass.Carrier;
            setWeaponIcons(PlayerClass.Carrier);
        } else if(component.equals(selectMutaliskTextButton.getComponents().get(0))){
            lastHoveredOtion = PlayerClass.Mutalisk;
            setWeaponIcons(PlayerClass.Mutalisk);
        }
        else {
            lastHoveredOtion = PlayerStats.getInstance().getPlayerClass();
            setWeaponIcons(lastHoveredOtion);
        }

        secondarySkillDescription = ClassSelectionBoardCreator.createSecondarySkillDescription(lastHoveredOtion);
        primaryWeaponDescription = ClassSelectionBoardCreator.createPrimaryWeaponDescription(lastHoveredOtion);
    }

    private void setWeaponIcons(PlayerClass playerClass) {
        switch (playerClass) {
            case Captain:
                primaryWeaponIcon.setNewImage(ImageEnums.Starcraft2ConcentratedLaser);
                secondaryWeaponIcon.setNewImage(ImageEnums.Starcraft2_Electric_Field);
                break;
            case FireFighter:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isFireFighterUnlocked()) {
                    primaryWeaponIcon.setNewImage(ImageEnums.Starcraft2FireBatWeapon);
                    secondaryWeaponIcon.setNewImage(ImageEnums.Starcraft2_Fire_Hardened_Shields);
                } else {
                    primaryWeaponIcon.setNewImage(ImageEnums.LockedIcon);
                    secondaryWeaponIcon.setNewImage(ImageEnums.LockedIcon);
                }
                break;
            case Carrier:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
                    primaryWeaponIcon.setNewImage(ImageEnums.CarrierSwitchGearsIcon);
                    secondaryWeaponIcon.setNewImage(ImageEnums.CarrierPlaceDroneIcon);
                } else {
                    primaryWeaponIcon.setNewImage(ImageEnums.LockedIcon);
                    secondaryWeaponIcon.setNewImage(ImageEnums.LockedIcon);
                }
                break;
            case Mutalisk:
                if (PlayerProfileManager.getInstance().getLoadedProfile().isMutaliskUnlocked()) {
                    primaryWeaponIcon.setNewImage(ImageEnums.BileIcon);
                    secondaryWeaponIcon.setNewImage(ImageEnums.VileEruptionIcon);
                } else {
                    primaryWeaponIcon.setNewImage(ImageEnums.LockedIcon);
                    secondaryWeaponIcon.setNewImage(ImageEnums.LockedIcon);
                }
            default:
                break;
        }
    }

    private void drawClassDescriptionText(Graphics2D g) {
        drawClassDescriptionText(g, classDescriptionBackgroundCard);
    }


    private void drawClassDescriptionText(Graphics2D g, GUIComponent backgroundCard) {
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
            String hitpoints = "Hitpoints: " + classDescription.getMaxHitpoints();
            String damage = "Damage: " + PlayerStats.sharedBaseDamage;
            String attackSpeed = "Attack speed: " + classDescription.getAttackSpeed();
            String difficulty = "Difficulty: " + classDescription.getDifficulty();


            if (!ClassSelectionBoardCreator.hasUnlockedClass(lastHoveredOtion)) {
                title = "Locked";
                desciption = classDescription.getUnlockCondition();
                hitpoints = null;
                difficulty = null;
                damage = null;
                attackSpeed = null;
            }

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
                FontMetrics descriptionMetrics = g.getFontMetrics();

                // Calculate the number of lines the description will take when word wrapping
                int lines = getWrappedLineCount(desciption, maxTextWidth, descriptionMetrics);
                drawDescriptionText(g, desciption, descriptionX, descriptionY, maxTextWidth, Color.WHITE);

                // Update descriptionY based on the actual rendered height of the description block
                descriptionY += lines * descriptionMetrics.getHeight();
            }

            descriptionY += 40;

            if (hitpoints != null) {
                g.setFont(new Font(textFont, Font.PLAIN, Math.round(16 * DataClass.getInstance().getResolutionFactor())));
                FontMetrics metrics = g.getFontMetrics();
                drawDescriptionText(g, hitpoints, descriptionX, descriptionY, maxTextWidth, Color.ORANGE);
                descriptionY += metrics.getHeight();
            }

            if (damage != null) {
                FontMetrics metrics = g.getFontMetrics();
                drawDescriptionText(g, damage, descriptionX, descriptionY, maxTextWidth, Color.ORANGE);
                descriptionY += metrics.getHeight();
            }

            if (attackSpeed != null) {
                FontMetrics metrics = g.getFontMetrics();
                drawDescriptionText(g, attackSpeed, descriptionX, descriptionY, maxTextWidth, Color.ORANGE);
                descriptionY += metrics.getHeight();
            }

            if (difficulty != null) {
                FontMetrics metrics = g.getFontMetrics();
                drawDescriptionText(g, difficulty, descriptionX, descriptionY, maxTextWidth, Color.ORANGE);
                descriptionY += metrics.getHeight();
            }
        }
    }

    private int getWrappedLineCount(String text, int maxTextWidth, FontMetrics metrics) {
        int lineCount = 0;
        if (text != null && !text.isEmpty()) {
            // Split the text into words
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                // Check if adding the word would exceed the max width
                if (metrics.stringWidth(line + word) > maxTextWidth) {
                    lineCount++; // Move to a new line
                    line = new StringBuilder(word + " "); // Start new line with the current word
                } else {
                    line.append(word).append(" ");
                }
            }

            // Count the last line (if any text remains)
            if (!line.isEmpty()) {
                lineCount++;
            }
        }
        return lineCount;
    }


    private void drawDescriptionBoxText(Graphics2D g) {
        drawAbilityDescriptionText(g, primaryWeaponExplanationBackgroundCard, primaryWeaponIcon, primaryWeaponDescription);
        drawAbilityDescriptionText(g, secondarySkillWeaponExplanationBackgroundCard, secondaryWeaponIcon, secondarySkillDescription);
    }


    private void drawAbilityDescriptionText(Graphics2D g, GUIComponent backgroundCard, GUIComponent weaponIcon, WeaponDescription weaponDescription) {
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
                g.setFont(new Font(textFont, Font.BOLD, Math.round(18 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));  // Larger font for the title
                drawDescriptionText(g, abilityName, descriptionX, descriptionY, maxTextWidth, Color.GREEN);
                FontMetrics titleMetrics = g.getFontMetrics();
                descriptionY += titleMetrics.getHeight() + Math.round(10 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor));  // Spacing after the title
            }

            if (abilityDescription != null) {
                g.setFont(new Font(textFont, Font.PLAIN, Math.round(14 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));  // Default font for description
                drawDescriptionText(g, abilityDescription, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
                FontMetrics descriptionMetrics = g.getFontMetrics();
                descriptionY += descriptionMetrics.getHeight() * ((abilityDescription.length() / maxTextWidth) + 1); // Estimate line height
            }
        }

    }

    private void drawDescriptionText(Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight() + 2;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        g2d.setColor(color);

        for (String word : words) {
            // If adding the new word exceeds the maximum line width, draw the line and start a new one
            if (metrics.stringWidth(line + word) > maxWidth) {
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


    /*------------------------Menu hooks--------------------------------*/

    // Activate the functionality of the specific menutile
    @Override
    protected void onConfirm() {
        GUIComponent selectedTile = grid.getSelected();
        selectedTile.activateComponent();
        if (selectedTile.getMenuFunctionality() == MenuFunctionEnums.Start_Game) {
            getTimer().stop();
        }
    }

    @Override
    protected void onBack() {
        BoardManager.getInstance().switchScreen(BoardManager.ScreenType.MAIN_MENU);
    }

    // Update the GUI only if there was an action that requires it
    @Override
    protected void onInputHandled() {
        recreateList();
    }

    // The class texts sit right after the grid and before the cursor
    @Override
    protected void drawScreenContent(Graphics2D g) {
        drawDescriptionBoxText(g);
        drawClassDescriptionText(g);
    }

    // The class buttons are one column, Return and Start share the bottom row
    @Override
    protected void onDirection(MenuAction action) {
        GUIComponent startTile = startGameButton.getComponents().get(0);
        GUIComponent returnTile = returnToMenuButton.getComponents().get(0);
        switch (action) {
            case LEFT:
            case RIGHT:
                if (grid.isEmpty()) {
                    return; // Do nothing if the grid is empty
                }
                GUIComponent cursorTile = menuCursor.getSelectedMenuTile(); // Lags behind the selection when one poll holds several keys; kept as it was
                if (cursorTile == returnTile) {
                    grid.select(startTile);
                } else if (cursorTile == startTile) {
                    grid.select(returnTile);
                } else if (grid.getSelected() == startTile) {
                    grid.select(selectCaptainTextButton.getComponents().get(0));
                } else {
                    grid.select(startTile);
                }
                break;
            case UP:
            case DOWN:
                if (grid.getSelected() == startTile) {
                    return; // Start is alone in its column
                }
                super.onDirection(action);
                break;
        }
    }

    public void recreateCursor() {
        menuCursor = ClassSelectionBoardCreator.createCursor(returnToMenuButton);
        grid.placeCursor(menuCursor);
    }

    public void addCursorAnimation() {
        animationManager.addUpperAnimation(ClassSelectionBoardCreator.createCursorAnimation(menuCursor));
    }
}
