package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;

import java.util.Random;

public class BarbedMissiles extends Item {

    private Random random = new Random();
    public static float procChance = 0.25f;

    public BarbedMissiles() {
        super(ItemEnums.BarbedMissiles, 1, ItemApplicationEnum.AfterCollision);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (!PlayerStats.getInstance().isHasThornsEnabled()) {
            PlayerStats.getInstance().setHasThornsEnabled(true);
        }

        float randomNumer = random.nextFloat(0, 1.01f);
        if (randomNumer > (this.quantity * procChance)) {
            return; //We missed, dont do anyhting
        }

        if (gameObject instanceof Enemy) {
            ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(gameObject, 1);
        } else if (gameObject instanceof Missile) {
            if (((Missile) gameObject).isDestructable()) {
                ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(gameObject, 1);
            }
        }
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)) {
            Item item = PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum);
            if (item != null) {
                return this.quantity * 0.2f < 1;
            } else return true; //we don't have any amount yet, return true
        } else return false; //we aren't the captain class, return false
    }
}
