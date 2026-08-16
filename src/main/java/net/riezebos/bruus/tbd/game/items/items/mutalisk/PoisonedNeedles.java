package net.riezebos.bruus.tbd.game.items.items.mutalisk;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageOverTime;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class PoisonedNeedles extends Item {

    public static float hpDamageRatio = 0.0005f; // 0.0005 = 5%

    public PoisonedNeedles() {
        super(ItemEnums.PoisonedNeedles, 1, ItemApplicationEnum.AfterCollision);
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public void applyEffectToObject(GameObject applier, GameObject target) {

        if(!target.isVisible() || target.getCurrentHitpoints() <= 0){
            return; //target is already dead
        }

        if(applier.getOwnerOrCreator() != null && applier.getOwnerOrCreator() instanceof SpaceShip) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(target.getXCoordinate());
            spriteConfiguration.setyCoordinate(target.getYCoordinate());
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.MutaliskPoison);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);

            //damage is overwritten because its HP% based
            DamageOverTime burningEffect = new DamageOverTime(0, 999999999, spriteAnimation, EffectIdentifiers.PoisonedNeedlesDoT);
            if (burningEffect.getAnimations().get(0) != null) {
                burningEffect.getAnimations().get(0).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
            }
            target.addEffect(burningEffect);
        }
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk);
    }
}
