package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FinalBossSpreadMissileAttack implements BossActionable {
    private double lastAttackedTime = GameState.getInstance().getGameSeconds() - 15;
    private double attackCooldown = 20;
    private int priority = 2;
    private int missilesPerBoardBlock = 2;
    private int missileWavesFired = 1;

    private SpriteAnimation attackingAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        attackCooldown = 30;

        double currentTime = GameState.getInstance().getGameSeconds();
        if (attackingAnimation == null) {
            initAttackAnimation(enemy);
        }


        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateAttackAnimationLocation(enemy);
            if (!attackingAnimation.isPlaying()) {
                attackingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(attackingAnimation);
            }


            if (attackingAnimation.isPlaying() && attackingAnimation.getCurrentFrame() == attackingAnimation.getTotalFrames() - 1) {
                doTheThing(enemy);
                if(missileWavesFired >= 8){
                    lastAttackedTime = currentTime;
                    missileWavesFired = 0;
                    enemy.setAttacking(false);
                    return true;
                }
                return false; //We finished
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
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        attackingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        attackingAnimation.setAnimationScale(0.5f);
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        attackingAnimation.addXOffset(Math.round(attackingAnimation.getWidth() * 0.1f));
    }

    private void updateAttackAnimationLocation(Enemy enemy) {
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
    }

    private void doTheThing(Enemy enemy) {
        for(int i = 0; i < missilesPerBoardBlock; i++){
            MissileManager.getInstance().addExistingMissile(createMissile(missileWavesFired, enemy));
        }
        missileWavesFired++;
    }

    private Missile createMissile(int boardBlock, Enemy enemy) {
        float scale = 0.75f;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(0);
        spriteConfiguration.setyCoordinate(0);
        spriteConfiguration.setScale(scale);
        spriteConfiguration.setImageType(MissileEnums.FinalBossChargingMissile.getImageType()); //todo placeholder

        MissileConfiguration missileConfiguration = new MissileConfiguration();
        missileConfiguration.setDamage(enemy.getDamage()); //placeholder
        missileConfiguration.setMissileType(MissileEnums.FinalBossSpreadMissile);

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setMovementSpeed(2); //will be overwritten anyway
        movementConfiguration.setPathFinder(new DestinationPathFinder());
        movementConfiguration.setDirection(Direction.RIGHT);

        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(enemy);

        Point destination = getRandomDestination(missile, boardBlock);
        missile.resetMovementPath();
        //todo set missile speed
        missile.setCenterCoordinates(attackingAnimation.getCenterXCoordinate(), attackingAnimation.getCenterYCoordinate());
        missile.getMovementConfiguration().setMovementSpeed(calculateMovementSpeed(destination, missile));
        missile.getMovementConfiguration().setDestination(destination);
        return missile;
    }

    private float calculateMovementSpeed(Point destination, Missile missile){
        double distance = Math.sqrt(
                Math.pow(destination.getX() - missile.getCenterXCoordinate(), 2) +
                        Math.pow(destination.getY() - missile.getCenterYCoordinate(), 2)
        );

        float baseMovementSpeed = 7; // adjust as needed
        int maxDistanceRange = DataClass.getInstance().getWindowWidth(); // adjust based on your max possible distance

        return (float) (distance / maxDistanceRange) * baseMovementSpeed;
    }

    private Point getRandomDestination(Missile missile, int boardBlock){
        return BoardBlockUpdater.getRandomCoordinateInBlock(boardBlock, missile.getWidth(), missile.getHeight());
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
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_2
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

    public double getLastAttackTime() {
        return this.lastAttackedTime;
    }

    public void setLastAttackTime(double lastAttackTime) {
        this.lastAttackedTime = lastAttackTime;
    }

}
