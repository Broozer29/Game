package net.riezebos.bruus.tbd.game.items.items.captain;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class PlasmaCoatedBullets extends Item {

    private float burningDamage;
    private double duration;

    public PlasmaCoatedBullets () {
        super(ItemEnums.PlasmaCoatedBullets, 1, ItemApplicationEnum.AfterCollision);
        calculateDuration();
        calculateBurningDamage();
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDuration();
        calculateBurningDamage();
    }

    private void calculateBurningDamage () {
        burningDamage =  PlayerStats.getInstance().getBaseDamage() * 0.01f * this.quantity;
    }

    private void calculateDuration () {
        this.duration = 1.5f * quantity;
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.PlasmaCoatedDebuff);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);

        DamageOverTime burningEffect = new DamageOverTime(burningDamage, duration, spriteAnimation, EffectIdentifiers.PlasmaCoatedBulletsBurning);
//        burningEffect.getAnimation().setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        gameObject.addEffect(burningEffect);
    }


    @Override
    public boolean isAvailable(){
        if(!this.itemEnum.isEnabled()){
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Captain) || PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}
