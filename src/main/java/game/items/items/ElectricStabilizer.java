package game.items.items;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.GameObject;
import game.gameobjects.missiles.specialAttacks.ElectroShred;
import game.items.Item;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.effectimplementations.DamageOverTime;
import game.items.effects.effectimplementations.FreezeEffect;
import game.items.enums.ItemApplicationEnum;
import game.items.enums.ItemEnums;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class ElectricStabilizer extends Item {
    private float duration;

    public ElectricStabilizer(){
        super(ItemEnums.ElectricDestabilizer, 1,EffectActivationTypes.AfterAHit, ItemApplicationEnum.AfterCollision);
        calculateDuration();

    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        if(applier instanceof ElectroShred) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(target.getXCoordinate());
            spriteConfiguration.setyCoordinate(target.getYCoordinate());
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.Healing);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);

            FreezeEffect freezeEffect = new FreezeEffect(duration, spriteAnimation);

            target.addEffect(freezeEffect);
        }
    }


    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        calculateDuration();
    }

    private void calculateDuration () {
        this.duration = 2f * quantity;
    }
}
