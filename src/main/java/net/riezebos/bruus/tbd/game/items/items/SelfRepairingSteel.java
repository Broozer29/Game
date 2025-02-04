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
    private float repairAmount;
    private boolean shouldApply;
    private float increasedShieldRegenPerTick = 0.2f;

    public SelfRepairingSteel () {
        super(ItemEnums.SelfRepairingSteel, 1, ItemApplicationEnum.ApplyOnCreation);
        calculateHealingAmount();
        shouldApply = true;
    }

    private void calculateHealingAmount () {
        repairAmount = quantity / 2f;
    }

    @Override
    public void increaseQuantityOfItem (int amount) {
        shouldApply = true;
        removeEffect();
        this.quantity += amount;

        applyEffectToObject(null);
    }

    @Override
    public void applyEffectToObject (GameObject gameObject) {
        //Apply the effect to the player gameobject
        if (gameObject != null) {
            PassiveHealthRegeneration healthRegeneration = new PassiveHealthRegeneration(repairAmount, EffectIdentifiers.SelfRepairingSteelHealthRegeneration);
            gameObject.addEffect(healthRegeneration);

            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
            spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
            spriteConfiguration.setScale(gameObject.getScale());
            spriteConfiguration.setImageType(ImageEnums.Healing);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
            SpriteAnimation healingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            healthRegeneration.setAnimation(healingAnimation);
        }

        //Apply the effect to the playerstats instance
        if (shouldApply) {
            PlayerStats.getInstance().modifyShieldRegenMultiplier(this.quantity * increasedShieldRegenPerTick);
            shouldApply = false;
        }
    }

    private void removeEffect () {
        if (quantity > 0) {
            PlayerStats.getInstance().modifyShieldRegenMultiplier(-(this.quantity * increasedShieldRegenPerTick));
        }
    }


    @Override
    public boolean isAvailable () {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }
        return true;
    }
}
