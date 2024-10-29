package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.visuals.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visuals.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class StickyDynamite extends Item {

    private float explosionDamage;

    public StickyDynamite () {
        super(ItemEnums.StickyDynamite, 1, ItemApplicationEnum.AfterCollision);
        this.explosionDamage = calculateExplosionDamage(quantity);
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        this.explosionDamage = calculateExplosionDamage(quantity);
    }

    private float calculateExplosionDamage (int quantity) {
        return quantity * (PlayerStats.getInstance().getBaseDamage() * 2);
    }


    private int stackCounter = 0;

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        stackCounter += 1;

        if (stackCounter >= 10) {
            ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, explosionDamage, true, false);
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setImageType(ImageEnums.StickyDynamiteExplosion);
            spriteConfiguration.setxCoordinate(gameObject.getCenterXCoordinate());
            spriteConfiguration.setyCoordinate(gameObject.getCenterYCoordinate());
            spriteConfiguration.setScale(0.5f);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
            Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
            explosion.setCenterCoordinates(gameObject.getXCoordinate() + gameObject.getWidth(), gameObject.getCenterYCoordinate());

            try {
                AudioManager.getInstance().addAudio(AudioEnums.StickyGrenadeExplosion);
            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }

            ExplosionManager.getInstance().addExplosion(explosion);
            stackCounter = 0;
        }

    }

}
