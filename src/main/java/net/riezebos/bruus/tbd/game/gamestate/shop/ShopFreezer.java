package net.riezebos.bruus.tbd.game.gamestate.shop;

import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.util.List;

// The freeze logic of the shop: the toggle with the cooldown after an unfreeze, the thaw marks and the thaw clock. It draws nothing (see ShopFreezeEffect).
public class ShopFreezer {

    // How long the whole thaw takes (ms); the cooldown and the end of the thaw are measured from it
    public static final int THAW_TOTAL_MS = 600;
    // After an unfreeze, the Freeze button waits until its thaw is over plus 0.1 s, so the thaw can't be spammed; freezing has no cooldown
    private static final int UNFREEZE_COOLDOWN_MS = THAW_TOTAL_MS + 100;

    // When the thaw of the carried items, or of the items just unfrozen, started (ms); 0 when none is playing
    private long unfreezeAnimationStartMillis = 0;
    private long lastUnfreezeMillis = 0;

    private static ShopFreezer instance = new ShopFreezer();

    private ShopFreezer() {

    }

    public static ShopFreezer getInstance() {
        return instance;
    }

    public boolean isFrozen() {
        ShopContents contents = ShopManager.getInstance().getContents();
        return contents != null && contents.isFrozen();
    }

    public long getUnfreezeAnimationStartMillis() {
        return unfreezeAnimationStartMillis;
    }

    // Freezes or unfreezes the whole shop, for the Freeze button
    public void toggleFreeze() {
        ShopContents contents = ShopManager.getInstance().getContents();
        long now = System.currentTimeMillis();
        if (contents == null || now - lastUnfreezeMillis < UNFREEZE_COOLDOWN_MS) {
            return;
        }
        if (contents.isFrozen()) {
            lastUnfreezeMillis = now;
            contents.getRows().stream()
                    .flatMap(List::stream)
                    .filter(shopSlot -> shopSlot.getStatus() == ShopSlotStatusEnums.Available)
                    .forEach(shopSlot -> shopSlot.setFrozen(true));
            unfreezeAnimationStartMillis = now;
        }
        contents.setFrozen(!contents.isFrozen());
        AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
        SaveManager.getInstance().exportCurrentSave();
    }

    // Called when the shop screen opens: the thaw of the carried items starts now, or is off when none is carried
    public void startUnfreezeAnimationIfFrozen() {
        unfreezeAnimationStartMillis = anySlotFrozen() ? System.currentTimeMillis() : 0;
    }

    // Forgets the carried marks once the thaw is over, so nothing is drawn or checked for it any more
    public void clearFrozenStatusOnShopItems() {
        ShopContents contents = ShopManager.getInstance().getContents();
        if (unfreezeAnimationStartMillis != 0 && (contents == null || System.currentTimeMillis() - unfreezeAnimationStartMillis >= THAW_TOTAL_MS)) {
            if (contents != null) {
                contents.getRows().stream()
                        .flatMap(List::stream)
                        .forEach(shopSlot -> shopSlot.setFrozen(false));
            }
            unfreezeAnimationStartMillis = 0;
        }
    }

    private boolean anySlotFrozen() {
        ShopContents contents = ShopManager.getInstance().getContents();
        if (contents == null) {
            return false;
        }
        return contents.getRows().stream()
                .flatMap(List::stream)
                .anyMatch(ShopSlot::isFrozen);
    }
}
