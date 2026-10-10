package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

//The window shown while the game is paused: the main menu card with a row of menu text for the title, centred on the screen
public class PauseWindow {

    private static final int CARD_WIDTH = 250;
    private static final int PADDING = 30;
    private static final int ROW_HEIGHT = 50;

    private ResizableCard card;
    private final List<String> rowTexts = new ArrayList<>();
    private final List<GUITextCollection> rows = new ArrayList<>();

    public PauseWindow () {
        rowTexts.add("PAUSED"); //The title is the first row
        rebuild();
    }

    //Computes the size and positions from the current window size
    private void rebuild () {
        DataClass data = DataClass.getInstance();
        float resolutionFactor = Math.min(data.getResolutionFactor(), DataClass.maxResolutionFactor);

        int padding = Math.round(PADDING * resolutionFactor);
        int rowHeight = Math.round(ROW_HEIGHT * resolutionFactor);
        int cardWidth = Math.round(CARD_WIDTH * resolutionFactor);
        int cardHeight = padding * 2 + rowTexts.size() * rowHeight;
        int cardX = (data.getWindowWidth() - cardWidth) / 2;
        int cardY = (data.getWindowHeight() - cardHeight) / 2;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(cardX);
        spriteConfiguration.setyCoordinate(cardY);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);
        card = new ResizableCard(spriteConfiguration, cardWidth, cardHeight);

        rows.clear();
        for (int i = 0; i < rowTexts.size(); i++) {
            GUITextCollection row = new GUITextCollection(0, cardY + padding + i * rowHeight, rowTexts.get(i));
            row.setScale(resolutionFactor);
            row.setCenterXCoordinate(cardX + cardWidth / 2);
            rows.add(row);
        }
    }

    //The card first, then every letter, in the order they are drawn
    public List<GUIComponent> getComponents () {
        List<GUIComponent> components = new ArrayList<>();
        components.add(card);
        for (GUITextCollection row : rows) {
            components.addAll(row.getComponents());
        }
        return components;
    }
}
