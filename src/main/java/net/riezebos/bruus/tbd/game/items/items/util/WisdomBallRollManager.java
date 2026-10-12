package net.riezebos.bruus.tbd.game.items.items.util;

import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopContents;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopSlot;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopSlotStatusEnums;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;

import java.util.*;

public class WisdomBallRollManager {

    private static WisdomBallRollManager instance = new WisdomBallRollManager();

    private WisdomBallRollManager() {

    }

    public static WisdomBallRollManager getInstance() {
        return instance;
    }

    //Returns the slots that were changed, so the shop board can play the animation at them
    public List<ShopSlot> applyWisdomBallRoll(ShopContents shopContents) {
        //The shop contents are rolled BEFORE this, so here we modify whatever the result of the refresh was
        WisdomBallRolls selectedEffect = WisdomBallRolls.getRandomEffect();

        List<List<ShopSlot>> shopItems = shopContents.getRows();
        List<ShopSlot> modifiedItems = new ArrayList<>();

        switch (selectedEffect) {
            case Copy_Inventory:
                modifiedItems.addAll(handleCopyInventory(shopItems));
                break;
            case Copy_A_Legendary:
                modifiedItems.addAll(handleCopyALegendary(shopItems));
                break;
            case All_Rare:
                modifiedItems.addAll(handleAllRare(shopItems));
                break;
            case Free_Items:
                modifiedItems.addAll(handleFreeItems(shopItems));
                break;
            case Add_A_Relic:
                modifiedItems.addAll(handleAddARelic(shopItems));
                break;
            case Single_Common:
                modifiedItems.addAll(handleSingleCommon(shopItems));
                break;
        }

        return modifiedItems;
    }

    //Makes the slot a regular available slot with the given item at its normal price
    private void setSlotToItem(ShopSlot slot, ItemEnums itemEnum) {
        slot.setItem(itemEnum);
        slot.setRarity(itemEnum.getItemRarity());
        slot.setCost(itemEnum.getItemRarity().getItemCost());
        slot.setStatus(ShopSlotStatusEnums.Available);
    }

    //If it is NOT available after a refresh, the row is NOT unlocked so we skip it
    private boolean isRowUnlocked(List<ShopSlot> shopRow) {
        return !shopRow.isEmpty() && shopRow.get(0).getStatus() != ShopSlotStatusEnums.Locked;
    }

    private List<ShopSlot> handleCopyInventory(List<List<ShopSlot>> shopItems) {
        List<ShopSlot> itemsToShowAnimAt = new ArrayList<>();
        Random random = new Random();

        // Get available items from player inventory
        List<ItemEnums> availableItemEnums = PlayerInventory.getInstance().getItems().entrySet().stream()
                .filter(entry -> entry.getValue().isAvailable())
                .filter(entry -> entry.getKey().getItemRarity() != ItemRarityEnums.Relic)
                .map(Map.Entry::getKey)
                .toList();

        if (availableItemEnums.isEmpty()) {
            return itemsToShowAnimAt;
        }

        // Create weighted list for random selection
        // 30% bonus for common, 15% bonus for rare, default for legendary
        List<ItemEnums> weightedItemList = new ArrayList<>();
        for (ItemEnums itemEnum : availableItemEnums) {
            ItemRarityEnums rarity = itemEnum.getItemRarity();
            if (rarity == ItemRarityEnums.Common) {
                // Add item multiple times for 30% bonus
                weightedItemList.add(itemEnum);
                weightedItemList.add(itemEnum);
                weightedItemList.add(itemEnum);
            } else if (rarity == ItemRarityEnums.Rare) {
                // Add item twice for 15% bonus
                weightedItemList.add(itemEnum);
                weightedItemList.add(itemEnum);
            } else {
                // Default for legendary
                weightedItemList.add(itemEnum);
            }
        }

        // For each item in each row, replace with a random item from player inventory
        for (List<ShopSlot> shopRow : shopItems) {
            if (!isRowUnlocked(shopRow)) {
                continue;
            }

            for (ShopSlot item : shopRow) {
                ItemEnums selectedItemEnum = weightedItemList.get(random.nextInt(weightedItemList.size()));

                setSlotToItem(item, selectedItemEnum);
                itemsToShowAnimAt.add(item);
            }
        }

        return itemsToShowAnimAt;
    }

