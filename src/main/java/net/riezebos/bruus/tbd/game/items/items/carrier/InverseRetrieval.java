package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.FreezeEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class InverseRetrieval extends Item {

    public static float explosionDamageRatio = 15;
    public static int disableDuration = 2;

    public InverseRetrieval() {
        super(ItemEnums.InverseRetrieval, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if(gameObject == null){
            return;
        }
        PlayerManager.getInstance().getSpaceship().setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        ExplosionManager.getInstance().addExplosion(this.createExplosion(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate()));
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    private Explosion createExplosion(int xCoordinate, int yCoordinate){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(ImageEnums.CarrierWarpExplosion); //placeholder
        spriteConfiguration.setScale(2);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, PlayerStats.getInstance().getBaseDamage() * (this.quantity * explosionDamageRatio),
                true, true);

        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);

        explosion.setCenterCoordinates(xCoordinate, yCoordinate);

        SpriteConfiguration freezeSpriteConfig = new SpriteConfiguration();
        freezeSpriteConfig.setxCoordinate(-200);
        freezeSpriteConfig.setyCoordinate(-200);
        freezeSpriteConfig.setScale(1);
        freezeSpriteConfig.setImageType(ImageEnums.FreezeEffect);

        SpriteAnimationConfiguration freezeSpriteAnimConfig = new SpriteAnimationConfiguration(freezeSpriteConfig, 2, true);
        SpriteAnimation freezeAnim = new SpriteAnimation(freezeSpriteAnimConfig);

        FreezeEffect freezeEffect = new FreezeEffect(disableDuration, freezeAnim);
        explosion.addEffectToApply(freezeEffect);
        return explosion;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}