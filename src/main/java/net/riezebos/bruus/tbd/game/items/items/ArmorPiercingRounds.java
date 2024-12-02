package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;

public class ArmorPiercingRounds extends Item {

    private float damageModifier;

    public ArmorPiercingRounds () {
        super(ItemEnums.ArmorPiercingRounds, 1, ItemApplicationEnum.BeforeCollision);
        calculateDamageModifier();
    }

    private void calculateDamageModifier () {
        damageModifier = (quantity * 0.2f);
    }

    @Override
    public void modifyAttackingObject (GameObject applier, GameObject target) {
        if (target instanceof Enemy enemy && enemy.getEnemyType().getEnemyCategory() == EnemyCategory.Mercenary) {
            applier.modifyBonusDamageMultiplier(damageModifier);
        }
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDamageModifier();
    }
}
