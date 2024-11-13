package net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.List;

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