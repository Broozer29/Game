package game.items.effects.effectimplementations;

import VisualAndAudioData.audio.AudioManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.items.effects.DormentExplosionActivationMethods;
import game.items.effects.EffectIdentifiers;
import game.items.effects.EffectInterface;
import game.items.effects.EffectActivationTypes;
import game.gameobjects.GameObject;
import game.gameobjects.neutral.Explosion;
import game.gameobjects.neutral.ExplosionConfiguration;
import game.gameobjects.neutral.ExplosionManager;
import game.gameobjects.player.PlayerManager;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DormentExplosion implements EffectInterface {

    private float damage;

    private ImageEnums explosionType;
    private EffectActivationTypes activationTypes;
    private boolean activated;
    private boolean boxCollision;

    private float burningDamage;
    private int burningDuration;
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
        this.explosionType = explosionType;
        this.activationMethod = activationMethod;
        this.activationTypes = activationType;
        this.activated = false;
        this.delayBeforeExplosion = delayBeforeExplosion;
        this.activationTime = GameStateInfo.getInstance().getGameSeconds() + delayBeforeExplosion;
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
        if(!activated) {
            switch (activationMethod) {
                case OnDeath -> {
                    if (gameObject.getCurrentHitpoints() <= 0) {
                        createExplosion(gameObject);
                    }
                }
                case Timed -> {
                    if (GameStateInfo.getInstance().getGameSeconds() > activationTime) {
                        createExplosion(gameObject);
                    }
                }
            }
        }
    }

    private void createExplosion (GameObject gameObject) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
         spriteConfiguration.setScale(getScaleByExplosionType());
        spriteConfiguration.setImageType(explosionType);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, getFrameDelayByExplosionType(), false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, damage, true, allowedToApplyOnHitEffects);

        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setScale(getScaleByExplosionType());
        explosion.getAnimation().setAnimationScale(getScaleByExplosionType());

        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        explosion.getAnimation().setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        explosion.setBoxCollision(boxCollision);
        explosion.setOwnerOrCreator(PlayerManager.getInstance().getSpaceship()); //Assume it's the player who has items, never the enemies. Could hinder later design


        if (burningDuration != 0 && burningDamage != 0) { //Assuming it's always gasoline at this point cause nothing else exists atm
            SpriteAnimationConfiguration burningConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
            burningConfig.getSpriteConfiguration().setImageType(ImageEnums.GasolineBurning);
            burningConfig.getSpriteConfiguration().setScale(1);
            SpriteAnimation burningAnimation = new SpriteAnimation(burningConfig);
            DamageOverTime burning = new DamageOverTime(burningDamage, burningDuration, burningAnimation, EffectIdentifiers.GasolineBurning);
            explosion.addEffectToApply(burning);
        }

        if(audioEnums != null){
            try {
                AudioManager.getInstance().addAudio(audioEnums);
            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        ExplosionManager.getInstance().addExplosion(explosion);
        activated = true;
    }

    private int getFrameDelayByExplosionType () {
        if (explosionType.equals(ImageEnums.GasolineExplosion)) {
            return 0;
        } else return 2;
    }

    private float getScaleByExplosionType () {
        if (explosionType.equals(ImageEnums.GasolineExplosion)) {
            return 1.15f;
        } else return 1;
    }

    @Override
    public boolean shouldBeRemoved () {
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
    public void increaseEffectStrength () {
        //Does nothing for dorment explosion
    }

    @Override
    public EffectInterface copy () {
        DormentExplosion copy = new DormentExplosion(burningDamage, explosionType, activationMethod, boxCollision, effectIdentifier, delayBeforeExplosion, activationTypes, allowedToApplyOnHitEffects);
        return copy;
    }

    public float getDamage () {
        return damage;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }


    public ImageEnums getExplosionType () {
        return explosionType;
    }

    public void setExplosionType (ImageEnums explosionType) {
        this.explosionType = explosionType;
    }

    public void setEffectTypesEnums (EffectActivationTypes effectTypesEnums) {
        this.activationTypes = effectTypesEnums;
    }

    public boolean isActivated () {
        return activated;
    }

    public void setActivated (boolean activated) {
        this.activated = activated;
    }

    public float getBurningDamage () {
        return burningDamage;
    }

    public void setBurningDamage (float burningDamage) {
        this.burningDamage = burningDamage;
    }


    public boolean isAllowedToApplyOnHitEffects () {
        return allowedToApplyOnHitEffects;
    }

    public void setAllowedToApplyOnHitEffects (boolean allowedToApplyOnHitEffects) {
        this.allowedToApplyOnHitEffects = allowedToApplyOnHitEffects;
    }

    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }

    public int getBurningDuration () {
        return burningDuration;
    }

    public void setBurningDuration (int burningDuration) {
        this.burningDuration = burningDuration;
    }

    public List<EffectInterface> getAdditionalEffects () {
        return additionalEffects;
    }

    public void setAdditionalEffects (List<EffectInterface> additionalEffects) {
        this.additionalEffects = additionalEffects;
    }

    public DormentExplosionActivationMethods getActivationMethod () {
        return activationMethod;
    }

    public void setActivationMethod (DormentExplosionActivationMethods activationMethod) {
        this.activationMethod = activationMethod;
    }

    public double getActivationTime () {
        return activationTime;
    }

    public void setActivationTime (double activationTime) {
        this.activationTime = activationTime;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DormentExplosion that = (DormentExplosion) o;
        return activationTypes == that.activationTypes;
    }

    @Override
    public EffectIdentifiers getEffectIdentifier () {
        return effectIdentifier;
    }

    @Override
    public int hashCode () {
        return Objects.hash(activationTypes);
    }

    public AudioEnums getAudioEnums () {
        return audioEnums;
    }

    public void setAudioEnums (AudioEnums audioEnums) {
        this.audioEnums = audioEnums;
    }
}
