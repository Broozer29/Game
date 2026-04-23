package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class PuncturingPierce extends Item {

    public static float damageIncreasePerMoveSpeedIncreaseModifier = 0.5f; //for every 100% move speed, gain 50% damage

    public PuncturingPierce () {
        super(ItemEnums.PuncturingPierce, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof Missile missile && missile.getOwnerOrCreator() instanceof SpaceShip){
            float difference = Math.abs(missile.getMovementConfiguration().getMovementSpeed() - missile.getMovementConfiguration().getOriginalMovementSpeed())
                    / missile.getMovementConfiguration().getOriginalMovementSpeed();
            missile.modifyBonusDamageMultiplier(this.quantity * (damageIncreasePerMoveSpeedIncreaseModifier * difference));
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

        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain)){
            return false;
        }
        return true;
    }

}
