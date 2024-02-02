package game.items.effects.effecttypes;

import VisualAndAudioData.image.ImageEnums;
import game.gamestate.GameStateInfo;
import game.items.effects.EffectInterface;
import game.items.effects.EffectActivationTypes;
import game.objects.GameObject;
import game.objects.neutral.Explosion;
import game.objects.neutral.ExplosionConfiguration;
import game.objects.neutral.ExplosionManager;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DormentExplosion implements EffectInterface {

    private float damage;
    private float scale;

    private ImageEnums explosionType;
    private EffectActivationTypes effectTypesEnums;
    private boolean activated;

    private float burningDamage;
    private int burningDuration;
    private DormentExplosionActivationMethods activationMethod;
    private double activationTime;
    private boolean allowedToApplyOnHitEffects = false;

    private List<EffectInterface> additionalEffects = new ArrayList<>();

    public DormentExplosion (float damage, float scale, ImageEnums explosionType, DormentExplosionActivationMethods activationMethod) {
        this.damage = damage;
        this.scale = scale;
        this.explosionType = explosionType;
        this.activationMethod = activationMethod;
        this.effectTypesEnums = EffectActivationTypes.DormentExplosion;
        this.activated = false;
        this.activationTime = GameStateInfo.getInstance().getGameSeconds() + 3; //3 seconds after applying

        if(this.activationMethod == DormentExplosionActivationMethods.OnDeath){
            allowedToApplyOnHitEffects = true;
        }

    }

    public void addAdditionalEffects (EffectInterface effect) {
        if (!additionalEffects.contains(effect)) {
            additionalEffects.add(effect);
        }
    }

    @Override
    public void activateEffect (GameObject gameObject) {
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

    private void createExplosion (GameObject gameObject) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(explosionType);
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, damage, true, allowedToApplyOnHitEffects);

        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        explosion.getAnimation().setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        explosion.setBoxCollision(true);
        explosion.setOwnerOrCreator(gameObject);


        if (burningDuration != 0 && burningDamage != 0) {
            SpriteAnimationConfiguration burningConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
            burningConfig.getSpriteConfiguration().setImageType(ImageEnums.GasolineBurning);
            burningConfig.getSpriteConfiguration().setScale(scale / 2);
            SpriteAnimation burningAnimation = new SpriteAnimation(burningConfig);
            DamageOverTime burning = new DamageOverTime(burningDamage, burningDuration, burningAnimation);
            explosion.addEffectToApply(burning);
        }

        ExplosionManager.getInstance().addExplosion(explosion);
        activated = true;
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
        return effectTypesEnums;
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
        DormentExplosion copy = new DormentExplosion(burningDamage, scale, explosionType, activationMethod);
        return copy;
    }

    public float getDamage () {
        return damage;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }

    public float getScale () {
        return scale;
    }

    public void setScale (float scale) {
        this.scale = scale;
    }

    public ImageEnums getExplosionType () {
        return explosionType;
    }

    public void setExplosionType (ImageEnums explosionType) {
        this.explosionType = explosionType;
    }

    public void setEffectTypesEnums (EffectActivationTypes effectTypesEnums) {
        this.effectTypesEnums = effectTypesEnums;
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
        return effectTypesEnums == that.effectTypesEnums;
    }

    @Override
    public int hashCode () {
        return Objects.hash(effectTypesEnums);
    }
}
