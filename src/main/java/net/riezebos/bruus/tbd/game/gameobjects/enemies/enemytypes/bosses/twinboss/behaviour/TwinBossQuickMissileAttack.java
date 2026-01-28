package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBossManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.HashSet;
import java.util.Set;

public class TwinBossQuickMissileAttack implements BossActionable {

    private static double lastAttackTime = GameState.getInstance().getGameSeconds();
    private static double attackCooldown = 4;
    private static int priority = 1;

    private static int missilesFired = 0;
    private static int missilesAllowedToFire = 0;
    private static float intermittenMissileCooldown = 0.125f;
    private static double lastMissileFiredTime = 0;

    private static int twinsPlayingChargingAnimation = 0;

    private static SpriteAnimation chargingAnimationToCheck = null;

    private static Set<TwinBoss> twinBossesThatFiredARound = new HashSet<>();

    public TwinBossQuickMissileAttack() {
        missilesAllowedToFire = 6 * TwinBossManager.twinCount; //6 missiles for each twinboss
    }

    public static void resetBehaviour(){
        lastAttackTime = GameState.getInstance().getGameSeconds();
        missilesFired = 0;
        chargingAnimationToCheck = null;
        twinsPlayingChargingAnimation = 0;
        lastMissileFiredTime = GameState.getInstance().getGameSeconds();
        twinBossesThatFiredARound.clear();
    }


    @Override
    public boolean activateBehaviour(Enemy enemy) {
        TwinBoss twinBoss = (TwinBoss) enemy; //we can safely cast since this behaviour is never added to other enemy types

        if (twinsPlayingChargingAnimation < TwinBossManager.twinCount) {
            //Note of potential buggyness: should a twinboss have a chargingAttack animation that is already playing, the behaviour will not be properly synchronized from the beginning
            if (!twinBoss.getChargingUpAttackAnimation().isPlaying()) {
                twinBoss.getChargingUpAttackAnimation().refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(twinBoss.getChargingUpAttackAnimation());
                twinsPlayingChargingAnimation++;

                if (chargingAnimationToCheck == null) {
                    //track 1 animation, when this animation is finished, all twins are allowed to fire
                    //this approach is used to keep twins in sync with each other
                    chargingAnimationToCheck = twinBoss.getChargingUpAttackAnimation();
                }
                return false; //return so that the next TwinBoss can start charging
            }
        }

        if (chargingAnimationToCheck != null && chargingAnimationToCheck.isFinished() &&
                twinsPlayingChargingAnimation == TwinBossManager.twinCount &&
                missilesFired < missilesAllowedToFire &&
                lastMissileFiredTime + intermittenMissileCooldown < GameState.getInstance().getGameSeconds() &&
                !twinBossesThatFiredARound.contains(twinBoss)) {
            //fire a missile, assume that the next call will be done by a different twin
            MissileManager.getInstance().addExistingMissile(createMissile(twinBoss));
            lastMissileFiredTime = GameState.getInstance().getGameSeconds();
            missilesFired++;
            twinBossesThatFiredARound.add(twinBoss);

            if(twinBossesThatFiredARound.size() == TwinBossManager.twinCount){
                twinBossesThatFiredARound.clear();
            }
            return false;
        }

        if(missilesFired >= missilesAllowedToFire){
            //finish & clean up behaviour for next use
            resetBehaviour();
            return true;
        }

        return false;
    }

    private Missile createMissile(Enemy enemy) {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                enemy.getXCoordinate(), enemy.getCenterYCoordinate(),
                missileType.getImageType(),0.65f);


        float movementSpeed = 6;

        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, enemy.getMovementConfiguration().getRotation()
        );


        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Boss Burst Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, enemy.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
        Point rotationCoordinates = new Point(
                spaceship.getCenterXCoordinate() - missile.getWidth() / 2,
                spaceship.getCenterYCoordinate() - missile.getHeight() / 2 + 4
        );
        missile.resetMovementPath();
        Point chargingCenterCoords = new Point(enemy.getChargingUpAttackAnimation().getCenterXCoordinate(), enemy.getChargingUpAttackAnimation().getCenterYCoordinate());
        missile.setCenterCoordinates(chargingCenterCoords.getX(), chargingCenterCoords.getY());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(chargingCenterCoords.getX(), chargingCenterCoords.getY());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(enemy);

        return missile;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return GameState.getInstance().getGameSeconds() >= lastAttackTime + attackCooldown;
    }
}
