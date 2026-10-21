package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlueBossSpawnFactory implements BossActionable {


    private double lastSpawnedTime = GameState.getInstance().getGameSeconds();
    private double spawnCooldown = 1;
    private int priority = 20;

    private SpriteAnimation spawnAnimation;

    private static Point upperRightCorner = new Point(
            DataClass.getInstance().getWindowWidth() * 0.85f,
            DataClass.getInstance().getPlayableWindowMaxHeight() * 0.15f);

    private static Point upperLeftCorner = new Point(
            DataClass.getInstance().getWindowWidth() * 0.15f,
            DataClass.getInstance().getPlayableWindowMaxHeight() * 0.15f);

    private static Point lowerRightCorner = new Point(
            DataClass.getInstance().getWindowWidth() * 0.85f,
            DataClass.getInstance().getPlayableWindowMaxHeight() * 0.85f
    );

    private static Point lowerLeftCorner = new Point(
            DataClass.getInstance().getWindowWidth() * 0.15f,
            DataClass.getInstance().getPlayableWindowMaxHeight() * 0.85f
    );

    private Map<Point, Enemy> spawnPointToEnemyMap;
    private List<Point> spawnPoints; // Ordered list of spawn points

    public BlueBossSpawnFactory() {
        spawnPoints = new ArrayList<>();
        spawnPoints.add(upperRightCorner);
        spawnPoints.add(lowerRightCorner);
        spawnPoints.add(upperLeftCorner);
        spawnPoints.add(lowerLeftCorner);

        spawnPointToEnemyMap = new HashMap<>();
        for (Point point : spawnPoints) {
            spawnPointToEnemyMap.put(point, null);
        }
    }


    @Override
    public boolean activateBehaviour(Enemy enemy) {
        updateFactoryStatus();
        double currentTime = GameState.getInstance().getGameSeconds();
        if (spawnAnimation == null) {
            initSpawnAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastSpawnedTime + spawnCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!spawnAnimation.isPlaying()) {
                enemy.setAttacking(true);
                spawnAnimation.setCenterCoordinates(getNextAvailableSpawnPoint().getX(), getNextAvailableSpawnPoint().getY());
                spawnAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(spawnAnimation);
            }


            if (spawnAnimation.isPlaying() && spawnAnimation.getCurrentFrame() == 4) {
                Enemy fourDirectionalDrone = createBlueBossFactory(enemy);
                EnemyManager.getInstance().addEnemy(fourDirectionalDrone);
                lastSpawnedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }

            return false; //we not finished yet
        }
        return true; //We dont have anything to do at this point
    }


    private void initSpawnAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        spawnAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spawnAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
    }

    private Enemy createBlueBossFactory(Enemy enemy) {
        EnemyEnums enemyEnums = EnemyEnums.BlueBossFactory;
        Point spawnPoint = getNextAvailableSpawnPoint();
        Enemy factory = EnemyCreator.createEnemy(enemyEnums, enemy.getXCoordinate(), enemy.getYCoordinate(), Direction.LEFT,
                enemyEnums.getDefaultScale(),
                enemyEnums.getMovementSpeed());
        factory.setOwnerOrCreator(enemy);
        factory.setCenterCoordinates(spawnPoint.getX(), spawnPoint.getY());
        factory.setAllowedToMove(true);
        spawnPointToEnemyMap.put(spawnPoint, factory);
        return factory;
    }

    private Point getNextAvailableSpawnPoint() {
        for (Point point : spawnPoints) {
            if (spawnPointToEnemyMap.get(point) == null) {
                return point;
            }
        }
        return new Point(-500, 500); //Default it to wayyy out of bounds if something went horribly wrong. But the isAvailable() method should prevent this from ever happening
    }

    private void updateFactoryStatus() {
        // Iterate over the map to find the corresponding enemy and reset the point
        for (Map.Entry<Point, Enemy> entry : spawnPointToEnemyMap.entrySet()) {
            if (entry.getValue() != null && (entry.getValue().getCurrentHitpoints() <= 0 || !entry.getValue().isVisible())) {
                spawnPointToEnemyMap.put(entry.getKey(), null); // Make the point available again
            }
        }
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
        updateFactoryStatus();
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastSpawnedTime + spawnCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && EnemyManager.getInstance().getEnemiesByType(EnemyEnums.BlueBossFactory).size() < 4; //this check will have to be replaced
    }
}
