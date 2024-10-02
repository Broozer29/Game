package game.gameobjects.enemies.enemytypes;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.missiles.MissileManager;
import game.gameobjects.missiles.specialAttacks.SpecialAttack;
import game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinders.DestinationPathFinder;
import game.movement.pathfinders.HoverPathFinder;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PulsingDrone extends Enemy {

    public PulsingDrone (SpriteAnimationConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig.getSpriteConfiguration(), 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 10;
        this.attackSpeed = 5;
        this.chargingUpAttackAnimation.setFrameDelay(5);
        this.chargingUpAttackAnimation.setAnimationScale(1);
        this.detonateOnCollision = false;
        this.knockbackStrength = 5;
    }


    @Override
    public void fireAction(){
        if(this.movementConfiguration.getPathFinder() instanceof DestinationPathFinder){
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }

        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (allowedToFire && currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {

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

    private void shootEnergyCircle(){
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setScale(1f);
        missileSpriteConfiguration.setImageType(ImageEnums.EnergyCircle);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(missileSpriteConfiguration, 3, false);
        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(this.getDamage(), false,
                true, false, false, false);
        SpecialAttack specialAttack = new SpecialAttack(spriteAnimationConfiguration, specialAttackConfiguration);
        specialAttack.setOwnerOrCreator(this);
        specialAttack.setObjectType("Pulsing Drone Energy Circle Attack");
        specialAttack.getAnimation().setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }
}
