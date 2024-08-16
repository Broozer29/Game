package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.managers.ShopManager;
import guiboards.BoardManager;
import guiboards.boards.ShopBoard;

public class VIPTicket extends Item {

    private int discount;

    public VIPTicket () {
        super(ItemEnums.VIPTicket, 1, ItemApplicationEnum.ApplyOnCreation);
        calculateDiscount();
    }

    private void calculateDiscount () {
        this.discount = 20 * this.quantity;
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDiscount();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerStats.getInstance().setShopRerollDiscount(discount);
        ShopManager.getInstance().calculateRerollCost();
        BoardManager.getInstance().getShopBoard().remakeShopRerollText();
    }
}
