package net.riezebos.bruus.tbd.game.items.items.util;


import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum WisdomBallRolls {

    // Formule is: gewicht / totaal gewicht van alle items * 100 = percentage dat deze roll geselecteerd wordt
    Single_Common(1.0f),
    Copy_Inventory(0.5f),
    All_Rare(0.9f),
    Copy_A_Legendary(1.0f),
    Add_A_Relic(0.75f),
    Free_Items(1.1f);

    private final float weight;

    WisdomBallRolls(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public static WisdomBallRolls getRandomEffect() {
        List<WisdomBallRolls> availableEffects = Arrays.stream(WisdomBallRolls.values())
                .filter(WisdomBallRolls::isAvailable)
                .collect(Collectors.toList());

        if (availableEffects.isEmpty()) {
            throw new IllegalStateException("WisdomBallRolls crashed trying to select a random effect.");
        }

        // Calculate total weight
        float totalWeight = 0.0f;
        for (WisdomBallRolls effect : availableEffects) {
            totalWeight += effect.getWeight();
        }

        // Random selection based on weight
        Random random = new Random();
        float randomValue = random.nextFloat() * totalWeight;

        float cumulativeWeight = 0.0f;
        for (WisdomBallRolls effect : availableEffects) {
            cumulativeWeight += effect.getWeight();
            if (randomValue <= cumulativeWeight) {
                return effect;
            }
        }

        // Fallback (should never reach here)
        return availableEffects.get(availableEffects.size() - 1);
    }

    public boolean isAvailable(){
        switch (this){
            case Single_Common, Free_Items, All_Rare: return true;
            case Copy_A_Legendary: return PlayerInventory.getInstance().getItems().values().stream().anyMatch(item -> item.getItemEnum().getItemRarity().equals(ItemRarityEnums.Legendary) && item.getQuantity() > 0);
            case Add_A_Relic: return ItemEnums.isRelicAvailable();
            case Copy_Inventory: return !PlayerInventory.getInstance().getItems().isEmpty();
            default: return false; //should NEVER reach here
        }
    }
}
