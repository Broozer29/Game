package game.items.items;

import VisualAndAudioData.image.ImageEnums;
import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectActivationTypes;
import game.items.effects.effecttypes.PassiveHealthRegeneration;
import game.objects.GameObject;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class SelfRepairingSteel extends Item {
    private float repairAmount;

    public SelfRepairingSteel(){
        super(ItemEnums.SelfRepairingSteel, 1, EffectActivationTypes.HealthRegeneration, ItemApplicationEnum.ApplyOnCreation);
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
        PassiveHealthRegeneration healthRegeneration = new PassiveHealthRegeneration(repairAmount);
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
