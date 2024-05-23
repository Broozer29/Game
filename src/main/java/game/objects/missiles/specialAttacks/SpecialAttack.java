package game.objects.missiles.specialAttacks;

import java.util.ArrayList;
import java.util.List;

import game.gamestate.GameStateInfo;
import game.objects.player.PlayerManager;
import game.util.BoardBlockUpdater;
import game.objects.GameObject;
import game.objects.missiles.Missile;
import game.util.CollisionDetector;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteAnimation;

public class SpecialAttack extends GameObject {
    protected List<Missile> specialAttackMissiles = new ArrayList<Missile>();
    protected boolean allowRepeatedDamage;
    protected boolean allowOnHitEffects;
    protected float onHitInterval;
    protected double lastOnHitInterval = 0f;

    public SpecialAttack (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration specialAttackConfiguration) {
        super(spriteAnimationConfiguration, null);
        this.damage = specialAttackConfiguration.getDamage();
        this.friendly = specialAttackConfiguration.isFriendly();
        this.allowedToDealDamage = specialAttackConfiguration.isAllowedToDealDamage();
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = specialAttackConfiguration.isBoxCollision();
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

    public void setScale (float newScale) {
        this.animation.setAnimationScale(newScale);
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

    public boolean isAllowOnHitEffects () {
        return allowOnHitEffects;
    }

    public void setAllowOnHitEffects (boolean allowOnHitEffects) {
        this.allowOnHitEffects = allowOnHitEffects;
    }

    public void checkEnemySpecialAttackCollision (GameObject gameObject) {
        if (this.getSpecialAttackMissiles().isEmpty()) {
            if (CollisionDetector.getInstance().detectCollision(gameObject, this)) {
                if (this.isAllowedToDealDamage()) {
                    gameObject.takeDamage(this.getDamage());
                }

                if (!this.isAllowRepeatedDamage()) {
                    this.setAllowedToDealDamage(false);
                }
            }
        }
    }
}