package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.BouncingPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnShurikens implements BossActionable {

    private int priority = 10;
    private int spawnCooldown = 30;
    private Random random;
    private double lastSpawnedTime;
    private int maxShurikens = 9; // spawns in groups of 4
    private float moveSpeedModifier = 0.75f;
    private List<Point> spawnPoints = new ArrayList<>();
    private List<SpriteAnimation> spawnAnimations = new ArrayList<>();


    public SpawnShurikens() {
        Point upperLeft = new Point(DataClass.getInstance().getWindowWidth() * 0.1f, DataClass.getInstance().getWindowHeight() * 0.1f);
        Point upperRight = new Point(DataClass.getInstance().getWindowWidth() * 0.9f, DataClass.getInstance().getWindowHeight() * 0.1f);
        Point lowerLeft = new Point(DataClass.getInstance().getWindowWidth() * 0.1f, DataClass.getInstance().getWindowHeight() * 0.9f);
        Point lowerRight = new Point(DataClass.getInstance().getWindowWidth() * 0.9f, DataClass.getInstance().getWindowHeight() * 0.9f);

        spawnPoints.add(upperLeft);
        spawnPoints.add(upperRight);
        spawnPoints.add(lowerLeft);
        spawnPoints.add(lowerRight);

        initSpawnAnimation(upperLeft);
        initSpawnAnimation(upperRight);
        initSpawnAnimation(lowerLeft);
        initSpawnAnimation(lowerRight);

    }

    private void initSpawnAnimation(Point location){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setScale(1);
        spriteConfiguration.setxCoordinate(location.getX());
        spriteConfiguration.setyCoordinate(location.getY());
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spriteAnimation.setCenterCoordinates(location.getX(), location.getY());
        spawnAnimations.add(spriteAnimation);
    }

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if(spawnAnimations.isEmpty() || spawnPoints.isEmpty()){
            return true; //safe-guard to prevent this behaviour from happening if it's broken
        }

        if (enemy.isAllowedToFire() && currentTime >= lastSpawnedTime + spawnCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!spawnAnimations.get(0).isPlaying()) {
                for(SpriteAnimation spriteAnimation : spawnAnimations) {
                    spriteAnimation.refreshAnimation();
                    AnimationManager.getInstance().addUpperAnimation(spriteAnimation);
                }
                enemy.setAttacking(true);
            }



            if(spawnAnimations.get(0).isPlaying() && spawnAnimations.get(0).getCurrentFrame() == 4) {
                for(SpriteAnimation spriteAnimation : spawnAnimations) {
                    Enemy fourDirectionalDrone = createShuriken(enemy, spriteAnimation);
                    EnemyManager.getInstance().addEnemy(fourDirectionalDrone);
                }
                lastSpawnedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }
            return false; //We still running this behaviour
        }
        return true; //We still running this behaviour

    }

    private Enemy createShuriken (Enemy enemy, SpriteAnimation spriteAnimation) {
        EnemyEnums enemyType = EnemyEnums.Shuriken;
        Direction direction = getRandomDirection();
        Enemy shuriken = EnemyCreator.createEnemy(enemyType, enemy.getXCoordinate(), enemy.getYCoordinate(), direction,
                enemyType.getDefaultScale(), enemyType.getMovementSpeed() * moveSpeedModifier,enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
        shuriken.setCenterCoordinates(spriteAnimation.getCenterXCoordinate(), spriteAnimation.getCenterYCoordinate());
        BouncingPathFinder pathFinder = new BouncingPathFinder();
        pathFinder.setMaxBounces(15);
        shuriken.getMovementConfiguration().setPathFinder(new BouncingPathFinder());
        shuriken.setOwnerOrCreator(enemy);

        return shuriken;
    }

    private Direction getRandomDirection(){
        if(random == null){
            random = new Random();
        }

        int number = random.nextInt(0, 4);
        return switch (number) {
            case 0 -> Direction.LEFT_UP;
            case 1 -> Direction.LEFT_DOWN;
            case 2 -> Direction.RIGHT_UP;
            case 3 -> Direction.RIGHT_DOWN;
            default -> Direction.LEFT_DOWN;
        };
    }


    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastSpawnedTime + spawnCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && EnemyManager.getInstance().getEnemiesByType(EnemyEnums.Shuriken).size() < maxShurikens;
    }
}
