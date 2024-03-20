package game.objects.neutral;

import java.util.ArrayList;
import java.util.List;

import game.items.effects.EffectInterface;
import game.objects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class Explosion extends GameObject {

    private List<EffectInterface> effectsToApply = new ArrayList<>();
    private boolean applyOnHitEffects;

    public Explosion (SpriteAnimationConfiguration spriteAnimationConfiguration, ExplosionConfiguration explosionConfiguration) {
        super(spriteAnimationConfiguration, null);
        this.friendly = explosionConfiguration.isFriendly();
        this.damage = explosionConfiguration.getDamage();
        this.allowedToDealDamage = explosionConfiguration.isAllowedToDealDamage();
        this.applyOnHitEffects = explosionConfiguration.isApplyOnHitEffects();
        this.setObjectType("Explosion");

        this.animation.setX(this.xCoordinate - (animation.getWidth() / 2));
        this.animation.setY(this.yCoordinate - (animation.getHeight() / 2));
    }


    public void addCollidedSprite (GameObject sprite) {
        if (!this.skipCollision.contains(sprite)) {
            this.skipCollision.add(sprite);
        }
    }

    public void addEffectToApply(EffectInterface effect){
        if(!this.effectsToApply.contains(effect)){
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
        if (this.animation.getCurrentFrame() > 5 || this.damage == 0) {
            setAllowedToDealDamage(false);
        }
    }

    private List<GameObject> skipCollision = new ArrayList<GameObject>();
    public boolean dealtDamageToTarget(GameObject objectToCheck){
        if(skipCollision.contains(objectToCheck)){
            return true;
        }
        return false;
    }

    public boolean isApplyOnHitEffects () {
        return applyOnHitEffects;
    }

    public void setApplyOnHitEffects (boolean applyOnHitEffects) {
        this.applyOnHitEffects = applyOnHitEffects;
    }
}