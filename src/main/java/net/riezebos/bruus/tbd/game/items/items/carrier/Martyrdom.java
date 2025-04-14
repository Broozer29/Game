package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Martyrdom extends Item {

    public static float attackSpeedIncrease = 1;
    public static float duration = 3.5f;

    public Martyrdom() {
        super(ItemEnums.Martyrdom, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        for(Drone drone : FriendlyManager.getInstance().getAllProtossDrones()){
            AttackSpeedModifierEffect attackSpeedModifierEffect = new AttackSpeedModifierEffect((attackSpeedIncrease * this.quantity),
                    duration, createAnimation(), EffectIdentifiers.MartyrdomAttackSpeed);
            drone.addEffect(attackSpeedModifierEffect);
        }
    }

    private SpriteAnimation createAnimation(){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-200);
        spriteConfiguration.setyCoordinate(-200);
        spriteConfiguration.setScale(0.4f);
        spriteConfiguration.setImageType(ImageEnums.MartyrdomAnimation); //Placeholder

        return new SpriteAnimation(new SpriteAnimationConfiguration(spriteConfiguration, 0, true));
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}