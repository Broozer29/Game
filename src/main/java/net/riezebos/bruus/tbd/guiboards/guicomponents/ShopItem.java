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
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

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
            ItemEnums pickedRelic = shopItemInformation.getItem();
            PlayerInventory.getInstance().addItem(pickedRelic);
            boolean relicDiscovered = PlayerInventory.getInstance().discoverItem(pickedRelic);
            SaveManager.getInstance().exportCurrentSave();
            AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
            BoardManager.getInstance().getGameBoard().signalThatRelicHasBeenChosen(); //sloppy implementation, this way, Relic shopitems are not compatible with shopboard as the active screen
            if (relicDiscovered) {
                showDiscoveredPopup(pickedRelic, popup -> BoardManager.getInstance().getGameBoard().addGUIAnimation(popup));
            }
        } else if (shopItemInformation.isAvailable() && shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);

            BonusKaart bonusKaart = (BonusKaart) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BonusKaart);
            ShopContents contents = ShopManager.getInstance().getContents();
            int amountOfTimes = 1;
            if (bonusKaart != null && !contents.isBonusKaartUsed()) {
                amountOfTimes += 1;
                contents.setBonusKaartUsed(true);
            }

            ItemEnums boughtItem = shopItemInformation.getItem(); //applySlot below replaces the information with the locked one
            for(int i = 0; i < amountOfTimes; i++){
                PlayerInventory.getInstance().addItem(boughtItem);
            }
            boolean itemDiscovered = PlayerInventory.getInstance().discoverItem(boughtItem);

            PlayerInventory.getInstance().spendCashMoney(shopItemInformation.getCost());
            slot.setStatus(ShopSlotStatusEnums.Bought);
            applySlot();
            SaveManager.getInstance().exportCurrentSave();
            if (itemDiscovered) {
                showDiscoveredPopup(boughtItem, popup -> BoardManager.getInstance().getShopBoard().addGUIAnimation(popup));
            }
        } else if (shopItemInformation.isAvailable() && !shopItemInformation.canAfford()) {
            AudioManager.getInstance().addAudio(AudioEnums.NotEnoughMinerals);
        }
    }

    //The popup of a first purchase or pick, built like the contract popup of ShopManager: the item icon, with "ITEM DISCOVERED:" and the item name in menu letters under it, all fading out together, placed just above the card that was bought or picked
    //Each component goes through addGUIAnimation so the board stacks it above earlier popups; the letters are placed under the icon afterwards
    private void showDiscoveredPopup(ItemEnums item, Consumer<GUIComponent> addToBoard) {
        DataClass data = DataClass.getInstance();
        int centerX = getCenterXCoordinate(); //The popup rises from this card, the one just bought or picked
        int centerY = getYCoordinate();
        float fadeStep = -0.00175f; //The same fade as the contract popup

        SpriteConfiguration iconConfiguration = new SpriteConfiguration();
        iconConfiguration.setxCoordinate(centerX);
        iconConfiguration.setyCoordinate(centerY);
        iconConfiguration.setScale(1);
        iconConfiguration.setImageType(item.getItemIcon());
        GUIComponent icon = new GUIComponent(iconConfiguration);
        icon.setImageDimensions(shopItemIconDimensions, shopItemIconDimensions);
        icon.setTransparancyAlpha(true, 1, fadeStep);
        icon.setCenterCoordinates(centerX, centerY);

        float textScale = data.getResolutionFactor() * MenuText.TEXT_SCALE;
        GUITextCollection titleText = MenuText.createText("ITEM DISCOVERED:", centerX, 0, textScale, MenuText.ALIGN_CENTER);
        String itemName = toMenuLetters(item.getItemName());
        GUITextCollection nameText = itemName.trim().isEmpty() ? null : MenuText.createText(itemName, centerX, 0, textScale, MenuText.ALIGN_CENTER);
        int gap = Math.round(4 * data.getResolutionFactor());
        int lineHeight = titleText.getHeight();
        int textHeight = lineHeight + (nameText == null ? 0 : gap + lineHeight);

        addToBoard.accept(icon);
        icon.setYCoordinate(Math.max(0, getYCoordinate() - icon.getHeight() - 2 * gap - textHeight)); //Icon and text stand together just above the card
        int titleTop = icon.getYCoordinate() + icon.getHeight() + gap;
        placeLetters(titleText, titleTop, fadeStep, addToBoard);
        if (nameText != null) {
            placeLetters(nameText, titleTop + lineHeight + gap, fadeStep, addToBoard);
        }
    }

    private static void placeLetters(GUITextCollection text, int top, float fadeStep, Consumer<GUIComponent> addToBoard) {
        for (GUIComponent letter : text.getComponents()) {
            letter.setTransparancyAlpha(true, 1, fadeStep);
            addToBoard.accept(letter);
            letter.setYCoordinate(top); //After the board placed it, which would stack every letter above the one before
        }
    }

    //The menu letters hold no symbols such as ' or -, so those are left out of the name
    private static String toMenuLetters(String text) {
        StringBuilder letters = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (character == ' ') {
                letters.append(character);
                continue;
            }
            try {
                ImageEnums.fromChar(character);
                letters.append(character);
            } catch (IllegalArgumentException e) {
                //No letter image for this character
            }
        }
        return letters.toString();
    }

    //The quest mark (an exclamation point) in the top right corner of a card that shows an item for sale or pick which is not in the ARMORY yet; the caller decides that with ShopkeeperWindow.isDiscovered
    public void drawNotInArmoryMark(Graphics2D g) {
        if (slot.getStatus() != ShopSlotStatusEnums.Available) {
            return;
        }
        BufferedImage icon = ImageDatabase.getInstance().getImage(ImageEnums.QuestMarkIcon);
        if (icon == null) {
            return;
        }
        int height = Math.max(13, Math.round(26 * DataClass.getInstance().getResolutionFactor()));
        int width = Math.max(1, Math.round(height * (float) icon.getWidth() / icon.getHeight()));
        int x = getXCoordinate() + getWidth() - width - 2;
        int y = getYCoordinate() + 2;
        Object oldInterpolation = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(icon, x, y, width, height, null);
        if (oldInterpolation != null) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
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
