package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class PyrrhicProtocol extends Item {

    public static float explosionDamageRatio = 15;

    public PyrrhicProtocol() {
        super(ItemEnums.PyrrhicProtocol, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        if(gameObject == null){
            return;
        }

        SpriteConfiguration explosionSpriteConfig = new SpriteConfiguration();
        explosionSpriteConfig.setxCoordinate(gameObject.getXCoordinate());
        explosionSpriteConfig.setyCoordinate(gameObject.getYCoordinate());
        explosionSpriteConfig.setScale(1);
        explosionSpriteConfig.setImageType(ImageEnums.CarrierDroneExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(explosionSpriteConfig, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true,
                PlayerStats.getInstance().getBaseDamage() * (this.getQuantity() * PyrrhicProtocol.explosionDamageRatio),
                true, false);
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setOwnerOrCreator(gameObject.getOwnerOrCreator());
        explosion.setScale(1);
        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        ExplosionManager.getInstance().addExplosion(explosion);
        AudioManager.getInstance().addAudio(AudioEnums.ProtossShipDeath); //Placeholder
    }

    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}