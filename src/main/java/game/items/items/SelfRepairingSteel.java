package game.items.items;

import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.effects.EffectIdentifiers;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effectimplementations.PassiveHealthRegeneration;
import game.gameobjects.GameObject;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class SelfRepairingSteel extends Item {
    private float repairAmount;

    public SelfRepairingSteel(){
        super(ItemEnums.SelfRepairingSteel, 1, EffectActivationTypes.CheckEveryGameTick, ItemApplicationEnum.ApplyOnCreation);
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
