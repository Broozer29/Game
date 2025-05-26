package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.spacestation;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.spacestation.behaviour.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class SpaceStationBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private BossActionable currentActiveBehavior = null;
    private double finishedAttackTime = 0;

    public SpaceStationBoss(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        this.setAllowedVisualsToRotate(false);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Explosion2);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(5);

        this.knockbackStrength = 10;
        this.damage = 16;


        SpaceStationLaserbeamAttack spaceStationLaserbeamAttack = new SpaceStationLaserbeamAttack();
        bossBehaviourList.add(spaceStationLaserbeamAttack);

        SpaceStationBurstMissilesAttack spaceStationBurstMissilesAttack = new SpaceStationBurstMissilesAttack();
        bossBehaviourList.add(spaceStationBurstMissilesAttack);

        SpaceStationSpawnNeedlers spaceStationSpawnEnemies = new SpaceStationSpawnNeedlers();
        bossBehaviourList.add(spaceStationSpawnEnemies);

        SpaceStationSpinningAttack spaceStationSpinningAttack = new SpaceStationSpinningAttack();
        bossBehaviourList.add(spaceStationSpinningAttack);

        SpaceStationSpawnDrone spaceStationSpawnDrone = new SpaceStationSpawnDrone();
        bossBehaviourList.add(spaceStationSpawnDrone);


        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

        finishedAttackTime = GameState.getInstance().getGameSeconds();
    }


    @Override
    public void fireAction() {
        if (WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
        }


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
        if (finishedAttackTime + 0.5 <= GameState.getInstance().getGameSeconds()) {
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
