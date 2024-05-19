package game.items.items;

import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.effects.EffectIdentifiers;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;
import game.items.effects.effecttypes.DamageOverTime;
import game.objects.player.PlayerStats;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PlasmaCoatedBullets extends Item {

    private float burningDamage;
    private double duration;

    public PlasmaCoatedBullets () {
        super(ItemEnums.PlasmaCoatedBullets, 1, EffectActivationTypes.CheckEveryGameTick, ItemApplicationEnum.AfterCollision);
        calculateDuration();
        calculateBurningDamage();
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDuration();
        calculateBurningDamage();
    }

    private void calculateBurningDamage () {
        burningDamage =  PlayerStats.getInstance().getBaseDamage() * 0.1f * this.quantity;
       ;
    }

    private void calculateDuration () {
        this.duration = (double) (4 * quantity) / 2;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(gameObject.getScale() / 2);
        spriteConfiguration.setImageType(ImageEnums.PlasmaCoatedDebuff);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);

        DamageOverTime burningEffect = new DamageOverTime(0.01f, duration, spriteAnimation, EffectIdentifiers.PlasmaCoatedBulletsBurning);
//        burningEffect.getAnimation().setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        gameObject.addEffect(burningEffect);
    }

    @Override
    public void triggerEffectForOneTimeEffects (GameObject gameObject) {
        //Burning is a damage over time effect, meaning it has no "activation", in the GameObject "updateObject"
        //loop, the object takes damage
    }

    //Needed for the loop in gameobject, where it retrieves this value in order to take that amount of damage
    public float getBurningDamage () {
        return burningDamage;
    }
}
