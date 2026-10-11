package net.riezebos.bruus.tbd.controllerInput;

import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUITextCollection;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuText;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuWindow;
import net.riezebos.bruus.tbd.guiboards.guicomponents.ResizableCard;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Short notices ("CONTROLLER 2 CONNECTED"), each in its own card, stacked at the top centre and drawn by the screens as their last drawing step
public class ControllerNotices {
    private static ControllerNotices instance = new ControllerNotices();
    private static final long NOTICE_DURATION_MILLIS = 3000;
    private static final int MAX_NOTICES = 3; //A new notice pushes the oldest out
    private static final int SIDE_MARGIN = 30; //From the card edge to the letters, left and right
    private static final int VERTICAL_MARGIN = 36; //From the card edge to the letters, top and bottom; keeps the card higher than its two corners, so the corners are not shrunk and the straight side edges show
    private static final int TOP_MARGIN = 10; //From the top of the window to the first card
    private static final int GAP = 10; //Between two cards
    private final List<Notice> notices = new ArrayList<>();

    //The card and letters are built on the first draw, so the image work happens on the drawing thread
    private static class Notice {
        private final String text;
        private final long expiryTime;
        private ResizableCard card;
        private GUITextCollection letters;

        private Notice(String text, long expiryTime) {
            this.text = text;
            this.expiryTime = expiryTime;
        }
    }

    private ControllerNotices() {
    }

    public static ControllerNotices getInstance() {
        return instance;
    }

    public synchronized void addNotice(String text) {
        if (notices.size() >= MAX_NOTICES) {
            notices.remove(0); //All notices last equally long, so the first is the oldest
        }
        notices.add(new Notice(text, System.currentTimeMillis() + NOTICE_DURATION_MILLIS));
    }

    public synchronized void draw(Graphics g) {
        long now = System.currentTimeMillis();
        notices.removeIf(notice -> notice.expiryTime <= now);
        if (notices.isEmpty()) {
            return;
        }

        float resolutionFactor = Math.min(DataClass.getInstance().getResolutionFactor(), DataClass.maxResolutionFactor);
        int cardTop = Math.round(TOP_MARGIN * resolutionFactor);
        for (Notice notice : notices) {
            if (notice.card == null) {
                build(notice, resolutionFactor);
            }
            //The card was built at (0,0), so its place in the stack is added to every image
            int offsetX = (DataClass.getInstance().getWindowWidth() - notice.card.getWidth()) / 2;
            g.drawImage(notice.card.getImage(), offsetX, cardTop, null);
            for (GUIComponent letter : notice.letters.getComponents()) {
                if (letter.getImage() != null) {
                    g.drawImage(letter.getImage(), offsetX + letter.getXCoordinate(), cardTop + letter.getYCoordinate(), null);
                }
            }
            cardTop += notice.card.getHeight() + Math.round(GAP * resolutionFactor);
        }
    }

    //A card as wide as the letters plus the side margins, with the border thickness of the pause window, and the letters centred in it
    private void build(Notice notice, float resolutionFactor) {
        GUITextCollection letters = MenuText.createText(notice.text, 0, 0, resolutionFactor * MenuText.TEXT_SCALE, MenuText.ALIGN_LEFT);
        int cardWidth = letters.getWidth() + 2 * Math.round(SIDE_MARGIN * resolutionFactor);
        int cardHeight = letters.getHeight() + 2 * Math.round(VERTICAL_MARGIN * resolutionFactor);

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(0);
        spriteConfiguration.setyCoordinate(0);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);
        notice.card = new ResizableCard(spriteConfiguration, cardWidth, cardHeight, MenuWindow.cardWidth());
        notice.letters = MenuText.createText(notice.text, cardWidth / 2, cardHeight / 2, resolutionFactor * MenuText.TEXT_SCALE, MenuText.ALIGN_CENTER);
    }
}
