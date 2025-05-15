package net.riezebos.bruus.tbd.game.items.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.PassiveHealthRegeneration;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class SelfRepairingSteel extends Item {
    public static float repairAmount = 0.0075f;

    public SelfRepairingSteel () {
        super(ItemEnums.SelfRepairingSteel, 1, ItemApplicationEnum.ApplyOnCreation);
    }


    @Override
    public void increaseQuantityOfItem (int amount) {
        removeEffect();
        this.quantity += amount;
        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Apply the effect to the player gameobject
        if (gameObject != null) {
            PassiveHealthRegeneration healthRegeneration = new PassiveHealthRegeneration(repairAmount * quantity, EffectIdentifiers.SelfRepairingSteelHealthRegeneration);
            gameObject.addEffect(healthRegeneration);

            //Animatie verwijderd omdat het nu permanent applied is
//            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
//            spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
//            spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
//            spriteConfiguration.setScale(gameObject.getScale());
//            spriteConfiguration.setImageType(ImageEnums.Healing);
//            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
//            SpriteAnimation healingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
//            healthRegeneration.setAnimation(healingAnimation);
        }

        //Apply the effect to the playerstats instance
    }

    private void removeEffect () {
    }


    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }
}
