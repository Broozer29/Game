package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.FreezeEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class BombasticBarrier extends Item {
    public static float modifierBonus = 2f;
    public static float stunSeconds = 2f;
    //todo needs to do more and replace the spriteanimation and item icon with actual stuff

    public BombasticBarrier() {
        super(ItemEnums.BombasticBarrier, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        Explosion explosion = createExplosion(gameObject);
        ExplosionManager.getInstance().addExplosion(explosion);
    }

    private Explosion createExplosion(GameObject gameObject){

        float baseDamage = gameObject.getDamage() * (modifierBonus * quantity);
        SpriteConfiguration explosionSpriteConfig = new SpriteConfiguration();
        explosionSpriteConfig.setImageType(ImageEnums.CarrierWarpExplosion); //todo placeholder
        explosionSpriteConfig.setxCoordinate(gameObject.getCenterXCoordinate());
        explosionSpriteConfig.setyCoordinate(gameObject.getCenterYCoordinate());
        explosionSpriteConfig.setScale(2);

        SpriteAnimationConfiguration explosionSpriteAnimationConfig = new SpriteAnimationConfiguration(explosionSpriteConfig, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, baseDamage,  false);
        Explosion explosion = new Explosion(explosionSpriteAnimationConfig, explosionConfiguration);
        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());

        SpriteConfiguration freezeEffectSpriteConfig = new SpriteConfiguration();
        freezeEffectSpriteConfig.setxCoordinate(-500); //hacky way to ensure it resizes before being moved unto the screen
        freezeEffectSpriteConfig.setyCoordinate(-500);
        freezeEffectSpriteConfig.setScale(0.3f);
        freezeEffectSpriteConfig.setImageType(ImageEnums.FreezeEffect);

        SpriteAnimationConfiguration explosionSpriteAnimConfig = new SpriteAnimationConfiguration(freezeEffectSpriteConfig, 0, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(explosionSpriteAnimConfig);

        FreezeEffect freezeEffect = new FreezeEffect(stunSeconds, spriteAnimation);
        explosion.addEffectToApply(freezeEffect);
        return explosion;
    }

    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return !PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Mutalisk);
    }
}
