package net.riezebos.bruus.tbd.game.gamestate.shop;

import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

import java.util.ArrayList;
import java.util.List;

// The rolling rules of the shop: the rare-slot rule, rows locked by difficulty and Help Requested's free Contracts.
public class ShopRoller {

    private ShopRoller() {
    }

    // Fills every row of the given contents with freshly rolled slots
    public static void rollShop(ShopContents contents) {
        List<List<ShopSlot>> rows = new ArrayList<>();
        for (int row = 0; row < ShopContents.ROW_COUNT; row++) {
            rows.add(rollRow(row));
        }
        contents.setRows(rows);
    }

    private static List<ShopSlot> rollRow(int row) {
        List<ShopSlot> slots = new ArrayList<>();
        boolean rolledRare = false;
        boolean rowLocked = ShopManager.getInstance().getRowsUnlocked() < row + 1;

        for (int i = 0; i < ShopContents.SLOTS_PER_ROW; i++) {
            if (rowLocked) {
                slots.add(new ShopSlot(ItemEnums.Locked, ItemRarityEnums.Locked, 0, ShopSlotStatusEnums.Locked));
                continue;
            }

            ShopSlot slot = rollSlot(!rolledRare ? ItemRarityEnums.getCommonOrRareEnum() : ItemRarityEnums.Common);
            if (slot.getRarity() == ItemRarityEnums.Rare) {
                rolledRare = true;
            }

            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.HelpRequested) != null && slot.getItem().equals(ItemEnums.Contract)) {
                slot.setCost(0);
            }
            slots.add(slot);
        }
        return slots;
    }

    // Rolls one available slot for the given slot type (Common, Rare, Legendary or Relic)
    public static ShopSlot rollSlot(ItemRarityEnums slotType) {
        ItemRarityEnums actualItemRarity = switch (slotType) {
            case Common -> ItemRarityEnums.getRandomCommonItemSlot();
            case Rare -> ItemRarityEnums.getRandomRareItemSlot();
            case Legendary -> ItemRarityEnums.Legendary;
            case Relic -> ItemRarityEnums.Relic;
            default -> ItemRarityEnums.getRandomCommonItemSlot();
        };

        ItemEnums item = getRandomAvailableItemByRarity(actualItemRarity);
        return new ShopSlot(item, item.getItemRarity(), item.getItemRarity().getItemCost(), ShopSlotStatusEnums.Available);
    }

    private static ItemEnums getRandomAvailableItemByRarity(ItemRarityEnums category) {
        int MAX_ATTEMPTS = 20;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            ItemEnums randomItem = ItemEnums.getRandomItemByRarity(category);

            Item tempItem = PlayerInventory.getInstance().getItemForAvailabilityCheck(randomItem);

            if (tempItem != null && tempItem.isAvailable()) {
                return randomItem;
            }

            attempts++;
        }

        // If no item was found after MAX_ATTEMPTS, return an overclock
        return ItemEnums.Overclock;
    }
}
