package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.PassiveHealthRegeneration;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class SelfRepairingSteel extends Item {
    private float repairAmount;

    public SelfRepairingSteel(){
        super(ItemEnums.SelfRepairingSteel, 1, ItemApplicationEnum.ApplyOnCreation);
        calculateHealingAmount();
    }

    private void calculateHealingAmount(){
        repairAmount = quantity / 2f;
    }

    public void increaseQuantityOfItem(int amount){
        this.quantity += amount;
        calculateHealingAmount();
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        PassiveHealthRegeneration healthRegeneration = new PassiveHealthRegeneration(repairAmount, EffectIdentifiers.SelfRepairingSteelHealthRegeneration);
        gameObject.addEffect(healthRegeneration);

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(gameObject.getScale());
        spriteConfiguration.setImageType(ImageEnums.Healing);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2 ,true);
        SpriteAnimation healingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        healthRegeneration.setAnimation(healingAnimation);

    }
}
