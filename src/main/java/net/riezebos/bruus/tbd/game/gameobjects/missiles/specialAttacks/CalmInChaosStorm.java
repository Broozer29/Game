package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.CalmInChaos;
import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class CalmInChaosStorm extends SpecialAttack {


    private float originalDamage = 0;
    private CalmInChaosStorm(SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration missileConfiguration, SpaceShip owner) {
        super(spriteAnimationConfiguration, missileConfiguration);
        this.setObjectType("FireShield");
        this.allowRepeatedDamage = true;
        this.appliesOnHitEffects = false;
        this.destroysMissiles = false;
        this.damagesMissiles = false;
        this.visualLayer = VisualLayer.Lower;
        this.setTransparancyAlpha(true, 0.05f, 0.025f);
        this.ownerOrCreator = owner;
        this.centeredAroundObject = true;
        this.originalDamage = this.getDamage();
    }


    @Override
    public void updateSpecialAttack() {
        if(!isDissipating && this.getTransparancyAlpha() >= 0.4f){
            this.setTransparancyAlpha(false, 0.4f, 0);
        }
        if ((ownerOrCreator == null || ownerOrCreator.getCurrentShieldPoints() < (ownerOrCreator.getMaxShieldPoints() * CalmInChaos.hitPointRequirement)) && !isDissipating) {
            startDissipation();
        }

        if(!isDissipating && ownerOrCreator != null && ownerOrCreator.getCurrentShieldPoints() >= (ownerOrCreator.getMaxShieldPoints() * CalmInChaos.hitPointRequirement)){
            this.setDamage(this.originalDamage * (ownerOrCreator.getCurrentShieldPoints() / ownerOrCreator.getMaxShieldPoints()));
        }

    }

    public void startDissipation() {
        this.setTransparancyAlpha(true, 1, -0.075f);
        super.isDissipating = true;
    }

    public static CalmInChaosStorm createEffect(SpaceShip owner) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.CalmInChaosEffect);
        spriteConfiguration.setxCoordinate(-500);
        spriteConfiguration.setyCoordinate(-500);
        spriteConfiguration.setScale(1);
        CalmInChaos calmInChaos = (CalmInChaos) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.CalmInChaos);
        float damageModifier = calmInChaos.getQuantity() * CalmInChaos.damageModifier;
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(
                owner.getDamage() * damageModifier,
                true,
                true,
                false,
                true,
                false,
                false
        );

        CalmInChaosStorm calmInChaosStorm = new CalmInChaosStorm(spriteAnimationConfiguration, specialAttackConfiguration, owner);
        calmInChaosStorm.addYOffset(10);
        return calmInChaosStorm;
    }

}
