package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public abstract class SecondaryPlayerGun {
    protected PlayerStats playerStats = PlayerStats.getInstance();

    protected int specialAttackCharges = 1;
    protected double lastSecondsSpecialAttackUsed = 0.0;
    protected double lastSecondsSpecialAttackChargeGained = 0.0;
    protected double secondsUntilNextSpecialAttackCharge = 0.0;

    public SecondaryPlayerGun() {
    }

    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType, SpaceShip owner) {
        //to be overridden
    }

    protected boolean attackOffCooldown(SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return false;
        }

        double currentTime = GameState.getInstance().getGameSeconds();
        return specialAttackCharges > 0 && currentTime >= lastSecondsSpecialAttackUsed + 0.15;
    }

    protected void spendCharge(SpaceShip owner) {
        if (specialAttackCharges > 0) {
            double currentTime = GameState.getInstance().getGameSeconds();
            specialAttackCharges--;
            lastSecondsSpecialAttackUsed = currentTime;
            // Reset the charge gain timer only if there's room for more charges
            if ((specialAttackCharges <= 0 && owner.getMaxSpecialAttackCharges() == 1) ||
                    (specialAttackCharges + 1 == owner.getMaxSpecialAttackCharges())) {
                lastSecondsSpecialAttackChargeGained = currentTime; // Start new charge cooldown
            }
        }
    }

    protected SpriteAnimationConfiguration createConfig(int xCoordinate, int yCoordinate, ImageEnums imageEnums, float scale, boolean loop) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(xCoordinate);
        spriteConfiguration.setyCoordinate(yCoordinate);
        spriteConfiguration.setImageType(imageEnums);
        spriteConfiguration.setScale(scale);

        return new SpriteAnimationConfiguration(spriteConfiguration, 2, loop);

    }

    public void updateFrameCount(SpaceShip owner) {
        double currentTime = GameState.getInstance().getGameSeconds();
        // Gain a new charge if enough time has passed since the last charge was gained and there is a slot available
        if (specialAttackCharges < owner.getMaxSpecialAttackCharges()) {
            if (currentTime >= Math.max(lastSecondsSpecialAttackChargeGained + (playerStats.getSpecialAttackCooldown() * owner.getSpecialAttackRechargeCooldownModifier()), 0.15f)) {
                lastSecondsSpecialAttackChargeGained = currentTime;
                specialAttackCharges++;
            }
        }

        // Calculate time until next charge
        if (specialAttackCharges < owner.getMaxSpecialAttackCharges()) {
            secondsUntilNextSpecialAttackCharge = Math.max((playerStats.getSpecialAttackCooldown() * owner.getSpecialAttackRechargeCooldownModifier()) - (currentTime - lastSecondsSpecialAttackChargeGained), 0.15f);
        } else {
            secondsUntilNextSpecialAttackCharge = 0; // No charging if full
        }
    }

    public void stopFiring(SpaceShip owner) {
        //to be overridden
    }

    public int getSpecialAttackCharges() {
        return this.specialAttackCharges;
    }

    public double getSecondsUntilNextAttackCharge() {
        return secondsUntilNextSpecialAttackCharge;
    }

    public void addSpecialCharge(SpaceShip owner) {
        this.specialAttackCharges += 1;
        if (this.specialAttackCharges >= owner.getMaxSpecialAttackCharges()) {
            this.specialAttackCharges = owner.getMaxSpecialAttackCharges();
            lastSecondsSpecialAttackChargeGained = GameState.getInstance().getGameSeconds();
            secondsUntilNextSpecialAttackCharge = 0;
        }
    }
}