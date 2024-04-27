package game.items.items;

import game.items.Item;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.objects.GameObject;

public class MoneyPrinter extends Item {

    private float moneyGainAmount;

    public MoneyPrinter(){
        super(ItemEnums.MoneyPrinter, 1, EffectActivationTypes.OnPlayerHit, ItemApplicationEnum.AfterCollision);
        calculateMoneyGainAmount();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PlayerInventory.getInstance().gainCashMoney(moneyGainAmount);
    }

    private void calculateMoneyGainAmount(){
        this.moneyGainAmount = quantity;
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateMoneyGainAmount();
    }

}
