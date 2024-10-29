package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.ElectroShred;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.FreezeEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

public class ElectricStabilizer extends Item {
    private float duration;

    public ElectricStabilizer(){
        super(ItemEnums.ElectricDestabilizer, 1, ItemApplicationEnum.AfterCollision);
        calculateDuration();
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        if(applier instanceof ElectroShred) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(target.getXCoordinate());
            spriteConfiguration.setyCoordinate(target.getYCoordinate());
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.FreezeEffect);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
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
