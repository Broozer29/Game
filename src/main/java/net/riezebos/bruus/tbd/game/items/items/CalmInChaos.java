package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.OutOfCombatDamageBonus;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class CalmInChaos extends Item {
    public static float damageBonus = 0.25f;
    public static int cooldown = 3;

    public CalmInChaos () {
        super(ItemEnums.CalmInChaos, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Apply the effect to the player gameobject
        if (gameObject != null) {
            OutOfCombatDamageBonus damageBonusEffect = new OutOfCombatDamageBonus(damageBonus * quantity, EffectIdentifiers.CalmInChaosDamageBonusModifier, cooldown);
            gameObject.addEffect(damageBonusEffect);
        }
    }

    private void removeEffect () {
        //not needed as the spaceship will be deleted and recreated
    }


    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }
}
