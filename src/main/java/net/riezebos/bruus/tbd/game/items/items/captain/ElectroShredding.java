package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class ElectroShredding extends Item {

    public static float bonusDamage = 1;

    public ElectroShredding() {
        super(ItemEnums.ElectroShredding, 1, ItemApplicationEnum.CustomActivation);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
//        ArmorModifierEffect armorModifierEffect = new ArmorModifierEffect(-bonusDamage, 9999999, null, EffectIdentifiers.ElectroShedding);
//        gameObject.addEffect(armorModifierEffect);
    }

    @Override
    public void applyEffectToObject(GameObject applier, GameObject target) {
//        if (applier instanceof ElectroShred) {
//            ArmorModifierEffect armorModifierEffect = new ArmorModifierEffect(-bonusDamage, 9999999, null, EffectIdentifiers.ElectroShedding);
//            target.addEffect(armorModifierEffect);
//        }
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }
}
