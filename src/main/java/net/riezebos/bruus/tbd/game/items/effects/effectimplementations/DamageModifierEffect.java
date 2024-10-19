package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

public class DamageModifierEffect implements EffectInterface {


    private float damageModifierAmount = 1.0f;
    private EffectActivationTypes effectTypesEnums;
    private double durationInSeconds;
    private double startTimeInSeconds;

    private SpriteAnimation animation;
    private GameObject modifiedObject;
    private boolean appliedToObject;
    private EffectIdentifiers effectIdentifier;

    public DamageModifierEffect (float damageModifierAmount, double durationInSeconds, SpriteAnimation animation, EffectIdentifiers effectIdentifier) {
        this.damageModifierAmount = damageModifierAmount;
        this.durationInSeconds = durationInSeconds;
        this.animation = animation;
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
        this.effectTypesEnums = EffectActivationTypes.CheckEveryGameTick;
        this.appliedToObject = false;
        this.effectIdentifier = effectIdentifier;
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        if (!appliedToObject) {
            this.modifiedObject = gameObject;
            if (gameObject instanceof SpaceShip) {
//                float oldValue = PlayerStats.getInstance().getNormalAttackDamage();
                PlayerStats.getInstance().modifyBonusDamageMultiplier(-0.5f);
//                OnScreenTextManager.getInstance().addText(PlayerStats.getInstance().getNormalAttackDamage() + " / " + oldValue,
//                        PlayerManager.getInstance().getSpaceship().getXCoordinate(), PlayerManager.getInstance().getSpaceship().getYCoordinate());
            } else if (gameObject.isFriendly()) {
                gameObject.modifyBonusDamageMultiplier(-0.5f);
            } else {
//                float oldValue = gameObject.getDamage();
                gameObject.modifyBonusDamageMultiplier(0.5f);
//                OnScreenTextManager.getInstance().addText(modifiedObject.getDamage() + " / " + oldValue,
//                        modifiedObject.getXCoordinate(), modifiedObject.getYCoordinate());
            }
            appliedToObject = true;
        }

        if (animation != null) {
            centerAnimation(gameObject);
        }

    }

    private void removeEffectsBeforeRemovingEffect () {
        if (modifiedObject instanceof SpaceShip) {
//            float oldValue = PlayerStats.getInstance().getNormalAttackDamage();
            PlayerStats.getInstance().modifyBonusDamageMultiplier(0.5f);
//            OnScreenTextManager.getInstance().addText(PlayerStats.getInstance().getNormalAttackDamage() + " / " + oldValue,
//                    PlayerManager.getInstance().getSpaceship().getXCoordinate(), PlayerManager.getInstance().getSpaceship().getYCoordinate());

        } else if (modifiedObject.isFriendly()) {
            modifiedObject.modifyBonusDamageMultiplier(0.5f);
        } else {
//            float oldValue = modifiedObject.getDamage();
            modifiedObject.modifyBonusDamageMultiplier(-0.5f);
//            OnScreenTextManager.getInstance().addText(modifiedObject.getDamage() + " / " + oldValue,
//                    modifiedObject.getXCoordinate(), modifiedObject.getYCoordinate());
        }

        if(animation != null){
            animation.setVisible(false);
        }

    }


    private void centerAnimation (GameObject object) {
        if (object.getAnimation() != null) {
            SpriteAnimation objectVisuals = object.getAnimation();
            animation.setCenterCoordinates(objectVisuals.getCenterXCoordinate(), objectVisuals.getCenterYCoordinate());
        } else {
            animation.setCenterCoordinates(object.getCenterXCoordinate(), object.getCenterYCoordinate());
        }


    }

    @Override
    public boolean shouldBeRemoved () {
        if (GameStateInfo.getInstance().getGameSeconds() - startTimeInSeconds >= durationInSeconds) {
            removeEffectsBeforeRemovingEffect(); //Assumes the effect is immediatly removed if this is true (currently is)
            return true;
        } else return false;
    }

    @Override
    public SpriteAnimation getAnimation () {
        return animation;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return effectTypesEnums;
    }

    @Override
    public void resetDuration () {
        this.startTimeInSeconds = GameStateInfo.getInstance().getGameSeconds();
    }

    @Override
    public void increaseEffectStrength () {
        //To be implemented later, if ever
    }

    @Override
    public EffectInterface copy () {
        DamageModifierEffect copiedEffect = new DamageModifierEffect(damageModifierAmount, durationInSeconds, animation.clone(), effectIdentifier);
        return copiedEffect;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }
}
