package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.util.EffectAnimationHelper;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

public class FreezeEffect implements EffectInterface {

    private double durationInSeconds;
    private double startTimeInSeconds;


    private boolean scaledToTarget = false;

    private SpriteAnimation animation;
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectTypesEnums;

    public FreezeEffect(double durationInSeconds, SpriteAnimation spriteAnimation){
        this.effectIdentifier = EffectIdentifiers.ElectricDestabilizerFreeze;
        this.durationInSeconds = durationInSeconds;
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.animation = spriteAnimation;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }
    @Override
    public void activateEffect (GameObject target) {
        if(target instanceof Enemy enemy){
            if(enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss)){
                //Bosses should be immune to this
                return;
            }
        }


        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (animation != null) {
            if (!scaledToTarget) {
                EffectAnimationHelper.scaleAnimation(target, animation);
                scaledToTarget = true;
                animation.setCenterCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
            }
        }

        if (currentTime - startTimeInSeconds < durationInSeconds) {
            target.setAllowedToMove(false);
            if(target instanceof Enemy enemy){
                enemy.setAllowedToFire(false);
            }
        }
    }



    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            return true;
        } else return false;

    }

    private void deleteEffect(GameObject gameObject){
        if(gameObject != null) {
            gameObject.setAllowedToMove(true);
            if (gameObject instanceof Enemy) {
                ((Enemy) gameObject).setAllowedToFire(true);
            }
        }
    }

    @Override
    public SpriteAnimation getAnimation () {
        return animation;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return this.effectTypesEnums;
    }

    @Override
    public void resetDuration () {
        // Reset the start time to the current game time
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
//does nothing
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public EffectInterface copy () {
        FreezeEffect copiedEffect = new FreezeEffect(this.durationInSeconds, this.animation);
        // Copy other necessary fields
        copiedEffect.startTimeInSeconds = this.startTimeInSeconds;
        copiedEffect.effectIdentifier = this.effectIdentifier;
        // Note: startTimeInSeconds may need special handling depending on desired behavior
        return copiedEffect;
    }

    @Override
    public void removeEffect (GameObject gameObject){
        if(animation != null){
            animation.setInfiniteLoop(false);
            animation.setVisible(false);
        }

        deleteEffect(gameObject);
        animation = null;
    }

}
