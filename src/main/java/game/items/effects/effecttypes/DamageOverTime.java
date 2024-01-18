package game.items.effects.effecttypes;

import game.gamestate.GameStateInfo;
import game.items.effects.EffectInterface;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;
import visualobjects.SpriteAnimation;

import java.util.Objects;

public class DamageOverTime implements EffectInterface {

    private EffectActivationTypes effectTypesEnums;
    private float damage;
    private long durationInSeconds;
    private long startTimeInSeconds;
    private int dotStacks;

    private SpriteAnimation animation;

    public DamageOverTime(float damage, int durationInSeconds) {
        this.damage = damage;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.DamageOverTime;
        this.dotStacks = 1;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    public void updateDamage(float damage){
        this.damage = damage;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        long currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime - startTimeInSeconds < durationInSeconds) {
            gameObject.takeDamage(this.damage * dotStacks);
        } else {
            this.dotStacks = 0;
        }
    }

    @Override
    public boolean shouldBeRemoved() {
        if(GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds){
            return true;
        } else return false;

    }

    @Override
    public void resetDuration(){
        // Reset the start time to the current game time
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength () {
        this.dotStacks+= 1;
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
        return effectTypesEnums == that.effectTypesEnums;
    }

    @Override
    public int hashCode () {
        return Objects.hash(effectTypesEnums);
    }
}
