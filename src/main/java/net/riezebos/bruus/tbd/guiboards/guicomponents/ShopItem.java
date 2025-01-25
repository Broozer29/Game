package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.guiboards.GUIComponentItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ShopItem extends GUIComponent {

    private ItemRarityEnums itemRarity;


    public ShopItem (SpriteConfiguration spriteConfiguration, ItemRarityEnums itemRarity) {
        super(spriteConfiguration);
        this.itemRarity = itemRarity;
        this.menuFunctionality = MenuFunctionEnums.PurchaseItem;
        initShopItem(spriteConfiguration);
    }

    private void initShopItem(SpriteConfiguration spriteConfiguration) {
        ItemRarityEnums actualItemRarity = switch (this.itemRarity) {
            case Common -> ItemRarityEnums.getRandomCommonItemSlot();
            case Rare -> ItemRarityEnums.getRandomRareItemSlot();
            case Legendary -> ItemRarityEnums.Legendary;
            case Relic -> ItemRarityEnums.Relic;
            default -> ItemRarityEnums.getRandomCommonItemSlot();
        };

        ItemEnums item = getRandomAvailableItemByRarity(actualItemRarity);

        this.imageEnum = item.getItemIcon();
        spriteConfiguration.setImageType(imageEnum);

        String itemDesc = ItemDescriptionRetriever.getDescriptionOfItem(item);
        this.shopItemInformation = new GUIComponentItemInformation(
                item, item.getItemRarity(), itemDesc, true, item.getItemRarity().getItemCost()
        );

        super.setImage(imageEnum);
    }


    private ItemEnums getRandomAvailableItemByRarity(ItemRarityEnums category) {
        int MAX_ATTEMPTS = 20;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            ItemEnums randomItem = ItemEnums.getRandomItemByRarity(category);

            Item tempItem = PlayerInventory.getInstance().createItemFromEnum(randomItem);

            if (tempItem != null && tempItem.isAvailable()) {
                return randomItem;
            }

            attempts++;
        }

        // If no item was found after MAX_ATTEMPTS, return an overclock
        return ItemEnums.Overclock;
    }

    public void lockItemInShop () {
        spriteConfiguration.setImageType(ImageEnums.LockedIcon);
        this.imageEnum = ImageEnums.LockedIcon;
        shopItemInformation.setItemDescription("Locked");
        shopItemInformation.setCost(0);
        shopItemInformation.setAvailable(false);
        shopItemInformation.setItemRarity(ItemRarityEnums.Locked);
        shopItemInformation.setItem(ItemEnums.Locked);
        super.setImage(imageEnum);
    }

    public void purchaseItemInShop () {
        if (shopItemInformation.isAvailable() && shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.ItemAcquired);
            PlayerInventory.getInstance().addItem(shopItemInformation.getItem());
            PlayerInventory.getInstance().spendCashMoney(shopItemInformation.getCost());
            lockItemInShop();
        } else if (shopItemInformation.isAvailable() && !shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
        }
    }

    public GUIComponentItemInformation getShopItemInformation () {
        return shopItemInformation;
    }

    @Override
    public void activateComponent () {
        AudioDatabase.getInstance().updateGameTick();
        purchaseItemInShop();
    }
}
