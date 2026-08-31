package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.PassiveHealthRegeneration;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class RegenerativeSteel extends Item {
    public static float repairAmount = 0.0115f;

    public RegenerativeSteel() {
        super(ItemEnums.RegenerativeSteel, 1, ItemApplicationEnum.ApplyOnSpaceShipCreation);
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
            PassiveHealthRegeneration healthRegeneration = new PassiveHealthRegeneration(repairAmount * quantity, EffectIdentifiers.SelfRepairingSteelHealthRegeneration);
            gameObject.addEffect(healthRegeneration);
        }
    }

    private void removeEffect () {
    }


    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk)){
            return false;
        }

        return true;
    }
}
