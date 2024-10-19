package game.gameobjects.neutral;

import java.util.ArrayList;
import java.util.List;

import game.items.effects.EffectInterface;
import game.gameobjects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class Explosion extends GameObject {

    private List<EffectInterface> effectsToApply = new ArrayList<>();
    private boolean applyOnHitEffects;
    private int maxAnimationFramesForDamage;

    public Explosion (SpriteAnimationConfiguration spriteAnimationConfiguration, ExplosionConfiguration explosionConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendly = explosionConfiguration.isFriendly();
        this.damage = explosionConfiguration.getDamage();
        this.allowedToDealDamage = explosionConfiguration.isAllowedToDealDamage();
        this.applyOnHitEffects = explosionConfiguration.isApplyOnHitEffects();
        this.setObjectType("Explosion");

        this.animation.setXCoordinate(this.xCoordinate - (animation.getWidth() / 2));
        this.animation.setYCoordinate(this.yCoordinate - (animation.getHeight() / 2));
        initAllowedToDealDamageFramesAmount();
    }


    public void addCollidedSprite (GameObject sprite) {
        if (!this.skipCollision.contains(sprite)) {
            this.skipCollision.add(sprite);
        }
    }

    public void addEffectToApply (EffectInterface effect) {
        if (!this.effectsToApply.contains(effect)) {
            effectsToApply.add(effect);
        }
    }

    public List<EffectInterface> getEffectsToApply () {
        return effectsToApply;
    }

    public List<GameObject> getCollidedSprites () {
        return this.skipCollision;
    }

    public void updateAllowedToDealDamage () {
        if (this.animation.getCurrentFrame() > this.maxAnimationFramesForDamage) {
            setAllowedToDealDamage(false);
        }
    }


    private void initAllowedToDealDamageFramesAmount () {
        if (this.isFriendly()) { //Should probably be different per animation, this is a bit of a shoddy fix
            this.maxAnimationFramesForDamage = this.animation.getTotalFrames();
        } else {
            this.maxAnimationFramesForDamage = 10;
        }
    }

    private List<GameObject> skipCollision = new ArrayList<GameObject>();

    public boolean dealtDamageToTarget (GameObject objectToCheck) {
        if (skipCollision.contains(objectToCheck)) {
            return true;
        }
        return false;
    }

    public void applyExplosionOnHitEffects (GameObject target) {
        for (EffectInterface effect : effectsToApply) {
            EffectInterface effectCopy = effect.copy();
            if (effectCopy != null) {
                target.addEffect(effectCopy);
            } else target.addEffect(effect);
        }
    }

    public boolean isApplyOnHitEffects () {
        return applyOnHitEffects;
    }

    public void setApplyOnHitEffects (boolean applyOnHitEffects) {
        this.applyOnHitEffects = applyOnHitEffects;
    }
}