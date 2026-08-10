package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.secondary;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SecondaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.InverseRetrieval;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class CarrierSecondaryGun extends SecondaryPlayerGun {
    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            handleCarrierSpecialAttack(owner);
            spendCharge(owner);
        }
    }

    private void handleCarrierSpecialAttack(SpaceShip owner) {
        Drone carrierBeacon = FriendlyCreator.getCarrierBeacon(owner);
        if (carrierBeacon != null) {
            carrierBeacon.setVisible(false);
            placeAnimationAtCarrierBeacon(carrierBeacon);
            handleInverseRetrieval(carrierBeacon);
        } else {
            carrierBeacon = FriendlyCreator.createCarrierBeacon(owner);
            int xCoordinate = Math.round(owner.getXCoordinate() + owner.getWidth() + (carrierBeacon.getWidth() * 0.6f));
            carrierBeacon.setCenterCoordinates(xCoordinate, owner.getCenterYCoordinate());
            placeAnimationAtCarrierBeacon(carrierBeacon);
            FriendlyManager.getInstance().placeCarrierBeacon(carrierBeacon);
        }
    }

    private void handleInverseRetrieval(GameObject carrierBeacon) {
        InverseRetrieval item = (InverseRetrieval) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InverseRetrieval);
        if (item != null) {
            item.applyEffectToObject(carrierBeacon);
        }
    }

    private void placeAnimationAtCarrierBeacon(GameObject carrierDrone) {
        SpriteAnimation spriteAnimation = new SpriteAnimation(createConfig(carrierDrone.getCenterXCoordinate(), carrierDrone.getCenterYCoordinate(),
                ImageEnums.SelectNewClassAnimation, 1, false));
        spriteAnimation.setFrameDelay(1);
        spriteAnimation.setCenterCoordinates(carrierDrone.getCenterXCoordinate(), carrierDrone.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }
}
