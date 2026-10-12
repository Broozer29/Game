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
        rollShop(contents, null);
    }

    // Fills every row of the given contents; the Available slots of the previous (frozen) shop are carried over
    public static void rollShop(ShopContents contents, ShopContents previous) {
        List<List<ShopSlot>> rows = new ArrayList<>();
        for (int row = 0; row < ShopContents.ROW_COUNT; row++) {
            rows.add(rollRow(row, previous == null ? null : previous.getRows().get(row)));
        }
        contents.setRows(rows);
    }

    private static List<ShopSlot> rollRow(int row, List<ShopSlot> previousRow) {
        List<ShopSlot> slots = new ArrayList<>();
        boolean rowLocked = ShopManager.getInstance().getRowsUnlocked() < row + 1;
        //A carried Rare counts as the row's Rare, so a fresh slot left of it can't roll a second one
        boolean rolledRare = !rowLocked && previousRow != null && previousRow.stream().anyMatch(ShopRoller::isCarriedRare);

        for (int i = 0; i < ShopContents.SLOTS_PER_ROW; i++) {
            if (rowLocked) {
                slots.add(new ShopSlot(ItemEnums.Locked, ItemRarityEnums.Locked, 0, ShopSlotStatusEnums.Locked));
                continue;
            }

            ShopSlot previousSlot = previousRow == null ? null : previousRow.get(i);
            if (previousSlot != null && previousSlot.getStatus() == ShopSlotStatusEnums.Available && isItemAvailable(previousSlot.getItem())) {
                //Carried over in the same slot, with its old rarity and price (a free item stays free)
                slots.add(new ShopSlot(previousSlot.getItem(), previousSlot.getRarity(), previousSlot.getCost(), ShopSlotStatusEnums.Available));
                continue;
            }

            ItemRarityEnums slotType = !rolledRare ? ItemRarityEnums.getCommonOrRareEnum() : ItemRarityEnums.Common;
            if (previousSlot != null && previousSlot.getStatus() == ShopSlotStatusEnums.Available) {
                //A carried item that is no longer offered is replaced by a roll of the same rarity slot
                slotType = previousSlot.getRarity() == ItemRarityEnums.Rare && rolledRare ? ItemRarityEnums.Common : previousSlot.getRarity();
            }
            ShopSlot slot = rollSlot(slotType);
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

    private static boolean isCarriedRare(ShopSlot slot) {
        return slot.getStatus() == ShopSlotStatusEnums.Available && slot.getRarity() == ItemRarityEnums.Rare && isItemAvailable(slot.getItem());
    }

    public static boolean isItemAvailable(ItemEnums item) {
        Item tempItem = PlayerInventory.getInstance().getItemForAvailabilityCheck(item);
        return tempItem != null && tempItem.isAvailable();
    }

    private static ItemEnums getRandomAvailableItemByRarity(ItemRarityEnums category) {
        int MAX_ATTEMPTS = 20;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            ItemEnums randomItem = ItemEnums.getRandomItemByRarity(category);

            if (isItemAvailable(randomItem)) {
                return randomItem;
            }

            attempts++;
        }

        // If no item was found after MAX_ATTEMPTS, return an overclock
        return ItemEnums.Overclock;
    }
}
