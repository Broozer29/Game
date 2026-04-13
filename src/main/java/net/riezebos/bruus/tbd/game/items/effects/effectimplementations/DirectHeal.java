package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class DirectHeal implements EffectInterface {

    private boolean hasHealed = false;
    private float maxHpToHeal = 0.0f;
    private List<SpriteAnimation> animationList = new ArrayList<>();
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;

    public DirectHeal(float maxHpToHeal, SpriteAnimation animation, EffectIdentifiers effectIdentifier){
        this.effectIdentifier = effectIdentifier;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.maxHpToHeal = maxHpToHeal;
        if(animation != null) {
            this.animationList.add(animation);
        }
    }
    @Override
    public void activateEffect (GameObject target) {
        if(target instanceof SpaceShip spaceShip && this.effectIdentifier.equals(EffectIdentifiers.YellowBossOrbHeal)) {
            maxHpToHeal *= 2.5f; //250% increased hedl for the player (10 * 2.5 = 25%)
        }

        target.setCurrentHitpoints(target.getCurrentHitpoints() + (target.getMaxHitPoints() * maxHpToHeal));
        if(target.getCurrentHitpoints() >= target.getMaxHitPoints()){
            target.setCurrentHitpoints(target.getMaxHitPoints());
        }
        if(!animationList.isEmpty()) {
            placeAnimation(target, animationList.get(0)); //assume there is only 1 animation because this effect is consumed immediatly
        }
        hasHealed = true;
    }

    private void placeAnimation(GameObject target, SpriteAnimation spriteAnimation){
        EffectAnimationHelper.scaleAnimation(target, spriteAnimation);
        spriteAnimation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        target.addEffectAnimation(spriteAnimation);
        AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
    }


    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return hasHealed;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return animationList;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return this.effectTypesEnums;
    }

    @Override
    public void resetDuration () {
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public EffectInterface copy () {
        //Shouldn't be used as this effect is immediatly consumed and removed on use
        return null;
    }

    @Override
    public void removeEffect (GameObject gameObject){
        if(!this.animationList.isEmpty() && this.animationList.get(0) != null){
            this.animationList.get(0).setInfiniteLoop(false);
            this.animationList.get(0).setVisible(false);
        }

        this.animationList.clear();
    }

}
