package net.riezebos.bruus.tbd.game.items.enums;

import java.awt.*;
import java.util.Random;

public enum ItemRarityEnums {
    Common(150, Color.WHITE),
    Rare(225, Color.GREEN),
    Legendary(300, Color.ORANGE),
    Locked(0, Color.GRAY);


    ItemRarityEnums (float itemCost, Color color) {
        this.itemCost = itemCost;
        this.color = color;
    }

    private float itemCost;
    private Color color;

    public float getItemCost () {
        return itemCost;
    }

    public void setItemCost (float itemCost) {
        this.itemCost = itemCost;
    }

    public static ItemRarityEnums getRandomCommonItemSlot () {
        Random rand = new Random();
        int chance = rand.nextInt(100);

        if (chance < 75) {
            return ItemRarityEnums.Common;
        } else if (chance < 95) {
            return ItemRarityEnums.Rare;
        } else {
            return ItemRarityEnums.Legendary;
        }
    }


    public static ItemRarityEnums getRandomRareItemSlot () {
        Random rand = new Random();
        int chance = rand.nextInt(100);

        if (chance < 80) {
            return ItemRarityEnums.Rare;
        } else {
            return ItemRarityEnums.Legendary;
        }
    }

    public Color getColor () {
        return color;
    }
}
