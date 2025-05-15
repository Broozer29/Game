package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships.AlienBomb;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.DormentExplosionActivationMethods;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DormentExplosion;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class CannisterOfGasoline extends Item {

    public static float igniteDurationBonus = 0.2f;

    public CannisterOfGasoline() {
        super(ItemEnums.CannisterOfGasoline, 1, ItemApplicationEnum.BeforeCollision); //Before collision to ensure the explosion goes off even if the target already has 0 HP
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        removeEffect();
        this.quantity += amount;
        upgradeIgnite();
    }


    private void upgradeIgnite() {
//        PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(this.quantity * 0.20f);
        PlayerStats.getInstance().modifyIgniteDurationModifier(this.quantity * igniteDurationBonus);
    }

    private void removeEffect() {
        if (quantity > 0) {
//            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(-(this.quantity * 0.20f));
            PlayerStats.getInstance().modifyIgniteDurationModifier(-(this.quantity * igniteDurationBonus));
        }
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if (gameObject instanceof AlienBomb) { //AlienBombs should be immune to this
            return;
        }

        DormentExplosion dormentExplosion = new DormentExplosion(0, ImageEnums.GasolineExplosion,
                DormentExplosionActivationMethods.OnDeath, false, EffectIdentifiers.GasolineDormantExplosion,
                0, EffectActivationTypes.OnObjectDeath, true);
        dormentExplosion.setBurningDamage(PlayerStats.getInstance().getIgniteDamage());
        dormentExplosion.setBurningDuration(PlayerStats.getInstance().getIgniteDuration());
        dormentExplosion.setAudioEnums(AudioEnums.Firewall);
        gameObject.addEffect(dormentExplosion);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }

}
