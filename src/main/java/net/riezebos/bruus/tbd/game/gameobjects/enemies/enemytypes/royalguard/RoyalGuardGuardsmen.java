package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class RoyalGuardGuardsmen extends Enemy {

    /*
    Wat moet deze enemy doen:
        RoyalGuard equivalent van de Pirate Scout. Kleine target, is constant aan het mikken en draaien naar de dichtstbijzijnde player en schiet periodisch kogels direct naar de speler
        Beweegt uit het scherm
     */


    private GameObject target = null;
    public RoyalGuardGuardsmen(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.damage = 10;
        this.attackSpeed = 1;
        this.knockbackStrength = 8;

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder pathFinder){
            movementConfiguration.setBoardBlockToHoverIn(6);
            pathFinder.setShouldDecreaseBoardBlock(true);

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
        this.allowedVisualsToRotate = true; //might not be needed
        double currentTime = GameState.getInstance().getGameSeconds();
        if(gameTickPerformanceCounter % gameTickPerformanceSaver == 0 || (target == null || !target.isVisible())) {
            target = PlayerManager.getInstance().getClosestSpaceShip(this); //relock to the closest target
        }
        gameTickPerformanceCounter++;

        rotate();

        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
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
        } else {
            this.rotateObjectTowardsDestination(false);
        }
    }


    private void shootMissile() {
        Missile missile = MissileManager.createMissileToSpecificTargetFromCenter(this, target,3);
        missile.setDamage(this.damage);
        missile.setScale(1f);
        MissileManager.getInstance().addExistingMissile(missile);
    }
}