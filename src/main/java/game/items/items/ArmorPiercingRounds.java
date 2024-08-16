package game.items.items;

import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.gameobjects.GameObject;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.enums.EnemyCategory;

public class ArmorPiercingRounds extends Item {

    private float damageModifier;

    public ArmorPiercingRounds(){
        super(ItemEnums.ArmorPiercingRounds, 1,ItemApplicationEnum.BeforeCollision);
        calculateDamageModifier();
    }

    private void calculateDamageModifier(){
        damageModifier = (quantity * 0.2f);
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target){
        if(target instanceof Enemy){
            if(((Enemy) target).getEnemyType().getEnemyCategory() == EnemyCategory.Mercenary){
                applier.modifyBonusDamageMultiplier(damageModifier);
            }
        }
    };
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateDamageModifier();
    }
}