    //Choose a random legendary from the players inventory and modify 1 item in the shop to become a copy of it
    private List<ShopSlot> handleCopyALegendary(List<List<ShopSlot>> shopItems) {
        List<ItemEnums> legendaryEnums = PlayerInventory.getInstance().getItems().keySet().stream()
                .filter(itemEnum -> itemEnum.getItemRarity() == ItemRarityEnums.Legendary)
                .toList();

        //no legendary in inventory, so we just default to free items instead
        if(legendaryEnums.isEmpty()) {
            return handleFreeItems(shopItems);
        }

        Random random = new Random();
        ItemEnums itemEnum = legendaryEnums.get(random.nextInt(legendaryEnums.size()));



        List<ShopSlot> eligibleShopItems = new ArrayList<>();

        for (List<ShopSlot> shopRow : shopItems) {
            if (!isRowUnlocked(shopRow)) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        ShopSlot selectedItem = eligibleShopItems.get(random.nextInt(eligibleShopItems.size()));

        setSlotToItem(selectedItem, itemEnum);

        List<ShopSlot> itemsToShowAnimAt = new ArrayList<>();
        itemsToShowAnimAt.add(selectedItem);

        return itemsToShowAnimAt;
    }

    private List<ShopSlot> handleAllRare(List<List<ShopSlot>> shopItems) {
        List<ShopSlot> itemsToShowAnimAt = new ArrayList<>();

        for (List<ShopSlot> shopRow : shopItems) {
            if (!isRowUnlocked(shopRow)) {
                continue;
            }

            for (ShopSlot item : shopRow) {
                ItemEnums itemEnum = getRandomAvailableItemByRarity(ItemRarityEnums.Rare);

                setSlotToItem(item, itemEnum);
                itemsToShowAnimAt.add(item);
            }
        }

        return itemsToShowAnimAt;
    }


    //Change the price of X amount of items to be free
    private List<ShopSlot> handleFreeItems(List<List<ShopSlot>> shopItems) {
        List<ShopSlot> eligibleShopItems = new ArrayList<>();
        List<ShopSlot> itemsToShowAnimAt = new ArrayList<>();
        Random random = new Random();

        for (List<ShopSlot> shopRow : shopItems) {
            if (!isRowUnlocked(shopRow)) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        for (int i = 0; i < 1 + ShopManager.getInstance().getRowsUnlocked(); i++) {
            ShopSlot selectedItem = eligibleShopItems.get(random.nextInt(eligibleShopItems.size()));
            selectedItem.setCost(0);
            itemsToShowAnimAt.add(selectedItem);
        }

        return itemsToShowAnimAt;
    }


    //Add a random relic that is available to the shop
    private List<ShopSlot> handleAddARelic(List<List<ShopSlot>> shopItems) {
        List<ShopSlot> eligibleShopItems = new ArrayList<>();
        List<ShopSlot> itemsToShowAnimAt = new ArrayList<>();
        Random random = new Random();

        ItemEnums itemEnum = getRandomAvailableItemByRarity(ItemRarityEnums.Relic);

        for (List<ShopSlot> shopRow : shopItems) {
            if (!isRowUnlocked(shopRow)) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        ShopSlot selectedItem = eligibleShopItems.get(random.nextInt(eligibleShopItems.size()));
        setSlotToItem(selectedItem, itemEnum);
        itemsToShowAnimAt.add(selectedItem);

        return itemsToShowAnimAt;
    }

    //Change all items in the shop to be a single common item
    private List<ShopSlot> handleSingleCommon(List<List<ShopSlot>> shopItems) {
        List<ShopSlot> eligibleShopItems = new ArrayList<>();
        List<ShopSlot> itemsToShowAnimAt = new ArrayList<>();
        ItemEnums itemEnum = getRandomAvailableItemByRarity(ItemRarityEnums.Common);

        for (List<ShopSlot> shopRow : shopItems) {
            if (!isRowUnlocked(shopRow)) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        for (ShopSlot item : eligibleShopItems) {
            setSlotToItem(item, itemEnum);
            itemsToShowAnimAt.add(item);
        }

        return itemsToShowAnimAt;
    }

    //Helper method to filter out unavailable items, originally taken from ShopManager
    private ItemEnums getRandomAvailableItemByRarity(ItemRarityEnums category) {
        int maxAttempts = 50;
        int attempts = 0;

        while (attempts < maxAttempts) {
            ItemEnums randomItem = ItemEnums.getRandomItemByRarity(category);
            Item tempItem = PlayerInventory.getInstance().getItemForAvailabilityCheck(randomItem);

            if (tempItem != null && tempItem.isAvailable()) {
                return randomItem;
            }

            attempts++;
        }

        return ItemEnums.Overclock;
    }

}
