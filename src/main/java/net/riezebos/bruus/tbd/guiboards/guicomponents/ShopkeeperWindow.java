package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemClassAvailability;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemTypeEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

//The SHOPKEEPER page shown over the main menu: a wide card with a grid of the icon of every item that is shown, and under it the details of the item under the cursor
//The texts are base values, so nothing on it depends on a run
//All sizes below are in unscaled pixels and get multiplied by the resolution factor
public class ShopkeeperWindow {

    private static final int CARD_WIDTH = 1000;
    private static final int CARD_HEIGHT = 670;
    private static final int PADDING = 35; //From the edge of the card to the grid and the details
    private static final int TILE_SIZE = 64;
    private static final int TILE_GAP = 8;
    private static final int GRID_ROWS = 4; //Rows of the grid that are shown at once
    private static final int DETAIL_GAP = 25; //From the bottom of the grid to the top of the details
    private static final int DETAIL_ICON_SIZE = 96;
    private static final int DETAIL_TEXT_GAP = 20; //From the detail icon to its texts
    private static final int SELECTION_THICKNESS = 3;
    private static final int WINDOW_MARGIN = 20; //The card never gets closer than this to the edge of the window
    private static final int FILTER_BAR_HEIGHT = 30; //The line above the grid with the active filters and the number of items
    private static final int FILTER_ROW_HEIGHT = 30;

    //The toggles of the filter pane, in the order the cursor moves through them: the class group, the rarity group, the type group, then the clear row
    private static final PlayerClass[] FILTER_CLASSES = {PlayerClass.Captain, PlayerClass.FireFighter, PlayerClass.Carrier};
    private static final ItemRarityEnums[] FILTER_RARITIES = {ItemRarityEnums.Common, ItemRarityEnums.Rare, ItemRarityEnums.Legendary, ItemRarityEnums.Relic};
    private static final ItemTypeEnums[] FILTER_TYPES = ItemTypeEnums.values();
    private static final int FIRST_RARITY_ROW = FILTER_CLASSES.length;
    private static final int FIRST_TYPE_ROW = FIRST_RARITY_ROW + FILTER_RARITIES.length;
    private static final int CLEAR_ROW = FIRST_TYPE_ROW + FILTER_TYPES.length;

    private static ShopkeeperWindow instance; //Only one page is shown at a time, so the buttons reach the one that is shown here

    private float resolutionFactor;
    private ResizableCard card;
    private List<ItemEnums> allItems; //Every item the page lists
    private List<ItemEnums> items; //The ones that pass the filters, which the grid shows
    private final ShopkeeperFilterState filters = new ShopkeeperFilterState(); //Stays when the page closes, so it can be loaded and saved as one object
    private boolean filterPaneOpen = false;
    private int filterCursor = 0; //The row of the filter pane under the cursor
    private int columns;
    private int visibleRows;
    private int selectedIndex = 0;
    private int firstVisibleRow = 0;
    private boolean open = false;
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

        allItems = new ArrayList<>();
        for (ItemEnums item : ItemEnums.values()) {
            if (isShownInShopkeeper(item)) {
                allItems.add(item);
            }
        }
        allItems.sort(Comparator.comparing(ItemEnums::getItemRarity).thenComparing(ItemEnums::getItemName, String.CASE_INSENSITIVE_ORDER));
        items = filteredItems();

