package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class RoyalGuardGuardsmen extends Enemy {

    /*
    Wat moet deze enemy doen:
        RoyalGuard equivalent van de Pirate Scout. Kleine target, wanneer er een speler dichtbij genoeg is, target die speler constant maar blijf de movement direction aanhouden
        Beweegt uit het scherm
     */

    private int attackRange = 450;
    private GameObject target = null;
    public RoyalGuardGuardsmen(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.1f);
        this.attackSpeed = 1.5f - (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.05f);
        this.knockbackStrength = 11;
        this.attackRange += Math.round(EnemyManager.getInstance().getEnemyDifficultyModifier() * 10f);

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder pathFinder){
            movementConfiguration.setBoardBlockToHoverIn(6);
            pathFinder.setShouldDecreaseBoardBlock(true);
            pathFinder.setSecondsToHoverStill(0);

            if(this.movementConfiguration.getRotation().equals(Direction.RIGHT)) {
                pathFinder.setDecreaseBoardBlockAmountBy(-1);
            } else {
                pathFinder.setDecreaseBoardBlockAmountBy(1);
            }
        }
    }


    private int gameTickPerformanceSaver = 5; //every 5 gameticks allow to search for a target
    private int gameTickPerformanceCounter = 0;
    @Override
    public void fireAction() {
        this.chargingUpAttackAnimation.setFrameDelay(0);

        this.allowedVisualsToRotate = true; //might not be needed
        double currentTime = GameState.getInstance().getGameSeconds();
        if(gameTickPerformanceCounter % gameTickPerformanceSaver == 0 || (target == null || !target.isVisible())) {
            target = PlayerManager.getInstance().getClosestSpaceShip(this); //relock to the closest target
        }
        gameTickPerformanceCounter++;

        if(isTooFarAway()){ //if the target is too far away, lose the target lock
            target = null;
        }
        rotate();

        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire && target != null) {
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

    private void rotate(){
        if(this.target != null){
            this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), true);
        }
        else if(this.movementConfiguration.getDestination() != null) {
            this.rotateObjectTowardsDestination(true);
        }
        else if(this.movementConfiguration.getRotation() != null && this.movementConfiguration.getDestination() == null) {
            this.rotateGameObjectTowards(this.movementConfiguration.getRotation(), true);
        }
    }

    private boolean isTooFarAway() {
        int attackRangeToCheck = attackRange;
        if (target != null) {
            attackRangeToCheck += 35; //voorkomt het constant roteren van locking/losing lock
        }

        Rectangle targetBounds = target.getBounds();
        double distance = ProtossUtils.getDistanceToRectangle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetBounds);
        return distance > attackRangeToCheck;
    }


    private void shootMissile() {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        // The charging up attack animation has finished, create and fire the missile
        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                missileType.getImageType(), 0.5f);


        float movementSpeed = 3f + (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.25f);
        //Create missile movement attributes and create a movement configuration

        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, missilePathFinder, this.movementRotation
        );


        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                this.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly,
                false, true, true);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        //get the coordinates for rotation of the missile
        SpaceShip spaceship = PlayerManager.getInstance().getClosestSpaceShip(this);
        net.riezebos.bruus.tbd.game.movement.Point rotationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - missile.getWidth() / 2,
                spaceship.getCenterYCoordinate() - missile.getHeight() / 2
        );

        // Any visual resizing BEFORE replacing starting coordinates and rotation
//        missile.setScale(0.3f);

        missile.resetMovementPath();

        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        //Finalized and ready for addition to the game
        MissileManager.getInstance().addExistingMissile(missile);
    }
}