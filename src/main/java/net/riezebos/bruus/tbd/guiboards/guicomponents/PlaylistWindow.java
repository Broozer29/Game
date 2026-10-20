package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.controllerInput.InputDevice;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.DescriptionTextQuality;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicPlace;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicTrack;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//The PLAYLIST page shown over the main menu: one card like the settings page, with a tab row (LEVELS, BOSSES, FINAL BOSS, MENUS),
//the songs of the selected tab on the left and the song that plays (or is selected) on the right, and hint cards under the card
//CONFIRM plays the selected song whole as a preview; the preview and the menu music are handled by AudioManager.startPreview and stopPreviewAndResume
//All sizes below are in unscaled pixels and get multiplied by the resolution factor
public class PlaylistWindow {

    private static final int CARD_WIDTH = 900;
    private static final int CARD_HEIGHT = 500;
    private static final int PADDING_X = 60; //From the left and right edge of the card to the list and the details; the frame of the card image is about 42 wide
    private static final int TAB_Y = 48; //From the top of the card to the centre of the tab row
    private static final int TAB_GAP = 44; //Between two tab names
    private static final int TAB_UNDERLINE_GAP = 8; //From the bottom of the tab text to the underline
    private static final int TAB_UNDERLINE_HEIGHT = 3;
    private static final int TAB_UNDERLINE_COLOR = 0xFFFFFFFF; //White
    private static final int BODY_TOP = 100; //From the top of the card to the top of the list and the details
    private static final int BODY_BOTTOM = 64; //From the bottom of the card to the bottom of the list and the details
    private static final int LIST_WIDTH = 440;
    private static final int COLUMN_GAP = 26; //Between the list and the details, with the thin line in the middle
    private static final int ROW_HEIGHT = 48;
    private static final int ROW_PADDING = 12;
    private static final int HINT_HEIGHT = 50; //The height of the shop's wide button cards
    private static final int HINT_GAP = 14; //Between the card and the hint cards, and between two hint cards
    private static final int TABS_CARD_WIDTH = 190;
    private static final int PLAY_CARD_WIDTH = 150;
    private static final int BACK_CARD_WIDTH = 150;
    private static final int WINDOW_MARGIN = 20; //The page never gets closer than this to the edge of the window
    private static final int FILLING_OFFSET = 5; //Where the filling starts inside the bar image, in pixels of the source image

    private static final Color LINE = new Color(43, 211, 194, 77);
    private static final Color ROW_FILL = new Color(43, 211, 194, 36);
    private static final Color ROW_BORDER = new Color(159, 233, 224);
    private static final Color INK = new Color(242, 247, 246);
    private static final Color MUTED = new Color(143, 165, 163);
    private static final Color YELLOW = new Color(255, 232, 26);
    private static final Color TEAL_SOFT = new Color(159, 233, 224);
    private static final Color PILL_BORDER = new Color(27, 127, 120);
    private static final Color PILL_FILL = new Color(43, 211, 194, 20);

    private enum PlaylistTab {LEVELS, BOSSES, FINAL_BOSS, MENUS}

    private static final Map<PlaylistTab, String> TAB_NAMES = new EnumMap<>(PlaylistTab.class);

    static {
        TAB_NAMES.put(PlaylistTab.LEVELS, "LEVELS");
        TAB_NAMES.put(PlaylistTab.BOSSES, "BOSSES");
        TAB_NAMES.put(PlaylistTab.FINAL_BOSS, "FINAL BOSS");
        TAB_NAMES.put(PlaylistTab.MENUS, "MENUS");
    }

    private static PlaylistWindow instance; //Only one page is shown at a time, so the buttons reach the one that is shown here

