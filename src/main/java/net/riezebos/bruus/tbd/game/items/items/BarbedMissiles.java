package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;

public class BarbedMissiles extends Item {


    public BarbedMissiles () {
        super(ItemEnums.BarbedMissiles, 1, ItemApplicationEnum.AfterCollision);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //instanceof is sloppy, but some gameobjects should be straight up ignored by thorns such as laser missiles or special attacks
        //can be fixed by keeping track of object types but this is just way easier without any drawbacks

        if (gameObject instanceof Enemy) {
            ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(gameObject, quantity);
        }
        else if(gameObject instanceof Missile){
            if(((Missile) gameObject).isDestructable()){
                ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(gameObject, quantity);
            }
        }
    }
}
