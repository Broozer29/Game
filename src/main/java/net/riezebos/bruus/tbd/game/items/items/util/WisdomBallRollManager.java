package net.riezebos.bruus.tbd.game.items.items.util;

import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.GUIComponentItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;

import java.util.*;

public class WisdomBallRollManager {

    private static WisdomBallRollManager instance = new WisdomBallRollManager();

    private WisdomBallRollManager() {

    }

    public static WisdomBallRollManager getInstance() {
        return instance;
    }

    public void applyWisdomBallRoll(List<List<GUIComponent>> shopItems) {
        //Shoprows enter AFTER being refreshed, so here we modify whatever the result of the refresh was
        WisdomBallRolls selectedEffect = WisdomBallRolls.getRandomEffect();

        List<GUIComponent> modifiedItems = new ArrayList<>();

        switch (selectedEffect) {
            case Copy_Inventory:
                modifiedItems.addAll(handleCopyInventory(shopItems));
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

        playWisdomBallAnimation(modifiedItems);
    }

    private Collection<? extends GUIComponent> handleCopyInventory(List<List<GUIComponent>> shopItems) {
        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();
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
        for (List<GUIComponent> shopRow : shopItems) {
            if (shopRow.isEmpty()) {
                continue;
            }

            // If it is NOT available after a refresh, the row is NOT unlocked so we skip it
            if (!shopRow.get(0).getShopItemInformation().isAvailable()) {
                continue;
            }

            for (GUIComponent item : shopRow) {
                ItemEnums selectedItemEnum = weightedItemList.get(random.nextInt(weightedItemList.size()));

                item.setShopItemInformation(new GUIComponentItemInformation(
                        selectedItemEnum,
                        selectedItemEnum.getItemRarity(),
                        ItemDescriptionRetriever.getDescriptionOfItem(selectedItemEnum),
                        true,
                        selectedItemEnum.getItemRarity().getItemCost()
                ));
                item.setImage(selectedItemEnum.getItemIcon());
                item.setImageDimensions(ShopBoardCreator.shopItemIconDimensions, ShopBoardCreator.shopItemIconDimensions);
                itemsToShowAnimAt.add(item);
            }
        }

        return itemsToShowAnimAt;
    }

    //Choose a random legendary from the players inventory and modify 1 item in the shop to become a copy of it
    private List<GUIComponent> handleCopyALegendary(List<List<GUIComponent>> shopItems) {
        List<ItemEnums> legendaryEnums = PlayerInventory.getInstance().getItems().keySet().stream()
                .filter(itemEnum -> itemEnum.getItemRarity() == ItemRarityEnums.Legendary)
                .toList();

        //no legendary in inventory, so we just default to free items instead
        if(legendaryEnums.isEmpty()) {
            return handleFreeItems(shopItems);
        }

        Random random = new Random();
        ItemEnums itemEnum = legendaryEnums.get(random.nextInt(legendaryEnums.size()));



        List<GUIComponent> eligibleShopItems = new ArrayList<>();

        for (List<GUIComponent> shopRow : shopItems) {
            if (shopRow.isEmpty()) {
                continue;
            }

            // If it is NOT available after a refresh, the row is NOT unlocked so we skip it
            if (!shopRow.get(0).getShopItemInformation().isAvailable()) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        GUIComponent selectedItem = eligibleShopItems.get(random.nextInt(eligibleShopItems.size()));

        selectedItem.setShopItemInformation(new GUIComponentItemInformation(
                itemEnum,
                itemEnum.getItemRarity(),
                ItemDescriptionRetriever.getDescriptionOfItem(itemEnum),
                true,
                itemEnum.getItemRarity().getItemCost()
        ));
        selectedItem.setImage(itemEnum.getItemIcon());
        selectedItem.setImageDimensions(ShopBoardCreator.shopItemIconDimensions, ShopBoardCreator.shopItemIconDimensions);

        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();
        itemsToShowAnimAt.add(selectedItem);

        return itemsToShowAnimAt;
    }

    private List<GUIComponent> handleAllRare(List<List<GUIComponent>> shopItems) {
        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();

        for (List<GUIComponent> shopRow : shopItems) {
            if (shopRow.isEmpty()) {
                continue;
            }

            if (!shopRow.get(0).getShopItemInformation().isAvailable()) {
                continue;
            }

            for (GUIComponent item : shopRow) {
                ItemEnums itemEnum = getRandomAvailableItemByRarity(ItemRarityEnums.Rare);

                item.setShopItemInformation(new GUIComponentItemInformation(
                        itemEnum,
                        itemEnum.getItemRarity(),
                        ItemDescriptionRetriever.getDescriptionOfItem(itemEnum),
                        true,
                        itemEnum.getItemRarity().getItemCost()
                ));
                item.setImage(itemEnum.getItemIcon());
                item.setImageDimensions(ShopBoardCreator.shopItemIconDimensions, ShopBoardCreator.shopItemIconDimensions);
                itemsToShowAnimAt.add(item);
            }
        }

        return itemsToShowAnimAt;
    }


    //Change the price of X amount of items to be free
    private List<GUIComponent> handleFreeItems(List<List<GUIComponent>> shopItems) {
        List<GUIComponent> eligibleShopItems = new ArrayList<>();
        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();
        Random random = new Random();

        for (List<GUIComponent> shopRow : shopItems) {
            if (shopRow.isEmpty()) {
                continue;
            }

            // If it is NOT available after a refresh, the row is NOT unlocked so we skip it
            if (!shopRow.get(0).getShopItemInformation().isAvailable()) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        for (int i = 0; i < 1 + ShopManager.getInstance().getRowsUnlocked(); i++) {
            GUIComponent selectedItem = eligibleShopItems.get(random.nextInt(eligibleShopItems.size()));
            selectedItem.getShopItemInformation().setCost(0);
            itemsToShowAnimAt.add(selectedItem);
        }

        return itemsToShowAnimAt;
    }


    //Add a random relic that is available to the shop
    private List<GUIComponent> handleAddARelic(List<List<GUIComponent>> shopItems) {
        List<GUIComponent> eligibleShopItems = new ArrayList<>();
        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();
        Random random = new Random();

        ItemEnums itemEnum = getRandomAvailableItemByRarity(ItemRarityEnums.Relic);

        for (List<GUIComponent> shopRow : shopItems) {
            if (shopRow.isEmpty()) {
                continue;
            }

            // If it is NOT available after a refresh, the row is NOT unlocked so we skip it
            if (!shopRow.get(0).getShopItemInformation().isAvailable()) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        GUIComponent selectedItem = eligibleShopItems.get(random.nextInt(eligibleShopItems.size()));
        selectedItem.setShopItemInformation(new GUIComponentItemInformation(
                itemEnum,
                itemEnum.getItemRarity(),
                ItemDescriptionRetriever.getDescriptionOfItem(itemEnum),
                true,
                itemEnum.getItemRarity().getItemCost()
        ));
        selectedItem.setImage(itemEnum.getItemIcon());
        selectedItem.setImageDimensions(ShopBoardCreator.shopItemIconDimensions, ShopBoardCreator.shopItemIconDimensions);
        itemsToShowAnimAt.add(selectedItem);

        return itemsToShowAnimAt;
    }

    //Change all items in the shop to be a single common item
    private List<GUIComponent> handleSingleCommon(List<List<GUIComponent>> shopItems) {
        List<GUIComponent> eligibleShopItems = new ArrayList<>();
        List<GUIComponent> itemsToShowAnimAt = new ArrayList<>();
        ItemEnums itemEnum = getRandomAvailableItemByRarity(ItemRarityEnums.Common);

        for (List<GUIComponent> shopRow : shopItems) {
            if (shopRow.isEmpty()) {
                continue;
            }

            // If it is NOT available after a refresh, the row is NOT unlocked so we skip it
            if (!shopRow.get(0).getShopItemInformation().isAvailable()) {
                continue;
            }

            eligibleShopItems.addAll(shopRow);
        }

        for (GUIComponent item : eligibleShopItems) {
            item.setShopItemInformation(new GUIComponentItemInformation(
                    itemEnum,
                    itemEnum.getItemRarity(),
                    ItemDescriptionRetriever.getDescriptionOfItem(itemEnum),
                    true,
                    itemEnum.getItemRarity().getItemCost()
            ));
            item.setImage(itemEnum.getItemIcon());
            item.setImageDimensions(ShopBoardCreator.shopItemIconDimensions, ShopBoardCreator.shopItemIconDimensions);
            itemsToShowAnimAt.add(item);
        }

        return itemsToShowAnimAt;
    }

    private void playWisdomBallAnimation(List<GUIComponent> itemsToShowAnimAt) {
        BoardManager.getInstance().getShopBoard().playWisdomBallAnimation(itemsToShowAnimAt);
    }

    //Helper method to filter out unavailable items, originally taken from ShopManager
    private ItemEnums getRandomAvailableItemByRarity(ItemRarityEnums category) {
        int maxAttempts = 50;
        int attempts = 0;

        while (attempts < maxAttempts) {
            ItemEnums randomItem = ItemEnums.getRandomItemByRarity(category);
            Item tempItem = PlayerInventory.getInstance().createItemFromEnum(randomItem);

            if (tempItem != null && tempItem.isAvailable()) {
                return randomItem;
            }

            attempts++;
        }

        return ItemEnums.Overclock;
    }

}
