package net.riezebos.bruus.tbd.game.items.items.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.FreezeEffect;
import net.riezebos.bruus.tbd.game.items.enums.ItemApplicationEnum;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class KineticDynamo extends Item {

    private float energyRaised = 0f;
    private float maxEneryCap = 1f;
    private int cooldown = 1;
    private double lastSecondsFired = 0;
    public boolean isMovingFast = false;
    public static float damageRatio = 2;

    public KineticDynamo() {
        super(ItemEnums.KineticDynamo, 1, ItemApplicationEnum.CustomActivation);
        calculateMaxEnergyCap();
    }

    @Override
    public void applyEffectToObject(GameObject spaceship) {
        //Unleash it in a shockwave
        if(energyRaised > 0.01f && GameState.getInstance().getGameSeconds() - lastSecondsFired > cooldown){
            ExplosionManager.getInstance().addExplosion(createExplosion(spaceship));
            energyRaised = 0.0f;
            lastSecondsFired = GameState.getInstance().getGameSeconds();
        }
    }

    public void buildUpEnergy(float directionX, float directionY) {
        // -0.1f to deal with floating point precision as float equals isn't reliable (4 - 0.1) = 3.9 > 2.5 = true. 2.4 > 2.5 = false
        if(isMovingFast) {
            float currentSpeed = (float) Math.sqrt(directionX * directionX + directionY * directionY);
            float speedRatio = currentSpeed / PlayerStats.getInstance().getMovementSpeed();

            // Ensure speedRatio is capped between 0 and 1 (if unexpected values appear)
            speedRatio = Math.min(speedRatio, 1.0f);

            // Calculate how much energy to build up
            float energyValue = speedRatio * 0.0125f;
            energyRaised += energyValue;
            // Check if the player has the Kinetic Dynamo equipped

            if (energyRaised > maxEneryCap) {
                energyRaised = maxEneryCap;
            }
        }
    }

    private void calculateMaxEnergyCap() {
        maxEneryCap = this.quantity;
    }

    private Explosion createExplosion(GameObject gameObject) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.CarrierWarpExplosion); //placeholder
        spriteConfiguration.setScale(2);

        float damage = (PlayerStats.getInstance().getBaseDamage() * damageRatio) * energyRaised;

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, damage,
                true, true);

        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        AudioManager.getInstance().addAudio(AudioEnums.ScarabExplosion);
        return explosion;
    }


    @Override
    public void increaseQuantityOfItem(int amount) {
        this.quantity += amount;
        calculateMaxEnergyCap();
    }


    @Override
    public boolean isAvailable() {
        if (!this.itemEnum.isEnabled()) {
            return false;
        }

        return PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.Carrier);
    }
}