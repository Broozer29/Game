package net.riezebos.bruus.tbd.guiboards.guicomponents;

import com.studiohartman.jamepad.ControllerButton;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.settings.BindableAction;
import net.riezebos.bruus.tbd.game.settings.Settings;
import net.riezebos.bruus.tbd.game.settings.SettingsManager;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.guiboards.boardcreators.MenuBoardCreator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//The settings page shown over the paused game or the main menu: a wide main menu card with a tab row and the rows of the selected tab (CONTROLS or SOUND)
//The CONTROLS tab's table and its rebind flow live in ControlsTab
//It also holds the whole flow around the page: its input, the SAVE CHANGES question and closing, so both screens use it the same way
//All sizes below are in unscaled pixels and get multiplied by the resolution factor
public class SettingsWindow {

    private static final int CARD_WIDTH = 700;
    private static final int CARD_HEIGHT = 450; //The same on both tabs, tall enough for the controls table
    private static final int TAB_Y = 45; //From the top of the card to the centre of the tab row
    private static final int CONTROLS_TAB_X = -130; //From the centre of the card to the centre of the CONTROLS tab
    private static final int SOUND_TAB_X = 110; //From the centre of the card to the centre of the SOUND tab
    private static final int TAB_UNDERLINE_GAP = 8; //From the bottom of the tab text to the underline
    private static final int TAB_UNDERLINE_HEIGHT = 3;
    private static final int TAB_UNDERLINE_COLOR = 0xFFFFFFFF; //White
    private static final int MUSIC_Y = 135; //From the top of the card to the centre of the row
    private static final int EFFECTS_Y = 205;
    private static final int SAVE_Y = 395; //The same on both tabs
    private static final int SAVE_X = 120; //From the centre of the card to the centre of SAVE, the same on both tabs
    private static final int LABEL_X = -215; //From the centre of the card to the start of a label
    private static final int VALUE_X = 0; //From the centre of the card to the start of a bar
    private static final int CURSOR_GAP = 15; //Space between the ship cursor and the label

    private static final int VOLUME_STEP = 10;
    private static final int BOTTOM_ROW_INDEX = 4; //In the CONTROLS rows: RESET, with SAVE after it

    private enum SettingsTab {CONTROLS, SOUND}

    private enum SettingsRow {MUSIC, EFFECTS, SAVE, FIRE, SPECIAL_ATTACK, AUTO_FIRE, FREEZE_SHOP, RESET}

    private static final Map<SettingsTab, List<SettingsRow>> TAB_ROWS = new EnumMap<>(SettingsTab.class);

    static {
        TAB_ROWS.put(SettingsTab.CONTROLS, List.of(SettingsRow.FIRE, SettingsRow.SPECIAL_ATTACK, SettingsRow.AUTO_FIRE, SettingsRow.FREEZE_SHOP, SettingsRow.RESET, SettingsRow.SAVE));
        TAB_ROWS.put(SettingsTab.SOUND, List.of(SettingsRow.MUSIC, SettingsRow.EFFECTS, SettingsRow.SAVE));
    }

    private static SettingsWindow instance; //Only one page is shown at a time, so the buttons reach the one that is shown here

    private final SettingsManager settingsManager = SettingsManager.getInstance();
    private final float resolutionFactor;
    private final int centerX;
    private final int cardTop;
    private ResizableCard card;
    private ControlsTab controlsTab;
    private final Map<SettingsTab, GUITextCollection> tabTexts = new EnumMap<>(SettingsTab.class);
    private final Map<SettingsTab, Picture> tabUnderlines = new EnumMap<>(SettingsTab.class); //One per tab, only the selected tab has its underline shown
    private final Map<SettingsRow, GUITextCollection> labels = new EnumMap<>(SettingsRow.class); //The part of every row the cursor stands beside; SAVE is the same on both tabs
    private VolumeBar musicBar;
    private VolumeBar effectsBar;
    private MenuCursor cursor;
    private SettingsTab selectedTab = SettingsTab.CONTROLS;
    private int selectedRowIndex = 0; //In the selected tab's row list
    private MenuWindow saveQuestion;
    private boolean open = false;
    private boolean questionOpen = false;
    private Runnable onClosed; //What the screen does after the page closed

    public static SettingsWindow getInstance () {
        if (instance == null) {
            instance = new SettingsWindow();
        }
        return instance;
    }

    private SettingsWindow () {
        DataClass data = DataClass.getInstance();
        resolutionFactor = Math.min(data.getResolutionFactor(), DataClass.maxResolutionFactor);
        centerX = data.getWindowWidth() / 2;
        cardTop = (data.getWindowHeight() - scaled(CARD_HEIGHT)) / 2;
        build();
    }

