package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnCoinsOnDeath;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class GreedIsGood extends Item {
    public static int mineralsPerPickup = 25;
    public static float mineralsPerPickupChance = 0.1f;

    public GreedIsGood() {
        super(ItemEnums.GreedIsGood, 1, ItemApplicationEnum.AfterCollision);
    }


    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        //Applies an effect to an object, with the applier provided for certain conditions
        if(applier.isFriendly() && target instanceof Enemy  && ((Enemy) target).getEnemyType().getEnemyCategory().equals(EnemyCategory.Medium)){
            target.addEffect(createCoinEffect());
        }
    }

    private SpawnCoinsOnDeath createCoinEffect(){
        return new SpawnCoinsOnDeath(mineralsPerPickup, 1, mineralsPerPickupChance);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null){
            return false;
        }
        return true;
    }
}