package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class PassiveHealthRegeneration implements EffectInterface {

    private EffectActivationTypes effectActivationTypes;
    private float healingAmount;
    private double lastTimeDamageTaken;
    private EffectIdentifiers effectIdentifier;

    private List<SpriteAnimation> animationList = new ArrayList<>();

    public PassiveHealthRegeneration (float healingAmount, EffectIdentifiers effectIdentifier) {
        this.healingAmount = healingAmount;
        this.lastTimeDamageTaken = GameState.getInstance().getGameSeconds();
        this.effectActivationTypes = EffectActivationTypes.CheckEveryGameTick;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime - gameObject.getLastGameSecondDamageTaken() > 0.001 && // More than 10ms have passed
                gameObject.getCurrentHitpoints() < gameObject.getMaxHitPoints()) {

            if (animationList.get(0) != null) {
                showHealingAnimation();
                centerHealingAnimation(gameObject);
            }

            gameObject.takeDamage(-healingAmount); // Apply healing
        } else {
            if (animationList.get(0) != null) {
                hideHealingAnimation();
            }
        }
    }

    private void centerHealingAnimation (GameObject gameObject) {
        animationList.get(0).setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
    }

    private void showHealingAnimation () {
        if (!AnimationManager.getInstance().getUpperAnimations().contains(animationList.get(0))) {
            AnimationManager.getInstance().addUpperAnimation(animationList.get(0));
        }
    }

    private void hideHealingAnimation () {
        if (AnimationManager.getInstance().getUpperAnimations().contains(animationList.get(0))) {
            AnimationManager.getInstance().getUpperAnimations().remove(animationList.get(0));
            animationList.get(0).refreshAnimation();
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        //the show/hide healing animation methods handle the visual side of the animation
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
    public void increaseEffectStrength (GameObject gameObject) {
        //Not needed, the "healingAmount" already factors this in
    }

    @Override
    public EffectInterface copy () {
        return null;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public void removeEffect (GameObject gameObject){
        if(animationList.get(0) != null){
            animationList.get(0).setInfiniteLoop(false);
            animationList.get(0).setVisible(false);
        }
        animationList.clear();
    }

}
