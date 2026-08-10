package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class Adrenaline extends Item {

    public static float attackSpeedIncrease = 0.4f;
    public static float duration = 2f;

    public Adrenaline() {
        super(ItemEnums.Adrenaline, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (gameObject instanceof SpaceShip spaceShip) {
            //applied from the takeDamage method of SpaceShip
            EffectInterface existingEffect = spaceShip.getEffectIfExists(EffectIdentifiers.AdrenalineAttackSpeedModifier);
            if (existingEffect != null) {
                existingEffect.resetDuration();
            } else {
                AttackSpeedModifierEffect attackSpeedModifierEffect = new AttackSpeedModifierEffect(attackSpeedIncrease * this.quantity, duration, null, EffectIdentifiers.AdrenalineAttackSpeedModifier);
                //todo een animatie toevoegen
                spaceShip.addEffect(attackSpeedModifierEffect);
            }
        }
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)) {
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk)){
            return false;
        }
        return true;
    }

}
