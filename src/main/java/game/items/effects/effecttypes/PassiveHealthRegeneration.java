package game.items.effects.effecttypes;

import game.gamestate.GameStateInfo;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectInterface;
import game.objects.GameObject;
import visualobjects.SpriteAnimation;

public class PassiveHealthRegeneration implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    private float healingAmount;
    private long lastTimeDamageTaken;

    private SpriteAnimation animation;

    public PassiveHealthRegeneration(float healingAmount){
        this.healingAmount = healingAmount;
        this.lastTimeDamageTaken = GameStateInfo.getInstance().getGameSeconds();
        this.effectActivationTypes = EffectActivationTypes.HealthRegeneration;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        long currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime - lastTimeDamageTaken > 3) { // More than 3 seconds have passed
            gameObject.takeDamage(-healingAmount); // Apply healing
        }
    }

    @Override
    public boolean shouldBeRemoved () {
        return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        //Maybe implemented later, possibly not
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return effectActivationTypes;
    }

    @Override
    public void resetDuration () {
        //Considering it should always be active but activate when in combat, this is not needed
    }

    @Override
    public void increaseEffectStrength () {
        //Not needed, the "healingAmount" already factors this in
    }
}
