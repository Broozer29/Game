package game.items.effects.effectimplementations;

import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.managers.AnimationManager;
import game.objects.GameObject;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class GainGoldOnDeath implements EffectInterface {

    private float goldAmount;
    private EffectIdentifiers effectIdentifier;

    private EffectActivationTypes effectActivationType;
    private boolean activated;

    private SpriteAnimation additionalAnimation;

    public GainGoldOnDeath (EffectActivationTypes effectActivationType, EffectIdentifiers effectIdentifier, float goldAmount) {
        this.effectActivationType = effectActivationType;
        this.effectIdentifier = effectIdentifier;
        this.goldAmount = goldAmount;
        this.activated = false;
        initAnimation();
    }

    private void initAnimation(){
        if(this.effectIdentifier.equals(EffectIdentifiers.CashCarrierGoldGain)){
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(0);
            spriteConfiguration.setyCoordinate(0);
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.CashExplosion);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
            additionalAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        }
    }


    @Override
    public void activateEffect (GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !activated) {
            PlayerInventory.getInstance().gainCashMoney(goldAmount * GameStateInfo.getInstance().getDifficultyCoefficient());
            activated = true;

            if(additionalAnimation != null){
                additionalAnimation.setAnimationScale((gameObject.getScale()) * 0.80f);
                additionalAnimation.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
                AnimationManager.getInstance().addLowerAnimation(additionalAnimation);
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
