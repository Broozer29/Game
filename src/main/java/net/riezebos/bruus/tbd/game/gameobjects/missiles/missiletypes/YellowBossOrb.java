package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.YellowBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.AttackSpeedModifierEffect;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DirectHeal;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.ModifyMovementSpeedEffect;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.ArmorCalculator;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.List;

public class YellowBossOrb extends Missile {

    private boolean isHealOrb = false;

    public YellowBossOrb(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.allowedVisualsToRotate = false;
        this.isDamageable = false;
        this.isDestructable = false;
        this.destroysMissiles = false;
        this.destructionAnimation = null; //remove it as I don't want an animation here
    }

    //should be called after creation
    public void initOrbVersion(boolean isHealOrb) {
        this.isHealOrb = isHealOrb;
        if (this.isHealOrb) {
            this.animation.setFrameDelay(5);
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(-500);
            spriteConfiguration.setyCoordinate(-500);
            spriteConfiguration.setScale(1);
            spriteConfiguration.setImageType(ImageEnums.YellowBossHealCollision);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            DirectHeal directHeal = new DirectHeal(0.045f, spriteAnimation, EffectIdentifiers.YellowBossOrbHeal); //heal 7.5% hp
            this.addEffectToApply(directHeal);
        } else {
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setxCoordinate(-500);
            spriteConfiguration.setyCoordinate(-500);
            spriteConfiguration.setScale(0.65f);
            spriteConfiguration.setImageType(ImageEnums.YellowBossVoidCollision);
            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            //Debuff the player or player friendly objects
            AttackSpeedModifierEffect attackSpeedModifierEffect = new AttackSpeedModifierEffect(-0.45f, 3, spriteAnimation, EffectIdentifiers.YellowBossVoidAttackSpeedModifier);
            ModifyMovementSpeedEffect moveSpeedModifierEffect = new ModifyMovementSpeedEffect(-0.3f, 3, null, EffectIdentifiers.YellowBossVoidMoveSpeedModifier);
            this.addEffectToApply(attackSpeedModifierEffect);
            this.addEffectToApply(moveSpeedModifierEffect);
        }
    }


    private int stepsTaken = 0;
    private int stepsBetweenEachRetarget = 10;
    private int stepsBetweenCollisionCheck = 3;

    //Every X steps, retarget to the center of the boss, since the boss moves up and down a bit. Do it every X steps to save performance
    public void missileAction() {
        stepsTaken++;
        if (stepsTaken % stepsBetweenEachRetarget == 0) {
            List<Enemy> yellowBossList = EnemyManager.getInstance().getEnemiesByType(EnemyEnums.YellowBoss); //assumes there can only be 1 boss at all times, breaks if there are multiple
            if (!yellowBossList.isEmpty()) {
                this.resetMovementPath();
                this.movementConfiguration.setDestination(new Point(yellowBossList.get(0).getCenterXCoordinate(), yellowBossList.get(0).getCenterYCoordinate()));
                if(this.isHealOrb){
                    this.setAllowedVisualsToRotate(true);
                    this.rotateGameObjectTowards(yellowBossList.get(0).getCenterXCoordinate(), yellowBossList.get(0).getCenterYCoordinate(), false);
                }
            } else {
                this.setVisible(false);
            }
        }

        if (stepsTaken % stepsBetweenCollisionCheck == 0 && this.isVisible()) {
            List<Enemy> yellowBossList = EnemyManager.getInstance().getEnemiesByType(EnemyEnums.YellowBoss);
            if (!yellowBossList.isEmpty()) {
                YellowBoss yellowBoss = (YellowBoss) yellowBossList.get(0); //assumes there can only be 1 boss at all times, breaks if there are multiple
                if (CollisionDetector.getInstance().detectCollisionIgnoringOwnerRules(this, yellowBoss) != null) {
                    this.setVisible(false);
                    if (this.isHealOrb) {
                        applyHealEffect(yellowBoss);
                    } else {
                        applyVoidEffect(yellowBoss);
                    }
                }
            }
        }
    }

    private void applyVoidEffect(YellowBoss yellowBoss) {
        //tbd
    }

    private void applyHealEffect(GameObject target) {
        //applies all effects instead of the directheal only, shouldnt be a problem since only the heal effect should be present
        for(EffectInterface effectInterface : effectsToApply){
            effectInterface.activateEffect(target);
        }
        AudioManager.getInstance().addAudio(AudioEnums.GenericSelect);
    }


    @Override
    //Overrides the parent behaviour but we need to change it if it is a healorb, so we make sure we dont go down the takeDamage path after collision by exiting early
    public void dealDamageToGameObject(GameObject target) {
        if(this.isHealOrb){
            applyHealEffect(target);
            return;
        }


        if (target.getCurrentHitpoints() <= 0.000f || !target.isVisible()) {
            return; //if it's dead, skip this operation
        }
        for (EffectInterface effectInterface : effectsToApply) {
            target.addEffect(effectInterface);
        }


        float damage = ArmorCalculator.calculateDamage(getDamage(), target);
        target.takeDamage(damage);

        if (showDamage && damage >= 1) {
            OnScreenTextManager.getInstance().addDamageNumberText(Math.round(damage), target.getCenterXCoordinate(),
                    target.getCenterYCoordinate(), isACrit, calculateFontSizeBasedOnDamageAmount(target, damage));
        }
    }

}
