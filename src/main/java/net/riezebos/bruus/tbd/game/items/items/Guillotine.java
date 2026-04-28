package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Guillotine extends Item {

    public static float hitpointsThreshold = 0.1f;

    public Guillotine() {
        super(ItemEnums.Guillotine, 1, ItemApplicationEnum.AfterCollision);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject (GameObject applier, GameObject target) {
        //Applies an effect to an object, with the applier provided for certain conditions

        if(applier.getOwnerOrCreator() != null && applier.getOwnerOrCreator() instanceof SpaceShip){
            if(target.getCurrentHitpoints() <= (target.getMaxHitPoints() * hitpointsThreshold)){
                target.takeDamage(target.getMaxHitPoints() * 999); //execute it
                target.setDestructionAnimation(null); //remove the animation and play the guillotine animation instead
                AnimationManager.getInstance().addUpperAnimation(createExecuteAnimation(target));
            }
        }
    }

    private SpriteAnimation createExecuteAnimation(GameObject target){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(target.getXCoordinate());
        spriteConfiguration.setyCoordinate(target.getYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.GuillotineEffect);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        EffectAnimationHelper.scaleAnimation(target, spriteAnimation);
        spriteAnimation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        return spriteAnimation;
    }




    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }


        if(this.quantity >= 8){ //bij meer dan 8 gaat de speler alles one-tappen dus deze niet available maken
            return false;
        }
        return true;
    }
}
