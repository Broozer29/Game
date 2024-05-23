package game.items.effects.effecttypes;

import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.managers.OnScreenTextManager;
import game.objects.GameObject;
import game.objects.player.PlayerManager;
import game.util.OnScreenText;
import visualobjects.SpriteAnimation;

public class GainGoldOnDeath implements EffectInterface {

    private float goldAmount;
    private EffectIdentifiers effectIdentifier;

    private EffectActivationTypes effectActivationType;
    private boolean activated;

    public GainGoldOnDeath (EffectActivationTypes effectActivationType, EffectIdentifiers effectIdentifier, float goldAmount) {
        this.effectActivationType = effectActivationType;
        this.effectIdentifier = effectIdentifier;
        this.goldAmount = goldAmount;
        this.activated = false;
    }


    @Override
    public void activateEffect (GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !activated) {
            PlayerInventory.getInstance().gainCashMoney(goldAmount * GameStateInfo.getInstance().getDifficultyCoefficient());
            activated = true;
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
        return this.effectActivationType;
    }

    @Override
    public void resetDuration () {
        //Shouldnt expire anyway
    }

    @Override
    public void increaseEffectStrength () {
        //Maybe do something later?
    }

    @Override
    public EffectInterface copy () {
        //Shouldn't be used
        return null;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
