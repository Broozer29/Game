package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.HashSet;
import java.util.Set;

public class StrikerBossCallingInTheTroops implements BossActionable {
    private static float healthActivationInterval = 0.2f;
    private Set<Float> activatedThresholds = new HashSet<>();

    @Override
    public boolean activateBehaviour(Enemy enemy) {

        playCallForHelpAnim(enemy);
        spawnEnemies();
        return true;
    }

    private void playCallForHelpAnim(Enemy enemy){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.CallForHelp);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation callForHelpAnimation = new SpriteAnimation(spriteAnimationConfiguration);

        callForHelpAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(callForHelpAnimation);

        AudioManager.getInstance().addAudio(AudioEnums.DistressCall);
    }

    private void spawnNeedlers(){
        for(int i = 0; i < 2; i++){
            Direction direction = i == 0 ? Direction.RIGHT : Direction.LEFT;
            EnemyEnums enemyEnums = EnemyEnums.Needler;
            for(int j = 2; j < 8; j++){
                float xCoordinate = direction.equals(Direction.RIGHT) ? -enemyEnums.getBaseWidth() * (enemyEnums.getDefaultScale() * 2) : DataClass.getInstance().getWindowWidth() + enemyEnums.getBaseWidth() * enemyEnums.getDefaultScale();
                float yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() * (j / 10f);
                EnemyManager.getInstance().addEnemy(
                        EnemyCreator.createEnemy(
                                enemyEnums, Math.round(xCoordinate), Math.round(yCoordinate), direction, enemyEnums.getDefaultScale(), enemyEnums.getMovementSpeed()
                        )
                );
            }
        }
    }

    private void spawnSeekers(){
        for(int i = 0; i < 6; i++){
            EnemyEnums enemyEnums = EnemyEnums.Seeker;
            Direction direction = i < 3 ? Direction.RIGHT : Direction.LEFT;
            float yCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.5f;
            float xCoordinate = direction.equals(Direction.RIGHT) ? -enemyEnums.getBaseWidth() * (enemyEnums.getDefaultScale() * 3) : DataClass.getInstance().getWindowWidth() + enemyEnums.getBaseWidth() * (enemyEnums.getDefaultScale() * 2);
            EnemyManager.getInstance().addEnemy(
                    EnemyCreator.createEnemy(
                            enemyEnums, Math.round(xCoordinate), Math.round(yCoordinate), direction, enemyEnums.getDefaultScale(), enemyEnums.getMovementSpeed()
                    )
            );
        }
    }


    private void spawnEnemies() {
        spawnNeedlers();
        spawnSeekers();
    }


    @Override
    public int getPriority() {
        return 10000;
    }

    private boolean canActivateWithCurrentHealth(Enemy enemy) {
        float currentHealthPercentage = enemy.getCurrentHitpoints() / enemy.getMaxHitPoints();
        for (float threshold = 0.8f; threshold > 0; threshold -= healthActivationInterval) {
            if (currentHealthPercentage <= threshold && !activatedThresholds.contains(threshold)) {
                activatedThresholds.add(threshold);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && enemy.getCurrentHitpoints() >= 1
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && canActivateWithCurrentHealth(enemy);
    }
}