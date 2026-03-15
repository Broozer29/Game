package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss;

import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.playerprofile.PlayerProfileManager;
import net.riezebos.bruus.tbd.game.util.ArmorCalculator;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TwinBoss extends Enemy {

    private BossActionable currentActiveBehavior = null;
    private boolean isPrimaryTwinBoss;
    private Point pointToTeleportBackTo = null;

    private List<Laserbeam> laserbeamList = new ArrayList<>();


    //todo the original sprite does not have identical dimensions for each image in the animation, need to go to GIMP and correct it so the boss doesnt jump around after cropping
    public TwinBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.BossExplosion);
        destroyedExplosionfiguration.getSpriteConfiguration().setScale(4);

        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 10;
        this.knockbackStrength = 9;
        this.allowedVisualsToRotate = true;

        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder pathFinder) {
            pathFinder.setShouldDecreaseBoardBlock(true);
            pathFinder.setSecondsToHoverStill(0);
        }

        //isEmpty check to ensure this is only done once
        if (EnemyManager.getInstance().getEnemiesByType(this.getEnemyType()).isEmpty()) {
            TwinBossManager.getInstance().setTwinBossMaxHealth(this.maxHitPoints);
            TwinBossManager.getInstance().setTwinBossCurrentHealth(this.currentHitpoints);
        }

        TwinBossManager.getInstance().addTwinBoss(this);
    }

    @Override
    protected void updateChargingAttackAnimationCoordination() {
        if (this.chargingUpAttackAnimation != null) {
            double baseDistance = (this.getWidth() + this.getHeight()) / 3.0; //
            Point chargingUpLocation = calculateFrontPosition(this.getCenterXCoordinate(), this.getCenterYCoordinate(), rotationAngle, baseDistance);
            this.chargingUpAttackAnimation.setCenterCoordinates(chargingUpLocation.getX(), chargingUpLocation.getY());
        }
    }

    @Override
    public void fireAction() {
        if (!allowedToFire && WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; // Boss is allowed to fire
        }

        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder hoverPathFinder) {
            if (this.getCurrentBoardBlock() <= 0) { // if its reaches 0 it will move out of bounds
                hoverPathFinder.setDecreaseBoardBlockAmountBy(-1);
            } else if (this.getCurrentBoardBlock() >= 6) {
                hoverPathFinder.setDecreaseBoardBlockAmountBy(1);
            }
        }

        updateChargingAttackAnimationCoordination();

        if (currentActiveBehavior != null) {
            //Important to note: twinBosses and their respective behaviour is NOT aware of each other. This means that the sequential order in which the twinBosses call activateBehaviour() MATTERS!
            //If one twinBoss calls activateBehaviour out of turn, their behaviour breaks down. Thus, the order of twinBoss enemies in TwinBossManager MUST be maintained.
            boolean completed = currentActiveBehavior.activateBehaviour(this);
            if (completed) {
                TwinBossManager.getInstance().clearAllTwinAttacks();
                TwinBossManager.getInstance().setLastUsedBossActionable(this.currentActiveBehavior);
            }
        }

        if (currentActiveBehavior == null) {
            currentActiveBehavior = TwinBossManager.getInstance().getAvailableBossActionable();
        }
    }

    @Override
    public void triggerOnDeathActions() {
        if(!TwinBossManager.isHasGrantedKillRewards()) {
            GameStatsTracker.getInstance().addEnemyKilled(1);
            PlayerProfileManager.getInstance().getLoadedProfile().addEmeralds(1);
            PlayerProfileManager.getInstance().exportCurrentProfile();
            GUIComponent emeraldIcon = GameUICreator.getInstance().createEmeraldObtainedIcon(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            BoardManager.getInstance().getGameBoard().addGUIAnimation(emeraldIcon);

            TwinBossManager.setHasGrantedKillRewards(true);
        }
    }

    @Override
    public float getCurrentHitpoints() {
        return TwinBossManager.getInstance().getTwinBossCurrentHealth();
    }

    @Override
    public float getMaxHitPoints() {
        return TwinBossManager.getInstance().getTwinBossMaxHealth();
    }

    @Override
    public void takeDamage(float damageTaken) {
        if (damageTaken > 0) {
            damageTaken = (ArmorCalculator.calculateDamage(damageTaken, this) * damageReductionMultiplier);
        }

        if (damageTaken > 0) {
            lastGameSecondDamageTaken = GameState.getInstance().getGameSeconds();
        }
        TwinBossManager.getInstance().damageTwinBoss(damageTaken);
    }

    public void killTwinBoss() {
        clearLaserbeams();
        if (this.xpOnDeath > 0) {
            PlayerStats.getInstance().addXP(xpOnDeath * ((float) 1 / TwinBossManager.twinCount)); //Divides the xp by the amount of bosses
        }
        if (this.destructionAnimation != null) {
            this.destructionAnimation.setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            AnimationManager.getInstance().addUpperAnimation(destructionAnimation);
        }

        AudioManager.getInstance().addAudio(deathSound);

        triggerOnDeathActions();

        this.setVisible(false);
        activateOnDeathEffects();
        PlayerInventory.getInstance().addMinerals(this.cashMoneyWorth * ((float) 1 / TwinBossManager.twinCount));
        GameStatsTracker.getInstance().addMoneyAcquired(this.cashMoneyWorth * ((float) 1 / TwinBossManager.twinCount));
    }

    @Override
    public void rotateAfterMovement() {
        if (this.isAllowedVisualsToRotate()) {
            SpaceShip spaceShip = PlayerManager.getInstance().getClosestSpaceShip(this);
            if(spaceShip != null){
                this.rotateGameObjectTowards(
                        spaceShip.getCenterXCoordinate(),
                        spaceShip.getCenterYCoordinate(),
                        true
                );
            }
        }
    }

    public boolean isPrimaryTwinBoss() {
        return isPrimaryTwinBoss;
    }

    public void setPrimaryTwinBoss(boolean primaryTwinBoss) {
        isPrimaryTwinBoss = primaryTwinBoss;
    }

    public Point getPointToTeleportBackTo() {
        return pointToTeleportBackTo;
    }

    public void setPointToTeleportBackTo(Point pointToTeleportBackTo) {
        this.pointToTeleportBackTo = pointToTeleportBackTo;
    }

    public void setCurrentActiveBehavior(BossActionable currentActiveBehavior) {
        this.currentActiveBehavior = currentActiveBehavior;
    }

    public BossActionable getCurrentActiveBehavior() {
        return currentActiveBehavior;
    }

    public void addLaserbeam(Laserbeam laserbeam) {
        this.laserbeamList.add(laserbeam);
    }

    public void clearLaserbeams(){
        for(Laserbeam laserbeam : this.laserbeamList) {
            laserbeam.setVisible(false);
        }
        this.laserbeamList.clear();
    }

    public List<Laserbeam> getLaserbeamList() {
        return laserbeamList;
    }
}
