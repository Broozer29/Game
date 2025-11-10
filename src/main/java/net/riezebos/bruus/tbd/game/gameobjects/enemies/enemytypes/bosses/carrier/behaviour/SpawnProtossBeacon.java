package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.carrier.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class SpawnProtossBeacon implements BossActionable {
    //Stuurt alle levende protoss naar de speler (zet gewoon de target in ProtossUtils op de speler met lage min/max distance
    //Alleen uitvoeren als er >= 5 protoss leven?
    private double lastSpawnedTime = 0;
    private double spawnCooldown = 16;
    private int priority = 16;
    private double lastCheckedTime;

    private SpriteAnimation spawnAnimation;
    private Enemy beacon = null;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (spawnAnimation == null) {
            initSpawnAnimation(enemy);
        }

        if (beacon != null && (beacon.getCurrentHitpoints() <= 0 || !beacon.isVisible())) {
            this.beacon = null;
            lastSpawnedTime = currentTime; //put it on cooldown after the beacon is killed
        }

        updateSpawnCooldown(enemy);



//        if (enemy.isAllowedToFire() && currentTime >= lastBeaconMovedTime + beaconMoveCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
//                && beacon != null) {
//            beacon.resetMovementPath();
//            setBeaconDestination(beacon);
//            lastBeaconMovedTime = currentTime;
//            return true;
//        }

        if (beacon == null && enemy.isAllowedToFire() && currentTime >= lastSpawnedTime + spawnCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!spawnAnimation.isPlaying()) {
                spawnAnimation.setCenterCoordinates(enemy.getXCoordinate() - (spawnAnimation.getWidth() / 2), enemy.getCenterYCoordinate());
                enemy.setAttacking(true);
                spawnAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(spawnAnimation);
            }


            if (spawnAnimation.isPlaying() && spawnAnimation.getCurrentFrame() == 4) {
                Enemy protossBeacon = createProtossBeacon(enemy);
                EnemyManager.getInstance().addEnemy(protossBeacon);
                lastSpawnedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }

            return false; //we not finished yet
        }
        return true; //We dont have anything to do at this point
    }


    private void updateSpawnCooldown(Enemy enemy) {
        if(enemy.getCurrentHitpoints() <= (enemy.getMaxHitPoints() * 0.35f)){
            spawnCooldown = 10;
        }
    }

    private void initSpawnAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        spawnAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spawnAnimation.setAnimationScale(0.6f);
        spawnAnimation.setCenterCoordinates(
                enemy.getXCoordinate() - (spawnAnimation.getWidth() / 2),
                enemy.getCenterYCoordinate()
        );
    }

    private Enemy createProtossBeacon(Enemy enemy) {
        EnemyEnums enemyEnums = EnemyEnums.EnemyCarrierBeacon;
        Enemy enemyProtossBeacon = EnemyCreator.createEnemy(enemyEnums, enemy.getXCoordinate(), enemy.getYCoordinate(), Direction.LEFT,
                enemyEnums.getDefaultScale(),
                enemyEnums.getMovementSpeed() + LevelManager.getInstance().getBossDifficultyLevel() * 0.5f,
                enemyEnums.getMovementSpeed() + LevelManager.getInstance().getBossDifficultyLevel() * 0.5f,
                MovementPatternSize.SMALL, false);

        enemyProtossBeacon.setOwnerOrCreator(enemy);
        enemyProtossBeacon.setCenterCoordinates(spawnAnimation.getCenterXCoordinate(), spawnAnimation.getCenterYCoordinate());
        enemyProtossBeacon.setAllowedVisualsToRotate(false); //dont want to rotate this visual
        enemyProtossBeacon.setAllowedToFire(false); //it doesnt have an attack
        setBeaconDestination(enemyProtossBeacon);
        this.beacon = enemyProtossBeacon;
        return enemyProtossBeacon;
    }

    private void setBeaconDestination(Enemy enemy) {
        enemy.setAllowedToMove(true);
        enemy.getMovementConfiguration().setPathFinder(new DestinationPathFinder());
        enemy.getMovementConfiguration().setDestination(new Point(
                PlayerManager.getInstance().getSpaceship().getCenterXCoordinate() - (enemy.getWidth() / 2),
                PlayerManager.getInstance().getSpaceship().getCenterYCoordinate() - (enemy.getHeight() / 2)
        ));
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
        double currentGameSeconds = GameState.getInstance().getGameSeconds();

        if (currentGameSeconds - lastCheckedTime < 0.5f) {
            return false;
        }

        lastCheckedTime = currentGameSeconds;

        return enemy.isAllowedToFire()
                && canSpawnMoreBeacons()
                && GameState.getInstance().getGameSeconds() >= lastSpawnedTime + spawnCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }


    private boolean canSpawnMoreBeacons() {
        return EnemyManager.getInstance().getEnemiesByType(EnemyEnums.EnemyCarrierBeacon).isEmpty();
    }

}
