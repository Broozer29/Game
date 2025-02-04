package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

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
            PlayerInventory.getInstance().addMinerals(goldAmount * GameStateInfo.getInstance().getDifficultyCoefficient());
            activated = true;

            if(additionalAnimation != null){
                additionalAnimation.setAnimationScale((gameObject.getScale()) * 0.80f);
                additionalAnimation.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
                AnimationManager.getInstance().addLowerAnimation(additionalAnimation);
            }
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
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
    public void increaseEffectStrength (GameObject gameObject) {
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

    @Override
    public void removeEffect (GameObject gameObject){
        if(additionalAnimation!= null){
            additionalAnimation.setInfiniteLoop(false);
            additionalAnimation.setVisible(false);
        }
        additionalAnimation = null;
    }
}
