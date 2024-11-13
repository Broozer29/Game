package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.util.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.guiboards.ShopItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ShopItem extends GUIComponent {

    private ItemRarityEnums itemRarity;
    private ShopItemInformation shopItemInformation;

    public ShopItem (SpriteConfiguration spriteConfiguration, ItemRarityEnums itemRarity) {
        super(spriteConfiguration);
        this.itemRarity = itemRarity;
        this.menuFunctionality = MenuFunctionEnums.PurchaseItem;
        initShopItem(spriteConfiguration);
    }

    private void initShopItem (SpriteConfiguration spriteConfiguration) {
        ItemRarityEnums actualItemRarity = null;
        switch (this.itemRarity) {
            case Common -> actualItemRarity = ItemRarityEnums.getRandomCommonItemSlot();
            case Rare -> actualItemRarity = ItemRarityEnums.getRandomRareItemSlot();
            case Legendary -> actualItemRarity = ItemRarityEnums.Legendary;
            default -> actualItemRarity = ItemRarityEnums.getRandomCommonItemSlot();
        }


        ItemEnums item = ItemEnums.getRandomItemByRarity(actualItemRarity);
        this.imageEnum = item.getItemIcon();
        spriteConfiguration.setImageType(imageEnum);
        String itemDesc = ItemDescriptionRetriever.getDescriptionOfItem(item);
        this.shopItemInformation = new ShopItemInformation(item, actualItemRarity, itemDesc, true, actualItemRarity.getItemCost());
        super.loadImage(imageEnum);
    }

    public void lockItemInShop () {
        spriteConfiguration.setImageType(ImageEnums.LockedIcon);
        this.imageEnum = ImageEnums.LockedIcon;
        shopItemInformation.setItemDescription("Locked");
        shopItemInformation.setCost(0);
        shopItemInformation.setAvailable(false);
        shopItemInformation.setItemRarity(ItemRarityEnums.Locked);
        shopItemInformation.setItem(ItemEnums.Locked);
        super.loadImage(imageEnum);
    }

    public void purchaseItemInShop () {
        if (shopItemInformation.isAvailable() && shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
            PlayerInventory.getInstance().addItem(shopItemInformation.getItem());
            PlayerInventory.getInstance().spendCashMoney(shopItemInformation.getCost());
            lockItemInShop();
        } else if (shopItemInformation.isAvailable() && !shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
        }
    }

    public ShopItemInformation getShopItemInformation () {
        return shopItemInformation;
    }

    @Override
    public void activateComponent () {
        AudioDatabase.getInstance().updateGameTick();
        purchaseItemInShop();
    }
}
