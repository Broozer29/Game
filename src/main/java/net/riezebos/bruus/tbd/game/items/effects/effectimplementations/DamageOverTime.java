package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.game.items.items.firefighter.CorrosiveOil;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DamageOverTime implements EffectInterface {

    private EffectActivationTypes effectTypesEnums;
    private float damage;
    private double durationInSeconds;
    private double startTimeInSeconds;
    private int dotStacks;
    private boolean offsetApplied = false;
    private boolean scaledToTarget = false;

    private List<SpriteAnimation> animationList = new ArrayList<>();
    private EffectIdentifiers effectIdentifier;

    private double lastDamageTime = 0;
    public static final double damageInterval = 0.01;
    private boolean appliedArmorDebuff = false;

    public DamageOverTime(float damage, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        if (animation != null) {
            this.animationList.add(animation);
        }
        this.effectIdentifier = effectIdentifier;
    }

    public DamageOverTime(float damage, double durationInSeconds, EffectIdentifiers effectIdentifier) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        this.animationList.add(initDefaultIgniteAnimation());
        this.effectIdentifier = effectIdentifier;
    }

    private SpriteAnimation initDefaultIgniteAnimation() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-40);
        spriteConfiguration.setyCoordinate(-40);
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.IgniteBurning);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        return new SpriteAnimation(spriteAnimationConfiguration);
    }


    @Override
    public void activateEffect(GameObject target) {
        double currentTime = GameState.getInstance().getGameSeconds();
        //Initial animation can't be scaled/offset at init since the target isnt provided yet, so we do it here
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            if (!scaledToTarget) {
                EffectAnimationHelper.scaleAnimation(target, this.animationList.get(0));
                scaledToTarget = true;
            }
            if (!offsetApplied) {
                EffectAnimationHelper.applyRandomOffset(target, this.animationList.get(0));
                offsetApplied = true;
            }

            applyCorrosiveOil(target); //Apply it once upon creation
        }

        //Deal actual damage
        if (currentTime - startTimeInSeconds < durationInSeconds && dotStacks > 0) {
            if (currentTime - lastDamageTime >= damageInterval) {
                target.takeDamage(this.damage * dotStacks);
                lastDamageTime = currentTime; // Update the last damage time

                if (this.effectIdentifier.equals(EffectIdentifiers.Ignite)) {
                    handleIgniteSpecialCases(target);
                }
            }
        } else {
            if (this.dotStacks > 0) {
                this.dotStacks -= 1;
                startTimeInSeconds = currentTime;

                if (dotStacks < animationList.size()) {
                    animationList.get(animationList.size() - 1).setVisible(false);
                    animationList.remove(animationList.size() - 1);
                }

            }
        }
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        return this.dotStacks == 0;
    }

    @Override
    public void resetDuration() {
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
        if (this.effectIdentifier.equals(EffectIdentifiers.Ignite) &&
                this.dotStacks < PlayerStats.getInstance().getMaxIgniteStacks()) {
            this.dotStacks += 1;
            applyCorrosiveOil(gameObject);

            if (animationList.size() < 5) {
                SpriteAnimation animation = initDefaultIgniteAnimation();
                EffectAnimationHelper.scaleAnimation(gameObject, animation);
                EffectAnimationHelper.applyRandomOffset(gameObject, animation);
                this.animationList.add(animation);
                gameObject.addEffectAnimation(animation);
                AnimationManager.getInstance().addUpperAnimation(animation);
            }

        }
    }

    private double lastTimeThornsApplied = 0;

    private void handleIgniteSpecialCases(GameObject target) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EntanglingFlames) != null &&
                (lastTimeThornsApplied + 0.75f) < GameState.getInstance().getGameSeconds()) {
            ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(target, PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EntanglingFlames).getQuantity());
            lastTimeThornsApplied = GameState.getInstance().getGameSeconds();
        }
    }

    private void applyCorrosiveOil(GameObject gameObject) {
        if (!this.effectIdentifier.equals(EffectIdentifiers.Ignite)) {
            return;
        }

        CorrosiveOil item = (CorrosiveOil) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.CorrosiveOil);
        if (item != null) {
            gameObject.adjustArmorBonus(-item.getArmorReductionPerStack());
        }
    }

    @Override
    public EffectInterface copy() {
        SpriteAnimation animation = null;
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            animation = this.animationList.get(0);
        }
        SpriteAnimation clonedAnimation = (animation != null) ? animation.clone() : null;

        DamageOverTime copiedEffect = new DamageOverTime(this.damage, this.durationInSeconds, clonedAnimation, effectIdentifier);
        // Copy other necessary fields
//        copiedEffect.dotStacks = this.dotStacks;  Not copying stacks, cause its probably a bit too OP and doesnt worth with multiple animations
        return copiedEffect;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
    }

    public EffectActivationTypes getEffectTypesEnums() {
        return effectTypesEnums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DamageOverTime that = (DamageOverTime) o;
        return Float.compare(damage, that.damage) == 0 && Double.compare(durationInSeconds, that.durationInSeconds) == 0 && Double.compare(startTimeInSeconds, that.startTimeInSeconds) == 0 && dotStacks == that.dotStacks && offsetApplied == that.offsetApplied && effectTypesEnums == that.effectTypesEnums;
    }

    @Override
    public int hashCode() {
        return Objects.hash(effectTypesEnums);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        for (SpriteAnimation animation : animationList) {
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }
        this.animationList.clear();
    }
}
