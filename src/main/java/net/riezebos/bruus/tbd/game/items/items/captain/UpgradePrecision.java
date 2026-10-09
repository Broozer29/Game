package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyStation;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.MissileDrone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

import java.util.Random;

public class UpgradePrecision extends Item {
    public static float critChance = 0.2f;
    private Random random = new Random();

    public UpgradePrecision() {
        super(ItemEnums.UpgradePrecision, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (gameObject instanceof Missile missile && missile.getOwnerOrCreator() instanceof MissileDrone) {
            if(random.nextFloat(0, 1) < critChance){
                missile.setIsACrit(true);
            }
        } else if (gameObject instanceof Missile missile && missile.getOwnerOrCreator() instanceof FriendlyStation){
            if(random.nextFloat(0, 1) < critChance * 2){
                missile.setIsACrit(true);
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

        if (this.quantity * critChance > 1) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain);
    }

}
