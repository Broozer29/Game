package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;

public class VIPTicket extends Item {

    private int discount;

    public VIPTicket () {
        super(ItemEnums.VIPTicket, 1, ItemApplicationEnum.UponPurchase);
        calculateDiscount();
    }

    private void calculateDiscount () {
        this.discount = 25 * this.quantity;
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
