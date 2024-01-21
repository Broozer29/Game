package game.items.effects.effecttypes;

import game.gamestate.GameStateInfo;
import game.items.effects.EffectInterface;
import game.items.effects.EffectActivationTypes;
import game.managers.AnimationManager;
import game.objects.GameObject;
import visualobjects.SpriteAnimation;

import java.util.Objects;
import java.util.Random;

public class DamageOverTime implements EffectInterface {

    private EffectActivationTypes effectTypesEnums;
    private float damage;
    private double durationInSeconds;
    private double startTimeInSeconds;
    private int dotStacks;
    private boolean offsetApplied = false;

    private SpriteAnimation animation;

    public DamageOverTime (float damage, double durationInSeconds, SpriteAnimation spriteAnimation) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.DamageOverTime;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        this.animation = spriteAnimation;
    }

    public void updateDamage (float damage) {
        this.damage = damage;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (animation != null && !offsetApplied) {
            applyRandomOffset(gameObject);
            offsetApplied = true;
        }
        if (currentTime - startTimeInSeconds < durationInSeconds) {
            gameObject.takeDamage(this.damage * dotStacks);
        } else {
            this.dotStacks = 0;
        }
    }

    private void applyRandomOffset (GameObject object) {
        Random random = new Random();
        int halfWidth = object.getWidth() / 2;
        int halfHeight = object.getHeight() / 2;

        int minWidth = 0;
        int maxWidth = object.getWidth() - (this.animation.getWidth());

//        int xOffset = random.nextInt(halfWidth);

        int xOffsetRange = maxWidth - minWidth;
        int xOffset = minWidth + (xOffsetRange > 0 ? random.nextInt(xOffsetRange) : 0);

        int yOffset = random.nextInt(halfHeight);

        animation.addXOffset(xOffset);
        animation.addYOffset(yOffset);
    }


    @Override
    public boolean shouldBeRemoved () {
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;

    }

    @Override
    public void resetDuration () {
        // Reset the start time to the current game time
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength () {
        this.dotStacks += 1;
    }

    @Override
    public EffectInterface copy () {
        DamageOverTime copiedEffect = new DamageOverTime(this.damage, this.durationInSeconds, this.animation.clone());
        // Copy other necessary fields
        copiedEffect.dotStacks = this.dotStacks;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
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
}
