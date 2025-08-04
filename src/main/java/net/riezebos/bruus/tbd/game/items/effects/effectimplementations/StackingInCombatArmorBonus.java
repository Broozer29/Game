package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class StackingInCombatArmorBonus implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    private float armorBonus;
    private boolean bonusApplied;
    private EffectIdentifiers effectIdentifier;
    private double duration;
    private float armorApplied;
    private List<SpriteAnimation> animationList = new ArrayList<>();
    private double lastDamageTime; // Time when the object last took damage

    public StackingInCombatArmorBonus(float armorBonus, EffectIdentifiers effectIdentifier, float duration){
        this.armorBonus = armorBonus;
        this.effectActivationTypes = EffectActivationTypes.CheckEveryGameTick;
        this.bonusApplied = false;
        this.effectIdentifier = effectIdentifier;
        this.duration = duration;
        this.lastDamageTime = 0;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        double currentTime = GameState.getInstance().getGameSeconds();

        // If more than 2 seconds have passed without damage, reset applied armor and remove the bonus
        if (currentTime - lastDamageTime >= duration) {
            // Remove all applied armor bonus
            if (armorApplied > 0) {
                gameObject.adjustArmorBonus(-armorApplied); // Decrease the armor by the total applied bonus
                armorApplied = 0; // Reset applied armor counter
                bonusApplied = false;
            }
        }

        // Check if the GameObject has taken damage recently (within 0.03 seconds) && making sure the player actually took damage
        if (currentTime - gameObject.getLastGameSecondDamageTaken() < 0.03 && gameObject.getLastGameSecondDamageTaken() != 0) {
            if (!bonusApplied) {
                // Apply armor bonus for the first time in this damage period
                gameObject.adjustArmorBonus(armorBonus); // Apply the armor bonus
                armorApplied += armorBonus; // Add to the total applied armor
                bonusApplied = true; // Flag that damage has been processed
                lastDamageTime = currentTime; // Update the last time damage was taken
            }
        } else {
            // Reset damageProcessed flag if no damage is detected within the threshold
            bonusApplied = false;
        }
    }


    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return false; //not needed in this effect
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList; //no animation yet
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return this.effectActivationTypes;
    }

    @Override
    public void resetDuration () {
        //Does nothing for this effect
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
        //not needed
    }

    @Override
    public EffectInterface copy () {
        return null; //this effect should not be copied
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject){
        if (bonusApplied) {
            gameObject.adjustArmorBonus(-armorBonus); // Decrease armor by armorBonus amount
            bonusApplied = false;
        }
        if(!this.animationList.isEmpty() && this.animationList.get(0) != null){
            animationList.get(0).setInfiniteLoop(false);
            animationList.get(0).setVisible(false);
        }
        animationList.clear();
    }

}
