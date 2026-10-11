package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class CalmInChaos extends Item {
    private static float targetDamagePercentage = 200f;
    public static float hitPointRequirement = 0.75f;
    public static float damageModifier = (float) (
            (targetDamagePercentage / 100f)
                    * SpecialAttack.defaultInternalTickCooldown
    );

    public CalmInChaos() {
        super(ItemEnums.CalmInChaos, 1, ItemApplicationEnum.CustomActivation);
    }


    @Override
    public void increaseQuantityOfItem(int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
//        if (gameObject != null) {
//            OutOfCombatDamageBonus damageBonusEffect = new OutOfCombatDamageBonus(damageBonus * quantity, EffectIdentifiers.CalmInChaosDamageBonusModifier, cooldown);
//            gameObject.addEffect(damageBonusEffect);
//        }
    }

    private void removeEffect() {
        //not needed as the spaceship will be deleted and recreated
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk)) {
            return false;
        }

        return true;
    }
}
