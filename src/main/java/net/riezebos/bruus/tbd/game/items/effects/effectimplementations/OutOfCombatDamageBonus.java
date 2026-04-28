package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class OutOfCombatDamageBonus implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    private float damageBonus;
    private boolean bonusApplied;
    private EffectIdentifiers effectIdentifier;
    private float cooldown;

    private List<SpriteAnimation> animationList = new ArrayList<>();

    public OutOfCombatDamageBonus(float damageBonus, EffectIdentifiers effectIdentifier, float cooldown){
        this.damageBonus = damageBonus;
        this.effectActivationTypes = EffectActivationTypes.CheckEveryGameTick;
        this.bonusApplied = false;
        this.effectIdentifier = effectIdentifier;
        this.cooldown = cooldown;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime - gameObject.getLastGameSecondDamageTaken() >= cooldown) {
            if (!bonusApplied) {
                gameObject.modifyBonusDamageMultiplier(damageBonus);
                bonusApplied = true; // Mark as applied
            }
        } else {
            if (bonusApplied) {
                gameObject.modifyBonusDamageMultiplier(-damageBonus);
                bonusApplied = false; // Reset so it can be applied again
            }
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return this.effectActivationTypes;
    }

    @Override
    public void resetDuration () {
        //Does nothing
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
        //Not needed
    }

    @Override
    public EffectInterface copy () {
        //should theoretically never be needed to be copied
        return null;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject){
        if(!this.animationList.isEmpty() && this.animationList.get(0) != null){
            animationList.get(0).setInfiniteLoop(false);
            animationList.get(0).setVisible(false);
        }
        animationList.clear();
    }
}
