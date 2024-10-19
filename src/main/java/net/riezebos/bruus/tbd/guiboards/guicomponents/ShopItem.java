package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.util.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.guiboards.MenuItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.AudioManager;
import net.riezebos.bruus.tbd.visuals.audiodata.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class ShopItem extends GUIComponent {

    private ItemRarityEnums itemRarity;
    private MenuItemInformation menuItemInformation;

    public ShopItem (SpriteConfiguration spriteConfiguration, ItemRarityEnums itemRarity) {
        super(spriteConfiguration);
        this.itemRarity = itemRarity;
        this.menuFunctionality = MenuFunctionEnums.PurchaseItem;
        initShopItem(spriteConfiguration);
    }

    public ShopItem (SpriteAnimationConfiguration spriteAnimationConfiguration, ItemRarityEnums itemRarity) {
        super(spriteAnimationConfiguration.getSpriteConfiguration());
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
        this.itemRarity = itemRarity;
        initShopItem(spriteAnimationConfiguration.getSpriteConfiguration());
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
        this.imageType = item.getItemIcon();
        spriteConfiguration.setImageType(imageType);
        String itemDesc = ItemDescriptionRetriever.getDescriptionOfItem(item);
        this.menuItemInformation = new MenuItemInformation(item, actualItemRarity, itemDesc, true, actualItemRarity.getItemCost());
        super.loadImage(imageType);
    }

    public void lockItemInShop () {
        spriteConfiguration.setImageType(ImageEnums.LockedIcon);
        this.imageType = ImageEnums.LockedIcon;
        menuItemInformation.setItemDescription("Locked");
        menuItemInformation.setCost(0);
        menuItemInformation.setAvailable(false);
        menuItemInformation.setItemRarity(ItemRarityEnums.Locked);
        menuItemInformation.setItem(ItemEnums.Locked);
        super.loadImage(imageType);
    }

    public void purchaseItemInShop () throws UnsupportedAudioFileException, IOException {
        if (menuItemInformation.isAvailable() && menuItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.Power_Up_Acquired);
            PlayerInventory.getInstance().addItem(menuItemInformation.getItem());
            PlayerInventory.getInstance().spendCashMoney(menuItemInformation.getCost());
            lockItemInShop();
        } else if (menuItemInformation.isAvailable() && !menuItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
        }
    }

    public MenuItemInformation getMenuItemInformation () {
        return menuItemInformation;
    }

    public void activateComponent () {
        AudioDatabase.getInstance().updateGameTick();
        try {
            purchaseItemInShop();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
