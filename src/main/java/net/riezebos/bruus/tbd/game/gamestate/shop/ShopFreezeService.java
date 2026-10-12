package net.riezebos.bruus.tbd.game.gamestate.shop;

import com.studiohartman.jamepad.ControllerButton;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveManager;
import net.riezebos.bruus.tbd.game.settings.BindableAction;
import net.riezebos.bruus.tbd.game.settings.InputBindings;
import net.riezebos.bruus.tbd.game.settings.SettingsManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.awt.event.KeyEvent;
import java.util.List;

// The freeze logic of the shop: the toggle with the cooldown after an unfreeze, the thaw marks and the thaw clock. It draws nothing (see ShopFreezeEffect).
public class ShopFreezeService {

    // How long the whole thaw takes (ms); the cooldown and the end of the thaw are measured from it
    public static final int THAW_TOTAL_MS = 600;
    // After an unfreeze, the toggle waits until its thaw is over plus 0.1 s, so the thaw can't be spammed; freezing has no cooldown
    private static final int UNFREEZE_COOLDOWN_MS = THAW_TOTAL_MS + 100;

    // When the thaw of the carried items, or of the items just unfrozen, started (ms); 0 when none is playing
    private long thawStartMillis = 0;
    private long lastUnfreezeMillis = 0;

    private static ShopFreezeService instance = new ShopFreezeService();

    private ShopFreezeService() {

    }

    public static ShopFreezeService getInstance() {
        return instance;
    }

    public boolean isFrozen() {
        ShopContents contents = ShopManager.getInstance().getContents();
        return contents != null && contents.isFrozen();
    }

    public long getThawStartMillis() {
        return thawStartMillis;
    }

    // Freezes or unfreezes the whole shop, for the Freeze button and the Freeze shop binding
    public void toggleFreeze() {
        ShopContents contents = ShopManager.getInstance().getContents();
        long now = System.currentTimeMillis();
        if (contents == null || now - lastUnfreezeMillis < UNFREEZE_COOLDOWN_MS) {
            return;
        }
        if (contents.isFrozen()) {
            lastUnfreezeMillis = now;
            //Unfreezing plays the same thaw as a new shop does on its kept items, on every item that was frozen
            for (List<ShopSlot> row : contents.getRows()) {
                for (ShopSlot slot : row) {
                    if (slot.getStatus() == ShopSlotStatusEnums.Available) {
                        slot.setCarried(true);
                    }
                }
            }
            thawStartMillis = now;
        }
        contents.setFrozen(!contents.isFrozen());
        AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
        SaveManager.getInstance().exportCurrentSave();
    }

    // Called when the shop screen opens: the thaw of the carried items starts now, or is off when none is carried
    public void startThawIfCarried() {
        thawStartMillis = anySlotCarried() ? System.currentTimeMillis() : 0;
    }

    // Forgets the carried marks once the thaw is over, so nothing is drawn or checked for it any more
    public void clearThawIfOver() {
        ShopContents contents = ShopManager.getInstance().getContents();
        if (thawStartMillis != 0 && (contents == null || System.currentTimeMillis() - thawStartMillis >= THAW_TOTAL_MS)) {
            if (contents != null) {
                for (List<ShopSlot> row : contents.getRows()) {
                    for (ShopSlot slot : row) {
                        slot.setCarried(false);
                    }
                }
            }
            thawStartMillis = 0;
        }
    }

    // The Freeze shop binding: a press of the bound key or button toggles the freeze (not while the inventory is open)
    // and is not also a confirm or back.
    // The result follows the hosted-input contract (true = the menu does not act on the poll). It is always false,
    // because the menu must still act on the other actions, such as directions.
    public boolean handleBindingInput(List<MenuAction> actions, boolean inventoryOpen) {
        List<Integer> keys = MenuInput.getInstance().pollPressedKeys();
        List<ControllerButton> buttons = MenuInput.getInstance().pollPressedButtons();
        if (inventoryOpen) {
            return false;
        }
        InputBindings bindings = SettingsManager.getInstance().getSettings().getInputBindings();
        int key = bindings.getKey(BindableAction.FREEZE_SHOP);
        ControllerButton button = bindings.getButton(BindableAction.FREEZE_SHOP);
        boolean pressed = (key != KeyEvent.VK_UNDEFINED && keys.contains(key)) || (button != null && buttons.contains(button));
        if (pressed) {
            toggleFreeze();
            actions.removeIf(action -> action == MenuAction.CONFIRM || action == MenuAction.BACK);
        }
        return false;
    }

    private boolean anySlotCarried() {
        ShopContents contents = ShopManager.getInstance().getContents();
        if (contents == null) {
            return false;
        }
        for (List<ShopSlot> row : contents.getRows()) {
            for (ShopSlot slot : row) {
                if (slot.isCarried()) {
                    return true;
                }
            }
        }
        return false;
    }
}
