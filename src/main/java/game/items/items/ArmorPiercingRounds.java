package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.objects.GameObject;
import game.objects.enemies.Enemy;
import game.objects.enemies.enums.EnemyCategory;

public class ArmorPiercingRounds extends Item {

    private float damageModifier;

    public ArmorPiercingRounds(){
        super(ItemEnums.ArmorPiercingRounds, 1, EffectActivationTypes.DamageModification, ItemApplicationEnum.AfterCollision);
        calculateDamageModifier();
    }

    private void calculateDamageModifier(){
        damageModifier = 1 + (quantity * 0.2f);
    }

    @Override
    public void modifyAttackValues (GameObject attack, GameObject target){
        if(target instanceof Enemy){
            if(((Enemy) target).getEnemyType().getEnemyCategory() == EnemyCategory.Boss){
                attack.setDamage(attack.getDamage() * damageModifier);
            }
        }
    };
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateDamageModifier();
    }
}