    private int scaled (int size) {
        return Math.round(size * resolutionFactor);
    }

    private void build () {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(centerX - scaled(CARD_WIDTH) / 2);
        spriteConfiguration.setyCoordinate(cardTop);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Square_Card);
        card = new ResizableCard(spriteConfiguration, scaled(CARD_WIDTH), scaled(CARD_HEIGHT));

        float textScale = resolutionFactor * MenuText.TEXT_SCALE;
        int tabCenterY = cardTop + scaled(TAB_Y);
        addTab(SettingsTab.CONTROLS, "CONTROLS", centerX + scaled(CONTROLS_TAB_X), tabCenterY, textScale);
        addTab(SettingsTab.SOUND, "SOUND", centerX + scaled(SOUND_TAB_X), tabCenterY, textScale);

        int labelX = centerX + scaled(LABEL_X);
        int valueX = centerX + scaled(VALUE_X);
        Settings settings = settingsManager.getSettings();
        labels.put(SettingsRow.MUSIC, MenuText.createText("MUSIC", labelX, cardTop + scaled(MUSIC_Y), textScale, MenuText.ALIGN_LEFT));
        labels.put(SettingsRow.EFFECTS, MenuText.createText("EFFECTS", labelX, cardTop + scaled(EFFECTS_Y), textScale, MenuText.ALIGN_LEFT));
        controlsTab = new ControlsTab(centerX, cardTop, resolutionFactor, textScale);
        labels.put(SettingsRow.FIRE, controlsTab.getLabel(BindableAction.FIRE));
        labels.put(SettingsRow.SPECIAL_ATTACK, controlsTab.getLabel(BindableAction.SPECIAL_ATTACK));
        labels.put(SettingsRow.AUTO_FIRE, controlsTab.getLabel(BindableAction.HOLD_FIRE));
        labels.put(SettingsRow.FREEZE_SHOP, controlsTab.getLabel(BindableAction.FREEZE_SHOP));
        labels.put(SettingsRow.RESET, controlsTab.getResetLabel());
        GUITextCollection saveText = MenuText.createText("SAVE", centerX + scaled(SAVE_X), cardTop + scaled(SAVE_Y), textScale, MenuText.ALIGN_CENTER);
        saveText.setMenuFunctionality(MenuFunctionEnums.Save_Settings); //Makes the first letter the button
        labels.put(SettingsRow.SAVE, saveText);

        musicBar = new VolumeBar(valueX, cardTop + scaled(MUSIC_Y), resolutionFactor, settings.getMusicVolume());
        effectsBar = new VolumeBar(valueX, cardTop + scaled(EFFECTS_Y), resolutionFactor, settings.getEffectsVolume());

        cursor = MenuBoardCreator.createMenuCursor(labels.get(TAB_ROWS.get(selectedTab).get(0)).getComponents().get(0));
        placeCursor();

