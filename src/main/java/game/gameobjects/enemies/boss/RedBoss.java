package game.gameobjects.enemies.boss;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.boss.behaviour.BossActionable;
import game.gameobjects.enemies.boss.behaviour.BurstMainAttackBossBehaviour;
import game.gameobjects.enemies.boss.behaviour.SpawnFourDirectionalDrone;
import game.gameobjects.enemies.boss.behaviour.SpawnShuriken;
import game.movement.MovementConfiguration;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

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
        this.damage = 10;
        this.allowedVisualsToRotate = false;

        BossActionable bossBehaviour1 = new SpawnShuriken();
        bossBehaviourList.add(bossBehaviour1);

        BossActionable bossBehaviour2 = new BurstMainAttackBossBehaviour();
        bossBehaviourList.add(bossBehaviour2);

        BossActionable bossBehaviour3 = new SpawnFourDirectionalDrone();
        bossBehaviourList.add(bossBehaviour3);

        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

    }


    protected void updateChargingAttackAnimationCoordination () {
        //Overwritten because bosses should only have 1!!! orientation, "LEFT" so we can do this manually and better. The dynamic distance calculation
        //Does not work with this bosses artwork
        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setCenterCoordinates(this.getXCoordinate() - (chargingUpAttackAnimation.getWidth() / 2), this.getCenterYCoordinate());
        }
    }

    @Override
    public void fireAction () {
        if (WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
        }


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
            // Attempt to execute the behavior
            boolean isCompleted = bossActionable.activateBehaviour(this);
            if (!isCompleted) {
                currentActiveBehavior = bossActionable; // Set this as the current active behavior
                break; // Stop looking at other behaviors, only execute one at a time
            }
        }
    }


}