    private float resolutionFactor;
    private ResizableCard card;
    private final Map<PlaylistTab, GUITextCollection> tabTexts = new EnumMap<>(PlaylistTab.class);
    private final Map<PlaylistTab, Picture> tabUnderlines = new EnumMap<>(PlaylistTab.class); //One per tab, only the selected tab has its underline shown
    private final Map<PlaylistTab, List<MusicTrack>> tabTracks = new EnumMap<>(PlaylistTab.class);
    private DisplayOnly tabsCard; //The hint cards under the card
    private DisplayOnly playCard;
    private DisplayOnly backCard;
    private GUITextCollection tabsText;
    private GUITextCollection playText;
    private GUITextCollection backText;
    private InputDevice hintsShownFor; //The device the hint texts were built for
    private PlaylistTab selectedTab = PlaylistTab.LEVELS;
    private int selectedIndex = 0;
    private int firstVisibleRow = 0;
    private int visibleRows;
    private MusicTrack previewTrack; //The song of the last CONFIRM, which stays shown after it ended
    private boolean previewEnded = false;
    private double lastTotalSeconds = 0; //The last length seen while the preview played, so the ended preview still shows it
    private BufferedImage barImage; //The game's progress bar art, scaled to the details column
    private BufferedImage fillingSource;
    private BufferedImage fillingShown;
    private int fillingShownSourceWidth = -1;
    private boolean open = false;
    private Runnable onClosed; //What the screen does after the page closed

    public static PlaylistWindow getInstance () {
        if (instance == null) {
            instance = new PlaylistWindow();
        }
        return instance;
    }

    private PlaylistWindow () {
    }

    private int scaled (int size) {
        return Math.round(size * resolutionFactor);
    }

    private int cardLeft () {
        return (DataClass.getInstance().getWindowWidth() - scaled(CARD_WIDTH)) / 2;
    }

    private int cardTop () {
        int blockHeight = scaled(CARD_HEIGHT) + scaled(HINT_GAP) + scaled(HINT_HEIGHT);
        return (DataClass.getInstance().getWindowHeight() - blockHeight) / 2;
    }

    private int listLeft () {
        return cardLeft() + scaled(PADDING_X);
    }

    private int detailsLeft () {
        return listLeft() + scaled(LIST_WIDTH) + scaled(COLUMN_GAP);
    }

    private int detailsWidth () {
        return scaled(CARD_WIDTH) - 2 * scaled(PADDING_X) - scaled(LIST_WIDTH) - scaled(COLUMN_GAP);
    }

    private int bodyTop () {
        return cardTop() + scaled(BODY_TOP);
    }

    private int bodyHeight () {
        return scaled(CARD_HEIGHT - BODY_TOP - BODY_BOTTOM);
    }

    private void build () {
        DataClass data = DataClass.getInstance();
        resolutionFactor = Math.min(data.getResolutionFactor(), DataClass.maxResolutionFactor);
        float fitWidth = (float) (data.getWindowWidth() - 2 * WINDOW_MARGIN) / CARD_WIDTH;
        float fitHeight = (float) (data.getWindowHeight() - 2 * WINDOW_MARGIN) / (CARD_HEIGHT + HINT_GAP + HINT_HEIGHT);
        resolutionFactor = Math.min(resolutionFactor, Math.min(fitWidth, fitHeight)); //A small window shrinks the page instead of cutting it off

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(cardLeft());
        spriteConfiguration.setyCoordinate(cardTop());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);
        card = new ResizableCard(spriteConfiguration, scaled(CARD_WIDTH), scaled(CARD_HEIGHT));

        buildTabs();
        buildTrackLists();

        int hintTop = cardTop() + scaled(CARD_HEIGHT) + scaled(HINT_GAP);
        int hintsWidth = scaled(TABS_CARD_WIDTH) + scaled(PLAY_CARD_WIDTH) + scaled(BACK_CARD_WIDTH) + 2 * scaled(HINT_GAP);
        int hintX = (data.getWindowWidth() - hintsWidth) / 2;
        tabsCard = createHintCard(hintX, hintTop, TABS_CARD_WIDTH);
        playCard = createHintCard(tabsCard.getXCoordinate() + tabsCard.getWidth() + scaled(HINT_GAP), hintTop, PLAY_CARD_WIDTH);
        backCard = createHintCard(playCard.getXCoordinate() + playCard.getWidth() + scaled(HINT_GAP), hintTop, BACK_CARD_WIDTH);
        hintsShownFor = null;

        visibleRows = Math.max(1, bodyHeight() / scaled(ROW_HEIGHT));

