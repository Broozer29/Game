package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.util.ArmorCalculator;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.*;

public class SpecialAttack extends GameObject {
    protected List<Missile> specialAttackMissiles = new ArrayList<Missile>();
    protected boolean allowRepeatedDamage;
    protected boolean destroysMissiles;
    protected boolean damagesMissiles;
    protected float maxHPDamagePercentage;
    private Map<GameObject, Double> affectedObjects = new HashMap<>();
    protected double internalTickCooldown = 0.25f;
    protected boolean isDissipating = false;
    protected boolean appliesItemEffects = true;

    public SpecialAttack (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration specialAttackConfiguration) {
        super(spriteAnimationConfiguration);
        this.damage = specialAttackConfiguration.getDamage();
        this.friendly = specialAttackConfiguration.isFriendly();
        this.allowedToDealDamage = specialAttackConfiguration.isAllowedToDealDamage();
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = specialAttackConfiguration.isBoxCollision();
        this.destroysMissiles = specialAttackConfiguration.isDestroysMissiles();

        if(this.destroysMissiles){
            this.damagesMissiles = false; //If it cant destroy them, dont damage them by default either.
        }
    }


    @Override
    //Overridden because SpecialAttacks behave like explosions. Missiles apply 1 on-hit effect and get removed, SpecialAttacks can apply
    //its effect to multiple, thus copies are required
    public void dealDamageToGameObject (GameObject target) {
        for (EffectInterface effect : effectsToApply) {
            EffectInterface effectCopy = effect.copy();
            if (effectCopy != null) {
                target.addEffect(effectCopy);
            } else target.addEffect(effect);
        }

        float damage = ArmorCalculator.calculateDamage(getDamage(), target);
        target.takeDamage(damage);

        if (showDamage && damage >= 1) {
            OnScreenTextManager.getInstance().addDamageNumberText(Math.round(damage), target.getCenterXCoordinate(),
                    target.getCenterYCoordinate(), isACrit);
        }

    }


    public void tryDealDamageAndApplyEffects (GameObject target) {
        double currentTime = GameState.getInstance().getGameSeconds();
        double lastAffectedTime = affectedObjects.getOrDefault(target, 0.0);

        if (currentTime >= lastAffectedTime + internalTickCooldown) {
            if (isAllowOnHitEffects()) {
                applyBeforeCollisionAttackModifyingItemEffects(target); //Might be problematic? Handle within item classes itself if it is problematic for some
            }
            dealDamageToGameObject(target);
            if (isAllowOnHitEffects() && appliesItemEffects) {
                applyAfterCollisionItemEffects(target);
            }
            affectedObjects.put(target, currentTime); // Update the last affected time
        }
    }

    public void updateSpecialAttackEnemyCollisionList () {
        Iterator<Map.Entry<GameObject, Double>> iterator = affectedObjects.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<GameObject, Double> entry = iterator.next();
            GameObject gameObject = entry.getKey();

            if (!gameObject.isVisible()) {
                iterator.remove(); // Remove objects that are no longer visible
            }
        }
    }

    public void updateSpecialAttack () {
        //Exists to be overriden
    }


    public void startDissipating () {
        //Exists to be overriden
    }

    public boolean isDissipating () {
        return isDissipating;
    }

    public SpriteAnimation getAnimation () {
        return this.animation;
    }

    public boolean centeredAroundPlayer () {
        return centeredAroundObject;
    }

    public void setCenteredAroundObject (Boolean bool) {
        this.centeredAroundObject = bool;
    }

    public List<Missile> getSpecialAttackMissiles () {
        return this.specialAttackMissiles;
    }

    public void setSpecialAttackMissiles (List<Missile> specialAttackMissiles) {
        this.specialAttackMissiles = specialAttackMissiles;
    }

    public boolean isAllowRepeatedDamage () {
        return allowRepeatedDamage;
    }

    public void setAllowRepeatedDamage (boolean allowRepeatedDamage) {
        this.allowRepeatedDamage = allowRepeatedDamage;
    }


    public void checkEnemySpecialAttackCollision (GameObject gameObject) {
        if (this.getSpecialAttackMissiles().isEmpty()) {
            CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(gameObject, this);
            if (collisionInfo != null) {
                if (this.isAllowedToDealDamage()) {
                    this.dealDamageToGameObject(gameObject);
                }

                if (!this.isAllowRepeatedDamage()) {
                    this.setAllowedToDealDamage(false);
                }
            }
        }
    }

    public boolean isAllowOnHitEffects () {
        return this.appliesOnHitEffects;
    }

    public boolean isDestroysMissiles () {
        return destroysMissiles;
    }

    public void setDestroysMissiles (boolean destroysMissiles) {
        this.destroysMissiles = destroysMissiles;
    }

    public boolean isDamagesMissiles () {
        return damagesMissiles;
    }

    public void setDamagesMissiles (boolean damagesMissiles) {
        this.damagesMissiles = damagesMissiles;
    }

    public float getMaxHPDamagePercentage () {
        return maxHPDamagePercentage;
    }

    public void setMaxHPDamagePercentage (float maxHPDamagePercentage) {
        this.maxHPDamagePercentage = maxHPDamagePercentage;
    }
}