package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;

import java.util.Random;

public class BarbedMissiles extends Item {


    private Random random = new Random();
    public BarbedMissiles () {
        super(ItemEnums.BarbedMissiles, 1, ItemApplicationEnum.AfterCollision);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //instanceof is sloppy, but some gameobjects should be straight up ignored by thorns such as laser missiles or special attacks
        //can be fixed by keeping track of object types but this is just way easier without any drawbacks

        float randomNumer = random.nextFloat(0, 1.01f);
        if(randomNumer > (this.quantity * 0.10)){
            return; //We missed, dont do anyhting
        }

        if (gameObject instanceof Enemy) {
            ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(gameObject, 1);
        }
        else if(gameObject instanceof Missile){
            if(((Missile) gameObject).isDestructable()){
                ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(gameObject, 1);
            }
        }
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            Item item = PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum);
            if(item != null){
                return item.getQuantity() < 10; //we cap at 10 * 10% = 100% chance
            } else return true; //we don't have any amount yet, return true
        } else return false; //we aren't the captain class, return false
    }
}
