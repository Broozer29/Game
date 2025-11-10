package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.StackingInCombatArmorBonus;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.game.items.enums.ItemRarityEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class StuiversBestFriend extends Item {
    public static float explosionDamageAmount = 15;
    private boolean hasActivatedThisRound = false;

    public StuiversBestFriend() {
        super(ItemEnums.Stuivie, 1, ItemApplicationEnum.CustomActivation);
    }


    @Override
    public void increaseQuantityOfItem(int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject(GameObject gameObject) {
        //Apply the effect to the player gameobject
        if(!hasActivatedThisRound) {
            AudioManager.getInstance().addAudio(AudioEnums.ScarabExplosion);
            ExplosionManager.getInstance().addExplosion(createExplosion(gameObject));
//            gameObject.addEffect(new StackingInCombatArmorBonus(500, EffectIdentifiers.StuivieArmorBonus, 3));
            hasActivatedThisRound = true;
        }
    }

    private Explosion createExplosion(GameObject gameObject) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.CarrierWarpExplosion); //placeholder
        spriteConfiguration.setScale(2);

        float damage = (PlayerStats.getInstance().getBaseDamage() * explosionDamageAmount);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, damage,
                true, true);

        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        return explosion;
    }


    private void removeEffect() {
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        if (this.itemEnum.getItemRarity().equals(ItemRarityEnums.Relic) && PlayerInventory.getInstance().getItemFromInventoryIfExists(this.itemEnum) != null) {
            return false;
        }
        return true;
    }

    public boolean isHasActivatedThisRound() {
        return hasActivatedThisRound;
    }

    public void setHasActivatedThisRound(boolean hasActivatedThisRound) {
        this.hasActivatedThisRound = hasActivatedThisRound;
    }
}
