package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss.behaviour.BurstMainAttackBossBehaviour;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss.behaviour.CrossingLaserbeamsAttack;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss.behaviour.SpawnFourDirectionalDrone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss.behaviour.SpawnShuriken;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RedBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private BossActionable currentActiveBehavior = null;

    public RedBoss (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Explosion2);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 12;
        this.allowedVisualsToRotate = false;
        this.destructionAnimation.setAnimationScale(3);
        this.knockbackStrength = 9;

        BossActionable bossBehaviour1 = new SpawnShuriken();
        bossBehaviourList.add(bossBehaviour1);

        BossActionable bossBehaviour2 = new BurstMainAttackBossBehaviour();
        bossBehaviourList.add(bossBehaviour2);

        BossActionable bossBehaviour3 = new SpawnFourDirectionalDrone();
        bossBehaviourList.add(bossBehaviour3);

        BossActionable bossBehaviour4 = new CrossingLaserbeamsAttack(true);
        bossBehaviourList.add(bossBehaviour4);

        BossActionable bossBehaviour5 = new CrossingLaserbeamsAttack(false);
        bossBehaviourList.add(bossBehaviour5);

        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

    }


    @Override
    protected void updateChargingAttackAnimationCoordination () {
        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setCenterCoordinates(this.getXCoordinate() - (chargingUpAttackAnimation.getWidth() / 2), this.getCenterYCoordinate());
        }
    }

    @Override
    public void fireAction () {
        if(!allowedToFire && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
                this.allowedToFire = true; // Boss is allowed to fire
            }

        updateChargingAttackAnimationCoordination();

        // If there's an active behavior, try to execute it
        if (currentActiveBehavior != null) {

            boolean isCompleted = currentActiveBehavior.activateBehaviour(this);
            if (isCompleted) {
                currentActiveBehavior = null; // Behavior finished, reset for next one
            } else {
                return; // If current behavior is still ongoing, stop further actions
            }
        }


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
