package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlamerForceField;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Flamer extends Enemy {

    public Flamer (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 5;
        this.attackSpeed = 5;
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        if(this.chargingUpAttackAnimation != null){
            this.chargingUpAttackAnimation.setFrameDelay(this.chargingUpAttackAnimation.getFrameDelay() + 1); //idk what the default value is so this
        }
    }

    public void fireAction () {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            updateChargingAttackAnimationCoordination();
            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile();
                this.isAttacking = false;
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }
    }

    private void shootMissile () {
//        MissileTypeEnums missileType = MissileTypeEnums.FlamerProjectile;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setScale(1.25f);
        missileSpriteConfiguration.setImageType(ImageEnums.EnergyCircle);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(missileSpriteConfiguration, 2, false);

        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(this.getDamage(), false, true, false, false, false, false);
        SpecialAttack specialAttack = new FlamerForceField(spriteAnimationConfiguration, specialAttackConfiguration);
        specialAttack.setOwnerOrCreator(this);
        specialAttack.setObjectType("Flamer Special Attack");

        specialAttack.setObjectToCenterAround(this);
        specialAttack.setCenteredAroundObject(true);

        specialAttack.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        this.objectsFollowingThis.add(specialAttack);

        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }

}