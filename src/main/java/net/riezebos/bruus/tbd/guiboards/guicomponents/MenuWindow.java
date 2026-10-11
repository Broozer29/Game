package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.MenuBoardCreator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

//A window shown over the game, such as the pause window and its questions: the main menu card with a title and options in menu text, centred on the screen
public class MenuWindow {

    private static final int CARD_WIDTH = 400;
    private static final int MARGIN = 55; //From the top of the card to the centre of the title, and from the centre of the last option to the bottom
    private static final int TITLE_GAP = 60; //From the centre of the title to the centre of the first option
    private static final int OPTION_GAP = 45; //From the centre of one option to the centre of the next

    private ResizableCard card;
    public record MenuRow (String text, MenuFunctionEnums function) {} //A null function makes the row a display-only title

    private final List<MenuRow> rowTexts = new ArrayList<>();
    private final List<GUITextCollection> rows = new ArrayList<>();
    private final List<GUITextCollection> options = new ArrayList<>(); //The rows that can be picked, in order
    private MenuCursor cursor;
    private int selectedOption = 0;

    public MenuWindow (String title, List<MenuRow> optionRows) {
        rowTexts.add(new MenuRow(title, null)); //The title is the first row
        rowTexts.addAll(optionRows);
        rebuild();
    }

    //Computes the size and positions from the current window size
    private void rebuild () {
        DataClass data = DataClass.getInstance();
        float resolutionFactor = Math.min(data.getResolutionFactor(), DataClass.maxResolutionFactor);

        float textScale = resolutionFactor * MenuText.TEXT_SCALE;
        int margin = Math.round(MARGIN * resolutionFactor);
        int titleGap = Math.round(TITLE_GAP * resolutionFactor);
        int optionGap = Math.round(OPTION_GAP * resolutionFactor);
        int cardWidth = Math.round(CARD_WIDTH * resolutionFactor);
        int cardHeight = margin * 2 + titleGap + (rowTexts.size() - 2) * optionGap;
        int cardX = (data.getWindowWidth() - cardWidth) / 2;
        int cardY = (data.getWindowHeight() - cardHeight) / 2;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(cardX);
        spriteConfiguration.setyCoordinate(cardY);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);
        card = new ResizableCard(spriteConfiguration, cardWidth, cardHeight);

        rows.clear();
        options.clear();
        for (int i = 0; i < rowTexts.size(); i++) {
            MenuRow menuRow = rowTexts.get(i);
            int centerY = cardY + margin + (i == 0 ? 0 : titleGap + (i - 1) * optionGap);
            GUITextCollection row = MenuText.createText(menuRow.text(), cardX + cardWidth / 2, centerY, textScale, MenuText.ALIGN_CENTER);
            if (menuRow.function() != null) {
                row.setMenuFunctionality(menuRow.function()); //Makes the first letter the button
                options.add(row);
            }
            rows.add(row);
        }

        cursor = MenuBoardCreator.createMenuCursor(options.get(0).getComponents().get(0));
        selectedOption = 0;
        placeCursor();
    }

    //Puts the cursor left of the selected option, the way the main menu does
    private void placeCursor () {
        GUIComponent selectedTile = options.get(selectedOption).getComponents().get(0);
        cursor.setSelectedMenuTile(selectedTile);
        cursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + cursor.getYDistanceModification());
        cursor.setXCoordinate(selectedTile.getXCoordinate() - cursor.getxDistanceToKeep());
    }

    public void selectOption (int index) {
        selectedOption = index;
        placeCursor();
    }

    public void selectFirstOption () {
        selectOption(0);
    }

    public void selectNextOption () {
        selectedOption = (selectedOption + 1) % options.size();
        placeCursor();
    }

    public void selectPreviousOption () {
        selectedOption = (selectedOption - 1 + options.size()) % options.size();
        placeCursor();
    }

    public void activateSelectedOption () {
        options.get(selectedOption).getComponents().get(0).activateComponent();
    }

    //The card first, then every letter, then the cursor, in the order they are drawn
    public List<GUIComponent> getComponents () {
        List<GUIComponent> components = new ArrayList<>();
        components.add(card);
        for (GUITextCollection row : rows) {
            components.addAll(row.getComponents());
        }
        components.add(cursor);
        return components;
    }
}
