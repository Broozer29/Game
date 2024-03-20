package game.objects.player.specialAttacks;

import java.util.ArrayList;
import java.util.List;

import game.util.BoardBlockUpdater;
import game.objects.GameObject;
import game.objects.missiles.Missile;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteAnimation;

public class SpecialAttack extends GameObject {
    protected List<Missile> specialAttackMissiles = new ArrayList<Missile>();
    protected boolean allowRepeatedDamage;


    public SpecialAttack (SpriteAnimationConfiguration spriteAnimationConfiguration, SpecialAttackConfiguration specialAttackConfiguration) {
        super(spriteAnimationConfiguration, null);
        this.damage = specialAttackConfiguration.getDamage();
        this.friendly = specialAttackConfiguration.isFriendly();
        this.allowedToDealDamage = specialAttackConfiguration.isAllowedToDealDamage();
        this.currentBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);
        this.boxCollision = specialAttackConfiguration.isBoxCollision();
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
}