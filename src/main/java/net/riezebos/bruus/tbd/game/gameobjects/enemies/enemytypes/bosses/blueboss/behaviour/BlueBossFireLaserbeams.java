package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.BlueBossFactory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.TrackingLaserBeam;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.List;

public class BlueBossFireLaserbeams implements BossActionable {
    public static float damageRatio = 0.5f;
    //Vuurt een tracking laserbeam vanaf de neus die de speler volgt
    private int priority = 15;
    private int cooldown = 12;
    private double lastFiredTime = GameState.getInstance().getGameSeconds();
    private boolean isFiringLaserbeams;
    private double startedFiringTime = 0;
    private double duration = 6.5;


    public static int laserbeamBodySegmentLength = 6;
    public static float laserBeamAngleStepSize = 0.3f;
    private SpriteAnimation chargingAnimation;
    private TrackingLaserBeam trackingLaserbeam;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
//        duration = 6.5f;
//        laserBeamAngleStepSize = 0.3f;
//        laserbeamBodySegmentLength = 6;
        double currentTime = GameState.getInstance().getGameSeconds();
        if (enemy.isAllowedToFire() && currentTime >= lastFiredTime + cooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!isFiringLaserbeams) {
                List<BlueBossFactory> otherFactories = EnemyManager.getInstance()
                        .getEnemiesByType(EnemyEnums.BlueBossFactory)
                        .stream()
                        .filter(BlueBossFactory.class::isInstance)
                        .map(BlueBossFactory.class::cast)
                        .toList();

                otherFactories.forEach(blueBossFactory -> blueBossFactory.fireLaserbeam());
                startedFiringTime = currentTime;
                isFiringLaserbeams = true;
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
                return false;
            }
        }

        if (isFiringLaserbeams && startedFiringTime + duration < currentTime) {
            List<BlueBossFactory> otherFactories = EnemyManager.getInstance()
                    .getEnemiesByType(EnemyEnums.BlueBossFactory)
                    .stream()
                    .filter(BlueBossFactory.class::isInstance)
                    .map(BlueBossFactory.class::cast)
                    .toList();

            otherFactories.forEach(blueBossFactory -> blueBossFactory.stopFiringLaserbeams());
            enemy.setAttacking(false);
            isFiringLaserbeams = false;
            lastFiredTime = currentTime;
            return true;
        }

        return false; //Laserbeams should removed and this attack is finished
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastFiredTime + cooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }

}