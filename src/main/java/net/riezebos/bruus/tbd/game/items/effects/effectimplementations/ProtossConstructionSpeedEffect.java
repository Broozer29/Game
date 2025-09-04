package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class ProtossConstructionSpeedEffect implements EffectInterface {

    private float constructionBonus = 1.0f;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private List<SpriteAnimation> animationList = new ArrayList<>();
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;

    public ProtossConstructionSpeedEffect(float constructionBonus, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.constructionBonus = constructionBonus;
        this.durationInSeconds = durationInSeconds;
        if(animation != null) {
            this.animationList.add(animation);
        }
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.appliedToObject = false;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        if (!appliedToObject) {
            PlayerStats.getInstance().modifyConstructionBonusSpeedModifier(constructionBonus);
            appliedToObject = true;
        }

        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            centerAnimation(gameObject);
        }

    }

    private void removeEffectsBeforeRemovingEffect(GameObject gameObject) {
        PlayerStats.getInstance().modifyConstructionBonusSpeedModifier(-constructionBonus);
    }


    private void centerAnimation(GameObject object) {
        if(this.animationList.isEmpty() || this.animationList.get(0) == null){
            return; //no animation exists
        }


        if (object.getAnimation() != null) {
            SpriteAnimation objectVisuals = object.getAnimation();
            this.animationList.get(0).setCenterCoordinates(objectVisuals.getCenterXCoordinate(), objectVisuals.getCenterYCoordinate());
        } else {
            this.animationList.get(0).setCenterCoordinates(object.getCenterXCoordinate(), object.getCenterYCoordinate());
        }


    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        if (GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return effectTypesEnums;
    }

    @Override
    public void resetDuration() {
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
        //To be implemented later, if ever
    }

    @Override
    public EffectInterface copy() {
        SpriteAnimation animation = null;
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            animation = this.animationList.get(0);
        }
        SpriteAnimation clonedAnimation = (animation != null) ? animation.clone() : null;

        return new ProtossConstructionSpeedEffect(constructionBonus, durationInSeconds, clonedAnimation, effectIdentifier);
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            this.animationList.get(0).setInfiniteLoop(false);
            this.animationList.get(0).setVisible(false);
        }
        removeEffectsBeforeRemovingEffect(gameObject);
        this.animationList.clear();
    }
}