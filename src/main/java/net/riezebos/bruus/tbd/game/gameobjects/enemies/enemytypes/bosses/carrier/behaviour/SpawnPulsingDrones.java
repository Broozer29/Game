package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.carrier.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class SpawnPulsingDrones implements BossActionable {
    //Summon beacons achter de carrier die in 360 graden om zich heen missiles schiet
    //Weinig damage, hoge attack speed?
    //Standaard plek waar ze spawnen?

    private static Point upperRightCorner = new Point(
            DataClass.getInstance().getWindowWidth() * 0.8f,
            DataClass.getInstance().getWindowHeight() * 0.1f);

    private static Point lowerRightCorner = new Point(
            DataClass.getInstance().getWindowWidth() * 0.8f,
            DataClass.getInstance().getPlayableWindowMaxHeight() * 0.9f
    );

    private double lastSpawnedTime = 0;
    private double spawnCooldown = 15;
    private double additionalSpawnCooldown = 25;
    private boolean shouldUseAdditionalSpawnCooldown = false;
    private int priority = 10;

    private SpriteAnimation upperRightCornerAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (upperRightCornerAnimation == null) {
            initSpawnAnimation();
        }

        if (enemy.isAllowedToFire() && currentTime >= lastSpawnedTime + spawnCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!upperRightCornerAnimation.isPlaying()) {
                enemy.setAttacking(true);
                upperRightCornerAnimation.refreshAnimation();
                SpriteAnimation clonedAnimation = upperRightCornerAnimation.clone();
                clonedAnimation.setCenterCoordinates(lowerRightCorner.getX(), lowerRightCorner.getY());
                AnimationManager.getInstance().addUpperAnimation(upperRightCornerAnimation);
                AnimationManager.getInstance().addUpperAnimation(clonedAnimation);
            }



            if (upperRightCornerAnimation.isPlaying() && upperRightCornerAnimation.getCurrentFrame() == 4) {
                Enemy protossBeacon = createProtossBeacon(enemy, upperRightCorner);
                Enemy protossBeacon2 = createProtossBeacon(enemy, lowerRightCorner);
                EnemyManager.getInstance().addEnemy(protossBeacon2);
                EnemyManager.getInstance().addEnemy(protossBeacon);
                lastSpawnedTime = currentTime;
                shouldUseAdditionalSpawnCooldown = true;
                enemy.setAttacking(false);
                return true; //We finished
            }

            return false; //we not finished yet
        }
        return true; //We dont have anything to do at this point
    }

    private void initSpawnAnimation() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(upperRightCorner.getX());
        spriteConfiguration.setyCoordinate(upperRightCorner.getY());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        upperRightCornerAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        upperRightCornerAnimation.setAnimationScale(0.6f);
        upperRightCornerAnimation.setCenterCoordinates(upperRightCorner.getX(), upperRightCorner.getY());
    }

    private Enemy createProtossBeacon(Enemy enemy, Point point) {
        EnemyEnums enemyEnums = EnemyEnums.CarrierPulsingDrone;
        Enemy enemyProtossBeacon = EnemyCreator.createEnemy(enemyEnums, enemy.getXCoordinate(), enemy.getYCoordinate(), Direction.LEFT,
                enemyEnums.getDefaultScale(), enemyEnums.getMovementSpeed(), enemyEnums.getMovementSpeed(), MovementPatternSize.SMALL, false);

        //Scout should immediatly change its move config upon spawning, so its responsible itself for surrounding the carrier
        enemyProtossBeacon.setOwnerOrCreator(enemy);
        enemyProtossBeacon.setCenterCoordinates(point.getX(), point.getY());
        enemyProtossBeacon.setAllowedToMove(false);
        return enemyProtossBeacon;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= getCurrentCooldown()
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && canSpawnMoreBeacons();
    }

    private double getCurrentCooldown(){
        if(this.shouldUseAdditionalSpawnCooldown){
            return spawnCooldown + additionalSpawnCooldown;
        } else {
            return spawnCooldown;
        }
    }


    private boolean canSpawnMoreBeacons() {
        return EnemyManager.getInstance().getEnemiesByType(EnemyEnums.CarrierPulsingDrone).isEmpty();
    }
}
