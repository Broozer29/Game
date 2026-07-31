package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class DisableAttackEffect implements EffectInterface {

    private double durationInSeconds;
    private double startTimeInSeconds;

    private boolean hasApplied = false; //needs a flag because the startTime needs to be set upon COLLISION, not on CREATION
    private boolean scaledToTarget = false;
    private List<SpriteAnimation> animationList = new ArrayList<>();
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;

    public DisableAttackEffect(double durationInSeconds, SpriteAnimation animation) {
        this.effectIdentifier = EffectIdentifiers.TazerAttackDisable;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        if (animation != null) {
            this.animationList.add(animation);
        }
        this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
    }

    @Override
    public void activateEffect(GameObject target) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (target instanceof Enemy enemy) {
            if (enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss)) {
                //Bosses should be immune to this
                return;
            }

            if(!hasApplied){
                this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
                hasApplied = true;
            }

            if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
                if (!scaledToTarget) {
                    EffectAnimationHelper.scaleAnimation(target, this.animationList.get(0));
                    scaledToTarget = true;
                }
                this.animationList.get(0).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
            }

            if (currentTime - startTimeInSeconds < durationInSeconds) {
                enemy.setAllowedToFire(false);
            }
        }

        if (target instanceof SpaceShip spaceShip){
            if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
                if (!scaledToTarget) {
                    EffectAnimationHelper.scaleAnimation(target, this.animationList.get(0));
                    scaledToTarget = true;
                }
                this.animationList.get(0).setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
            }

            if(!hasApplied){
                this.startTimeInSeconds = GameState.getInstance().getGameSeconds();
                hasApplied = true;
            }

            if (currentTime - startTimeInSeconds < durationInSeconds) {
                spaceShip.setAllowedToAttack(false);
            }
        }
    }


    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        if (GameState.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;

    }

    private void deleteEffect(GameObject gameObject) {
        if (gameObject != null) {
            if (gameObject instanceof Enemy enemy) {
                enemy.setAllowedToFire(true);
            } else if(gameObject instanceof SpaceShip spaceShip){
                spaceShip.setAllowedToAttack(true);
            }

        }
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
        // Reset the start time to the current game time
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

        DisableAttackEffect copiedEffect = new DisableAttackEffect(this.durationInSeconds, clonedAnimation);
        // Copy other necessary fields
        copiedEffect.startTimeInSeconds = this.startTimeInSeconds;
        copiedEffect.effectIdentifier = this.effectIdentifier;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
        return copiedEffect;
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        if (!this.animationList.isEmpty() && this.animationList.get(0) != null) {
            this.animationList.get(0).setInfiniteLoop(false);
            this.animationList.get(0).setVisible(false);
        }

        deleteEffect(gameObject);
        this.animationList.clear();
    }

}
