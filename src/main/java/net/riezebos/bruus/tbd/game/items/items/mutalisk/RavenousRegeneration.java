package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.MutaliskHealthRegenBonusEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;

public class RavenousRegeneration extends Item {
    public static float healingModifier = 1f;
    public static int duration = 4;

    public RavenousRegeneration(){
        super(ItemEnums.RavenousRegeneration, 1,  ItemApplicationEnum.CustomActivation); //customactiovation instead of aftercollision since collision is overriden in mutaliskmissile
    }


    @Override
    public void applyEffectToObject (GameObject gameObject) {
    }

    public EffectInterface createEffect(){
        EffectInterface effect = new MutaliskHealthRegenBonusEffect(healingModifier * quantity, duration);
        return effect;
    }

    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk);
    }
}
