package net.riezebos.bruus.tbd.game.gamestate.shop;

import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

// One slot of the shop. Jackson bean: it is saved as part of ShopContents.
public class ShopSlot {

    private ItemEnums item;
    private ItemRarityEnums rarity;
    private float cost;
    private ShopSlotStatusEnums status;

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
}
