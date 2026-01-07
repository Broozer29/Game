package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.behaviour.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StrikerBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private BossActionable currentActiveBehavior = null;
    private double finishedAttackTime = 0;

    public StrikerBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Explosion2);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 10;
        this.knockbackStrength = 9;
        this.allowedVisualsToRotate = true;

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder){
            hoverPathFinder.setSecondsToHoverStill(0);
            hoverPathFinder.setShouldDecreaseBoardBlock(true);
            hoverPathFinder.setShouldChangeBoardBlockEverXHover(2);
        }

        StrikerBossCloneLaserbeamAttack strikerBossCloneLaserbeamAttack = new StrikerBossCloneLaserbeamAttack();
        bossBehaviourList.add(strikerBossCloneLaserbeamAttack);

        StrikerBossFourCorners strikerBossFourCorners = new StrikerBossFourCorners();
        bossBehaviourList.add(strikerBossFourCorners);

        StrikerBossMissileAttack strikerBossMissileAttack = new StrikerBossMissileAttack();
        bossBehaviourList.add(strikerBossMissileAttack);

        StrikerBossCallingInTheTroops strikerBossCallingInTheTroops = new StrikerBossCallingInTheTroops();
        bossBehaviourList.add(strikerBossCallingInTheTroops);

        StrikerBossBombingRunAttack strikerBossBombingRunAttack = new StrikerBossBombingRunAttack();
        bossBehaviourList.add(strikerBossBombingRunAttack);

        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

        finishedAttackTime = GameState.getInstance().getGameSeconds();

    }

    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
    }

    @Override
    public void rotateAfterMovement(){
        //We overwrite the default behaviour because we want to move freely but also keep aiming at the player at all times, unique to this enemy
        this.rotateGameObjectTowards(
                PlayerManager.getInstance().getSpaceship().getCenterXCoordinate(),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate(),
                false
        );
    }

    @Override
    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 3.0; //shorter distance
            Point chargingUpLocation = calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }

    @Override
    public void fireAction() {
        if (!allowedToFire && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
        }

        if(this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder){
            if(this.getCurrentBoardBlock() <= 1){ // if its reaches 0 it will move out of bounds
                hoverPathFinder.setDecreaseBoardBlockAmountBy(-1);
            } else if (this.getCurrentBoardBlock() >= 6){
                hoverPathFinder.setDecreaseBoardBlockAmountBy(1);
            }
        }

        updateChargingAttackAnimationCoordination();


        // If there's an active behavior, try to execute it
        if (currentActiveBehavior != null) {
            boolean isCompleted = currentActiveBehavior.activateBehaviour(this);
            if (isCompleted) {
                currentActiveBehavior = null; // Behavior finished, reset for next one
                finishedAttackTime = GameState.getInstance().getGameSeconds();
            } else {
                return; // If current behavior is still ongoing, stop further actions
            }
        }


        //Wait 0.5 seconds between attacks
        if (finishedAttackTime + 0.5f <= GameState.getInstance().getGameSeconds()) {
            // If no current behavior is active, find the next behavior to execute
            for (BossActionable bossActionable : bossBehaviourList) {
                // Attempt to execute the behavior, if available
                if (bossActionable.isAvailable(this)) {
                    boolean isCompleted = bossActionable.activateBehaviour(this);
                    if (!isCompleted) {
                        currentActiveBehavior = bossActionable; // Set this as the current active behavior
                        break; // Stop looking at other behaviors, only execute one at a time
                    }
                }
            }
        }
    }
}
