package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.primary;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.PrimaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.KineticDynamo;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class CarrierPrimaryGun extends PrimaryPlayerGun {

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireGenericBehaviour(owner);
            handleCarrierAttack(owner);
        }
    }


    private boolean carrierFastSwitch = false;
    private void handleCarrierAttack(SpaceShip owner) {
        if (!carrierFastSwitch) {
            owner.isAllowedToBuildProtoss = false;
            carrierFastSwitch = true;
            owner.modifyMovementSpeedModifier(0.6f);
            AudioManager.getInstance().addAudio(AudioEnums.ClassCarrierSpeedingUp);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchFast, owner);
            updateKineticDynamo(true, owner);
        } else if (carrierFastSwitch) {
            owner.isAllowedToBuildProtoss = true;
            carrierFastSwitch = false;
            owner.modifyMovementSpeedModifier(-0.6f);
            AudioManager.getInstance().addAudio(AudioEnums.ClassCarrierSlowingDown);
            addSwitchingGearAnimation(ImageEnums.ProtossCarrierSwitchSlow, owner);
            updateKineticDynamo(false, owner);
        }
    }

    private void updateKineticDynamo(boolean newValue, GameObject owner) {
        KineticDynamo kineticDynamo = (KineticDynamo) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.KineticDynamo);
        if (kineticDynamo != null) {
            if (!newValue) {
                kineticDynamo.applyEffectToObject(owner);
            }
            kineticDynamo.isMovingFast = newValue;
        }

    }


    private void addSwitchingGearAnimation(ImageEnums imageType, SpaceShip owner) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(owner.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(owner.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(imageType);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate());
        owner.addPlayerFollowingAnimation(spriteAnimation);
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }


    @Override
    public float getOrangeBarMaxValue(SpaceShip owner) {
        return owner.getProtossShipBuildTime();
    }

    @Override
    public float getOrangeBarCurrentValue(SpaceShip owner) {
        return owner.getProtossShipBuilderTimer();
    }
}
