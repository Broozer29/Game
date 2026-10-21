package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.controllerInput.InputDevice;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemClassAvailability;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemTypeEnums;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfile;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.settings.SettingsManager;
import net.riezebos.bruus.tbd.guiboards.boards.ShopFreezeEffect;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.DescriptionTextQuality;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

//The SHOPKEEPER page shown over the main menu: a wide card with a bar on top (item counter card, active filters, button hint cards),
//a box with the icon grid on the left and a box with the details of the item under the cursor on the right
//The texts are base values, so nothing on it depends on a run
//All sizes below are in unscaled pixels and get multiplied by the resolution factor
public class ShopkeeperWindow {

    private static final int CARD_WIDTH = 1076;
    private static final int CARD_HEIGHT = 834;
    private static final int PADDING = 88; //From the left and right edge of the card to the bar and the two boxes; the frame of the card image is about 42 wide, so the boxes keep a gap of about 46 inside it
    private static final int BOTTOM_PADDING = 96; //From the bottom of the card to the bottom of the boxes; larger than the sides so the boxes stay clear of the rounded bottom corners of the card
    private static final int BAR_TOP = 88; //From the top of the card to the bar with the counter, the chips and the button hints
    private static final int BAR_HEIGHT = 50; //The height of the shop's wide button cards
    private static final int BOXES_TOP = 172; //From the top of the card to the top of the grid box and the details box
    private static final int BOX_GAP = 16; //Between the grid box and the details box
    private static final int BOX_PADDING = 22; //Inside the grid box; more than the frost around the selected icon reaches, so it stays inside the box border
    private static final int GRID_CARD_SOURCE_WIDTH = 695; //Card X5; the details card is cut with the same scale so both borders are equally thin
    private static final int DETAILS_CARD_SOURCE_WIDTH = 405; //Card X1
    private static final int CARD_BORDER_SOURCE = 20; //The glowing border of both card images, in pixels of the source image
    private static final int COLUMNS = 6;
    private static final int TILE_SIZE = 58;
    private static final int TILE_GAP = 14; //More than the frost around the selected icon reaches, so it stays off the neighbours
    private static final int FROST_PADDING = 3; //From the selected icon to its frost, the same as around a frozen shop item
    private static final int DETAIL_PADDING = 20;
    private static final int DETAIL_ICON_SIZE = 80;
    private static final int CHIP_HEIGHT = 24;
    private static final int COUNT_CARD_WIDTH = 160; //The wide cards of the bar, in the style of the shop's MAIN MENU button
    private static final int FILTERS_CARD_WIDTH = 170;
    private static final int CLEAR_CARD_WIDTH = 150;
    private static final int CARD_GAP = 12;
    private static final int WINDOW_MARGIN = 20; //The card never gets closer than this to the edge of the window
    private static final int FILTER_ROW_HEIGHT = 34;

    private static final Color INSET_FILL = new Color(12, 43, 43);
    private static final Color BOX_BORDER = new Color(29, 106, 100);
    private static final Color ACCENT = new Color(95, 242, 222);
    private static final Color MUTED = new Color(143, 177, 173);
    private static final Color CLASS_CHIP_TEXT = new Color(247, 201, 72);
    private static final Color CLASS_CHIP_BORDER = new Color(122, 100, 32);
    private static final Color CLASS_CHIP_FILL = new Color(42, 36, 16);
    private static final Color TYPE_CHIP_FILL = new Color(11, 47, 46);

    //The toggles of the filter pane, in the order the cursor moves through them: the class column, the type column, the rarity column, then the clear row
    private static final PlayerClass[] FILTER_CLASSES = {PlayerClass.Captain, PlayerClass.FireFighter, PlayerClass.Carrier};
    private static final ItemTypeEnums[] FILTER_TYPES = ItemTypeEnums.values();
    private static final ItemRarityEnums[] FILTER_RARITIES = {ItemRarityEnums.Common, ItemRarityEnums.Rare, ItemRarityEnums.Legendary, ItemRarityEnums.Relic};
    private static final int FIRST_TYPE_ROW = FILTER_CLASSES.length;
    private static final int FIRST_RARITY_ROW = FIRST_TYPE_ROW + FILTER_TYPES.length;
    private static final int CLEAR_ROW = FIRST_RARITY_ROW + FILTER_RARITIES.length;
    private static final int[] COLUMN_START = {0, FIRST_TYPE_ROW, FIRST_RARITY_ROW, CLEAR_ROW}; //The first row of each column, and the clear row at the end
    private static final String[] COLUMN_HEADERS = {"CLASS", "TYPE", "RARITY"};

    private static ShopkeeperWindow instance; //Only one page is shown at a time, so the buttons reach the one that is shown here

