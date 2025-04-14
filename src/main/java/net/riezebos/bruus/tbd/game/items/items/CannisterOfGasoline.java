package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships.AlienBomb;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
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

    private float explosionDamage;
    private float burningDamage;
    private int duration;
    private boolean shouldApply = false;

    public CannisterOfGasoline () {
        super(ItemEnums.CannisterOfGasoline, 1, ItemApplicationEnum.BeforeCollision); //Before collision to ensure the explosion goes off even if the target already has 0 HP
        this.explosionDamage = calculateExplosionDamage(quantity);
        this.burningDamage = calculateBurningDamage(quantity);
        this.duration = calculateDuration(quantity);
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        removeEffect();
        this.quantity += amount;
        this.explosionDamage = calculateExplosionDamage(quantity);
        this.burningDamage = calculateBurningDamage(quantity);
        this.duration = calculateDuration(quantity);
        upgradeIgnite();
    }


    private void upgradeIgnite () {
        PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(this.quantity * 0.20f);
        PlayerStats.getInstance().modifyIgniteDurationModifier(this.quantity * 0.20f);
    }

    private void removeEffect () {
        if (quantity > 0) {
            PlayerStats.getInstance().modifyIgniteItemDamageMultiplier(-(this.quantity * 0.20f));
            PlayerStats.getInstance().modifyIgniteDurationModifier(-(this.quantity * 0.20f));
        }
    }


    private float calculateBurningDamage (int quantity) {
        PlayerClass currentPlayerClass = PlayerStats.getInstance().getPlayerClass();
        switch (currentPlayerClass) {
            case FireFighter:
                return PlayerStats.getInstance().getFireFighterIgniteDamage();
            case Captain:
            default:
                return PlayerStats.getInstance().getBaseDamage() * 0.015f * quantity;
        }
    }

    private float calculateExplosionDamage (int quantity) {
        return 0;
    }

    private int calculateDuration (int quantity) {
        return quantity * 2;
    }


    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (gameObject instanceof AlienBomb) { //AlienBombs should be immune to this
            return;
        }

        DormentExplosion dormentExplosion = new DormentExplosion(explosionDamage, ImageEnums.GasolineExplosion,
                DormentExplosionActivationMethods.OnDeath, false, EffectIdentifiers.GasolineDormantExplosion,
                0, EffectActivationTypes.OnObjectDeath, true);
        dormentExplosion.setBurningDamage(burningDamage);
        dormentExplosion.setBurningDuration(duration);
        dormentExplosion.setAudioEnums(AudioEnums.Firewall);
        gameObject.addEffect(dormentExplosion);
    }

    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }

}
