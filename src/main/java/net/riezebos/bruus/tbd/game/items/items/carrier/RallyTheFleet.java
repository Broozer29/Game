package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.ArmorModifierEffect;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.List;

public class RallyTheFleet extends Item {

    public static float attackSpeedModifier = 0.35f;
    public static int armorBonus = 15;

    public RallyTheFleet() {
        super(ItemEnums.RallyTheFleet, 1, ItemApplicationEnum.CustomActivation);
    }

    @Override
    public void applyEffectToObject(GameObject carrierDrone) {
        if(carrierDrone == null){
            return;
        }

        SpriteConfiguration pulseSpriteConfig = new SpriteConfiguration();
        pulseSpriteConfig.setxCoordinate(carrierDrone.getXCoordinate());
        pulseSpriteConfig.setyCoordinate(carrierDrone.getYCoordinate());
        pulseSpriteConfig.setScale(1);
        pulseSpriteConfig.setImageType(ImageEnums.CarrierDronePulse);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(pulseSpriteConfig, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(carrierDrone.getCenterXCoordinate(), carrierDrone.getCenterYCoordinate());

        List<Drone> ships = FriendlyManager.getInstance().getAllProtossDrones();
        carrierDrone.getAnimation().recalculateBoundsAndSize();

        for(Drone protoss : ships){
            double distance = ProtossUtils.getDistanceToRectangle(protoss.getCenterXCoordinate(), protoss.getCenterYCoordinate(), carrierDrone.getBounds());

            if(distance <= ProtossUtils.maxHoverDistance){
                protoss.addEffect(createArmorEffect());
                protoss.addEffect(createAttackSpeedEffect());
            }
        }

        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }

    private AttackSpeedModifierEffect createAttackSpeedEffect(){
        float modifierAmount = RallyTheFleet.attackSpeedModifier * this.getQuantity();
        SpriteConfiguration spriteConfig = new SpriteConfiguration();
        spriteConfig.setxCoordinate(-200);
        spriteConfig.setyCoordinate(-200);
        spriteConfig.setScale(1);
        spriteConfig.setImageType(ImageEnums.Charging);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfig, 2, true);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        return new AttackSpeedModifierEffect(modifierAmount, 3, spriteAnimation, EffectIdentifiers.CarrierBeaconAttackSpeedBonus);
    }

    private ArmorModifierEffect createArmorEffect(){
        int armorBonus = RallyTheFleet.armorBonus * this.getQuantity();
        return new ArmorModifierEffect(armorBonus, 3, null, EffectIdentifiers.CarrierBeaconArmorBonus);
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