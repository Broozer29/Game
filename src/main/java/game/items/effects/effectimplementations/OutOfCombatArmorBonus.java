package game.items.effects.effectimplementations;

import game.gamestate.GameStateInfo;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.gameobjects.GameObject;
import visualobjects.SpriteAnimation;

public class OutOfCombatArmorBonus implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    float armorBonus;
    private double lastTimeDamageTaken;
    private boolean bonusApplied;
    private EffectIdentifiers effectIdentifier;

    private SpriteAnimation animation;

    public OutOfCombatArmorBonus(float armorBonus, EffectIdentifiers effectIdentifier){
        this.armorBonus = armorBonus;
        this.lastTimeDamageTaken = GameStateInfo.getInstance().getGameSeconds();
        this.effectActivationTypes = EffectActivationTypes.CheckEveryGameTick;
        this.bonusApplied = false;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (currentTime - gameObject.getLastGameSecondDamageTaken() >= 3) {
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
    public boolean shouldBeRemoved () {
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
    public void increaseEffectStrength () {
        //Not needed
    }

    @Override
    public EffectInterface copy () {
        System.out.println("Not needed");
        return null;
    }

    public EffectActivationTypes getEffectActivationTypes () {
        return effectActivationTypes;
    }

    public float getArmorBonus () {
        return armorBonus;
    }

    public double getLastTimeDamageTaken () {
        return lastTimeDamageTaken;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
