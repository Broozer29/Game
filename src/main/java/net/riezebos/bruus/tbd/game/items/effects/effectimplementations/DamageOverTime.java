package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.items.firefighter.CorrosiveOil;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Objects;


public class DamageOverTime implements EffectInterface {

    private EffectActivationTypes effectTypesEnums;
    private float damage;
    private double durationInSeconds;
    private double startTimeInSeconds;
    private int dotStacks;
    private boolean offsetApplied = false;
    private boolean scaledToTarget = false;

    private SpriteAnimation animation;
    private EffectIdentifiers effectIdentifier;

    private double lastDamageTime = 0;
    private final double damageInterval = 0.01;
    private boolean appliedArmorDebuff = false;

    public DamageOverTime (float damage, double durationInSeconds, SpriteAnimation spriteAnimation, EffectIdentifiers effectIdentifier) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        this.animation = spriteAnimation;
        this.effectIdentifier = effectIdentifier;
    }

    public DamageOverTime (float damage, double durationInSeconds, EffectIdentifiers effectIdentifier) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        initDefaultAnimation();
        this.effectIdentifier = effectIdentifier;
    }

    private void initDefaultAnimation () {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-40);
        spriteConfiguration.setyCoordinate(-40);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.IgniteBurning);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        this.animation = new SpriteAnimation(spriteAnimationConfiguration);
    }


    @Override
    public void activateEffect (GameObject target) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (animation != null) {
            if (!scaledToTarget) {
                EffectAnimationHelper.scaleAnimation(target, animation);
                scaledToTarget = true;
            }
            if (!offsetApplied) {
                EffectAnimationHelper.applyRandomOffset(target, animation);
                offsetApplied = true;
            }
            applyCorrosiveOil(target); //Apply it once upon creation
        }
        if (currentTime - startTimeInSeconds < durationInSeconds) {
            if (currentTime - lastDamageTime >= damageInterval) {
                target.takeDamage(this.damage * dotStacks);
                lastDamageTime = currentTime; // Update the last damage time

                if (this.effectIdentifier.equals(EffectIdentifiers.Ignite)) {
                    handleIgniteSpecialCases(target);
                }
            }
        } else {
            this.dotStacks = 0;
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;

    }

    @Override
    public void resetDuration () {
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
        if (this.effectIdentifier.equals(EffectIdentifiers.Ignite)) {
            if (PlayerStats.getInstance().getPlayerClass().equals(PlayerClass.FireFighter)) {
                if (this.dotStacks < PlayerStats.getInstance().getFireFighterIgniteMaxStacks()) {
                    this.dotStacks += 1;
                    applyCorrosiveOil(gameObject);
                }
            }
        }
    }

    private double lastTimeThornsApplied = 0;

    private void handleIgniteSpecialCases (GameObject target) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EntanglingFlames) != null &&
                (lastTimeThornsApplied + 0.75f) < GameStateInfo.getInstance().getGameSeconds()) {
            ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(target, PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EntanglingFlames).getQuantity());
            lastTimeThornsApplied = GameStateInfo.getInstance().getGameSeconds();
        }
    }

    private void applyCorrosiveOil (GameObject gameObject){
        CorrosiveOil item = (CorrosiveOil) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.CorrosiveOil);
        if(item != null){
            gameObject.adjustArmorBonus(-item.getArmorReductionPerStack());
        }
    }

    @Override
    public EffectInterface copy () {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-40);
        spriteConfiguration.setyCoordinate(-40);
        spriteConfiguration.setScale(this.animation.getScale());
        spriteConfiguration.setImageType(this.animation.getImageEnum());

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, this.animation.getFrameDelay(), this.animation.isInfiniteLoop());
        DamageOverTime copiedEffect = new DamageOverTime(this.damage, this.durationInSeconds, new SpriteAnimation(spriteAnimationConfiguration), effectIdentifier);
        // Copy other necessary fields
        copiedEffect.dotStacks = this.dotStacks;
        return copiedEffect;
    }

    public SpriteAnimation getAnimation () {
        return animation;
    }

    public void setAnimation (SpriteAnimation animation) {
        this.animation = animation;
    }

    public EffectActivationTypes getEffectTypesEnums () {
        return effectTypesEnums;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DamageOverTime that = (DamageOverTime) o;
        return Float.compare(damage, that.damage) == 0 && Double.compare(durationInSeconds, that.durationInSeconds) == 0 && Double.compare(startTimeInSeconds, that.startTimeInSeconds) == 0 && dotStacks == that.dotStacks && offsetApplied == that.offsetApplied && effectTypesEnums == that.effectTypesEnums && Objects.equals(animation, that.animation);
    }

    @Override
    public int hashCode () {
        return Objects.hash(effectTypesEnums);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject) {
        if (animation != null) {
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }
        animation = null;
    }
}
