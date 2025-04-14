package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class OutOfCombatArmorBonus implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    private float armorBonus;
    private boolean bonusApplied;
    private EffectIdentifiers effectIdentifier;
    private float cooldown;

    private SpriteAnimation animation;

    public OutOfCombatArmorBonus(float armorBonus, EffectIdentifiers effectIdentifier, float cooldown){
        this.armorBonus = armorBonus;
        this.effectActivationTypes = EffectActivationTypes.CheckEveryGameTick;
        this.bonusApplied = false;
        this.effectIdentifier = effectIdentifier;
        this.cooldown = cooldown;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime - gameObject.getLastGameSecondDamageTaken() >= cooldown) {
            // Apply the armor bonus only once when out of combat
            if (!bonusApplied) {
                gameObject.adjustArmorBonus(armorBonus); // Increase armor by armorBonus amount
                bonusApplied = true; // Mark as applied
            }
        } else {
            // Remove the armor bonus when taking damage and reset bonusApplied
            if (bonusApplied) {
                gameObject.adjustArmorBonus(-armorBonus); // Decrease armor by armorBonus amount
                bonusApplied = false; // Reset so it can be applied again
            }
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        return null;
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
        System.out.println("Not needed");
        return null;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject){
        if(animation != null){
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }
        animation = null;
    }
}
