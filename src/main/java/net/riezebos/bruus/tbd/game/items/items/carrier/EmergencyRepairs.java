package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.ProtossConstructionSpeedEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class EmergencyRepairs extends Item {

    public static float constructionSpeedBonusMultiplier = 0.5f;
    public static float duration = 1.75f;

    public EmergencyRepairs(){
        super(ItemEnums.EmergencyRepairs, 1,  ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        gameObject.addEffect(createProtossConstructionSpeedEffect());
    }

    private EffectInterface createProtossConstructionSpeedEffect(){
        return new ProtossConstructionSpeedEffect(this.quantity * constructionSpeedBonusMultiplier, duration, null, EffectIdentifiers.EmergencyRepairsConstructionIncrease);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }
        if(!PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier)){
            return false;
        }

        return true;
    }
}