    private float resolutionFactor;
    private ResizableCard card;
    private ResizableCard gridCard; //The grid box and the details box, cut from Card X5 and Card X1
    private ResizableCard detailsCard;
    private List<ItemEnums> allItems; //Every enabled item; the ones not bought or picked yet are drawn locked
    private List<ItemEnums> items; //The ones that pass the filters, which the grid shows
    private final ShopkeeperFilterState filters = new ShopkeeperFilterState(); //Loaded from the saved settings when the page opens, and saved at once after every change
    private boolean filterPaneOpen = false;
    private int filterCursor = 0; //The row of the filter pane under the cursor
    private int visibleRows;
    private int selectedIndex = 0;
    private int firstVisibleRow = 0;
    private boolean open = false;
    private DisplayOnly countCard; //The wide cards of the bar: the counter on the left, the two button hints on the right
    private DisplayOnly filtersCard;
    private DisplayOnly clearCard;
    private GUITextCollection countText; //The texts on the cards, in menu letters at the scale of the shop's buttons
    private GUITextCollection filtersText;
    private GUITextCollection clearText;
    private String countShown; //The texts the collections were built from
    private String filtersShown;
    private String clearShown;
    private Runnable onClosed; //What the screen does after the page closed

    public static ShopkeeperWindow getInstance () {
        if (instance == null) {
            instance = new ShopkeeperWindow();
        }
        return instance;
    }

    private ShopkeeperWindow () {
    }

    //The items the page lists: the ones that can be bought or found, which leaves out the disabled ones and the Locked placeholder
    public static boolean isShownInShopkeeper (ItemEnums item) {
        return item.isEnabled() && item.getItemRarity() != ItemRarityEnums.Locked;
    }

    //Whether the player has bought or picked the item (the profile lists it), or showFullArmory shows everything; the grid and the details draw an item that is not discovered locked
    public static boolean isDiscovered (ItemEnums item) {
        if (DevTestSettings.showFullArmory) {
            return true;
        }
        PlayerProfile profile = PlayerProfileManager.getInstance().getLoadedProfile();
        return profile != null && profile.getDiscoveredItems().contains(item.name());
    }

    private int scaled (int size) {
        return Math.round(size * resolutionFactor);
    }

    private void build () {
        DataClass data = DataClass.getInstance();
        resolutionFactor = Math.min(data.getResolutionFactor(), DataClass.maxResolutionFactor);
        float fitWidth = (float) (data.getWindowWidth() - 2 * WINDOW_MARGIN) / CARD_WIDTH;
        float fitHeight = (float) (data.getWindowHeight() - 2 * WINDOW_MARGIN) / CARD_HEIGHT;
        resolutionFactor = Math.min(resolutionFactor, Math.min(fitWidth, fitHeight)); //A small window shrinks the page instead of cutting it off

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(cardLeft());
        spriteConfiguration.setyCoordinate(cardTop());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);
        card = new ResizableCard(spriteConfiguration, scaled(CARD_WIDTH), scaled(CARD_HEIGHT));

        int gridWidth = gridBoxWidth();
        SpriteConfiguration gridConfiguration = new SpriteConfiguration();
        gridConfiguration.setxCoordinate(0);
        gridConfiguration.setyCoordinate(0);
        gridConfiguration.setScale(1);
        gridConfiguration.setImageType(ImageEnums.Card_X5);
        gridCard = new ResizableCard(gridConfiguration, gridWidth, boxHeight());
        SpriteConfiguration detailsConfiguration = new SpriteConfiguration();
        detailsConfiguration.setxCoordinate(0);
        detailsConfiguration.setyCoordinate(0);
        detailsConfiguration.setScale(1);
        detailsConfiguration.setImageType(ImageEnums.Card_X1);
        int detailsReferenceWidth = Math.round((float) gridWidth * DETAILS_CARD_SOURCE_WIDTH / GRID_CARD_SOURCE_WIDTH);
        detailsCard = new ResizableCard(detailsConfiguration, detailsBoxWidth(), boxHeight(), detailsReferenceWidth);

        allItems = new ArrayList<>();
        for (ItemEnums item : ItemEnums.values()) {
            if (isShownInShopkeeper(item)) {
                allItems.add(item);
            }
        }
        allItems.sort(Comparator.comparing(ItemEnums::getItemRarity).thenComparing(ItemEnums::getItemName, String.CASE_INSENSITIVE_ORDER));
        items = filteredItems();
        countCard = createBarCard(cardLeft() + scaled(PADDING), COUNT_CARD_WIDTH);
        clearCard = createBarCard(cardLeft() + scaled(CARD_WIDTH) - scaled(PADDING) - scaled(CLEAR_CARD_WIDTH), CLEAR_CARD_WIDTH);
        filtersCard = createBarCard(clearCard.getXCoordinate() - scaled(CARD_GAP) - scaled(FILTERS_CARD_WIDTH), FILTERS_CARD_WIDTH);
        countShown = null;
        filtersShown = null;
        clearShown = null;

