package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class HighVelocityLasers extends Item {

    public static float moveSpeedModifier = 0.2f;

    public HighVelocityLasers () {
        super(ItemEnums.HighVelocityLasers, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof Missile missile){
            missile.getMovementConfiguration().setXMovementSpeed(missile.getMovementConfiguration().getXMovementSpeed() * (1 + (moveSpeedModifier * quantity)));
            missile.getMovementConfiguration().setYMovementSpeed(missile.getMovementConfiguration().getYMovementSpeed() * (1 + (moveSpeedModifier * quantity)));
        }
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        if(PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return false;
        }
        return true;
    }

}
