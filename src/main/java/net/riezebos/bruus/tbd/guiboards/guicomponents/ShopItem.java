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
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

    //The popup of a first purchase or pick, built like the contract popup of ShopManager: the item icon, with "ITEM DISCOVERED:" and the item name in menu letters under it, all fading out together
    //Each component goes through addGUIAnimation so the board stacks it above earlier popups; the letters are placed under the icon afterwards
    private void showDiscoveredPopup(ItemEnums item, Consumer<GUIComponent> addToBoard) {
        DataClass data = DataClass.getInstance();
        int centerX = data.getWindowWidth() / 2;
        int centerY = data.getWindowHeight() / 2;
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
        icon.setYCoordinate(icon.getYCoordinate() - gap - textHeight); //Makes room under the icon for the text, so the text does not cover the popup of earlier items
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

    //A small teal mark in the top right corner of a card that shows an item for sale or pick which is not in the ARMORY yet; the caller decides that with ShopkeeperWindow.isDiscovered
    public void drawNotInArmoryMark(Graphics2D g) {
        if (slot.getStatus() != ShopSlotStatusEnums.Available) {
            return;
        }
        int size = Math.max(8, getWidth() / 4);
        int x = getXCoordinate() + getWidth() - size - 2;
        int y = getYCoordinate() + 2;
        Object oldAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color dark = new Color(5, 21, 20);
        g.setColor(dark);
        g.fillOval(x - 1, y - 1, size + 2, size + 2);
        g.setColor(new Color(95, 242, 222));
        g.fillOval(x, y, size, size);
        g.setColor(dark);
        int stroke = Math.max(2, size / 6);
        g.fillRect(x + (size - stroke) / 2, y + size / 5, stroke, size / 3);
        g.fillRect(x + (size - stroke) / 2, y + size * 3 / 5, stroke, stroke);
        if (oldAntialiasing != null) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
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