        //The menu letters have no question mark, so the question has none
        saveQuestion = new MenuWindow("SAVE CHANGES", List.of(
                new MenuWindow.MenuRow("SAVE", MenuFunctionEnums.Save_Settings_Changes),
                new MenuWindow.MenuRow("DISCARD", MenuFunctionEnums.Discard_Settings_Changes)));
    }

    //A tab name in the menu letters, with a white underline as wide as the name that shows only while the tab is selected
    private void addTab (SettingsTab tab, String name, int tabCenterX, int tabCenterY, float textScale) {
        GUITextCollection tabText = MenuText.createText(name, tabCenterX, tabCenterY, textScale, MenuText.ALIGN_CENTER);
        BufferedImage underlineImage = new BufferedImage(tabText.getWidth(), scaled(TAB_UNDERLINE_HEIGHT), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < underlineImage.getWidth(); x++) {
            for (int y = 0; y < underlineImage.getHeight(); y++) {
                underlineImage.setRGB(x, y, TAB_UNDERLINE_COLOR);
            }
        }
        Picture tabUnderline = new Picture(ImageEnums.Frame);
        tabUnderline.setPicture(underlineImage);
        tabUnderline.setCenterCoordinates(tabCenterX, tabCenterY + tabText.getHeight() / 2 + scaled(TAB_UNDERLINE_GAP));
        tabTexts.put(tab, tabText);
        tabUnderlines.put(tab, tabUnderline);
    }

    private SettingsRow selectedRow () {
        return TAB_ROWS.get(selectedTab).get(selectedRowIndex);
    }

    //Shows another tab with its first row selected
    private void selectTab (SettingsTab tab) {
        if (tab == selectedTab) {
            return;
        }
        selectedTab = tab;
        selectedRowIndex = 0;
        controlsTab.reset();
        placeCursor();
    }

    //The action a CONTROLS row stands for; null for every other row
    private BindableAction actionOf (SettingsRow row) {
        switch (row) {
            case FIRE:
                return BindableAction.FIRE;
            case SPECIAL_ATTACK:
                return BindableAction.SPECIAL_ATTACK;
            case AUTO_FIRE:
                return BindableAction.HOLD_FIRE;
            case FREEZE_SHOP:
                return BindableAction.FREEZE_SHOP;
            default:
                return null;
        }
    }

    //Puts the cursor left of the selected row, with a gap
    private void placeCursor () {
        GUIComponent selectedTile = labels.get(selectedRow()).getComponents().get(0);
        cursor.setSelectedMenuTile(selectedTile);
        cursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + cursor.getYDistanceModification());
        cursor.setXCoordinate(selectedTile.getXCoordinate() - cursor.getxDistanceToKeep() - scaled(CURSOR_GAP));
    }

    //Shows the page on the CONTROLS tab with its first row selected and the settings in use; onClosed says what the screen does once the page is closed
    public void open (Runnable onClosed) {
        this.onClosed = onClosed;
        selectedTab = SettingsTab.CONTROLS;
        selectedRowIndex = 0;
        labels.clear();
        tabTexts.clear();
        tabUnderlines.clear();
        build(); //Built again, so the cursor shows the ship of the class in use
        open = true;
        questionOpen = false;
        MenuInput.getInstance().startScreen();
    }

    public boolean isOpen () {
        return open;
    }

    //Hides the page without saving, discarding or telling the screen, for when the screen itself pauses, resumes or changes
    public void closeSilently () {
        open = false;
        questionOpen = false;
        onClosed = null; //A screen's close step only belongs to the open it came with
        controlsTab.reset();
    }

    //The page and the question act on what MenuInput reports
    public void handleInput (List<MenuAction> actions) {
        //The raw presses are asked on every call, so none is kept for later
        List<Integer> keys = MenuInput.getInstance().pollPressedKeys();
        List<ControllerButton> buttons = MenuInput.getInstance().pollPressedButtons();
        if (!questionOpen && controlsTab.isWaiting()) {
            actions = controlsTab.handleWhileWaiting(actions, keys, buttons, actionOf(selectedRow())); //LB, RB, Tab, Enter, Space and A bind instead of acting
        }
        for (MenuAction action : actions) {
            if (questionOpen) {
                if (action == MenuAction.UP) {
                    saveQuestion.selectPreviousOption();
                } else if (action == MenuAction.DOWN) {
                    saveQuestion.selectNextOption();
                } else if (action == MenuAction.CONFIRM) {
                    saveQuestion.activateSelectedOption();
                    return;
                } else if (action == MenuAction.BACK) {
                    closeQuestion();
                    return;
                }
                continue;
            }
            SettingsTab[] tabs = SettingsTab.values();
            if (action == MenuAction.PREVIOUS_TAB) {
                selectTab(tabs[Math.max(selectedTab.ordinal() - 1, 0)]); //No wrap: nothing happens on the first tab
            } else if (action == MenuAction.NEXT_TAB) {
                selectTab(tabs[Math.min(selectedTab.ordinal() + 1, tabs.length - 1)]); //No wrap: nothing happens on the last tab
            } else if (action == MenuAction.CYCLE_TAB) {
                selectTab(tabs[(selectedTab.ordinal() + 1) % tabs.length]); //Tab on the keyboard: from the last tab back to the first
            } else if (action == MenuAction.UP) {
                selectPreviousRow();
            } else if (action == MenuAction.DOWN) {
                selectNextRow();
            } else if (action == MenuAction.LEFT) {
                changeSelectedValue(-1);
            } else if (action == MenuAction.RIGHT) {
                changeSelectedValue(1);
            } else if (action == MenuAction.CONFIRM) {
                confirmSelected();
                return;
            } else if (action == MenuAction.BACK) {
                requestClose();
                return;
            }
        }
    }

    //Closing the page: unsaved changes are asked about first
    public void requestClose () {
        if (!askBeforeLeaving()) {
            close();
        }
    }

    //With unsaved changes, opens the SAVE CHANGES question (or keeps it open) and returns true; otherwise false and nothing happens
    public boolean askBeforeLeaving () {
        if (questionOpen) {
            return true; //The player answers the question
        }
        if (!settingsManager.hasUnsavedChanges()) {
            return false;
        }
        questionOpen = true;
        controlsTab.reset(); //Any rebind that waited ends; the bindings already made stay
        saveQuestion.selectFirstOption(); //SAVE first, so a double press does not throw the changes away
        MenuInput.getInstance().startScreen();
        return true;
    }

    //Back from the question to the page
    private void closeQuestion () {
        questionOpen = false;
        MenuInput.getInstance().startScreen();
    }

    private void close () {
        Runnable closedStep = onClosed;
        closeSilently();
        if (closedStep != null) {
            closedStep.run();
        }
    }

    //Writes the settings to settings.json and closes the page
    public void saveAndClose () {
        settingsManager.saveSettings();
        close();
    }

    public void discardAndClose () {
        settingsManager.discardChanges();
        controlsTab.reset();
        AudioManager.getInstance().applyVolumeSettings(); //The sound goes back with the values
        close();
    }

    private void showSettings () {
        Settings settings = settingsManager.getSettings();
        musicBar.setPercentage(settings.getMusicVolume());
        effectsBar.setPercentage(settings.getEffectsVolume());
    }

    private void selectNextRow () {
        if (selectedTab == SettingsTab.CONTROLS) {
            //The bottom row (RESET and SAVE) is one stop: down from FREEZE SHOP lands on RESET, down from the bottom row goes back to FIRE
            selectedRowIndex = selectedRowIndex >= BOTTOM_ROW_INDEX ? 0 : Math.min(selectedRowIndex + 1, BOTTOM_ROW_INDEX);
            placeCursor();
            return;
        }
        selectedRowIndex = (selectedRowIndex + 1) % TAB_ROWS.get(selectedTab).size();
        placeCursor();
    }

    private void selectPreviousRow () {
        if (selectedTab == SettingsTab.CONTROLS) {
            selectedRowIndex = selectedRowIndex == 0 ? BOTTOM_ROW_INDEX : selectedRowIndex >= BOTTOM_ROW_INDEX ? BOTTOM_ROW_INDEX - 1 : selectedRowIndex - 1;
            placeCursor();
            return;
        }
        int rowCount = TAB_ROWS.get(selectedTab).size();
        selectedRowIndex = (selectedRowIndex - 1 + rowCount) % rowCount;
        placeCursor();
    }

    //Moves the selected volume up (1) or down (-1) by 10%; the sound follows at once
    private void changeSelectedValue (int direction) {
        Settings settings = settingsManager.getSettings();
        if (selectedRow() == SettingsRow.MUSIC) {
            settings.setMusicVolume(settings.getMusicVolume() + direction * VOLUME_STEP);
        } else if (selectedRow() == SettingsRow.EFFECTS) {
            settings.setEffectsVolume(settings.getEffectsVolume() + direction * VOLUME_STEP);
        } else {
            //On the bottom row of the CONTROLS tab, right goes from RESET to SAVE and left back
            if (selectedRow() == SettingsRow.RESET && direction > 0 || selectedRow() == SettingsRow.SAVE && direction < 0 && selectedTab == SettingsTab.CONTROLS) {
                selectedRowIndex += direction;
                placeCursor();
            }
            return;
        }
        AudioManager.getInstance().applyVolumeSettings();
        showSettings();
    }

    //SAVE saves and closes, RESET puts the default layouts back and an action row waits for its input; the volume rows only change with left and right
    private void confirmSelected () {
        SettingsRow row = selectedRow();
        if (row == SettingsRow.SAVE) {
            labels.get(SettingsRow.SAVE).getComponents().get(0).activateComponent();
        } else if (row == SettingsRow.RESET) {
            controlsTab.resetBindings();
        } else if (actionOf(row) != null) {
            controlsTab.startWaiting(actionOf(row));
        }
    }

    //The page first, with the question over it when it is open, in the order they are drawn
    public List<GUIComponent> getComponents () {
        List<GUIComponent> components = new ArrayList<>();
        for (GUIComponent component : getPageComponents()) {
            if (questionOpen && component instanceof MenuCursor) {
                continue; //Only the question's cursor shows while the question is open
            }
            components.add(component);
        }
        if (questionOpen) {
            components.addAll(saveQuestion.getComponents());
        }
        return components;
    }

    //The card first, then the tab, the rows and the cursor, in the order they are drawn
    private List<GUIComponent> getPageComponents () {
        List<GUIComponent> components = new ArrayList<>();
        components.add(card);
        components.add(tabUnderlines.get(selectedTab));
        for (SettingsTab tab : SettingsTab.values()) {
            components.addAll(tabTexts.get(tab).getComponents());
        }
        for (SettingsRow row : TAB_ROWS.get(selectedTab)) {
            components.addAll(labels.get(row).getComponents());
        }
        if (selectedTab == SettingsTab.CONTROLS) {
            components.addAll(controlsTab.getComponents());
        }
        if (selectedTab == SettingsTab.SOUND) {
            components.addAll(musicBar.getComponents());
            components.addAll(effectsBar.getComponents());
        }
        components.add(cursor);
        return components;
    }
}
