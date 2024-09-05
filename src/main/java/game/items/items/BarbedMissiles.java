package game.items.items;

import game.gameobjects.GameObject;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.missiles.Missile;
import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.util.ThornsDamageDealer;

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
