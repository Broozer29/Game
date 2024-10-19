package net.riezebos.bruus.tbd.game.items.enums;

import java.util.Random;

public enum ItemRarityEnums {
    Common(150),
    Rare(225),
    Legendary(300),
    Locked(0);


    ItemRarityEnums (float itemCost) {
        this.itemCost = itemCost;
    }

    private float itemCost;

    public float getItemCost () {
        return itemCost;
    }

    public void setItemCost (float itemCost) {
        this.itemCost = itemCost;
    }

    public static ItemRarityEnums getRandomCommonItemSlot () {
        Random rand = new Random();
        int chance = rand.nextInt(100);

        if (chance < 79) {
            return ItemRarityEnums.Common;
        } else if (chance < 99) {
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


}
