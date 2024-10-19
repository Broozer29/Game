package game.gameobjects.missiles.specialAttacks;

import java.util.ArrayList;
import java.util.List;

import game.gamestate.GameStateInfo;
import game.util.BoardBlockUpdater;
import game.gameobjects.GameObject;
import game.gameobjects.missiles.Missile;
import game.util.collision.CollisionDetector;
import game.util.collision.CollisionInfo;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteAnimation;

public class SpecialAttack extends GameObject {
    protected List<Missile> specialAttackMissiles = new ArrayList<Missile>();
    protected boolean allowRepeatedDamage;
    protected float onHitInterval;
    protected double lastOnHitInterval = 0f;
    protected boolean destroysMissiles;

    public SpecialAttack (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration specialAttackConfiguration) {
        super(spriteAnimationConfiguration);
        this.damage = specialAttackConfiguration.getDamage();
        this.friendly = specialAttackConfiguration.isFriendly();
        this.allowedToDealDamage = specialAttackConfiguration.isAllowedToDealDamage();
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = specialAttackConfiguration.isBoxCollision();
        this.destroysMissiles = specialAttackConfiguration.isDestroysMissiles();
    }

    public float getOnHitInterval () {
        return onHitInterval;
    }

    public void setOnHitInterval (float onHitInterval) {
        this.onHitInterval = onHitInterval;
    }

    public double getLastOnHitInterval () {
        return lastOnHitInterval;
    }

    public void setLastOnHitInterval (double lastOnHitInterval) {
        this.lastOnHitInterval = lastOnHitInterval;
    }

    public boolean canApplyEffectAgain () {
        if (GameStateInfo.getInstance().getGameSeconds() >= (lastOnHitInterval + onHitInterval)) {
            return true;
        } else return false;
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
}