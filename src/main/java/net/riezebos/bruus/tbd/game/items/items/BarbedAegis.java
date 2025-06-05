package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

import java.util.Random;

public class BarbedAegis extends Item {

    public static float procChance = 0.25f;
    public static float procChanceIncrease = 0.05f;
    public static float damageReduction = 0.6f;
    private static Random random = new Random();

    public BarbedAegis() {
        super(ItemEnums.BarbedAegis, 1, ItemApplicationEnum.ApplyOnCreation);
    }

    @Override
    public void applyEffectToObject(GameObject targetMissile) {
        if (random.nextDouble() < (procChance + (procChanceIncrease * quantity))) {
            targetMissile.setDamage(targetMissile.getDamage() * (1 - damageReduction));
        }
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }
}
