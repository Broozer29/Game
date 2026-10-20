package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class TemporaryImmunity implements EffectInterface {

    private double durationInSeconds;
    private double startTimeInSeconds;

    private boolean hasApplied = false; //needs a flag because the startTime needs to be set upon APPLYING, not on CREATION
    private boolean scaledToTarget = false;
    private List<SpriteAnimation> animationList = new ArrayList<>();
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;

    public TemporaryImmunity(double durationInSeconds, SpriteAnimation animation) {
        this.effectIdentifier = EffectIdentifiers.TemporaryImmunity;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        if (animation != null) {
            this.animationList.add(animation);
        }
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    public TemporaryImmunity(double durationInSeconds) {
        this(durationInSeconds, null);
    }

    @Override
    public void activateEffect(GameObject target) {
        if (!(target instanceof SpaceShip spaceShip)) {
            return;
        }

        if (!hasApplied) {
            this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
            spaceShip.setImmune(true);
            hasApplied = true;
        }

        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            if (!scaledToTarget) {
                EffectAnimationHelper.scaleAnimation(target, this.animationList.get(0));
                scaledToTarget = true;
            }
            this.animationList.get(0).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        }
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        return GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return this.effectTypesEnums;
    }

    @Override
    public void resetDuration() {
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
        //does nothing
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return effectIdentifier;
    }

    @Override
    public EffectInterface copy() {
        SpriteAnimation animation = null;
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            animation = this.animationList.get(0);
        }
        SpriteAnimation clonedAnimation = (animation != null) ? animation.clone() : null;

        return new TemporaryImmunity(this.durationInSeconds, clonedAnimation);
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            this.animationList.get(0).setInfiniteLoop(false);
            this.animationList.get(0).setVisible(false);
        }

        if (gameObject instanceof SpaceShip spaceShip) {
            spaceShip.setImmune(false);
        }
        this.animationList.clear();
    }
}
