package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class StickyDynamite extends Item {

    private float explosionDamage;
    private float scaleFactor;
    private float chanceToProc;
    private Random random = new Random();

    public StickyDynamite () {
        super(ItemEnums.StickyDynamite, 1, ItemApplicationEnum.AfterCollision);
        this.chanceToProc = 0.1f;
        this.explosionDamage = calculateExplosionDamage(quantity);
        calculateScaleFactor();
    }

    public void increaseQuantityOfItem (int amount) {
        this.quantity += amount;
        this.explosionDamage = calculateExplosionDamage(quantity);
        calculateScaleFactor();
    }

    private void calculateScaleFactor(){
        scaleFactor = 0.9f + (quantity * 0.1f);
    }

    private float calculateExplosionDamage (int quantity) {
        return quantity * (PlayerStats.getInstance().getBaseDamage() * 2);
    }



    @Override
    public void applyEffectToObject (GameObject gameObject) {
        if (random.nextFloat() <= chanceToProc) {
            ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, explosionDamage, true, false);
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setImageType(ImageEnums.StickyDynamiteExplosion);
            spriteConfiguration.setxCoordinate(gameObject.getCenterXCoordinate());
            spriteConfiguration.setyCoordinate(gameObject.getCenterYCoordinate());
            spriteConfiguration.setScale(0.5f * scaleFactor);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
            Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
            explosion.setCenterYCoordinate(gameObject.getCenterYCoordinate());
            explosion.setXCoordinate(gameObject.getXCoordinate());

            AudioManager.getInstance().addAudio(AudioEnums.StickyGrenadeExplosion);

            ExplosionManager.getInstance().addExplosion(explosion);
        }

    }

}
