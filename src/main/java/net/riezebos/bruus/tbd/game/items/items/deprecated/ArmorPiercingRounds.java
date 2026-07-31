package net.riezebos.bruus.tbd.game.items.items.deprecated;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ArmorPiercingRounds extends Item {

    public static float damageModifier = 0.25f;

    public ArmorPiercingRounds () {
        super(ItemEnums.ArmorPiercingRounds, 1, ItemApplicationEnum.BeforeCollision);
    }


    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target) {
        if (target instanceof Enemy enemy && enemy.getEnemyType().getEnemyCategory() == EnemyCategory.MiniBoss) {
            applier.modifyBonusDamageMultiplier(quantity * damageModifier);
        }
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter)){
            return false;
        }

        return true;
    }
}
