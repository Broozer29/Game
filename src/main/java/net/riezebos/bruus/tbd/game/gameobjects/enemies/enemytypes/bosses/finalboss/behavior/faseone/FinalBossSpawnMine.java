package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileCreator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.finalboss.FinalBossMine;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.RegularPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FinalBossSpawnMine implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 3.5;
    private int priority = 2;

    private SpriteAnimation attackingAnimation;
    private Point mineLocation;
    private Random random = new Random();

    //Extra space on top of the outermost reflecting ring, so the mine and the ring blocks never overlap
    private static float extraDistanceFromRings = 75;
    private static int maxLocationAttempts = 100;

    /*
            Description of what this should do:
            See the missile/explosion created by FinalBossDropMineCharge.java, we use the exact same thing here but we create it differently
            Every X few seconds, place a mine randomly on the screen and place an animation on it.
            See the warpIn animation and it's creation to get the animation and center it on the missile.
            As for choosing the x/y coordinates, some complexity:
            widht = DataClass.getInstance.getWindowWidth();
            height = DataClass.getInstance.getPlayableWindowHeight();
            - Due to other attack reasons: spawn within a range of width * 0.1 and width * 0.9f and the height * 0.1 and height * 0.9f
            - Due to boss being stationary and having reflecting shields around him, see FinalBossCreateRotatingReflectingBlocks.java for the distance, keep a distance from the boss so these do not overlap
      */


    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (attackingAnimation == null) {
            initAttackAnimation(enemy);
        }


        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            if (!attackingAnimation.isPlaying()) {
                mineLocation = selectMineLocation(enemy);
                updateAttackAnimationLocation();
                attackingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(attackingAnimation);
            }


            if (attackingAnimation.isPlaying() && attackingAnimation.getCurrentFrame() == attackingAnimation.getTotalFrames() - 1) {
                doTheThing(enemy);
                lastAttackedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }
            return false; //We still running this behaviour
        }
        return true; //We still running this behaviour

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

    private void updateAttackAnimationLocation() {
        attackingAnimation.setCenterCoordinates(Math.round(mineLocation.getX()), Math.round(mineLocation.getY()));
    }

    //Random location within 10%-90% of the screen, outside of the reflecting rings around the boss
    //and, when possible, with a gap of one mine width/height to the other mines
    private Point selectMineLocation(Enemy enemy) {
        float width = DataClass.getInstance().getWindowWidth();
        float minHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        float height = DataClass.getInstance().getPlayableWindowMaxHeight() - minHeight;
        float minDistanceFromBoss = FinalBossCreateRotatingReflectingBlocks.getOutermostRingDistance() + extraDistanceFromRings;

        List<FinalBossMine> existingMines = MissileManager.getInstance().getMissiles().stream()
                .filter(missile -> missile instanceof FinalBossMine && missile.isVisible() && missile.getOwnerOrCreator() == enemy)
                .map(missile -> (FinalBossMine) missile)
                .toList();

        List<Point> playerPoints = PlayerManager.getInstance().getAllSpaceShips().stream().map(spaceShip -> new Point(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate())).collect(Collectors.toList());
        //The warp in animation is drawn on the mine location at the same scale, so it stands in for the size of the mine that doesn't exist yet
        float mineWidth = attackingAnimation.getWidth();
        float mineHeight = attackingAnimation.getHeight();

        float x = 0;
        float y = 0;
        Point clearOfRingsOnly = null;
        for (int i = 0; i < maxLocationAttempts; i++) {
            x = width * 0.1f + random.nextFloat() * (width * 0.4f); //no longer 0.8 but 0.4 so we should only spawn on the left side, sloppy hack
            y = minHeight + height * 0.1f + random.nextFloat() * height * 0.8f;
            if (Math.hypot(x - enemy.getCenterXCoordinate(), y - enemy.getCenterYCoordinate()) < minDistanceFromBoss) {
                continue;
            }

            //A mine on top of a player detonates instantly, so this matters as much as staying clear of the rings
            if (!isAwayFromPlayers(x, y, playerPoints, mineWidth, mineHeight)) {
                continue;
            }

            if (isAwayFromMines(x, y, existingMines)) {
                return new Point(x, y);
            }

            if (clearOfRingsOnly == null) {
                clearOfRingsOnly = new Point(x, y);
            }
        }

        //No spot away from the other mines found, staying clear of the rings matters more
        if (clearOfRingsOnly != null) {
            return clearOfRingsOnly;
        }

        //No free spot found, fall back to the left side of the allowed area which is furthest from the stationary boss
        return new Point(width * 0.1f, y);
    }

    //Two mines of the same size leave a gap of one width/height between them when their centers are 2 widths or 2 heights apart
    private boolean isAwayFromMines(float x, float y, List<FinalBossMine> existingMines) {
        for (FinalBossMine mine : existingMines) {
            boolean tooCloseHorizontally = Math.abs(x - mine.getCenterXCoordinate()) < mine.getWidth() * 2;
            boolean tooCloseVertically = Math.abs(y - mine.getCenterYCoordinate()) < mine.getHeight() * 2;
            if (tooCloseHorizontally && tooCloseVertically) {
                return false;
            }
        }
        return true;
    }

    //Same check as isAwayFromMines: the mine center stays 2 mine widths or 2 mine heights away from the center of every player
    private boolean isAwayFromPlayers(float x, float y, List<Point> playerPoints, float mineWidth, float mineHeight) {
        for (Point playerPoint : playerPoints) {
            boolean tooCloseHorizontally = Math.abs(x - playerPoint.getX()) < mineWidth * 2;
            boolean tooCloseVertically = Math.abs(y - playerPoint.getY()) < mineHeight * 2;
            if (tooCloseHorizontally && tooCloseVertically) {
                return false;
            }
        }
        return true;
    }

    private void doTheThing(Enemy enemy) {
        createFinalBossMine(enemy);
    }

    //Same mine as FinalBossDropMineCharge, but it stays where it's placed
    private void createFinalBossMine(Enemy enemy) {
        int xCoordinate = Math.round(mineLocation.getX());
        int yCoordinate = Math.round(mineLocation.getY());

        //Create the sprite configuration which gets upgraded to spriteanimation if needed by the MissileCreator
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(xCoordinate, yCoordinate,
                MissileEnums.FinalBossMine.getImageType(), 0.5f);

        //Create missile movement attributes and create a movement configuration. No speed, so the mine stays in place
        MissileEnums missileType = MissileEnums.FinalBossMine;
        PathFinder missilePathFinder = new RegularPathFinder();
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                0, missilePathFinder, Direction.LEFT
        );

        //Create remaining missile attributes and a missile configuration
        boolean isFriendly = false;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType,
                enemy.getDamage() * 2f, missileType.getDeathOrExplosionImageEnum(), isFriendly, true,
                true, true);

        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        FinalBossMine missile = (FinalBossMine) MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(enemy);
        missile.setDetonatesAfterTimer(false);
        missile.setCenterCoordinates(xCoordinate, yCoordinate);
        missile.setAllowedVisualsToRotate(false);
        missile.setAllowedToMove(false);
        missile.getAnimation().setFrameDelay(4);
        missile.setCurrentHitpoints(50);
        missile.setMaxHitPoints(50);
        missile.setTransparancyAlpha(true, 0.2f, 0.1f);
        MissileManager.getInstance().addExistingMissile(missile);
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
        if (enemy instanceof FinalBoss finalBoss) {
            return finalBoss.isAllowedToFire()
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_1
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

}
