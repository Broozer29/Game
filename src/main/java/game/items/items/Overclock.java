package game.items.items;

import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;
import game.objects.player.PlayerStats;

public class Overclock extends Item {

    //Applies 10% attack speed increase

    public Overclock(){
        super(ItemEnums.Overclock, 1, EffectActivationTypes.PlayerStatsModification, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        PlayerStats.getInstance().setAttackSpeedBonus(this.quantity * 10);
    }

}