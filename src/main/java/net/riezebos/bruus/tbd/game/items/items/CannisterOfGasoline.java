package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.AlienBomb;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.effects.DormentExplosionActivationMethods;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DormentExplosion;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class CannisterOfGasoline extends Item {

    private float explosionDamage;
    private float burningDamage;
    private int duration;

    public CannisterOfGasoline () {
        super(ItemEnums.CannisterOfGasoline, 1, ItemApplicationEnum.BeforeCollision); //Before collision to ensure the explosion goes off even if the target already has 0 HP
        this.explosionDamage = calculateExplosionDamage(quantity);
        this.burningDamage = calculateBurningDamage(quantity);
        this.duration = calculateDuration(quantity);
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        this.explosionDamage = calculateExplosionDamage(quantity);
        this.burningDamage = calculateBurningDamage(quantity);
        this.duration = calculateDuration(quantity);
    }

    private float calculateBurningDamage (int quantity) {
        return PlayerStats.getInstance().getBaseDamage() * 0.15f * quantity;
    }

    private float calculateExplosionDamage (int quantity) {
        return 0;
    }

    private int calculateDuration (int quantity) {
        return quantity * 2;
    }


    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if(gameObject instanceof AlienBomb){ //AlienBombs should be immune to this
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



}
