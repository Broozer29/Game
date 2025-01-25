package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.ModifyMovementSpeedEffect;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class PulsingDrone extends Enemy {

    public PulsingDrone (SpriteAnimationConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 0;
        this.attackSpeed = 4;
        this.chargingUpAttackAnimation.setFrameDelay(10);
        this.chargingUpAttackAnimation.setAnimationScale(1.6f);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
    }


    @Override
    public void fireAction(){
        if(this.movementConfiguration.getPathFinder() instanceof DestinationPathFinder){
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }

        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (allowedToFire && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            updateChargingAnimation();
            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootEnergyCircle();
                this.isAttacking = false;
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }
    }

    private void updateChargingAnimation(){
        this.chargingUpAttackAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
    }

    private void shootEnergyCircle(){
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setScale(1f);
        missileSpriteConfiguration.setImageType(ImageEnums.EnergyCircle);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(missileSpriteConfiguration, 3, false);
        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(this.getDamage(), false,
                true, false, false, false, false);
        SpecialAttack specialAttack = new SpecialAttack(spriteAnimationConfiguration, specialAttackConfiguration);
        SpriteAnimation animation = createDebuffAnimation();
        specialAttack.setDestroysMissiles(false);

        EffectInterface effect = new ModifyMovementSpeedEffect(-0.5f,1.5, animation, EffectIdentifiers.PulsingDroneSlow);
        specialAttack.addEffectToApply(effect);

        specialAttack.setOwnerOrCreator(this);
        specialAttack.setAllowRepeatedDamage(false);
        specialAttack.setObjectType("Pulsing Drone Energy Circle Attack");
        specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

    private SpriteAnimation createDebuffAnimation(){
        SpriteConfiguration spriteconfig = new SpriteConfiguration();
        spriteconfig.setxCoordinate(this.getCenterXCoordinate());
        spriteconfig.setyCoordinate(this.getCenterYCoordinate());
        spriteconfig.setScale(1f);
        spriteconfig.setImageType(ImageEnums.SuperChargedBuff);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteconfig, 2, true);
        return new SpriteAnimation(spriteAnimationConfiguration);
    }
}
