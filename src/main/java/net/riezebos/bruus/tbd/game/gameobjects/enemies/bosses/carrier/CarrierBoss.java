package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.carrier;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.carrier.behaviour.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.boardcreators.AchievementUnlockHelper;
import net.riezebos.bruus.tbd.guiboards.boardcreators.BoonSelectionBoardCreator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CarrierBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();
    private BossActionable currentActiveBehavior = null;
    private double finishedAttackTime = 0;

    public CarrierBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.ProtossDestroyedExplosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 16;
        this.allowedVisualsToRotate = false;
        this.destructionAnimation.setAnimationScale(4);
        this.knockbackStrength = 9;

        BossActionable bossBehaviour1 = new SpawnProtossScout();
        bossBehaviourList.add(bossBehaviour1);

        BossActionable bossBehaviour2 = new SpawnProtossShuttle();
        bossBehaviourList.add(bossBehaviour2);

        BossActionable bossBehaviour3 = new SpawnPulsingDrones();
        bossBehaviourList.add(bossBehaviour3);

        BossActionable bossBehaviour4 = new SpawnProtossBeacon();
        bossBehaviourList.add(bossBehaviour4);

        BossActionable bossBehaviour5 = new FireTrackingLaserbeam();
        bossBehaviourList.add(bossBehaviour5);

        bossBehaviourList = bossBehaviourList.stream()
                .sorted(Comparator.comparingInt(BossActionable::getPriority).reversed())
                .collect(Collectors.toList());

        finishedAttackTime = GameState.getInstance().getGameSeconds();

    }


    @Override
    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setCenterCoordinates(this.getXCoordinate() - (chargingUpAttackAnimation.getWidth() / 2), this.getCenterYCoordinate());
        }
    }


    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();

        if(!PlayerProfileManager.getInstance().getLoadedProfile().isCarrierUnlocked()) {
            BoardManager.getInstance().getShopBoard().addGUIAnimation(AchievementUnlockHelper.createUnlockGUIComponent(ImageEnums.CarrierUnlock));
            PlayerProfileManager.getInstance().getLoadedProfile().setCarrierUnlocked(true);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            AudioManager.getInstance().addAudio(AudioEnums.AchievementUnlocked);
        }
    }

    @Override
    public void fireAction() {
        if (!allowedToFire && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
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
