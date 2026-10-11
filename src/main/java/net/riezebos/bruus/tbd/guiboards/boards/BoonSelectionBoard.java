package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.Boon;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.BountyHunter;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.ClubAccess;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.CompoundWealth;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.utility.TreasureHunter;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.BoonSelectionBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuGrid;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoonSelectionBoard extends MenuBoard {
    private final MenuGrid grid = new MenuGrid();

    private GUIComponent selectBoonsTitle;

    private GUIComponent firstColumnBackgroundCard;
    private GUIComponent thirdColumnBackgroundCard;
    private GUIComponent fifthColumnBackgroundCard;

    private GUIComponent mainMenuButton;
    private GUIComponent startGameBackgroundCard;
    private GUITextCollection selectClassButton;
    private GUITextCollection startGameButton;
    private GUITextCollection emeraldsAvailableText;

    private GUIComponent descriptionBackgroundCard;
    private GUIComponent descriptionBox;

    private GUITextCollection selectNepotism;
    private GUIComponent upgradeNepotism;

    private GUITextCollection selectDefault;
    private GUITextCollection selectManMode;
    private GUITextCollection selectMonoCultural;
    private GUITextCollection selectDoubleTrouble;
    private GUITextCollection selectNightmare;
    private GUITextCollection selectFormatted;

    private GUITextCollection selectClubAccess;
    private GUIComponent upgradeClubAccess;
    private GUITextCollection selectCompoundInterest;
    private GUIComponent upgradeCompoundInterest;
    private GUITextCollection selectBountyHunter;
    private GUIComponent upgradeBountyHunter;
    private GUITextCollection selectTreasureHunter;
    private GUIComponent upgradeTreasureHunter;

    private GUIComponent boonColumnSelectionIndicator;
    private GUIComponent gameModifierColumnSelectionIndicator;
    private GUITextCollection boonColumnTitleText;
    private GUITextCollection gameModifiersColumnTitleText;

    private int textPadding = Math.round(40 * DataClass.getInstance().getResolutionFactor());
    private boolean initializedMenuObjects = false;

    private DescriptionInfo currentDescription;
    private boolean shouldDrawBoonDescription;

    private static class DescriptionInfo {
        String title = null;
        String cost = null;
        String descriptionText = null;
    }

    public BoonSelectionBoard() {
        grids.add(grid);

        initMenuTiles();
        getTimer().start();
    }

    private void initMenuTiles() {
        selectBoonsTitle = BoonSelectionBoardCreator.createSelectUpgradesTitleCard();
        firstColumnBackgroundCard = BoonSelectionBoardCreator.createFirstColumnBackgroundCard();
        thirdColumnBackgroundCard = BoonSelectionBoardCreator.createThirdColumnBackgroundCard();
        fifthColumnBackgroundCard = BoonSelectionBoardCreator.createFifthColumnBackgroundCard();
        mainMenuButton = BoonSelectionBoardCreator.createReturnToSelectionBackgroundCard();
        startGameBackgroundCard = BoonSelectionBoardCreator.createStartGameBackgroundCard();

        emeraldsAvailableText = BoonSelectionBoardCreator.createEmeraldText();
        descriptionBackgroundCard = BoonSelectionBoardCreator.createDescriptionBackgroundCard();
        descriptionBox = BoonSelectionBoardCreator.createDescriptionComponent(descriptionBackgroundCard);

        selectClassButton = BoonSelectionBoardCreator.createReturnToClassSelectionButton(mainMenuButton);
        startGameButton = BoonSelectionBoardCreator.createStartGameButton(startGameBackgroundCard);


        recreateBoonsComponents();

        boonColumnSelectionIndicator = BoonSelectionBoardCreator.createUpgradeSelectedCheckmark();
        gameModifierColumnSelectionIndicator = BoonSelectionBoardCreator.createUpgradeSelectedCheckmark();
        boonColumnTitleText = BoonSelectionBoardCreator.createFirstColumnTitle(firstColumnBackgroundCard);
        gameModifiersColumnTitleText = BoonSelectionBoardCreator.createThirdColumnTitle(thirdColumnBackgroundCard);

//        offenseColumnTitleText = BoonSelectionBoardCreator.createFifthColumnTitle(fifthColumnBackgroundCard);
//        offenseWIPText = BoonSelectionBoardCreator.createWorkInProgressText(fifthColumnBackgroundCard);

        menuCursor = BoonSelectionBoardCreator.createCursor(selectNepotism.getComponents().get(0));
        initializedMenuObjects = true;
        recreateWindow();
    }

    @Override
    public void recreateWindow() {
        if (initializedMenuObjects) {
            menuCursor = BoonSelectionBoardCreator.createCursor(selectNepotism.getComponents().get(0));
            //Clear all existing columns/rows/grid then re-add them
            recreateList();
            grid.reset();
            grid.placeCursor(menuCursor);
            refreshDescription();
        }
    }

    public void recreateEmeraldText() {
        emeraldsAvailableText = BoonSelectionBoardCreator.createEmeraldText();
    }

    public void recreateList() {
        grid.clear();
        offTheGridObjects.clear();

        offTheGridObjects.add(selectBoonsTitle);
        offTheGridObjects.add(firstColumnBackgroundCard);
        offTheGridObjects.add(thirdColumnBackgroundCard);
        offTheGridObjects.add(fifthColumnBackgroundCard);
        offTheGridObjects.add(mainMenuButton);
        offTheGridObjects.add(startGameBackgroundCard);
        offTheGridObjects.add(descriptionBackgroundCard);

        recreateBoonsComponents();

        offTheGridObjects.addAll(emeraldsAvailableText.getComponents());
        offTheGridObjects.addAll(boonColumnTitleText.getComponents());
        offTheGridObjects.addAll(gameModifiersColumnTitleText.getComponents());


        List<GUIComponent> firstRow = new ArrayList<>();
        List<GUIComponent> secondRow = new ArrayList<>();
        List<GUIComponent> thirdRow = new ArrayList<>();
        List<GUIComponent> fourthRow = new ArrayList<>();
        List<GUIComponent> fifthRow = new ArrayList<>();
        List<GUIComponent> sixthRow = new ArrayList<>();
        List<GUIComponent> seventhRow = new ArrayList<>();

        firstRow.add(selectNepotism.getComponents().get(0));
        addAllButFirstComponent(selectNepotism);
        firstRow.add(upgradeNepotism);

        secondRow.add(selectClubAccess.getComponents().get(0));
        addAllButFirstComponent(selectClubAccess);
        secondRow.add(upgradeClubAccess);

        thirdRow.add(selectCompoundInterest.getComponents().get(0));
        addAllButFirstComponent(selectCompoundInterest);
        thirdRow.add(upgradeCompoundInterest);

        fourthRow.add(selectBountyHunter.getComponents().get(0));
        addAllButFirstComponent(selectBountyHunter);
        fourthRow.add(upgradeBountyHunter);

        fifthRow.add(selectTreasureHunter.getComponents().get(0));
        addAllButFirstComponent(selectTreasureHunter);
        fifthRow.add(upgradeTreasureHunter);

        sixthRow.add(selectClassButton.getComponents().get(0));
        addAllButFirstComponent(selectClassButton);

        firstRow.add(selectDefault.getComponents().get(0));
        addAllButFirstComponent(selectDefault);

        secondRow.add(selectManMode.getComponents().get(0));
        addAllButFirstComponent(selectManMode);

        thirdRow.add(selectMonoCultural.getComponents().get(0));
        addAllButFirstComponent(selectMonoCultural);

        fourthRow.add(selectDoubleTrouble.getComponents().get(0));
        addAllButFirstComponent(selectDoubleTrouble);

        fifthRow.add(selectNightmare.getComponents().get(0));
        addAllButFirstComponent(selectNightmare);

        sixthRow.add(selectFormatted.getComponents().get(0));
        addAllButFirstComponent(selectFormatted);

        seventhRow.add(startGameButton.getComponents().get(0));
        addAllButFirstComponent(startGameButton);

        grid.addRow(firstRow);
        grid.addRow(secondRow);
        grid.addRow(thirdRow);
        grid.addRow(fourthRow);
        grid.addRow(fifthRow);
        grid.addRow(sixthRow);
        grid.addRow(seventhRow);

        offTheGridObjects.add(menuCursor);

        updateCheckmarkLocations();

        if (showUtilityCheckmark) {
            offTheGridObjects.add(boonColumnSelectionIndicator);
        }

        if (showGameModeCheckMark) {
            offTheGridObjects.add(gameModifierColumnSelectionIndicator);
        }

        grid.placeCursor(menuCursor);
        refreshDescription();
    }

    private void recreateBoonsComponents() {
        selectNepotism = BoonSelectionBoardCreator.createNepotismSelectionButton(firstColumnBackgroundCard);
        upgradeNepotism = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectNepotism, MenuFunctionEnums.UpgradeNepotism);

        selectClubAccess = BoonSelectionBoardCreator.createOtherBoonButtons(selectNepotism.getComponents().get(0), ClubAccess.getInstance());
        upgradeClubAccess = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectClubAccess, MenuFunctionEnums.UpgradeClubAccess);

        selectCompoundInterest = BoonSelectionBoardCreator.createOtherBoonButtons(selectClubAccess.getComponents().get(0), CompoundWealth.getInstance());
        upgradeCompoundInterest = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectCompoundInterest, MenuFunctionEnums.UpgradeCompoundWealth);

        selectBountyHunter = BoonSelectionBoardCreator.createOtherBoonButtons(selectCompoundInterest.getComponents().get(0), BountyHunter.getInstance());
        upgradeBountyHunter = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectBountyHunter, MenuFunctionEnums.UpgradeBountyHunter);

        selectTreasureHunter = BoonSelectionBoardCreator.createOtherBoonButtons(selectBountyHunter.getComponents().get(0), TreasureHunter.getInstance());
        upgradeTreasureHunter = BoonSelectionBoardCreator.createUpgradeBoonButton(firstColumnBackgroundCard, selectTreasureHunter, MenuFunctionEnums.UpgradeTreasureHunter);

        selectDefault = BoonSelectionBoardCreator.createSelectDefaultButton(thirdColumnBackgroundCard);
        selectManMode = BoonSelectionBoardCreator.createOtherGameModeButton(selectDefault.getComponents().get(0), GameMode.ManMode);
        selectMonoCultural = BoonSelectionBoardCreator.createOtherGameModeButton(selectManMode.getComponents().get(0), GameMode.MonoCultural);
        selectDoubleTrouble = BoonSelectionBoardCreator.createOtherGameModeButton(selectMonoCultural.getComponents().get(0), GameMode.DoubleTrouble);
        selectNightmare = BoonSelectionBoardCreator.createOtherGameModeButton(selectDoubleTrouble.getComponents().get(0), GameMode.Nightmare);
        selectFormatted = BoonSelectionBoardCreator.createOtherGameModeButton(selectNightmare.getComponents().get(0), GameMode.Formatted);
        emeraldsAvailableText = BoonSelectionBoardCreator.createEmeraldText();
    }

    //Helper method to add all text collection components that are intended to be displayonly
    private void addAllButFirstComponent(GUITextCollection textCollection) {
        for (int i = 1; i < textCollection.getComponents().size(); i++) {
            offTheGridObjects.add(textCollection.getComponents().get(i));
        }
    }

    private void refreshDescription() {
        GUIComponent selectedTile = grid.getSelected();
        if (selectedTile != null) {
            currentDescription = prepareDescriptionText(selectedTile);
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
        BoardManager.getInstance().switchScreen(BoardManager.ScreenType.CLASS_SELECTION);
    }

    // Update the GUI only if there was an action that requires it
    @Override
    protected void onInputHandled() {
        recreateList();
    }

    // The cursor sits in offTheGridObjects (under the tiles and the checkmarks), not in the cursor layer of the base
    @Override
    protected boolean drawsMenuCursor() {
        return false;
    }

    // The description is drawn after the cursor and the grid
    @Override
    protected void drawScreenOverlay(Graphics2D g) {
        drawDescriptionInfo(g, currentDescription);
    }

    /*------------------------Navigation--------------------------------*/

    // The grid is rows of columns: R0 [Nepotism, its upgrade, Default], R1 [ClubAccess, upgrade, ManMode],
    // R2 [CompoundInterest, upgrade, MonoCultural], R3 [BountyHunter, upgrade, DoubleTrouble],
    // R4 [TreasureHunter, upgrade, Nightmare], R5 [Class button, Formatted], R6 [Start].
    // The old screen wrapped up and down inside a column, MenuGrid wraps over all rows, so the moves where those
    // differ, and the old jumps between named buttons, are listed here. Everything else is the MenuGrid default.
    @Override
    protected void onDirection(MenuAction action) {
        // Like the old code, the jumps between named buttons look at the tile the cursor was last placed on.
        // That is refreshed once per poll, so with several directions in one poll it lags behind the selection.
        GUIComponent target = jumpFromCursorTile(action, menuCursor.getSelectedMenuTile());
        if (target == null) {
            // Moves of the old column grid that the MenuGrid default does not give. They also keep the old result
            // for the stale-cursor cases (Start left/right, Class button right, upgrade Nepotism up,
            // upgrade Treasure Hunter down, Formatted down) when the cursor tile lags behind the selection.
            target = jumpFromSelectedTile(action, grid.getSelected());
        }
        if (target != null) {
            grid.select(target);
            return;
        }
        super.onDirection(action);
    }

    private GUIComponent jumpFromCursorTile(MenuAction action, GUIComponent cursorTile) {
        GUIComponent classButton = selectClassButton.getComponents().get(0);
        GUIComponent start = startGameButton.getComponents().get(0);
        switch (action) {
            case LEFT:
            case RIGHT:
                if (cursorTile == classButton) {
                    return start;
                }
                if (cursorTile == start) {
                    return classButton;
                }
                return null;
            case UP:
                return cursorTile == upgradeNepotism ? classButton : null;
            case DOWN:
                if (cursorTile == upgradeTreasureHunter) {
                    return classButton;
                }
                if (cursorTile == selectFormatted.getComponents().get(0)) {
                    return start;
                }
                return null;
            default:
                return null;
        }
    }

    private GUIComponent jumpFromSelectedTile(MenuAction action, GUIComponent selected) {
        GUIComponent classButton = selectClassButton.getComponents().get(0);
        GUIComponent start = startGameButton.getComponents().get(0);
        GUIComponent formatted = selectFormatted.getComponents().get(0);
        List<GUIComponent> boonTiles = List.of(selectNepotism.getComponents().get(0), selectClubAccess.getComponents().get(0),
                selectCompoundInterest.getComponents().get(0), selectBountyHunter.getComponents().get(0),
                selectTreasureHunter.getComponents().get(0));
        List<GUIComponent> modeTiles = List.of(selectDefault.getComponents().get(0), selectManMode.getComponents().get(0),
                selectMonoCultural.getComponents().get(0), selectDoubleTrouble.getComponents().get(0),
                selectNightmare.getComponents().get(0), formatted);
        switch (action) {
            case LEFT:
                if (boonTiles.contains(selected) || selected == classButton) {
                    return start;
                }
                if (selected == formatted) {
                    return upgradeTreasureHunter;
                }
                return selected == start ? selectDefault.getComponents().get(0) : null;
            case RIGHT:
                if (modeTiles.contains(selected)) {
                    return start;
                }
                if (selected == classButton) {
                    return upgradeTreasureHunter;
                }
                return selected == start ? selectNepotism.getComponents().get(0) : null;
            case UP:
                if (selected == selectNepotism.getComponents().get(0)) {
                    return classButton;
                }
                if (selected == upgradeNepotism) {
                    return upgradeTreasureHunter;
                }
                if (selected == selectDefault.getComponents().get(0)) {
                    return formatted;
                }
                if (selected == formatted) {
                    return selectNightmare.getComponents().get(0);
                }
                return selected == start ? start : null;
            case DOWN:
                if (selected == classButton) {
                    return selectNepotism.getComponents().get(0);
                }
                if (selected == upgradeTreasureHunter) {
                    return upgradeNepotism;
                }
                if (selected == formatted) {
                    return selectDefault.getComponents().get(0);
                }
                return selected == start ? start : null;
            default:
                return null;
        }
    }

    private DescriptionInfo prepareDescriptionText(GUIComponent selectedTileHelper) {
        DescriptionInfo descriptionInfo = new DescriptionInfo();
        if (selectedTileHelper.getMenuFunctionality() != null) {
            if (BoonSelectionBoardCreator.getBoonByMenuFunctionality(selectedTileHelper.getMenuFunctionality()) != null) {
                Boon selectedBoon = BoonSelectionBoardCreator.getBoonByMenuFunctionality(selectedTileHelper.getMenuFunctionality());
                if (selectedBoon.isUnlocked()) {
                    descriptionInfo.descriptionText = selectedBoon.getBoonDescription();
                    descriptionInfo.title = selectedBoon.getBoonName() + " rank: " + selectedBoon.getCurrentLevel();
                } else {
                    descriptionInfo.descriptionText = selectedBoon.getBoonUnlockCondition();
                    descriptionInfo.title = "LOCKED";
                }

                if (!selectedBoon.isUnlocked()) {
                    descriptionInfo.cost = "";
                } else if (selectedBoon.canUpgradeFurther()) {
                    descriptionInfo.cost = "Costs: " + selectedBoon.getBoonUpgradeCost() + " emerald to upgrade. You currently have: " + PlayerProfileManager.getInstance().getLoadedProfile().getEmeralds();
                } else {
                    descriptionInfo.cost = "Max rank reached";
                }
                shouldDrawBoonDescription = true;
            } else if (BoonSelectionBoardCreator.getGameModeByMenuFunctionality(selectedTileHelper.getMenuFunctionality()) != null) {
                GameMode gameMode = BoonSelectionBoardCreator.getGameModeByMenuFunctionality(selectedTileHelper.getMenuFunctionality());
                descriptionInfo.descriptionText = gameMode.getDescription();
                descriptionInfo.title = gameMode.getName();
                shouldDrawBoonDescription = true;
            } else { //not a boon selected
                descriptionInfo.descriptionText = selectedTileHelper.getDescriptionOfComponent();
                shouldDrawBoonDescription = false;
            }
        }

        return descriptionInfo;
    }

    private void drawDescriptionInfo(Graphics2D g2d, DescriptionInfo descriptionInfo) {
        int boxWidth = descriptionBackgroundCard.getWidth();
        int boxHeight = descriptionBackgroundCard.getHeight();
        String textFont = DataClass.getInstance().getTextFont();
        int maxTextWidth = boxWidth - (textPadding * 2);

        int descriptionX = descriptionBox.getXCoordinate() + textPadding;
        int descriptionY = descriptionBox.getYCoordinate() + textPadding;


        if (shouldDrawBoonDescription && descriptionInfo.title != null) {
            g2d.setFont(new Font(textFont, Font.BOLD, Math.round(22 * DataClass.getInstance().getResolutionFactor())));  // Larger font for the title
            drawDescriptionText(g2d, descriptionInfo.title, descriptionX, descriptionY, maxTextWidth, Color.GREEN);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            descriptionY += titleMetrics.getHeight() * ((descriptionInfo.descriptionText.length() / maxTextWidth) + 2);  // Spacing after the title
        }


        // Set font and draw the description if available
        if (descriptionInfo.descriptionText != null) {
            g2d.setFont(new Font(textFont, Font.PLAIN, Math.round(18 * DataClass.getInstance().getResolutionFactor())));  // Default font for description
            drawDescriptionText(g2d, descriptionInfo.descriptionText, descriptionX, descriptionY, maxTextWidth, Color.WHITE);
            FontMetrics descriptionMetrics = g2d.getFontMetrics();
            descriptionY += descriptionMetrics.getHeight() * ((descriptionInfo.descriptionText.length() / maxTextWidth) + 1); // Estimate line height
        }

        if (shouldDrawBoonDescription && descriptionInfo.cost != null) {
            g2d.setFont(new Font(textFont, Font.ITALIC, Math.round(16 * DataClass.getInstance().getResolutionFactor())));  // Slightly smaller italic font for the cost
            FontMetrics costMetrics = g2d.getFontMetrics();
            int costY = descriptionBox.getYCoordinate() + boxHeight - textPadding - costMetrics.getHeight();
            drawDescriptionText(g2d, descriptionInfo.cost, descriptionX, costY, maxTextWidth, Color.WHITE);
        }
    }

    private void drawDescriptionText(Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight() + 2; //small increase
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


    private void updateCheckmarkLocations() {
        placeBoonColumnCheckmark();
        placeGameModeColumnCheckmark();
    }

    private boolean showUtilityCheckmark = false;

    private void placeBoonColumnCheckmark() {
        Boon utilityBoon = BoonManager.getInstance().getActiveBoon();

        if (utilityBoon == null) {
            showUtilityCheckmark = false;
            return; //Nothing selected, so we dont set it
        }

        BoonEnums boonEnums = utilityBoon.getBoonEnum();
        int checkMarkXCoordinate = 0;
        int checkMarkYCoordinate = 0;
        GUITextCollection selectedTextCollection = null;

        switch (boonEnums) {
            case NEPOTISM -> {
                selectedTextCollection = selectNepotism;
            }
            case CLUB_ACCESS -> {
                selectedTextCollection = selectClubAccess;
            }
            case BOUNTY_HUNTER -> {
                selectedTextCollection = selectBountyHunter;
            }
            case TREASURE_HUNTER -> {
                selectedTextCollection = selectTreasureHunter;
            }
            case COMPOUND_WEALTH -> {
                selectedTextCollection = selectCompoundInterest;
            }
            default -> {
                showUtilityCheckmark = false;
                return;
            }
        }


        checkMarkXCoordinate = selectedTextCollection.getStartingXCoordinate() + Math.round((selectedTextCollection.getWidth() / 2));
        checkMarkYCoordinate = selectedTextCollection.getComponents().get(0).getCenterYCoordinate();

        boonColumnSelectionIndicator.setCenterCoordinates(checkMarkXCoordinate, checkMarkYCoordinate);
        showUtilityCheckmark = true;
    }


    private boolean showGameModeCheckMark = false;

    private void placeGameModeColumnCheckmark() {
        GameMode selectedGameMode = GameState.getInstance().getGameMode();

        if (selectedGameMode == null) {
            showGameModeCheckMark = false;
            return; //Nothing selected, so we dont set it
        }

        int checkMarkXCoordinate = 0;
        int checkMarkYCoordinate = 0;
        GUITextCollection selectedTextCollection = null;

        switch (selectedGameMode) {
            case Default -> {
                selectedTextCollection = selectDefault;
                break;
            }
            case MonoCultural -> {
                selectedTextCollection = selectMonoCultural;
                break;
            }
            case ManMode -> {
                selectedTextCollection = selectManMode;
                break;
            }
            case DoubleTrouble -> {
                selectedTextCollection = selectDoubleTrouble;
                break;
            }
            case Nightmare -> {
                selectedTextCollection = selectNightmare;
                break;
            }
            case Formatted -> {
                selectedTextCollection = selectFormatted;
                break;
            }
            default -> {
                OnScreenTextManager.getInstance().addText("UnImplemented!");
                showGameModeCheckMark = false;
                return;
            }
        }


        checkMarkXCoordinate = selectedTextCollection.getStartingXCoordinate() + Math.round((selectedTextCollection.getWidth() / 2));
        checkMarkYCoordinate = selectedTextCollection.getComponents().get(0).getCenterYCoordinate();

        gameModifierColumnSelectionIndicator.setCenterCoordinates(checkMarkXCoordinate, checkMarkYCoordinate);
        showGameModeCheckMark = true;
    }

}
