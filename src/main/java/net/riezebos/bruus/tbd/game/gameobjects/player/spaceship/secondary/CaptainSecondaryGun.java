package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.secondary;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.ElectroShred;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SecondaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.captain.AnionInverter;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class CaptainSecondaryGun extends SecondaryPlayerGun {

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireElectroShred(xCoordinate, yCoordinate, owner);
            spendCharge(owner);
        }
    }

    public static float electroShredBonusDamageModifier = 1.5f;
    private void fireElectroShred(int xCoordinate, int yCoordinate, SpaceShip owner) {
        float damage = owner.getSpecialAttackDamage() * electroShredBonusDamageModifier;
        float scale = 1.5f;
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AnionInverter) != null) {
            scale *= (1 + AnionInverter.scaleBonus);
        }

        SpriteAnimationConfiguration spriteAnimationConfiguration = createConfig(xCoordinate, yCoordinate, owner.getElectroShredImageEnum(), 1, false);
        spriteAnimationConfiguration.setFrameDelay(3);

        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);
        SpecialAttack specialAttack = new ElectroShred(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(scale);
        specialAttack.setOwnerOrCreator(owner);

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AnionInverter) != null) {
            specialAttack.setTransparancyAlpha(false, 0.5f, 0);
        }
        AudioManager.getInstance().addAudio(AudioEnums.Default_EMP);
        owner.addFollowingSpecialAttack(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleElectrify) != null) {
            PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ModuleElectrify).applyEffectToObject(owner);
        }
    }
}