        barImage = null; //Built on the first draw, at the width of the details column
        fillingShown = null;
        fillingShownSourceWidth = -1;
    }

    //The tab names in the menu letters, side by side and centred on the card, each with a white underline as wide as the name that shows only while the tab is selected
    private void buildTabs () {
        tabTexts.clear();
        tabUnderlines.clear();
        float textScale = resolutionFactor * MenuText.TEXT_SCALE;
        int tabCenterY = cardTop() + scaled(TAB_Y);
        int totalWidth = 0;
        Map<PlaylistTab, Integer> widths = new EnumMap<>(PlaylistTab.class);
        for (PlaylistTab tab : PlaylistTab.values()) {
            int width = MenuText.createText(TAB_NAMES.get(tab), 0, 0, textScale, MenuText.ALIGN_LEFT).getWidth();
            widths.put(tab, width);
            totalWidth += width;
        }
        totalWidth += (PlaylistTab.values().length - 1) * scaled(TAB_GAP);
        int x = DataClass.getInstance().getWindowWidth() / 2 - totalWidth / 2;
        for (PlaylistTab tab : PlaylistTab.values()) {
            int tabCenterX = x + widths.get(tab) / 2;
            GUITextCollection tabText = MenuText.createText(TAB_NAMES.get(tab), tabCenterX, tabCenterY, textScale, MenuText.ALIGN_CENTER);
            BufferedImage underlineImage = new BufferedImage(tabText.getWidth(), scaled(TAB_UNDERLINE_HEIGHT), BufferedImage.TYPE_INT_ARGB);
            for (int px = 0; px < underlineImage.getWidth(); px++) {
                for (int py = 0; py < underlineImage.getHeight(); py++) {
                    underlineImage.setRGB(px, py, TAB_UNDERLINE_COLOR);
                }
            }
            Picture tabUnderline = new Picture(ImageEnums.Frame);
            tabUnderline.setPicture(underlineImage);
            tabUnderline.setCenterCoordinates(tabCenterX, tabCenterY + tabText.getHeight() / 2 + scaled(TAB_UNDERLINE_GAP));
            tabTexts.put(tab, tabText);
            tabUnderlines.put(tab, tabUnderline);
            x += widths.get(tab) + scaled(TAB_GAP);
        }
    }

    //The songs of each tab: LEVELS the level songs, BOSSES the boss songs then the fallback songs, FINAL BOSS the three phases, MENUS the main menu, shop and game over songs
    private void buildTrackLists () {
        tabTracks.clear();
        for (PlaylistTab tab : PlaylistTab.values()) {
            tabTracks.put(tab, new ArrayList<>());
        }
        for (MusicTrack track : MusicTrack.values()) {
            if (track.getPlace() == MusicPlace.LEVEL) {
                tabTracks.get(PlaylistTab.LEVELS).add(track);
            } else if (track.getPlace() == MusicPlace.BOSS) {
                tabTracks.get(PlaylistTab.BOSSES).add(track);
            } else if (track.getPlace() == MusicPlace.FINAL_BOSS) {
                tabTracks.get(PlaylistTab.FINAL_BOSS).add(track);
            } else if (track.getPlace() == MusicPlace.MAIN_MENU || track.getPlace() == MusicPlace.SHOP || track.getPlace() == MusicPlace.GAME_OVER) {
                tabTracks.get(PlaylistTab.MENUS).add(track);
            }
        }
        for (MusicTrack track : MusicTrack.values()) {
            if (track.getPlace() == MusicPlace.FALLBACK) {
                tabTracks.get(PlaylistTab.BOSSES).add(track); //After the boss songs
            }
        }
    }

    //A wide card like the shop's MAIN MENU button, under the page card
    private DisplayOnly createHintCard (int x, int y, int unscaledWidth) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(x);
        spriteConfiguration.setyCoordinate(y);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Wide_Card);
        DisplayOnly displayOnly = new DisplayOnly(spriteConfiguration);
        displayOnly.setImageDimensions(scaled(unscaledWidth), scaled(HINT_HEIGHT));
        return displayOnly;
    }

    //Menu letters centred on a card, at the scale of the shop's buttons
    private GUITextCollection createCardText (String text, DisplayOnly cardComponent) {
        return MenuText.createText(text, cardComponent.getCenterXCoordinate(), cardComponent.getCenterYCoordinate(), resolutionFactor, MenuText.ALIGN_CENTER);
    }

    //Builds the hint texts again when the device used last changed; the menu letters have no slash
    private void refreshHintTexts () {
        InputDevice device = MenuInput.getInstance().getLastDevice();
        if (device == hintsShownFor) {
            return;
        }
        hintsShownFor = device;
        boolean keyboard = device == InputDevice.KEYBOARD;
        tabsText = createCardText(keyboard ? "TAB TABS" : "LB RB TABS", tabsCard);
        playText = createCardText(keyboard ? "ENTER PLAY" : "A PLAY", playCard);
        backText = createCardText(keyboard ? "ESC BACK" : "B BACK", backCard);
    }

    private List<MusicTrack> selectedTracks () {
        return tabTracks.get(selectedTab);
    }

    private MusicTrack selectedTrack () {
        List<MusicTrack> tracks = selectedTracks();
        return tracks.isEmpty() ? null : tracks.get(selectedIndex);
    }

    //Shows the page on the LEVELS tab with its first song selected; the menu music keeps playing until a preview starts
    //onClosed says what the screen does once the page is closed
    public void open (Runnable onClosed) {
        this.onClosed = onClosed;
        build(); //Built again, so it follows the size of the window
        selectedTab = PlaylistTab.LEVELS;
        selectedIndex = 0;
        firstVisibleRow = 0;
        previewTrack = null;
        previewEnded = false;
        lastTotalSeconds = 0;
        open = true;
        MenuInput.getInstance().startScreen();
    }

    public boolean isOpen () {
        return open;
    }

    //Hides the page without telling the screen, for when the screen itself pauses, resumes or changes
    //The preview stops and the menu music resumes where it was
    public void closeSilently () {
        open = false;
        onClosed = null; //A screen's close step only belongs to the open it came with
        previewTrack = null;
        previewEnded = false;
        AudioManager.getInstance().stopPreviewAndResume();
    }

    private void close () {
        Runnable closedStep = onClosed;
        closeSilently();
        if (closedStep != null) {
            closedStep.run();
        }
    }

    //The page acts on what MenuInput reports
    public void handleInput (List<MenuAction> actions) {
        //A preview that ended by itself is noticed here; the bar then shows full and the menu music stays paused until the page closes
        if (previewTrack != null && !previewEnded) {
            double total = AudioManager.getInstance().getPreviewTotalSeconds();
            if (isKnownLength(total)) {
                lastTotalSeconds = total;
            }
            if (AudioManager.getInstance().pollPreviewEnded()) {
                previewEnded = true;
            }
        }
        PlaylistTab[] tabs = PlaylistTab.values();
        for (MenuAction action : actions) {
            if (action == MenuAction.PREVIOUS_TAB) {
                selectTab(tabs[Math.max(selectedTab.ordinal() - 1, 0)]); //No wrap: nothing happens on the first tab
            } else if (action == MenuAction.NEXT_TAB) {
                selectTab(tabs[Math.min(selectedTab.ordinal() + 1, tabs.length - 1)]); //No wrap: nothing happens on the last tab
            } else if (action == MenuAction.CYCLE_TAB) {
                selectTab(tabs[(selectedTab.ordinal() + 1) % tabs.length]); //Tab on the keyboard: from the last tab back to the first
            } else if (action == MenuAction.UP) {
                moveSelection(-1);
            } else if (action == MenuAction.DOWN) {
                moveSelection(1);
            } else if (action == MenuAction.CONFIRM) {
                playSelected();
            } else if (action == MenuAction.BACK) {
                close();
                return;
            }
        }
    }

    private void selectTab (PlaylistTab tab) {
        if (tab == selectedTab) {
            return;
        }
        selectedTab = tab;
        selectedIndex = 0;
        firstVisibleRow = 0;
    }

    //Moving the cursor plays nothing
    private void moveSelection (int direction) {
        int count = selectedTracks().size();
        if (count == 0) {
            return;
        }
        selectedIndex = (selectedIndex + direction + count) % count;
        if (selectedIndex < firstVisibleRow) {
            firstVisibleRow = selectedIndex;
        } else if (selectedIndex >= firstVisibleRow + visibleRows) {
            firstVisibleRow = selectedIndex - visibleRows + 1;
        }
    }

    private void playSelected () {
        MusicTrack track = selectedTrack();
        if (track == null) {
            return;
        }
        if (!AudioManager.getInstance().startPreview(track)) {
            return;
        }
        previewTrack = track;
        previewEnded = false;
        lastTotalSeconds = 0;
    }

    private static boolean isKnownLength (double seconds) {
        return seconds > 0 && !Double.isNaN(seconds) && !Double.isInfinite(seconds);
    }

    //Where the song plays: the boss name, the phase, the screen, or the random regular level
    private String whereItPlays (MusicTrack track) {
        switch (track.getPlace()) {
            case LEVEL:
                return "random regular level";
            case BOSS:
                return bossName(track.getBoss());
            case FINAL_BOSS:
                return "Final boss, phase " + track.getPhase();
            case MAIN_MENU:
                return "Main menu";
            case SHOP:
                return "Shop";
            case GAME_OVER:
                return "Game over";
            case FALLBACK:
            default:
                return "fallback";
        }
    }

    //The enum name with a space before every capital: SpaceStationBoss becomes Space Station Boss
    private String bossName (EnemyEnums boss) {
        return boss == null ? "" : boss.name().replaceAll("(?<=[a-z])(?=[A-Z])", " ");
    }

    private String artistOf (MusicTrack track) {
        return track.getArtist().isEmpty() ? "Artist unknown" : track.getArtist();
    }

    private static String formatTime (double seconds) {
        int total = (int) Math.floor(seconds);
        return String.format("%d:%02d", total / 60, total % 60);
    }

    //The page card, the selected tab's underline, the tab names and the hint cards with their texts, drawn like every other component; the list and the details go over them in draw
    public List<GUIComponent> getComponents () {
        refreshHintTexts();
        List<GUIComponent> components = new ArrayList<>();
        components.add(card);
        components.add(tabUnderlines.get(selectedTab));
        for (PlaylistTab tab : PlaylistTab.values()) {
            components.addAll(tabTexts.get(tab).getComponents());
        }
        components.add(tabsCard);
        components.addAll(tabsText.getComponents());
        components.add(playCard);
        components.addAll(playText.getComponents());
        components.add(backCard);
        components.addAll(backText.getComponents());
        return components;
    }

    private Font font (int style, int size) {
        return new Font(DataClass.getInstance().getTextFont(), style, scaled(size));
    }

    //The song list, the thin line, and the details with the progress bar
    public void draw (Graphics2D g2d) {
        Object oldInterpolation = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        Stroke oldStroke = g2d.getStroke();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        DescriptionTextQuality quality = DescriptionTextQuality.apply(g2d);

        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(LINE);
        int bodyTop = bodyTop();
        int bodyBottom = bodyTop + bodyHeight();
        g2d.drawLine(listLeft(), bodyTop - scaled(6), listLeft() + scaled(LIST_WIDTH) + scaled(COLUMN_GAP) + detailsWidth(), bodyTop - scaled(6));
        int dividerX = listLeft() + scaled(LIST_WIDTH) + scaled(COLUMN_GAP) / 2;
        g2d.drawLine(dividerX, bodyTop, dividerX, bodyBottom);

        drawList(g2d);
        drawDetails(g2d);

        g2d.setStroke(oldStroke);
        quality.restore(g2d);
        if (oldInterpolation != null) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
        }
    }

    //Cuts the text to the width with three dots when it does not fit
    private String fit (String text, FontMetrics metrics, int maxWidth) {
        if (metrics.stringWidth(text) <= maxWidth) {
            return text;
        }
        String cut = text;
        while (cut.length() > 1 && metrics.stringWidth(cut + "...") > maxWidth) {
            cut = cut.substring(0, cut.length() - 1);
        }
        return cut + "...";
    }

    private void drawList (Graphics2D g2d) {
        List<MusicTrack> tracks = selectedTracks();
        int rowHeight = scaled(ROW_HEIGHT);
        int rowWidth = scaled(LIST_WIDTH);
        int left = listLeft();
        int top = bodyTop();
        int padding = scaled(ROW_PADDING);
        for (int visible = 0; visible < visibleRows; visible++) {
            int index = firstVisibleRow + visible;
            if (index >= tracks.size()) {
                break;
            }
            MusicTrack track = tracks.get(index);
            int rowTop = top + visible * rowHeight;
            if (index == selectedIndex) {
                g2d.setColor(ROW_FILL);
                g2d.fillRoundRect(left, rowTop + 1, rowWidth, rowHeight - 2, scaled(8), scaled(8));
                g2d.setColor(ROW_BORDER);
                g2d.drawRoundRect(left, rowTop + 1, rowWidth, rowHeight - 2, scaled(8), scaled(8));
            }
            g2d.setFont(font(Font.PLAIN, 12));
            String where = whereItPlays(track);
            int whereWidth = g2d.getFontMetrics().stringWidth(where);
            g2d.setColor(MUTED);
            drawCenteredText(g2d, where, left + rowWidth - padding - whereWidth, rowTop, rowHeight);

            int textWidth = rowWidth - 3 * padding - whereWidth;
            g2d.setFont(font(Font.BOLD, 15));
            g2d.setColor(track == previewTrack ? YELLOW : INK);
            drawText(g2d, fit(track.getTitle(), g2d.getFontMetrics(), textWidth), left + padding, rowTop + rowHeight * 2 / 5);
            g2d.setFont(font(Font.PLAIN, 12));
            g2d.setColor(MUTED);
            drawText(g2d, fit(artistOf(track), g2d.getFontMetrics(), textWidth), left + padding, rowTop + rowHeight * 7 / 10 + scaled(2));
        }
        if (tracks.size() > visibleRows) {
            int trackHeight = visibleRows * rowHeight;
            int thumbHeight = Math.max(scaled(16), trackHeight * visibleRows / tracks.size());
            int thumbTop = top + (trackHeight - thumbHeight) * firstVisibleRow / (tracks.size() - visibleRows);
            g2d.setColor(LINE);
            g2d.fillRoundRect(left + rowWidth + scaled(6), thumbTop, scaled(3), thumbHeight, scaled(3), scaled(3));
        }
    }

    //Draws the text with its vertical middle at the given y
    private void drawText (Graphics2D g2d, String text, int x, int middleY) {
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.drawString(text, x, middleY - metrics.getHeight() / 2 + metrics.getAscent());
    }

    //Draws the text with its vertical middle at the middle of the given band
    private void drawCenteredText (Graphics2D g2d, String text, int x, int bandTop, int bandHeight) {
        drawText(g2d, text, x, bandTop + bandHeight / 2);
    }

    //The playing (or last played) song, otherwise the selected one, with the progress bar and the time at the bottom
    private void drawDetails (Graphics2D g2d) {
        MusicTrack shown = previewTrack != null ? previewTrack : selectedTrack();
        int left = detailsLeft();
        int width = detailsWidth();
        int y = bodyTop();
        int bodyBottom = bodyTop() + bodyHeight();

        String state = previewTrack == null ? "SELECTED" : (previewEnded ? "PLAYED" : "NOW PLAYING");
        g2d.setFont(font(Font.BOLD, 11));
        g2d.setColor(MUTED);
        drawText(g2d, state, left, y + scaled(8));
        y += scaled(26);
        if (shown != null) {
            g2d.setFont(font(Font.BOLD, 24));
            g2d.setColor(YELLOW);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            for (String line : wrapLines(shown.getTitle(), titleMetrics, width, 2)) {
                g2d.drawString(line, left, y + titleMetrics.getAscent());
                y += titleMetrics.getHeight();
            }
            y += scaled(14);
            y = drawKeyValue(g2d, "ARTIST", artistOf(shown), left, y, width, false);
            drawKeyValue(g2d, "PLAYS IN", whereItPlays(shown), left, y, width, true);
        }
        drawProgress(g2d, left, bodyBottom, width);
    }

    //The label in small letters and the value beside it; the value sits in a pill when asked; returns the y of the next row
    private int drawKeyValue (Graphics2D g2d, String label, String value, int left, int y, int width, boolean pill) {
        int rowHeight = scaled(30);
        g2d.setFont(font(Font.BOLD, 11));
        g2d.setColor(MUTED);
        drawText(g2d, label, left, y + rowHeight / 2);
        int valueLeft = left + scaled(86);
        g2d.setFont(font(Font.PLAIN, 14));
        FontMetrics metrics = g2d.getFontMetrics();
        String shownValue = fit(value, metrics, width - scaled(86) - (pill ? scaled(20) : 0));
        if (pill) {
            int pillWidth = metrics.stringWidth(shownValue) + scaled(20);
            int pillHeight = metrics.getHeight() + scaled(6);
            int pillTop = y + (rowHeight - pillHeight) / 2;
            g2d.setColor(PILL_FILL);
            g2d.fillRoundRect(valueLeft, pillTop, pillWidth, pillHeight, pillHeight, pillHeight);
            g2d.setColor(PILL_BORDER);
            g2d.drawRoundRect(valueLeft, pillTop, pillWidth, pillHeight, pillHeight, pillHeight);
            g2d.setColor(TEAL_SOFT);
            drawText(g2d, shownValue, valueLeft + scaled(10), y + rowHeight / 2);
        } else {
            g2d.setColor(INK);
            drawText(g2d, shownValue, valueLeft, y + rowHeight / 2);
        }
        return y + rowHeight + scaled(4);
    }

    //Wraps at spaces into at most maxLines lines; the last line is cut with three dots when the title is still longer
    private List<String> wrapLines (String text, FontMetrics metrics, int maxWidth, int maxLines) {
        List<String> lines = new ArrayList<>();
        String line = "";
        for (String word : text.split(" ")) {
            String attempt = line.isEmpty() ? word : line + " " + word;
            if (metrics.stringWidth(attempt) <= maxWidth || line.isEmpty()) {
                line = attempt;
            } else {
                lines.add(line);
                line = word;
            }
        }
        lines.add(line);
        while (lines.size() > maxLines) {
            String last = lines.remove(lines.size() - 1);
            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + " " + last);
        }
        lines.set(lines.size() - 1, fit(lines.get(lines.size() - 1), metrics, maxWidth));
        return lines;
    }

    //The game's progress bar, filled to the part of the song played, with the time played on the left and the length on the right
    //While the length is unknown or the song has not started, the bar is empty and the times show --:--
    private void drawProgress (Graphics2D g2d, int left, int bodyBottom, int width) {
        AudioManager audio = AudioManager.getInstance();
        double total = previewEnded || previewTrack == null ? lastTotalSeconds : audio.getPreviewTotalSeconds();
        if (previewTrack != null && !previewEnded && isKnownLength(total)) {
            lastTotalSeconds = total;
        }
        boolean lengthKnown = isKnownLength(total);
        double played = previewTrack == null ? 0 : (previewEnded ? total : audio.getPreviewSeconds());
        boolean started = previewTrack != null && (previewEnded || played > 0);
        float fraction = lengthKnown && started ? (float) Math.min(1.0, played / total) : 0f;

        ensureBarImages(width);
        int barTop = bodyBottom - scaled(26) - barImage.getHeight();
        g2d.drawImage(barImage, left, barTop, null);
        float sourceScale = (float) barImage.getWidth() / ImageDatabase.getInstance().getImage(ImageEnums.ProgressBar).getWidth();
        int sourceWidth = Math.round(fillingSource.getWidth() * fraction);
        if (sourceWidth > 0) {
            if (sourceWidth != fillingShownSourceWidth) {
                BufferedImage cut = fillingSource.getSubimage(0, 0, sourceWidth, fillingSource.getHeight());
                fillingShown = Picture.scaled(cut, Math.max(1, Math.round(sourceWidth * sourceScale)), Math.max(1, Math.round(fillingSource.getHeight() * sourceScale)));
                fillingShownSourceWidth = sourceWidth;
            }
            int fillingTop = barTop + (barImage.getHeight() - fillingShown.getHeight()) / 2;
            g2d.drawImage(fillingShown, left + Math.round(FILLING_OFFSET * sourceScale), fillingTop, null);
        }

        String timeText = started ? formatTime(played) : "--:--";
        String lengthText = lengthKnown ? formatTime(total) : "--:--";
        g2d.setFont(font(Font.PLAIN, 13));
        g2d.setColor(TEAL_SOFT);
        FontMetrics metrics = g2d.getFontMetrics();
        int textMiddle = barTop + barImage.getHeight() + scaled(14);
        drawText(g2d, timeText, left, textMiddle);
        drawText(g2d, lengthText, left + width - metrics.stringWidth(lengthText), textMiddle);
    }

    //The bar art scaled to the width of the details column
    private void ensureBarImages (int width) {
        if (barImage != null) {
            return;
        }
        BufferedImage source = ImageDatabase.getInstance().getImage(ImageEnums.ProgressBar);
        float factor = (float) width / source.getWidth();
        barImage = Picture.scaled(source, width, Math.round(source.getHeight() * factor));
        fillingSource = ImageDatabase.getInstance().getImage(ImageEnums.ProgressBarFilling);
    }
}