        columns = Math.max(1, (scaled(CARD_WIDTH) - 2 * scaled(PADDING) + scaled(TILE_GAP)) / (scaled(TILE_SIZE) + scaled(TILE_GAP)));
        visibleRows = GRID_ROWS;
    }

    private int cardLeft () {
        return (DataClass.getInstance().getWindowWidth() - scaled(CARD_WIDTH)) / 2;
    }

    private int cardTop () {
        return (DataClass.getInstance().getWindowHeight() - scaled(CARD_HEIGHT)) / 2;
    }

    //Shows the page with the first item selected; onClosed says what the screen does once the page is closed
    public void open (Runnable onClosed) {
        this.onClosed = onClosed;
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
                if (selectedIndex - columns >= 0) {
                    selectItem(selectedIndex - columns);
                }
            } else if (action == MenuAction.DOWN) {
                if (selectedIndex + columns < items.size()) {
                    selectItem(selectedIndex + columns);
                } else if (selectedIndex / columns < (items.size() - 1) / columns) {
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

    //With the pane open, up and down move through the toggles, confirm switches one, and back closes the pane and leaves the page open
    private void handleFilterPaneAction (MenuAction action) {
        if (action == MenuAction.UP) {
            filterCursor = Math.max(0, filterCursor - 1);
        } else if (action == MenuAction.DOWN) {
            filterCursor = Math.min(CLEAR_ROW, filterCursor + 1);
        } else if (action == MenuAction.CONFIRM) {
            if (filterCursor == CLEAR_ROW) {
                clearFilters();
            } else {
                flipToggle(filterCursor);
                applyFilters();
            }
        } else if (action == MenuAction.BACK) {
            filterPaneOpen = false;
        }
    }

    private void clearFilters () {
        filters.clear();
        applyFilters();
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
        int lastFirstRow = items.isEmpty() ? 0 : Math.max(0, (items.size() - 1) / columns - visibleRows + 1);
        firstVisibleRow = Math.min(firstVisibleRow, lastFirstRow);
        selectItem(newIndex);
    }

    private boolean isToggleOn (int row) {
        if (row < FIRST_RARITY_ROW) {
            return filters.getClasses().contains(FILTER_CLASSES[row]);
        } else if (row < FIRST_TYPE_ROW) {
            return filters.getRarities().contains(FILTER_RARITIES[row - FIRST_RARITY_ROW]);
        }
        return filters.getTypes().contains(FILTER_TYPES[row - FIRST_TYPE_ROW]);
    }

    private void flipToggle (int row) {
        boolean on = isToggleOn(row);
        if (row < FIRST_RARITY_ROW) {
            flip(filters.getClasses(), FILTER_CLASSES[row], on);
        } else if (row < FIRST_TYPE_ROW) {
            flip(filters.getRarities(), FILTER_RARITIES[row - FIRST_RARITY_ROW], on);
        } else {
            flip(filters.getTypes(), FILTER_TYPES[row - FIRST_TYPE_ROW], on);
        }
    }

    private <T> void flip (Set<T> set, T value, boolean on) {
        if (on) {
            set.remove(value);
        } else {
            set.add(value);
        }
    }

    //How many items would show if only this toggle were changed
    private int countWithToggleFlipped (int row) {
        flipToggle(row);
        int count = 0;
        for (ItemEnums item : allItems) {
            if (matchesFilters(item, filters)) {
                count++;
            }
        }
        flipToggle(row);
        return count;
    }

    private String toggleLabel (int row) {
        if (row < FIRST_RARITY_ROW) {
            return FILTER_CLASSES[row] == PlayerClass.FireFighter ? "Firefighter" : FILTER_CLASSES[row].toString();
        } else if (row < FIRST_TYPE_ROW) {
            return FILTER_RARITIES[row - FIRST_RARITY_ROW].toString();
        }
        return FILTER_TYPES[row - FIRST_TYPE_ROW].toString();
    }

    //Moves the cursor to an item and scrolls the grid by whole rows until that item's row is shown
    private void selectItem (int index) {
        selectedIndex = index;
        int row = selectedIndex / columns;
        if (row < firstVisibleRow) {
            firstVisibleRow = row;
        } else if (row >= firstVisibleRow + visibleRows) {
            firstVisibleRow = row - visibleRows + 1;
        }
    }

    //The card, drawn like every other component; the grid and the details go over it in draw
    public List<GUIComponent> getComponents () {
        List<GUIComponent> components = new ArrayList<>();
        components.add(card);
        return components;
    }

    //The icons of the shown rows, the frame around the selected one and the details of that item, over the card
    public void draw (Graphics2D g2d) {
        Object oldInterpolation = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int gridLeft = cardLeft() + scaled(PADDING);
        int gridTop = cardTop() + scaled(PADDING) + scaled(FILTER_BAR_HEIGHT);
        drawFilterBar(g2d, gridLeft, cardTop() + scaled(PADDING) + scaled(18));
        int step = scaled(TILE_SIZE) + scaled(TILE_GAP);
        for (int row = 0; row < visibleRows; row++) {
            for (int column = 0; column < columns; column++) {
                int index = (firstVisibleRow + row) * columns + column;
                if (index >= items.size()) {
                    break;
                }
                int x = gridLeft + column * step;
                int y = gridTop + row * step;
                BufferedImage icon = ImageDatabase.getInstance().getImage(items.get(index).getItemIcon());
                if (icon != null) {
                    g2d.drawImage(icon, x, y, scaled(TILE_SIZE), scaled(TILE_SIZE), null);
                }
                if (index == selectedIndex) {
                    g2d.setColor(items.get(index).getItemRarity().getColor());
                    g2d.setStroke(new BasicStroke(scaled(SELECTION_THICKNESS)));
                    g2d.drawRect(x - scaled(TILE_GAP) / 2, y - scaled(TILE_GAP) / 2, step - 1, step - 1);
                }
            }
        }
        if (oldInterpolation != null) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
        }
        int detailsTop = gridTop + visibleRows * step + scaled(DETAIL_GAP);
        if (items.isEmpty()) {
            g2d.setFont(new Font(DataClass.getInstance().getTextFont(), Font.BOLD, scaled(22)));
            g2d.setColor(Color.WHITE);
            g2d.drawString("No items found", gridLeft, gridTop + scaled(22));
        }
        if (filterPaneOpen) {
            drawFilterPane(g2d, gridLeft, detailsTop);
        } else if (!items.isEmpty()) {
            drawDetails(g2d, items.get(selectedIndex), gridLeft, detailsTop);
        }
    }

    //The active filters and how many of the items pass them, e.g. "Captain, Relic - 12 of 76 items"
    private void drawFilterBar (Graphics2D g2d, int left, int baseline) {
        List<String> names = new ArrayList<>();
        for (int row = 0; row < CLEAR_ROW; row++) {
            if (isToggleOn(row)) {
                names.add(toggleLabel(row));
            }
        }
        String text = items.size() + " of " + allItems.size() + " items";
        if (!names.isEmpty()) {
            text = String.join(", ", names) + " - " + text;
        }
        g2d.setFont(new Font(DataClass.getInstance().getTextFont(), Font.PLAIN, scaled(18)));
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, left, baseline);
    }

    //The three groups side by side over the details, each toggle with the number of items that would show if only it were changed, and the clear row under them
    private void drawFilterPane (Graphics2D g2d, int left, int top) {
        String textFont = DataClass.getInstance().getTextFont();
        int columnWidth = (cardLeft() + scaled(CARD_WIDTH) - scaled(PADDING) - left) / 3;
        int rowHeight = scaled(FILTER_ROW_HEIGHT);
        int firstRowBaseline = top + scaled(22) + rowHeight;
        String[] headers = {"CLASS", "RARITY", "TYPE"};
        int[] groupStarts = {0, FIRST_RARITY_ROW, FIRST_TYPE_ROW, CLEAR_ROW};

        for (int group = 0; group < headers.length; group++) {
            int x = left + group * columnWidth;
            g2d.setFont(new Font(textFont, Font.BOLD, scaled(20)));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString(headers[group], x, top + scaled(22));
            g2d.setFont(new Font(textFont, Font.PLAIN, scaled(18)));
            for (int row = groupStarts[group]; row < groupStarts[group + 1]; row++) {
                int baseline = firstRowBaseline + (row - groupStarts[group]) * rowHeight;
                String text = (isToggleOn(row) ? "[x] " : "[ ] ") + toggleLabel(row) + " (" + countWithToggleFlipped(row) + ")";
                drawFilterRow(g2d, text, x, baseline, columnWidth, isToggleOn(row) ? Color.CYAN : Color.WHITE, row == filterCursor);
            }
        }
        int clearBaseline = firstRowBaseline + FILTER_TYPES.length * rowHeight + rowHeight / 2; //Under the longest group
        drawFilterRow(g2d, "Clear all filters", left, clearBaseline, columnWidth, Color.WHITE, filterCursor == CLEAR_ROW);
    }

    private void drawFilterRow (Graphics2D g2d, String text, int x, int baseline, int width, Color color, boolean underCursor) {
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.setColor(color);
        g2d.drawString(text, x, baseline);
        if (underCursor) {
            g2d.setStroke(new BasicStroke(scaled(SELECTION_THICKNESS)));
            g2d.drawRect(x - scaled(6), baseline - metrics.getAscent() - scaled(2), width - scaled(10), metrics.getHeight() + scaled(4));
        }
    }

    //The icon, then the name in its rarity colour, the rarity, the class groups, the types, the full description and the price
    private void drawDetails (Graphics2D g2d, ItemEnums item, int left, int top) {
        BufferedImage icon = ImageDatabase.getInstance().getImage(item.getItemIcon());
        if (icon != null) {
            g2d.drawImage(icon, left, top, scaled(DETAIL_ICON_SIZE), scaled(DETAIL_ICON_SIZE), null);
        }
        String textFont = DataClass.getInstance().getTextFont();
        int textX = left + scaled(DETAIL_ICON_SIZE) + scaled(DETAIL_TEXT_GAP);
        int maxTextWidth = cardLeft() + scaled(CARD_WIDTH) - scaled(PADDING) - textX;
        int y = top + scaled(22);

        g2d.setFont(new Font(textFont, Font.BOLD, scaled(22)));
        drawWrappedText(g2d, item.getItemName(), textX, y, maxTextWidth, item.getItemRarity().getColor());
        y += g2d.getFontMetrics().getHeight();

        g2d.setFont(new Font(textFont, Font.PLAIN, scaled(18)));
        int lineHeight = g2d.getFontMetrics().getHeight() + 2;
        drawWrappedText(g2d, item.getItemRarity().toString(), textX, y, maxTextWidth, item.getItemRarity().getColor());
        y += lineHeight;
        drawWrappedText(g2d, "Class: " + classText(item.getClassAvailability()), textX, y, maxTextWidth, Color.WHITE);
        y += lineHeight;
        drawWrappedText(g2d, "Type: " + typesText(item.getItemTypes()), textX, y, maxTextWidth, Color.WHITE);
        y += lineHeight;
        drawWrappedText(g2d, "Price: " + Math.round(item.getItemRarity().getItemCost()), textX, y, maxTextWidth, Color.WHITE);

        //The description starts under the icon and uses the whole width of the card, so a long text stays on the card
        int descriptionX = left;
        int descriptionY = Math.max(y + lineHeight, top + scaled(DETAIL_ICON_SIZE) + lineHeight);
        int descriptionWidth = cardLeft() + scaled(CARD_WIDTH) - scaled(PADDING) - descriptionX;
        drawWrappedText(g2d, ItemDescriptionRetriever.getDescriptionOfItem(item, true), descriptionX, descriptionY, descriptionWidth, Color.WHITE);
    }

    private String classText (ItemClassAvailability classAvailability) {
        if (classAvailability == ItemClassAvailability.All) {
            return "All classes";
        }
        List<String> names = new ArrayList<>();
        classAvailability.getPlayerClasses().forEach(playerClass -> names.add(playerClass.toString()));
        return String.join(" and ", names);
    }

    private String typesText (List<ItemTypeEnums> types) {
        List<String> names = new ArrayList<>();
        types.forEach(type -> names.add(type.toString()));
        return String.join(", ", names);
    }

    //Draws the text in lines no wider than maxWidth, starting at y for the first line
    private void drawWrappedText (Graphics2D g2d, String text, int x, int y, int maxWidth, Color color) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight() + 2;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        g2d.setColor(color);

        for (String word : words) {
            //If adding the new word exceeds the maximum line width, draw the line and start a new one
            if (metrics.stringWidth(line.toString() + word) > maxWidth) {
                g2d.drawString(line.toString(), x, y);
                line = new StringBuilder(word).append(" ");
                y += lineHeight;
            } else {
                line.append(word).append(" ");
            }
        }

        if (line.length() > 0) {
            g2d.drawString(line.toString(), x, y);
        }
    }
}
