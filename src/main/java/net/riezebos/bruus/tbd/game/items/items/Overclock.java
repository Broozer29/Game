package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class Overclock extends Item {

    //Applies 10% attack speed increase

    public Overclock(){
        super(ItemEnums.Overclock, 1,  ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject){
        PlayerStats.getInstance().setAttackSpeedBonus(this.quantity * 10);
    }

}
