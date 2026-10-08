package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss.FinalBossMine;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnCoinsOnDeath;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class ShurikenMiniBoss extends Enemy {

    /*

        Rework: Make it smaller, slightly slower but do something on each bounce. Leave a stationary drone or something, make it a nuisance if it stays alive too long
        placed drones are destructable and detonate when this dies
     */

    private double lastGameSecondsBombDropped = 0;
    private double bombDropCooldown = 2.5f;

    public ShurikenMiniBoss(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(3f);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;

        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.4f);

        this.hasAttack = false;
        if(this.movementConfiguration.getPathFinder() instanceof BouncingPathFinder bouncingPathFinder){
            bouncingPathFinder.setMaxBounces(100);
            bouncingPathFinder.setUseCenteredCoordinatesInstead(true);
        }

        SpawnCoinsOnDeath goldOnDeathEffect = new SpawnCoinsOnDeath(25, 3,1.0f);
        this.addEffect(goldOnDeathEffect);
    }


    public void fireAction () {
        dropBomb();
    }

    private void dropBomb() {
        if (GameState.getInstance().getGameSeconds() >= lastGameSecondsBombDropped + bombDropCooldown) {
            lastGameSecondsBombDropped = GameState.getInstance().getGameSeconds();
            createFinalBossMine();
        }
    }

    //todo replace this with a mine that does a regular explosion instead of exploding into missiles, this is a quick change but needs proper implementation, dont want to reuse finalbossmine here tbh
    private void createFinalBossMine() {
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(this.getCenterXCoordinate(), this.getCenterYCoordinate(),
                MissileEnums.FinalBossMine.getImageType(), 0.5f);

        Random random = new Random();
        float movementSpeed = 2f;


        //Create missile movement attributes and create a movement configuration
        MissileEnums missileType = MissileEnums.FinalBossMine;
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, Direction.RIGHT
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, true,
                true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        FinalBossMine missile = (FinalBossMine) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false);
        missile.setAllowedToMove(false);
        missile.getAnimation().setFrameDelay(4);
        missile.setCurrentHitpoints(5000);
        missile.setMaxHitPoints(5000);
        MissileManager.getInstance().addExistingMissile(missile);
    }
}