        visibleRows = Math.max(1, (boxHeight() - 2 * scaled(BOX_PADDING) + scaled(TILE_GAP)) / (scaled(TILE_SIZE) + scaled(TILE_GAP)));
    }

    private int cardLeft () {
        return (DataClass.getInstance().getWindowWidth() - scaled(CARD_WIDTH)) / 2;
    }

    private int cardTop () {
        return (DataClass.getInstance().getWindowHeight() - scaled(CARD_HEIGHT)) / 2;
    }

    private int boxHeight () {
        return scaled(CARD_HEIGHT - BOXES_TOP - BOTTOM_PADDING);
    }

    private int detailsBoxWidth () {
        return scaled(CARD_WIDTH) - 2 * scaled(PADDING) - gridBoxWidth() - scaled(BOX_GAP);
    }

    private int gridBoxWidth () {
        return 2 * scaled(BOX_PADDING) + COLUMNS * scaled(TILE_SIZE) + (COLUMNS - 1) * scaled(TILE_GAP);
    }

    //Shows the page with the first item selected; onClosed says what the screen does once the page is closed
    public void open (Runnable onClosed) {
        this.onClosed = onClosed;
        filters.load(SettingsManager.getInstance().getSavedSettings().getShopkeeperFilters());
        build(); //Built again, so it follows the size of the window
        selectedIndex = 0;
        firstVisibleRow = 0;
        filterPaneOpen = false;
        open = true;
        MenuInput.getInstance().startScreen();
    }

    public boolean isOpen () {
        return open;
    }

    //Hides the page without telling the screen, for when the screen itself pauses, resumes or changes
    public void closeSilently () {
        open = false;
        onClosed = null; //A screen's close step only belongs to the open it came with
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
        for (MenuAction action : actions) {
            if (action == MenuAction.OPEN_FILTERS) {
                filterPaneOpen = !filterPaneOpen;
                filterCursor = 0; //The pane opens on its first toggle
            } else if (action == MenuAction.CLEAR_FILTERS) {
                clearFilters();
            } else if (filterPaneOpen) {
                handleFilterPaneAction(action);
            } else if (items.isEmpty() && action != MenuAction.BACK) {
                //Nothing to move over while no item passes the filters
            } else if (action == MenuAction.LEFT) {
                selectItem((selectedIndex - 1 + items.size()) % items.size());
            } else if (action == MenuAction.RIGHT) {
                selectItem((selectedIndex + 1) % items.size());
            } else if (action == MenuAction.UP) {
                if (selectedIndex - COLUMNS >= 0) {
                    selectItem(selectedIndex - COLUMNS);
                }
            } else if (action == MenuAction.DOWN) {
                if (selectedIndex + COLUMNS < items.size()) {
                    selectItem(selectedIndex + COLUMNS);
                } else if (selectedIndex / COLUMNS < (items.size() - 1) / COLUMNS) {
                    selectItem(items.size() - 1); //The last row is shorter than the column of the cursor
                }
            } else if (action == MenuAction.CONFIRM) {
                //Does nothing yet; this is the one place where the selected item could be acted on
            } else if (action == MenuAction.BACK) {
                close();
                return;
            }
        }
    }

    //With the pane open, left and right move between the columns (keeping the row, or the last row of a shorter column), up and down move within a column,
    //confirm switches one, and back closes the pane and leaves the page open. Down from the last toggle of a column goes to the clear row, up from it goes back to the first column
    private void handleFilterPaneAction (MenuAction action) {
        int column = columnOf(filterCursor);
        if (action == MenuAction.UP) {
            if (filterCursor == CLEAR_ROW) {
                filterCursor = FIRST_TYPE_ROW - 1;
            } else if (filterCursor > COLUMN_START[column]) {
                filterCursor--;
            }
        } else if (action == MenuAction.DOWN) {
            if (filterCursor != CLEAR_ROW) {
                filterCursor = filterCursor == COLUMN_START[column + 1] - 1 ? CLEAR_ROW : filterCursor + 1;
            }
        } else if (action == MenuAction.LEFT) {
            if (filterCursor != CLEAR_ROW && column > 0) {
                moveToColumn(column - 1, filterCursor - COLUMN_START[column]);
            }
        } else if (action == MenuAction.RIGHT) {
            if (filterCursor != CLEAR_ROW && column < COLUMN_HEADERS.length - 1) {
                moveToColumn(column + 1, filterCursor - COLUMN_START[column]);
            }
        } else if (action == MenuAction.CONFIRM) {
            if (filterCursor == CLEAR_ROW) {
                clearFilters();
            } else {
                flipToggle(filterCursor);
                applyFilters();
                saveFilters();
            }
        } else if (action == MenuAction.BACK) {
            filterPaneOpen = false;
        }
    }

    //The column a row belongs to; the clear row is column 3
    private int columnOf (int row) {
        int column = 0;
        while (column < COLUMN_START.length - 1 && row >= COLUMN_START[column + 1]) {
            column++;
        }
        return column;
    }

    private void moveToColumn (int column, int row) {
        int length = COLUMN_START[column + 1] - COLUMN_START[column];
        filterCursor = COLUMN_START[column] + Math.min(row, length - 1);
    }

    private void clearFilters () {
        filters.clear();
        applyFilters();
        saveFilters();
    }

    private void saveFilters () {
        SettingsManager.getInstance().saveShopkeeperFilters(filters.toSaved());
    }

    //Toggles in one group add up, and the groups narrow each other; a group with nothing switched on lets every item through
    //A class toggle matches every item whose class group contains that class
    private static boolean matchesFilters (ItemEnums item, ShopkeeperFilterState filterState) {
        boolean classMatches = filterState.getClasses().isEmpty()
                || item.getClassAvailability().getPlayerClasses().stream().anyMatch(filterState.getClasses()::contains);
        boolean rarityMatches = filterState.getRarities().isEmpty() || filterState.getRarities().contains(item.getItemRarity());
        boolean typeMatches = filterState.getTypes().isEmpty() || item.getItemTypes().stream().anyMatch(filterState.getTypes()::contains);
        return classMatches && rarityMatches && typeMatches;
    }

    private List<ItemEnums> filteredItems () {
        List<ItemEnums> result = new ArrayList<>();
        for (ItemEnums item : allItems) {
            if (matchesFilters(item, filters)) {
                result.add(item);
            }
        }
        return result;
    }

    //Filters the grid again after a change, and keeps the cursor on the same item when that item still passes
    private void applyFilters () {
        ItemEnums selectedItem = items.isEmpty() ? null : items.get(selectedIndex);
        items = filteredItems();
        int newIndex = Math.max(0, items.indexOf(selectedItem)); //The first item when the selected one dropped out
        int lastFirstRow = items.isEmpty() ? 0 : Math.max(0, (items.size() - 1) / COLUMNS - visibleRows + 1);
        firstVisibleRow = Math.min(firstVisibleRow, lastFirstRow);
        selectItem(newIndex);
    }

    private boolean isToggleOn (int row) {
        if (row < FIRST_TYPE_ROW) {
            return filters.getClasses().contains(FILTER_CLASSES[row]);
        } else if (row < FIRST_RARITY_ROW) {
            return filters.getTypes().contains(FILTER_TYPES[row - FIRST_TYPE_ROW]);
        }
        return filters.getRarities().contains(FILTER_RARITIES[row - FIRST_RARITY_ROW]);
    }

    private void flipToggle (int row) {
        boolean on = isToggleOn(row);
        if (row < FIRST_TYPE_ROW) {
            flip(filters.getClasses(), FILTER_CLASSES[row], on);
        } else if (row < FIRST_RARITY_ROW) {
            flip(filters.getTypes(), FILTER_TYPES[row - FIRST_TYPE_ROW], on);
        } else {
            flip(filters.getRarities(), FILTER_RARITIES[row - FIRST_RARITY_ROW], on);
        }
    }

    private <T> void flip (Set<T> set, T value, boolean on) {
        if (on) {
            set.remove(value);
        } else {
            set.add(value);
        }
    }

    private String toggleLabel (int row) {
        if (row < FIRST_TYPE_ROW) {
            return FILTER_CLASSES[row] == PlayerClass.FireFighter ? "Firefighter" : FILTER_CLASSES[row].toString();
        } else if (row < FIRST_RARITY_ROW) {
            return FILTER_TYPES[row - FIRST_TYPE_ROW].toString();
        }
        return FILTER_RARITIES[row - FIRST_RARITY_ROW].toString();
    }

    //Moves the cursor to an item and scrolls the grid by whole rows until that item's row is shown
    private void selectItem (int index) {
        selectedIndex = index;
        int row = selectedIndex / COLUMNS;
        if (row < firstVisibleRow) {
            firstVisibleRow = row;
        } else if (row >= firstVisibleRow + visibleRows) {
            firstVisibleRow = row - visibleRows + 1;
        }
    }

    //A wide card like the shop's MAIN MENU button, in the bar
    private DisplayOnly createBarCard (int x, int unscaledWidth) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(x);
        spriteConfiguration.setyCoordinate(cardTop() + scaled(BAR_TOP));
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Wide_Card);
        DisplayOnly displayOnly = new DisplayOnly(spriteConfiguration);
        displayOnly.setImageDimensions(scaled(unscaledWidth), scaled(BAR_HEIGHT));
        return displayOnly;
    }

    //Menu letters centred on a card, at the scale of the shop's buttons
    private GUITextCollection createCardText (String text, DisplayOnly cardComponent) {
        return MenuText.createText(text, cardComponent.getCenterXCoordinate(), cardComponent.getCenterYCoordinate(), resolutionFactor, MenuText.ALIGN_CENTER);
    }

    private boolean isFiltering () {
        for (int row = 0; row < CLEAR_ROW; row++) {
            if (isToggleOn(row)) {
                return true;
            }
        }
        return false;
    }

    //Builds the texts again when they changed: the counter, and the buttons of the device used last with Close instead of Filters while the pane is open
    private void refreshBarTexts () {
        String count = isFiltering() ? items.size() + " OF " + allItems.size() : allItems.size() + " ITEMS";
        if (!count.equals(countShown)) {
            countShown = count;
            countText = createCardText(count, countCard);
        }
        boolean keyboard = MenuInput.getInstance().getLastDevice() == InputDevice.KEYBOARD;
        String filtersLine = (keyboard ? "F " : "X ") + (filterPaneOpen ? "CLOSE" : "FILTERS");
        if (!filtersLine.equals(filtersShown)) {
            filtersShown = filtersLine;
            filtersText = createCardText(filtersLine, filtersCard);
        }
        String clearLine = keyboard ? "C CLEAR" : "Y CLEAR";
        if (!clearLine.equals(clearShown)) {
            clearShown = clearLine;
            clearText = createCardText(clearLine, clearCard);
        }
    }

    //The page card and the three bar cards with their texts, drawn like every other component; the chips, the grid box and the details box go over them in draw
    public List<GUIComponent> getComponents () {
        refreshBarTexts();
        List<GUIComponent> components = new ArrayList<>();
        components.add(card);
        components.add(countCard);
        components.addAll(countText.getComponents());
        components.add(filtersCard);
        components.addAll(filtersText.getComponents());
        components.add(clearCard);
        components.addAll(clearText.getComponents());
        return components;
    }

    //The bar, the grid box with the frosted frame around the selected icon, and the details box (or the filter pane) over the card
    public void draw (Graphics2D g2d) {
        Object oldInterpolation = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        Stroke oldStroke = g2d.getStroke();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        DescriptionTextQuality quality = DescriptionTextQuality.apply(g2d);

        drawBar(g2d);
        drawGridBox(g2d);
        int detailLeft = cardLeft() + scaled(PADDING) + gridBoxWidth() + scaled(BOX_GAP);
        int detailWidth = detailsBoxWidth();
        int boxTop = cardTop() + scaled(BOXES_TOP);
        g2d.drawImage(detailsCard.getImage(), detailLeft, boxTop, null);
        if (filterPaneOpen) {
            drawFilterPane(g2d, detailLeft, boxTop, detailWidth);
        } else if (!items.isEmpty()) {
            drawDetails(g2d, items.get(selectedIndex), detailLeft, boxTop, detailWidth, boxHeight());
        }

        g2d.setStroke(oldStroke);
        quality.restore(g2d);
        if (oldInterpolation != null) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
        }
    }

    private Font font (int style, int size) {
        return new Font(DataClass.getInstance().getTextFont(), style, scaled(size));
    }

    //Draws the text with its vertical middle at the middle of the given band
    private void drawCenteredText (Graphics2D g2d, String text, int x, int bandTop, int bandHeight, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.setColor(color);
        g2d.drawString(text, x, bandTop + (bandHeight - metrics.getHeight()) / 2 + metrics.getAscent());
    }

    //The active filters as chips between the counter card and the button cards; the cards and their texts are components
    private void drawBar (Graphics2D g2d) {
        refreshBarTexts();
        int barTop = cardTop() + scaled(BAR_TOP);
        int barHeight = scaled(BAR_HEIGHT);
        int chipX = countCard.getXCoordinate() + countCard.getWidth() + scaled(CARD_GAP);
        int chipsRight = filtersCard.getXCoordinate() - scaled(CARD_GAP);
        int chipHeight = scaled(CHIP_HEIGHT);
        int chipTop = barTop + (barHeight - chipHeight) / 2;
        g2d.setFont(font(Font.PLAIN, 14));
        for (int row = 0; row < CLEAR_ROW; row++) {
            if (!isToggleOn(row)) {
                continue;
            }
            int width = chipWidth(g2d, toggleLabel(row));
            if (chipX + width > chipsRight) {
                break; //No room left before the button cards
            }
            drawChip(g2d, toggleLabel(row), chipX, chipTop, width, row < FIRST_TYPE_ROW);
            chipX += width + scaled(8);
        }
    }

    //The width of a chip with this text, in the font that is set
    private int chipWidth (Graphics2D g2d, String text) {
        return g2d.getFontMetrics().stringWidth(text) + scaled(16);
    }

    //A small rounded chip; class chips are yellow and the others teal
    private void drawChip (Graphics2D g2d, String text, int x, int y, int width, boolean classChip) {
        int height = scaled(CHIP_HEIGHT);
        g2d.setColor(classChip ? CLASS_CHIP_FILL : TYPE_CHIP_FILL);
        g2d.fillRoundRect(x, y, width, height, height, height);
        g2d.setColor(classChip ? CLASS_CHIP_BORDER : BOX_BORDER);
        g2d.setStroke(new BasicStroke(Math.max(1, scaled(1))));
        g2d.drawRoundRect(x, y, width, height, height, height);
        drawCenteredText(g2d, text, x + scaled(8), y, height, classChip ? CLASS_CHIP_TEXT : ACCENT);
    }

    //The grid in its own box: the icons, and the frost of a frozen shop item around the selected one, in the rarity colour of that item
    private void drawGridBox (Graphics2D g2d) {
        int boxLeft = cardLeft() + scaled(PADDING);
        int boxTop = cardTop() + scaled(BOXES_TOP);
        g2d.drawImage(gridCard.getImage(), boxLeft, boxTop, null);
        int gridLeft = boxLeft + scaled(BOX_PADDING);
        int gridTop = boxTop + scaled(BOX_PADDING);
        int tile = scaled(TILE_SIZE);
        int step = tile + scaled(TILE_GAP);
        int selectedX = -1;
        int selectedY = 0;
        for (int row = 0; row < visibleRows; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                int index = (firstVisibleRow + row) * COLUMNS + column;
                if (index >= items.size()) {
                    break;
                }
                int x = gridLeft + column * step;
                int y = gridTop + row * step;
                BufferedImage icon = ImageDatabase.getInstance().getImage(isDiscovered(items.get(index)) ? items.get(index).getItemIcon() : ImageEnums.LockedIcon);
                if (icon != null) {
                    g2d.drawImage(icon, x, y, tile, tile, null);
                }
                if (index == selectedIndex) {
                    selectedX = x;
                    selectedY = y;
                }
            }
        }
        if (selectedX >= 0) {
            //Drawn after all icons, so the glow is not covered by the next icon
            //Clipped to the inside of the box border, so the frame never crosses it
            Shape oldClip = g2d.getClip();
            int border = Math.round(CARD_BORDER_SOURCE * (float) gridBoxWidth() / GRID_CARD_SOURCE_WIDTH); //The inner edge of the grid card's border
            g2d.clipRect(boxLeft + border, boxTop + border, gridBoxWidth() - 2 * border, boxHeight() - 2 * border);
            ShopFreezeEffect.getInstance().drawFrozenFrame(g2d, selectedX, selectedY, tile, tile, scaled(FROST_PADDING), items.get(selectedIndex).getItemRarity().getColor());
            g2d.setClip(oldClip);
        }
        if (items.isEmpty()) {
            g2d.setFont(font(Font.BOLD, 22));
            drawCenteredText(g2d, "No items found", gridLeft, boxTop, boxHeight(), Color.WHITE);
        }
    }

    //The three columns side by side in the details box, and the clear row under them
    private void drawFilterPane (Graphics2D g2d, int boxLeft, int boxTop, int boxWidth) {
        int padding = scaled(DETAIL_PADDING);
        int left = boxLeft + padding;
        int top = boxTop + padding;
        int columnGap = scaled(12);
        int columnWidth = (boxWidth - 2 * padding - 2 * columnGap) / COLUMN_HEADERS.length;
        int rowHeight = scaled(FILTER_ROW_HEIGHT);

        g2d.setFont(font(Font.BOLD, 16));
        drawCenteredText(g2d, "FILTERS", left, top, scaled(22), Color.WHITE);
        int rowsTop = top + scaled(22) + scaled(14);
        int longestColumn = 0;

        for (int column = 0; column < COLUMN_HEADERS.length; column++) {
            int x = left + column * (columnWidth + columnGap);
            g2d.setFont(font(Font.BOLD, 13));
            drawCenteredText(g2d, COLUMN_HEADERS[column], x, rowsTop, scaled(20), MUTED);
            int length = COLUMN_START[column + 1] - COLUMN_START[column];
            longestColumn = Math.max(longestColumn, length);
            for (int i = 0; i < length; i++) {
                int row = COLUMN_START[column] + i;
                int y = rowsTop + scaled(24) + i * (rowHeight + scaled(4));
                drawFilterRow(g2d, row, x, y, columnWidth, rowHeight);
            }
        }

        int clearTop = rowsTop + scaled(24) + longestColumn * (rowHeight + scaled(4)) + scaled(10);
        int arc = scaled(10);
        int clearWidth = boxWidth - 2 * padding;
        if (filterCursor == CLEAR_ROW) {
            g2d.setColor(new Color(13, 54, 52));
            g2d.fillRoundRect(left, clearTop, clearWidth, rowHeight, arc, arc);
        }
        g2d.setColor(filterCursor == CLEAR_ROW ? ACCENT : BOX_BORDER);
        g2d.setStroke(new BasicStroke(Math.max(1, scaled(filterCursor == CLEAR_ROW ? 2 : 1))));
        g2d.drawRoundRect(left, clearTop, clearWidth, rowHeight, arc, arc);
        g2d.setFont(font(Font.PLAIN, 15));
        String clearText = "Clear all filters";
        drawCenteredText(g2d, clearText, left + (clearWidth - g2d.getFontMetrics().stringWidth(clearText)) / 2, clearTop, rowHeight,
                filterCursor == CLEAR_ROW ? Color.WHITE : MUTED);
    }

    //One toggle: a checkbox and its name; the row under the cursor gets a teal fill and border
    private void drawFilterRow (Graphics2D g2d, int row, int x, int y, int width, int height) {
        int arc = scaled(10);
        boolean underCursor = row == filterCursor;
        boolean on = isToggleOn(row);
        if (underCursor) {
            g2d.setColor(new Color(13, 54, 52));
            g2d.fillRoundRect(x, y, width, height, arc, arc);
            g2d.setColor(ACCENT);
            g2d.setStroke(new BasicStroke(Math.max(1, scaled(2))));
            g2d.drawRoundRect(x, y, width, height, arc, arc);
        }
        int box = scaled(16);
        int boxX = x + scaled(8);
        int boxY = y + (height - box) / 2;
        if (on) {
            g2d.setColor(ACCENT);
            g2d.fillRoundRect(boxX, boxY, box, box, scaled(4), scaled(4));
            g2d.setColor(new Color(4, 34, 24));
            g2d.setStroke(new BasicStroke(Math.max(1, scaled(2))));
            g2d.drawLine(boxX + box / 5, boxY + box / 2, boxX + box * 2 /5, boxY + box * 7 / 10);
            g2d.drawLine(boxX + box * 2 / 5, boxY + box * 7 / 10, boxX + box * 4 / 5, boxY + box * 3 / 10);
        } else {
            g2d.setColor(BOX_BORDER);
            g2d.setStroke(new BasicStroke(Math.max(1, scaled(2))));
            g2d.drawRoundRect(boxX, boxY, box, box, scaled(4), scaled(4));
        }
        g2d.setFont(font(Font.PLAIN, 14));
        drawCenteredText(g2d, toggleLabel(row), boxX + box + scaled(6), y, height, on ? ACCENT : Color.WHITE);
    }

    //An item that is not discovered shows only the lock icon, "???" and the rarity badge
    //The header (icon, name in its rarity colour, rarity badge), the rows CLASS, TYPE and PRICE, and the description in an inset box that stays inside the details box
    private void drawDetails (Graphics2D g2d, ItemEnums item, int boxLeft, int boxTop, int boxWidth, int boxHeight) {
        int padding = scaled(DETAIL_PADDING);
        int left = boxLeft + padding;
        int right = boxLeft + boxWidth - padding;
        int top = boxTop + padding;
        Color rarityColor = item.getItemRarity().getColor();
        boolean discovered = isDiscovered(item);

        int iconSize = scaled(DETAIL_ICON_SIZE);
        BufferedImage icon = ImageDatabase.getInstance().getImage(discovered ? item.getItemIcon() : ImageEnums.LockedIcon);
        if (icon != null) {
            g2d.drawImage(icon, left, top, iconSize, iconSize, null);
        }
        int textX = left + iconSize + scaled(16);
        g2d.setFont(font(Font.BOLD, 26));
        FontMetrics nameMetrics = g2d.getFontMetrics();
        List<String> nameLines = wrapLines(discovered ? item.getItemName() : "???", nameMetrics, right - textX);
        int badgeHeight = scaled(22);
        int textHeight = nameLines.size() * nameMetrics.getHeight() + scaled(8) + badgeHeight;
        int y = top + Math.max(0, (iconSize - textHeight) / 2) + nameMetrics.getAscent();
        g2d.setColor(rarityColor);
        for (String line : nameLines) {
            g2d.drawString(line, textX, y);
            y += nameMetrics.getHeight();
        }
        //The rarity badge: filled with the rarity colour
        g2d.setFont(font(Font.BOLD, 13));
        String rarity = item.getItemRarity().toString().toUpperCase();
        int badgeWidth = g2d.getFontMetrics().stringWidth(rarity) + scaled(20);
        int badgeTop = y - nameMetrics.getAscent() + scaled(8);
        g2d.setColor(rarityColor);
        g2d.fillRoundRect(textX, badgeTop, badgeWidth, badgeHeight, scaled(8), scaled(8));
        drawCenteredText(g2d, rarity, textX + scaled(10), badgeTop, badgeHeight, new Color(5, 21, 20));
        if (!discovered) {
            return;
        }

        //The labelled rows
        int rowsTop = Math.max(top + iconSize, badgeTop + badgeHeight) + scaled(20);
        int labelWidth = scaled(70);
        int chipsX = left + labelWidth;
        int chipHeight = scaled(CHIP_HEIGHT);
        int rowGap = scaled(10);
        g2d.setFont(font(Font.PLAIN, 14));
        int rowsBottom = drawLabelledChips(g2d, "CLASS", classNames(item.getClassAvailability()), true, left, chipsX, right, rowsTop);
        rowsBottom = drawLabelledChips(g2d, "TYPE", typeNames(item.getItemTypes()), false, left, chipsX, right, rowsBottom + rowGap);
        int priceTop = rowsBottom + rowGap;
        g2d.setFont(font(Font.BOLD, 13));
        drawCenteredText(g2d, "PRICE", left, priceTop, chipHeight, MUTED);
        //The money icon of the shop
        BufferedImage gemIcon = ImageDatabase.getInstance().getImage(ImageEnums.TopazGem7);
        int gemHeight = scaled(22);
        int gem = gemIcon == null ? 0 : Math.round(gemHeight * (float) gemIcon.getWidth() / gemIcon.getHeight());
        if (gemIcon != null) {
            g2d.drawImage(gemIcon, chipsX, priceTop + (chipHeight - gemHeight) / 2, gem, gemHeight, null);
        }
        g2d.setFont(font(Font.BOLD, 20));
        drawCenteredText(g2d, String.valueOf(Math.round(item.getItemRarity().getItemCost())), chipsX + gem + scaled(8), priceTop, chipHeight, Color.WHITE);

        //The description in its own inset box, from under the rows to the bottom of the details box
        int insetTop = priceTop + chipHeight + scaled(18);
        int insetHeight = boxTop + boxHeight - padding - insetTop;
        if (insetHeight <= 0) {
            return;
        }
        int arc = scaled(12);
        g2d.setColor(INSET_FILL);
        g2d.fillRoundRect(left, insetTop, right - left, insetHeight, arc, arc);
        g2d.setColor(BOX_BORDER);
        g2d.setStroke(new BasicStroke(Math.max(1, scaled(1))));
        g2d.drawRoundRect(left, insetTop, right - left, insetHeight, arc, arc);

        int innerPadding = scaled(14);
        int textWidth = right - left - 2 * innerPadding;
        int textHeightAvailable = insetHeight - 2 * innerPadding;
        String description = ItemDescriptionRetriever.getDescriptionOfItem(item, true);
        //Starts at the normal size and shrinks until the whole text fits inside the box
        List<String> lines = null;
        FontMetrics metrics = null;
        for (int size = 18; size >= 10; size--) {
            g2d.setFont(font(Font.PLAIN, size));
            metrics = g2d.getFontMetrics();
            lines = wrapLines(description, metrics, textWidth);
            if (lines.size() * (metrics.getHeight() + 2) <= textHeightAvailable) {
                break;
            }
        }
        Shape oldClip = g2d.getClip();
        g2d.clipRect(left + scaled(2), insetTop + scaled(2), right - left - scaled(4), insetHeight - scaled(4)); //Whatever still does not fit at the smallest size is cut off
        g2d.setColor(Color.WHITE);
        int lineY = insetTop + innerPadding + metrics.getAscent();
        for (String line : lines) {
            g2d.drawString(line, left + innerPadding, lineY);
            lineY += metrics.getHeight() + 2;
        }
        g2d.setClip(oldClip);
    }

    //A label on the left and its values as chips to the right of it, wrapping to a next line of chips when they do not fit; returns the bottom of the last chip row
    private int drawLabelledChips (Graphics2D g2d, String label, List<String> values, boolean classChips, int labelX, int chipsX, int right, int top) {
        int chipHeight = scaled(CHIP_HEIGHT);
        g2d.setFont(font(Font.BOLD, 13));
        drawCenteredText(g2d, label, labelX, top, chipHeight, MUTED);
        g2d.setFont(font(Font.PLAIN, 14));
        int x = chipsX;
        int y = top;
        for (String value : values) {
            int width = chipWidth(g2d, value);
            if (x > chipsX && x + width > right) {
                x = chipsX;
                y += chipHeight + scaled(6);
            }
            drawChip(g2d, value, x, y, width, classChips);
            x += width + scaled(8);
        }
        return y + chipHeight;
    }

    private List<String> classNames (ItemClassAvailability classAvailability) {
        List<String> names = new ArrayList<>();
        if (classAvailability == ItemClassAvailability.All) {
            names.add("All classes");
            return names;
        }
        classAvailability.getPlayerClasses().forEach(playerClass -> names.add(playerClass == PlayerClass.FireFighter ? "Firefighter" : playerClass.toString()));
        return names;
    }

    private List<String> typeNames (List<ItemTypeEnums> types) {
        List<String> names = new ArrayList<>();
        types.forEach(type -> names.add(type.toString()));
        return names;
    }

    //Splits the text into lines no wider than maxWidth; a line break in the text starts a new line
    private List<String> wrapLines (String text, FontMetrics metrics, int maxWidth) {
        List<String> lines = new ArrayList<>();
        for (String paragraph : text.split("\n", -1)) {
            StringBuilder line = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                //If adding the new word exceeds the maximum line width, finish the line and start a new one
                if (line.length() > 0 && metrics.stringWidth(line + " " + word) > maxWidth) {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    line.append(line.length() > 0 ? " " : "").append(word);
                }
            }
            lines.add(line.toString());
        }
        return lines;
    }
}
