package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnRecyclePartOnDeath;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class Recycler  extends Item {

    public static float spawnChance = 0.10f;

    public Recycler() {
        super(ItemEnums.Recycler, 1, ItemApplicationEnum.BeforeCollision);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target){
        //apply the effect to the applier, so that the applier applies it to the target and not this item itself
        if(target.getEffectIfExists(EffectIdentifiers.SpawnRecyclePartOnDeath) == null){
            SpawnRecyclePartOnDeath spawnRecyclePartOnDeath = new SpawnRecyclePartOnDeath();
            target.addEffect(spawnRecyclePartOnDeath);
        }
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        if((this.quantity * spawnChance) >= 1){
            return false;
        }
        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}