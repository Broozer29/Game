package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.DifficultySelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuGrid;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DifficultySelectionBoard extends MenuBoard {


    private GUIComponent chooseDifficultyText;
    private GUIComponent startGameButtonBackgroundCard;
    private GUITextCollection startGameButton;
    private GUIComponent returnToMenuBackgroundCard;
    private GUITextCollection returnToMenuButton;

    private GUIComponent manModeBackgroundCard;
    private GUITextCollection manModeButton;

    private GUIComponent selectTribeBackgroundCard;
    private GUITextCollection selectTribe;
    private GUIComponent selectMiniBossBackgroundCard;
    private GUITextCollection selectMiniboss;

    private GUIComponent selectPirates;
    private GUIComponent selectZerg;
    private GUIComponent selectRoyalGuard;

    private GUIComponent selectNoMiniBoss;
    private GUIComponent selectOneMiniBoss;
    private GUIComponent selectTwoMiniBoss;

    private GUIComponent totalDifficultyBackgroundCard;
    private GUITextCollection totalDifficultyIcon;
    private GUITextCollection totalDifficultyText;

    private GUIComponent descriptionBoxBackgroundCard;

    private final MenuGrid grid = new MenuGrid();
    private boolean initializedMenuObjects = false;


    public DifficultySelectionBoard() {
        grids.add(grid);

        initMenuTiles();
        getTimer().start();
    }

    public void initMenuTiles() {
        chooseDifficultyText = DifficultySelectionBoardCreator.createChoosDifficultyText();
        startGameButtonBackgroundCard = DifficultySelectionBoardCreator.createStartGameButtonBackground();
        startGameButton = DifficultySelectionBoardCreator.createStartGameButton(startGameButtonBackgroundCard);

        selectTribeBackgroundCard = DifficultySelectionBoardCreator.createDifficultySelectionBackgroundCard();
        selectTribe = DifficultySelectionBoardCreator.createTitleText(selectTribeBackgroundCard, "ENEMY TRIBE");

        selectPirates = DifficultySelectionBoardCreator.createPirateSelection(selectTribeBackgroundCard);
        selectZerg = DifficultySelectionBoardCreator.createZergSelection(selectTribeBackgroundCard);
        selectRoyalGuard = DifficultySelectionBoardCreator.createRoyalGuardSelection(selectTribeBackgroundCard);

        selectMiniBossBackgroundCard = DifficultySelectionBoardCreator.createMiniBossSelectionBackgroundCard();
        selectMiniboss = DifficultySelectionBoardCreator.createTitleText(selectMiniBossBackgroundCard, "MINI BOSSES");

        selectNoMiniBoss = DifficultySelectionBoardCreator.createNoMiniBossSelection(selectMiniBossBackgroundCard);
        selectOneMiniBoss = DifficultySelectionBoardCreator.createOneMiniBossSelection(selectMiniBossBackgroundCard);
        selectTwoMiniBoss = DifficultySelectionBoardCreator.createTwoMiniBossSelection(selectMiniBossBackgroundCard);

        totalDifficultyBackgroundCard = DifficultySelectionBoardCreator.createTotalDifficultyBackgroundCard();
        totalDifficultyText = DifficultySelectionBoardCreator.createTitleText(totalDifficultyBackgroundCard, "SELECTED DIFFICULTY");
        totalDifficultyIcon = DifficultySelectionBoardCreator.createNextLevelDifficultyIcon(totalDifficultyBackgroundCard);

        descriptionBoxBackgroundCard = DifficultySelectionBoardCreator.createDescriptionBoxBackgroundCard();

        returnToMenuBackgroundCard = DifficultySelectionBoardCreator.createReturnToMainMenuBackgroundCard();
        returnToMenuButton = DifficultySelectionBoardCreator.createReturn(returnToMenuBackgroundCard);

        manModeBackgroundCard = DifficultySelectionBoardCreator.createManModeBackgroundCard();
        manModeButton = DifficultySelectionBoardCreator.createManModeButton(manModeBackgroundCard);

        menuCursor = DifficultySelectionBoardCreator.createCursor(selectPirates);
        initializedMenuObjects = true;

        updateSelectedDifficultyIcons();
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
        updateSelectedDifficultyIcons();

        totalDifficultyIcon = DifficultySelectionBoardCreator.createNextLevelDifficultyIcon(totalDifficultyBackgroundCard);

        offTheGridObjects.add(chooseDifficultyText);
        offTheGridObjects.add(startGameButtonBackgroundCard);
        offTheGridObjects.add(selectTribeBackgroundCard);
        offTheGridObjects.add(selectMiniBossBackgroundCard);
        offTheGridObjects.add(returnToMenuBackgroundCard);
        offTheGridObjects.add(manModeBackgroundCard);
        offTheGridObjects.addAll(selectTribe.getComponents());
        offTheGridObjects.addAll(selectMiniboss.getComponents());
        offTheGridObjects.add(totalDifficultyBackgroundCard);
        offTheGridObjects.addAll(totalDifficultyIcon.getComponents());
        offTheGridObjects.addAll(totalDifficultyText.getComponents());
        offTheGridObjects.add(descriptionBoxBackgroundCard);


        List<GUIComponent> firstRow = new ArrayList<>();
        firstRow.add(selectPirates);
        firstRow.add(selectZerg);
        firstRow.add(selectRoyalGuard);
        firstRow.add(selectNoMiniBoss);
        firstRow.add(selectOneMiniBoss);
        firstRow.add(selectTwoMiniBoss);


        List<GUIComponent> secondRow = new ArrayList<>();
        secondRow.add(returnToMenuButton.getComponents().get(0));
        addAllButFirstComponent(returnToMenuButton);
        secondRow.add(manModeButton.getComponents().get(0));
        addAllButFirstComponent(manModeButton);
        secondRow.add(startGameButton.getComponents().get(0));
        addAllButFirstComponent(startGameButton);
        grid.addRow(firstRow);
        grid.addRow(secondRow);
        grid.placeCursor(menuCursor);
    }

    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    public void updateSelectedDifficultyIcons() {
        if (initializedMenuObjects) {
            ShopBoardCreator.updateDifficultyIconsToDifficulty(LevelManager.getInstance().getCurrentLevelDifficulty(),
                    selectPirates, selectZerg, selectRoyalGuard);

            ShopBoardCreator.updateMiniBossIconsToSelection(LevelManager.getInstance().getCurrentMiniBossConfig(),
                    selectNoMiniBoss, selectOneMiniBoss, selectTwoMiniBoss);
        }
    }

    private void drawClassDescriptionText(Graphics2D g) {
        drawClassDescriptionText(g, descriptionBoxBackgroundCard, menuCursor.getSelectedMenuTile().getDescriptionOfComponent());
    }


    private void drawClassDescriptionText(Graphics2D g, GUIComponent backgroundCard, String description) {
        int boxWidth = backgroundCard.getWidth();
        int boxHeight = backgroundCard.getHeight();

        int horizontalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int verticalPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
        int maxTextWidth = boxWidth - (horizontalPadding * 2);
        String textFont = DataClass.getInstance().getTextFont();

        int descriptionX = backgroundCard.getXCoordinate() + horizontalPadding;
        int descriptionY = backgroundCard.getYCoordinate() + verticalPadding;

        if (description != null) {
            g.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor))));
            drawDescriptionText(g, description, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
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
            PlayerInventory.getInstance().resetInventory();
            getTimer().stop();
        }
    }

    @Override
    protected void onBack() {
        BoardManager.getInstance().switchScreen(BoardManager.ScreenType.CLASS_SELECTION);
    }

    // Update the GUI only if there was an action that requires it
    @Override
    protected void onInputHandled() {
        recreateList();
    }

    // The description sits right after the grid and before the cursor
    @Override
    protected void drawScreenContent(Graphics2D g) {
        drawClassDescriptionText(g);
    }
}
