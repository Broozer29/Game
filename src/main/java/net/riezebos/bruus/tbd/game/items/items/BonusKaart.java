package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class BonusKaart extends Item {
    private boolean usedThisShop = false;
    public BonusKaart() {
        super(ItemEnums.BonusKaart, 1, ItemApplicationEnum.CustomActivation);
    }


    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
    }

    public boolean isUsedThisShop() {
        return usedThisShop;
    }

    public void setUsedThisShop(boolean usedThisShop) {
        this.usedThisShop = usedThisShop;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }

        return true;
    }
}