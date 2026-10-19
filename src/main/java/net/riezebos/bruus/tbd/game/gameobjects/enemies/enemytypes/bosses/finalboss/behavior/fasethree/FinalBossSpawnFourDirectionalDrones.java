package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasethree;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Random;

public class FinalBossSpawnFourDirectionalDrones implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 25;
    private int priority = 2;
    private int amountToSpawn = 2;
    private Random random;
    private int spawnedCounter = 0;

    private SpriteAnimation attackingAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (attackingAnimation == null) {
            initAttackAnimation(enemy);
        }


        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            doTheThing(enemy);
            lastAttackedTime = currentTime;
            enemy.setAttacking(false);
            return true; //We finished
        }
        return true; //We still running this behaviour

    }

    private Enemy createFourDirectionalDrone(Enemy enemy) {
        EnemyEnums enemyEnums = EnemyEnums.FourDirectionalDrone;
        Enemy fourDirectionalDrone = EnemyCreator.createEnemy(enemyEnums, enemy.getXCoordinate(), enemy.getYCoordinate(), Direction.LEFT,
                enemyEnums.getDefaultScale(),
                enemyEnums.getMovementSpeed());

        Point point = BoardBlockUpdater.getRandomCoordinateInBlock(getRandomBoardBlock(), fourDirectionalDrone.getWidth(), fourDirectionalDrone.getHeight());

        if (spawnedCounter == 0 || spawnedCounter % 2 == 0) {
            point.setY(getYCoordinate(true));
        } else {
            point.setY(getYCoordinate(false));
        }

        fourDirectionalDrone.getMovementConfiguration().setDestination(point);
        fourDirectionalDrone.getMovementConfiguration().setCurrentPath(new Path(new ArrayList<Point>(), Direction.LEFT)); //init with an empty path so to immediatly allow firing, see FourDirectionalDrone.fireAction()
        fourDirectionalDrone.setCenterCoordinates(point.getX(), point.getY());
        fourDirectionalDrone.setAllowedToMove(false);
        fourDirectionalDrone.setOwnerOrCreator(enemy);

        //Zet de Y coordinates uit elkaar zodat de kruisjes verschillende kanten op gaan
        spawnedCounter++;
        if (spawnedCounter % amountToSpawn == 0) {
            spawnedCounter = 0;
        }

        return fourDirectionalDrone;
    }

    private int getYCoordinate(boolean upperHalf) {
        if (upperHalf) {
            return random.nextInt(60, DataClass.getInstance().getPlayableWindowMaxHeight() / 2);
        } else {
            return random.nextInt(DataClass.getInstance().getPlayableWindowMaxHeight() / 2, DataClass.getInstance().getPlayableWindowMaxHeight() - 60);
        }
    }

    private int getRandomBoardBlock() {
        if (random == null) {
            random = new Random();
        }

        return random.nextInt(0, 5);
    }


    private void initAttackAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn); //vervangen met de daadwerkelijke anim

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        attackingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        attackingAnimation.setAnimationScale(0.5f);
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        attackingAnimation.addXOffset(Math.round(attackingAnimation.getWidth() * 0.1f));
    }

    private void doTheThing(Enemy enemy) {
        for (int i = 0; i < amountToSpawn; i++) {
            Enemy fourDirectionalDrone = createFourDirectionalDrone(enemy);
            fourDirectionalDrone.setOwnerOrCreator(enemy);
            EnemyManager.getInstance().addEnemy(fourDirectionalDrone);
            AnimationManager.getInstance().addUpperAnimation(createSmokeAnim(fourDirectionalDrone));
        }
    }

    private SpriteAnimation createSmokeAnim(Enemy fourDirectionalDrone) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-100);
        spriteConfiguration.setyCoordinate(-100);
        spriteConfiguration.setScale(1.15f);
        spriteConfiguration.setImageType(ImageEnums.SmokeExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        SpriteAnimation anim = new SpriteAnimation(spriteAnimationConfiguration);

        anim.setCenterCoordinates(fourDirectionalDrone.getXCoordinate(), fourDirectionalDrone.getYCoordinate());
        return anim;
    }


    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setLastAttackTime(double lastAttackTime) {
        this.lastAttackedTime = lastAttackTime;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        if (enemy instanceof FinalBoss finalBoss) {
            return finalBoss.isAllowedToFire()
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_3
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss)
                    && EnemyManager.getInstance().getEnemiesByType(EnemyEnums.FourDirectionalDrone).size() < 5;
        }
        return false;
    }

}
