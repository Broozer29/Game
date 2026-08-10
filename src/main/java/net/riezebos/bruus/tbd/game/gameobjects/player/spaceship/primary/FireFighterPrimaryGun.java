package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.primary;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlameThrower;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerPrimaryAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.PrimaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FireFighterPrimaryGun extends PrimaryPlayerGun {

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerPrimaryAttackTypes playerAttackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireGenericBehaviour(owner);
            startFiringFlameThrower(xCoordinate, yCoordinate, owner);
        }
    }


    // Fuel tank mechanics
    private float FUEL_DEPLETION_RATE = 0.3f;
    private float FUEL_REGENERATION_RATE = 0.35f;
    private float FUEL_MINIMUM_REQUIRED = 10f;
    public static float fireFighterBonusDamageRatio = 1;

    private void startFiringFlameThrower(int xCoordinate, int yCoordinate, SpaceShip owner) {

        if (this.channeledAttack == null && orangeBarCurrentValue >= FUEL_MINIMUM_REQUIRED) {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(xCoordinate);
            spriteConfiguration.setyCoordinate(yCoordinate);
            spriteConfiguration.setImageType(ImageEnums.FireFighterFlameThrowerAppearing);

            float damage = owner.getDamage() * fireFighterBonusDamageRatio;

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 3, true);
            SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, false, true);
            SpecialAttack specialAttack = new FlameThrower(spriteAnimationConfiguration, missileConfiguration);
            specialAttack.setCenteredAroundObject(true);
            specialAttack.setScale(0.9f);
            specialAttack.addXOffset((specialAttack.getAnimation().getWidth() / 2) - Math.round((specialAttack.getAnimation().getWidth() * 0.005f)));
            specialAttack.setOwnerOrCreator(owner);
            owner.addFollowingSpecialAttack(specialAttack);
            this.channeledAttack = specialAttack;
            updateFlameThrowerDamageFromInfernalPreIgniter(this.channeledAttack);
            MissileManager.getInstance().addSpecialAttack(specialAttack);
            AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        }
    }

    private void updateFlameThrowerDamageFromInfernalPreIgniter(SpecialAttack specialAttack) {
        //deprecated item effect but still functional, can be reused for a different item now that infernal preigniter has been reworked
//        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.InfernalPreIgniter) != null){
//            specialAttack.setDamage(playerStats.getNormalAttackDamage() * ((this.orangeBarCurrentValue / this.orangeBarMaxValue) * InfernalPreIgniter.maxDamageBonnus));
//        }
    }


    @Override
    public void stopFiring(SpaceShip owner) {
        if (this.channeledAttack != null && !this.channeledAttack.isDissipating()) {
            this.channeledAttack.startDissipating();
            timeChannelAttackGetsCleared = GameState.getInstance().getGameSeconds();
        }
    }

    @Override
    public void updateFrameCount(SpaceShip owner) {
        super.updateFrameCount(owner);

        if (orangeBarMaxValue < 0) {
            orangeBarMaxValue = 125 * owner.getFuelCannisterMaxCapacityModifier();
        }

        if (orangeBarCurrentValue < 0) {
            orangeBarCurrentValue = orangeBarMaxValue;
        }

        if (this.channeledAttack == null && orangeBarCurrentValue < orangeBarMaxValue) {
            orangeBarCurrentValue += FUEL_REGENERATION_RATE * owner.getFuelCannisterRegenModifier();
            if (orangeBarCurrentValue > orangeBarMaxValue) {
                orangeBarCurrentValue = orangeBarMaxValue; // Clamp at max value
            }
        }

        if (channeledAttack != null) {
            if (orangeBarCurrentValue <= 0) {
                // If no fuel is available, prevent firing
                stopFiring(owner);
                return;
            }

            // Deplete fuel while firing
            updateFlameThrowerDamageFromInfernalPreIgniter(this.channeledAttack);
            orangeBarCurrentValue -= Math.max(FUEL_DEPLETION_RATE * owner.getFuelCannisterUsageModifier(), 0.01f);
            if (orangeBarCurrentValue < 0) {
                orangeBarCurrentValue = 0; // Clamp at 0
            }
        }
    }

}
