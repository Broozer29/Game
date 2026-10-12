package net.riezebos.bruus.tbd.game.gamestate.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

// One slot of the shop. Jackson bean: it is saved as part of ShopContents.
public class ShopSlot {

    private ItemEnums item;
    private ItemRarityEnums rarity;
    private float cost;
    private ShopSlotStatusEnums status;
    // Set only on a slot carried over from a frozen shop, to play the thaw once. Not saved.
    @JsonIgnore
    private boolean carried;

    public ShopSlot() {
    }

    public ShopSlot(ItemEnums item, ItemRarityEnums rarity, float cost, ShopSlotStatusEnums status) {
        this.item = item;
        this.rarity = rarity;
        this.cost = cost;
        this.status = status;
    }

    public ItemEnums getItem() {
        return item;
    }

    public void setItem(ItemEnums item) {
        this.item = item;
    }

    public ItemRarityEnums getRarity() {
        return rarity;
    }

    public void setRarity(ItemRarityEnums rarity) {
        this.rarity = rarity;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public ShopSlotStatusEnums getStatus() {
        return status;
    }

    public void setStatus(ShopSlotStatusEnums status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isCarried() {
        return carried;
    }

    @JsonIgnore
    public void setCarried(boolean carried) {
        this.carried = carried;
    }
}
