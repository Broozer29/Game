package game.items.items;

import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effecttypes.DamageOverTime;
import game.items.effects.effecttypes.DormentExplosion;
import game.objects.GameObject;
import game.objects.neutral.Explosion;
import game.objects.neutral.ExplosionConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class CannisterOfGasoline extends Item {

    private float explosionDamage;
    private float burningDamage;

    private int duration;

    public CannisterOfGasoline () {
        super(ItemEnums.CannisterOfGasoline, 1, EffectActivationTypes.DormentExplosion, ItemApplicationEnum.AfterCollision);
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
        return quantity;
    }

    private float calculateExplosionDamage (int quantity) {
        return 75 * quantity;
    }

    private int calculateDuration (int quantity) {
        return quantity * 5;
    }


    @Override
    public void applyEffectToObject (GameObject gameObject) {
        DormentExplosion dormentExplosion = new DormentExplosion(explosionDamage, 2, ImageEnums.Rocket_1_Explosion);
        dormentExplosion.setBurningDamage(burningDamage);
        dormentExplosion.setBurningDuration(duration);
        gameObject.addEffect(dormentExplosion);
    }

    @Override
    public void triggerEffectForOneTimeEffects (GameObject gameObject){
    }


}
