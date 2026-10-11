package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
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
import java.util.List;

//The settings page shown over the paused game or the main menu: a wide main menu card with a tab row and the rows of the selected tab (only SOUND for now)
//It also holds the whole flow around the page: its input, the SAVE CHANGES question and closing, so both screens use it the same way
//All sizes below are in unscaled pixels and get multiplied by the resolution factor
public class SettingsWindow {

    private static final int CARD_WIDTH = 700;
    private static final int CARD_HEIGHT = 380;
    private static final int TAB_Y = 45; //From the top of the card to the centre of the tab row
    private static final int TAB_X = -110; //From the centre of the card to the centre of the SOUND tab
    private static final int TAB_UNDERLINE_GAP = 8; //From the bottom of the tab text to the underline
    private static final int TAB_UNDERLINE_HEIGHT = 3;
    private static final int TAB_UNDERLINE_COLOR = 0xFFFFFFFF; //White
    private static final int[] ROW_Y = {135, 205, 295}; //MUSIC, EFFECTS and SAVE, from the top of the card to their centre
    private static final int LABEL_X = -215; //From the centre of the card to the start of a label
    private static final int VALUE_X = 0; //From the centre of the card to the start of a bar
    private static final int CURSOR_GAP = 15; //Space between the ship cursor and the label

    private static final int ROW_MUSIC = 0;
    private static final int ROW_EFFECTS = 1;
    private static final int ROW_SAVE = 2;
    private static final int VOLUME_STEP = 10;

    private static SettingsWindow instance; //Only one page is shown at a time, so the buttons reach the one that is shown here

    private final SettingsManager settingsManager = SettingsManager.getInstance();
    private final float resolutionFactor;
    private final int centerX;
    private final int cardTop;
    private ResizableCard card;
    private Picture tabUnderline;
    private GUITextCollection tabText;
    private final List<GUITextCollection> labels = new ArrayList<>(); //In row order, the part of every row the cursor stands beside
    private VolumeBar musicBar;
    private VolumeBar effectsBar;
    private MenuCursor cursor;
    private int selectedRow = ROW_MUSIC;
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
        int tabCenterX = centerX + scaled(TAB_X);
        int tabCenterY = cardTop + scaled(TAB_Y);
        tabText = MenuText.createText("SOUND", tabCenterX, tabCenterY, textScale, MenuText.ALIGN_CENTER);
        BufferedImage underlineImage = new BufferedImage(tabText.getWidth(), scaled(TAB_UNDERLINE_HEIGHT), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < underlineImage.getWidth(); x++) {
            for (int y = 0; y < underlineImage.getHeight(); y++) {
                underlineImage.setRGB(x, y, TAB_UNDERLINE_COLOR);
            }
        }
        tabUnderline = new Picture(ImageEnums.Frame);
        tabUnderline.setPicture(underlineImage);
        tabUnderline.setCenterCoordinates(tabCenterX, tabCenterY + tabText.getHeight() / 2 + scaled(TAB_UNDERLINE_GAP));

        int labelX = centerX + scaled(LABEL_X);
        int valueX = centerX + scaled(VALUE_X);
        Settings settings = settingsManager.getSettings();
        labels.add(MenuText.createText("MUSIC", labelX, cardTop + scaled(ROW_Y[ROW_MUSIC]), textScale, MenuText.ALIGN_LEFT));
        labels.add(MenuText.createText("EFFECTS", labelX, cardTop + scaled(ROW_Y[ROW_EFFECTS]), textScale, MenuText.ALIGN_LEFT));
        GUITextCollection saveText = MenuText.createText("SAVE", centerX, cardTop + scaled(ROW_Y[ROW_SAVE]), textScale, MenuText.ALIGN_CENTER);
        saveText.setMenuFunctionality(MenuFunctionEnums.Save_Settings); //Makes the first letter the button
        labels.add(saveText);

        musicBar = new VolumeBar(valueX, cardTop + scaled(ROW_Y[ROW_MUSIC]), resolutionFactor, settings.getMusicVolume());
        effectsBar = new VolumeBar(valueX, cardTop + scaled(ROW_Y[ROW_EFFECTS]), resolutionFactor, settings.getEffectsVolume());

        cursor = MenuBoardCreator.createMenuCursor(labels.get(ROW_MUSIC).getComponents().get(0));
        placeCursor();

        //The menu letters have no question mark, so the question has none
        saveQuestion = new MenuWindow("SAVE CHANGES", List.of(
                new MenuWindow.MenuRow("SAVE", MenuFunctionEnums.Save_Settings_Changes),
                new MenuWindow.MenuRow("DISCARD", MenuFunctionEnums.Discard_Settings_Changes)));
    }

    //Puts the cursor left of the selected row, with a gap
    private void placeCursor () {
        GUIComponent selectedTile = labels.get(selectedRow).getComponents().get(0);
        cursor.setSelectedMenuTile(selectedTile);
        cursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + cursor.getYDistanceModification());
        cursor.setXCoordinate(selectedTile.getXCoordinate() - cursor.getxDistanceToKeep() - scaled(CURSOR_GAP));
    }

    //Shows the page with MUSIC selected and the settings in use; onClosed says what the screen does once the page is closed
    public void open (Runnable onClosed) {
        this.onClosed = onClosed;
        selectedRow = ROW_MUSIC;
        labels.clear();
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
    }

    //The page and the question act on what MenuInput reports
    public void handleInput (List<MenuAction> actions) {
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
            if (action == MenuAction.UP) {
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
        AudioManager.getInstance().applyVolumeSettings(); //The sound goes back with the values
        close();
    }

    private void showSettings () {
        Settings settings = settingsManager.getSettings();
        musicBar.setPercentage(settings.getMusicVolume());
        effectsBar.setPercentage(settings.getEffectsVolume());
    }

    public void selectNextRow () {
        selectedRow = (selectedRow + 1) % labels.size();
        placeCursor();
    }

    public void selectPreviousRow () {
        selectedRow = (selectedRow - 1 + labels.size()) % labels.size();
        placeCursor();
    }

    //Moves the selected volume up (1) or down (-1) by 10%; the sound follows at once
    public void changeSelectedValue (int direction) {
        Settings settings = settingsManager.getSettings();
        if (selectedRow == ROW_MUSIC) {
            settings.setMusicVolume(settings.getMusicVolume() + direction * VOLUME_STEP);
        } else if (selectedRow == ROW_EFFECTS) {
            settings.setEffectsVolume(settings.getEffectsVolume() + direction * VOLUME_STEP);
        } else {
            return;
        }
        AudioManager.getInstance().applyVolumeSettings();
        showSettings();
    }

    //SAVE saves and closes; the volume rows only change with left and right
    public void confirmSelected () {
        if (selectedRow == ROW_SAVE) {
            labels.get(ROW_SAVE).getComponents().get(0).activateComponent();
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
        components.add(tabUnderline);
        components.addAll(tabText.getComponents());
        for (GUITextCollection label : labels) {
            components.addAll(label.getComponents());
        }
        components.addAll(musicBar.getComponents());
        components.addAll(effectsBar.getComponents());
        components.add(cursor);
        return components;
    }
}
