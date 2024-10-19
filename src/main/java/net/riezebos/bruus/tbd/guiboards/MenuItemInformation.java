package net.riezebos.bruus.tbd.guiboards;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

public class MenuItemInformation {

    private ItemEnums item;
    private ItemRarityEnums itemRarity;
    private String itemDescription;
    private boolean available;
    private float cost;

    public MenuItemInformation (ItemEnums item, ItemRarityEnums itemRarity, String itemDescription, boolean available, float cost) {
        this.item = item;
        this.itemRarity = itemRarity;
        this.itemDescription = itemDescription;
        this.available = available;
        this.cost = cost;
    }

    public ItemEnums getItem () {
        return item;
    }

    public void setItem (ItemEnums item) {
        this.item = item;
    }

    public String getItemDescription () {
        return itemDescription;
    }

    public void setItemDescription (String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean isAvailable () {
        return available;
    }

    public boolean canAfford(){
        return PlayerInventory.getInstance().getCashMoney() >= cost;
    }

    public void setAvailable (boolean available) {
        this.available = available;
    }

    public float getCost () {
        return cost;
    }

    public void setCost (float cost) {
        this.cost = cost;
    }

    public ItemRarityEnums getItemRarity () {
        return itemRarity;
    }

    public void setItemRarity (ItemRarityEnums itemRarity) {
        this.itemRarity = itemRarity;
    }
}
