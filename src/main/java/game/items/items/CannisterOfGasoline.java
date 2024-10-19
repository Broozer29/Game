package game.items.items;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.player.PlayerStats;
import game.items.Item;
import game.items.effects.DormentExplosionActivationMethods;
import game.items.effects.EffectIdentifiers;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effectimplementations.DormentExplosion;
import game.gameobjects.GameObject;
import game.gameobjects.enemies.enemytypes.AlienBomb;

public class CannisterOfGasoline extends Item {

    private float explosionDamage;
    private float burningDamage;
    private int duration;

    public CannisterOfGasoline () {
        super(ItemEnums.CannisterOfGasoline, 1, ItemApplicationEnum.AfterCollision);
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
