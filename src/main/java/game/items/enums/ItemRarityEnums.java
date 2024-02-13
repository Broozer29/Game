package game.items.enums;

import java.util.Random;

public enum ItemRarityEnums {
    Common, Rare, Legendary;

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
