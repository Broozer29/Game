package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.gamestate.save.SaveManager;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopContents;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopSlot;
import net.riezebos.bruus.tbd.game.gamestate.shop.ShopSlotStatusEnums;
import net.riezebos.bruus.tbd.game.items.ItemDescriptionRetriever;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.game.items.items.BonusKaart;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.GUIComponentItemInformation;
import net.riezebos.bruus.tbd.guiboards.boardEnums.MenuFunctionEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import static net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator.shopItemIconDimensions;

public class ShopItem extends GUIComponent {

    private final ShopSlot slot;

    public ShopItem(SpriteConfiguration spriteConfiguration, ShopSlot slot) {
        super(spriteConfiguration);
        this.slot = slot;
        this.menuFunctionality = MenuFunctionEnums.PurchaseItem;
        applySlot();
    }

    public ShopSlot getSlot() {
        return slot;
    }

    //Draws this button from its slot; a bought or locked slot looks locked
    public void applySlot() {
        if (slot.getStatus() == ShopSlotStatusEnums.Available) {
            this.imageEnum = slot.getItem().getItemIcon();
            this.shopItemInformation = new GUIComponentItemInformation(
                    slot.getItem(), slot.getRarity(), ItemDescriptionRetriever.getDescriptionOfItem(slot.getItem()), true, slot.getCost()
            );
        } else {
            this.imageEnum = ImageEnums.LockedIcon;
            this.shopItemInformation = new GUIComponentItemInformation(
                    ItemEnums.Locked, ItemRarityEnums.Locked, "Locked", false, 0
            );
        }

        spriteConfiguration.setImageType(imageEnum);
        super.setImage(imageEnum);
        super.setImageDimensions(shopItemIconDimensions, shopItemIconDimensions);
    }

    public void purchaseItemInShop() {
        if (shopItemInformation.getItemRarity().equals(ItemRarityEnums.Relic) && GameState.getInstance().getGameState().equals(GameStatusEnums.SelectingRelic)) {
            PlayerInventory.getInstance().addItem(shopItemInformation.getItem());
            SaveManager.getInstance().exportCurrentSave();
            AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
            BoardManager.getInstance().getGameBoard().signalThatRelicHasBeenChosen(); //sloppy implementation, this way, Relic shopitems are not compatible with shopboard as the active screen
        } else if (shopItemInformation.isAvailable() && shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);

            BonusKaart bonusKaart = (BonusKaart) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BonusKaart);
            ShopContents contents = ShopManager.getInstance().getContents();
            int amountOfTimes = 1;
            if (bonusKaart != null && !contents.isBonusKaartUsed()) {
                amountOfTimes += 1;
                contents.setBonusKaartUsed(true);
            }

            for(int i = 0; i < amountOfTimes; i++){
                PlayerInventory.getInstance().addItem(shopItemInformation.getItem());
            }

            PlayerInventory.getInstance().spendCashMoney(shopItemInformation.getCost());
            slot.setStatus(ShopSlotStatusEnums.Bought);
            applySlot();
            SaveManager.getInstance().exportCurrentSave();
        } else if (shopItemInformation.isAvailable() && !shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
        }
    }

    public GUIComponentItemInformation getShopItemInformation() {
        return shopItemInformation;
    }

    @Override
    public void activateComponent() {
        AudioDatabase.getInstance().updateGameTick();
        purchaseItemInShop();
    }
}
