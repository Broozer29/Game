package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.LingeringFlame;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.DormentExplosionActivationMethods;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DormentExplosion implements EffectInterface {

    private float damage;

    private ImageEnums imageType;
    private EffectActivationTypes activationTypes;
    private boolean activated;
    private boolean boxCollision;

    private float burningDamage;
    private float burningDuration;
    private DormentExplosionActivationMethods activationMethod;
    private double activationTime;
    private boolean allowedToApplyOnHitEffects = false;
    private float delayBeforeExplosion;
    private EffectIdentifiers effectIdentifier;
    private AudioEnums audioEnums;

    private List<EffectInterface> additionalEffects = new ArrayList<>();

    public DormentExplosion (float damage, ImageEnums explosionType, DormentExplosionActivationMethods activationMethod, boolean boxCollision
            , EffectIdentifiers effectIdentifier, float delayBeforeExplosion, EffectActivationTypes activationType, boolean allowedToApplyOnHitEffects) {
        this.damage = damage;
        this.boxCollision = boxCollision;
        this.imageType = explosionType;
        this.activationMethod = activationMethod;
        this.activationTypes = activationType;
        this.activated = false;
        this.delayBeforeExplosion = delayBeforeExplosion;
        this.activationTime = GameState.getInstance().getGameSeconds() + delayBeforeExplosion;
        this.effectIdentifier = effectIdentifier;
        this.allowedToApplyOnHitEffects = allowedToApplyOnHitEffects;

    }

    public void addAdditionalEffects (EffectInterface effect) {
        if (!additionalEffects.contains(effect)) {
            additionalEffects.add(effect);
        }
    }

    @Override
    public void activateEffect (GameObject gameObject) {
        if (!activated) {
            switch (activationMethod) {
                case OnDeath -> {
                    if (gameObject.getCurrentHitpoints() <= 0) {
                        activateDormantExplosion(gameObject);
                        activated = true;
                    }
                }
                case Timed -> {
                    if (GameState.getInstance().getGameSeconds() > activationTime) {
                        activateDormantExplosion(gameObject);
                        activated = true;
                    }
                }
            }
        }
    }

    private void activateDormantExplosion (GameObject gameObject) {
        if(this.isSpecialAttack()){
            createSpecialAttack(gameObject);
            return; //We dont want an explosion but a special attack
        }


        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(imageType);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, getFrameDelayByExplosionType(), false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, damage, true, allowedToApplyOnHitEffects);

        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        handleSpecialAttack(explosion);

        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        explosion.getAnimation().setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        explosion.setBoxCollision(boxCollision);
        explosion.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship()); //Assume it's the player who has items, never the enemies. Could hinder later design

        if (this.effectIdentifier.equals(EffectIdentifiers.GasolineDormantExplosion)) {
            if (burningDuration != 0 && burningDamage != 0) {
                SpriteAnimationConfiguration burningConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
                burningConfig.getSpriteConfiguration().setImageType(ImageEnums.IgniteBurning);
                burningConfig.getSpriteConfiguration().setScale(1);
                SpriteAnimation burningAnimation = new SpriteAnimation(burningConfig);
                DamageOverTime burning = new DamageOverTime(burningDamage, burningDuration, burningAnimation, EffectIdentifiers.Ignite);
                explosion.addEffectToApply(burning);
            }
        }

        if (audioEnums != null) {
            AudioManager.getInstance().addAudio(audioEnums);
        }

        ExplosionManager.getInstance().addExplosion(explosion);

    }

    private boolean isSpecialAttack(){
        return this.effectIdentifier.equals(EffectIdentifiers.FlameDetonationDormentExplosion);
    }

    private void createSpecialAttack(GameObject gameObject){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(imageType);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);

        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(0, true,
                true, false, true, false, false);
        SpecialAttack lingeringFlame = new LingeringFlame(spriteAnimationConfiguration, specialAttackConfiguration);
        lingeringFlame.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        handleSpecialAttack(lingeringFlame);
        MissileManager.getInstance().addSpecialAttack(lingeringFlame);
    }

    private int getFrameDelayByExplosionType () {
        if (imageType.equals(ImageEnums.GasolineExplosion)) {
            return 0;
        } else return 2;
    }

    private void handleSpecialAttack (GameObject specialAttackOrExplosion) {
        switch(specialAttackOrExplosion.getImageEnum()){
            case GasolineExplosion:
                specialAttackOrExplosion.setScale(1.15f);
                break;
            case LingeringFlameLooping:
                specialAttackOrExplosion.setTransparancyAlpha(true, 0.0f, 0.03f);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean shouldBeRemoved (GameObject gameObject) {
        return activated;
    }

    @Override
    public SpriteAnimation getAnimation () {
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums () {
        return activationTypes;
    }

    @Override
    public void resetDuration () {
        //Does nothing for dorment explosion
    }

    @Override
    public void increaseEffectStrength (GameObject gameObject) {
        //Does nothing for dorment explosion (but can be implemented later? Poggers)
    }

    @Override
    public EffectInterface copy () {
        return new DormentExplosion(burningDamage, imageType, activationMethod, boxCollision, effectIdentifier, delayBeforeExplosion, activationTypes, allowedToApplyOnHitEffects);
    }

    public float getDamage () {
        return damage;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }


    public void setBurningDamage (float burningDamage) {
        this.burningDamage = burningDamage;
    }

    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }


    public void setBurningDuration (float burningDuration) {
        this.burningDuration = burningDuration;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    public void setAudioEnums (AudioEnums audioEnums) {
        this.audioEnums = audioEnums;
    }

    @Override
    public void removeEffect (GameObject gameObject) {

    }
}
