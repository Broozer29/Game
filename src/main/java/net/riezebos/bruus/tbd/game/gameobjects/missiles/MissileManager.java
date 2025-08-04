package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.ReflectiveBlocks;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes.TazerProjectile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FireShield;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FlameThrower;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.ReflectiveShielding;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;
import net.riezebos.bruus.tbd.game.util.VisualLayer;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class MissileManager {

    private static MissileManager instance = new MissileManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private PlayerManager playerManager = PlayerManager.getInstance();
    private CollisionDetector collisionDetector = CollisionDetector.getInstance();
    private CopyOnWriteArrayList<Missile> missiles = new CopyOnWriteArrayList<Missile>();
    private CopyOnWriteArrayList<SpecialAttack> specialAttacks = new CopyOnWriteArrayList<SpecialAttack>();
    private CopyOnWriteArrayList<Laserbeam> laserbeams = new CopyOnWriteArrayList<>();
    private PerformanceLogger performanceLogger = null;
    private static float laserBeamCooldown = 0.045f;


    private MissileManager() {
        this.performanceLogger = new PerformanceLogger("Missile Manager");
    }

    public static MissileManager getInstance() {
        return instance;
    }

    public void resetManager() {
        for (Missile missile : missiles) {
            missile.setVisible(false);
        }

        for (SpecialAttack specialAttack : specialAttacks) {
            specialAttack.setVisible(false);
        }

        for (Laserbeam laserbeam : laserbeams) {
            laserbeam.setVisible(false);
        }

        removeInvisibleProjectiles();

        missiles.clear();
        specialAttacks.clear();
        laserbeams.clear();
        performanceLogger.reset();
    }


    private void initManagersIfNull() {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        if (animationManager == null) {
            animationManager = AnimationManager.getInstance();
        }

        if (playerManager == null) {
            playerManager = PlayerManager.getInstance();
        }
    }

    public void updateGameTick() {
//        PerformanceLoggerManager.timeAndLog(performanceLogger, "Total", () -> {
        initManagersIfNull();
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Move Missiles", this::moveMissiles);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Remove Invisible Projects", this::removeInvisibleProjectiles);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Check Missile Collision", this::checkMissileCollisions);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Trigger Missile Actions", this::triggerMissileActions);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Laserbeams", this::updateLaserBeams);
//                });
//        moveMissiles();
//        removeInvisibleProjectiles();
//        checkMissileCollisions();
//        triggerMissileActions();
//        updateLaserBeams();
    }


    private void updateLaserBeams() {
        for (Laserbeam laserbeam : laserbeams) {
            if (!laserbeam.getOwner().isVisible()) {
                laserbeam.setVisible(false);
            }
            if (!laserbeam.isVisible()) {
                laserbeams.remove(laserbeam);
                continue; //It's gone so updating it will crash
            }

            laserbeam.update();
            SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
            CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(spaceship, laserbeam);
            if (collisionInfo.isCollided()) {
                if (laserbeam.isBlocksMovement()) {
                    spaceship.resetToPreviousPosition();
                    spaceship.applyKnockback(collisionInfo, laserbeam.getKnockBackStrength());
                }


                if (GameState.getInstance().getGameSeconds() - spaceship.getLastTimeDamageTakenFromLaserbeams() >= laserBeamCooldown) {
                    spaceship.takeDamage(laserbeam.getDamage());
                    spaceship.setLastTimeDamageTakenFromLaserbeams(GameState.getInstance().getGameSeconds());
                }
            }


            for (Drone drone : FriendlyManager.getInstance().getAllProtossDrones()) {
                double currentTime = GameState.getInstance().getGameSeconds();
                double timeSinceLastDamage = currentTime - drone.getLastTimeDamageTakenFromLaserbeams();
                // Only check for collisions if enough time has passed since the last damage
                if (timeSinceLastDamage >= laserBeamCooldown) {
                    collisionInfo = CollisionDetector.getInstance().detectCollision(drone, laserbeam);

                    if (collisionInfo.isCollided()) {
                        drone.takeDamage(laserbeam.getDamage());
                        drone.setLastTimeDamageTakenFromLaserbeams(currentTime);
                    }
                }
            }


        }

    }


    private void removeInvisibleProjectiles() {
        for (int i = 0; i < missiles.size(); i++) {
            if (!missiles.get(i).isVisible()) {
                missiles.get(i).deleteObject();
                missiles.remove(i);
                i--;
            }
        }

        for (int i = 0; i < specialAttacks.size(); i++) {
            if (!specialAttacks.get(i).isVisible()) {
                specialAttacks.get(i).deleteObject();
                specialAttacks.remove(i);
                i--;
            }
        }
    }

    private void moveMissiles() {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                missile.move();
                missile.updateGameObjectEffects();
            }
        }
    }

    private void checkMissileCollisions() {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) { //Check special interactions first currently only tazers
                    checkMissileCollisionWithPlayer(missile);
                    checkMissileCollisionWithEnemies(missile);
                } else if (missile.isFriendly()) {  //Then generic friendly missiles on enemies
                    checkMissileCollisionWithEnemies(missile);
                } else { // Then generic enemy missiles on friendlies
                    checkMissileCollisionWithPlayer(missile);
                    checkMissileCollisionWithDrones(missile);
                }

                if (missile.interactsWithMissiles()) {
                    checkMissileCollisionWithMissiles(missile);
                }
            }
        }

        // Handle special attacks
        for (SpecialAttack specialAttack : specialAttacks) {
            if (specialAttack.getAnimation().isVisible()) {
                if (specialAttack.isFriendly()) {
                    checkSpecialAttackWithEnemyCollision(specialAttack);
                    checkSpecialAttackWithEnemyMissileCollision(specialAttack);
                } else {
                    specialAttack.checkEnemySpecialAttackCollision(PlayerManager.getInstance().getSpaceship());
                    checkEnemySpecialAttackMissileCollision(specialAttack);
                }
            }
        }
    }


    private void checkEnemySpecialAttackMissileCollision(SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty()) {
            for (Missile missile : missiles) {
                if (missile.isFriendly()) {
                    CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, specialAttack);
                    if (collisionInfo != null) {
                        missile.destroyMissile();
                    }
                }
            }
        }
    }

    // Checks collision between special attacks and enemies
    private void checkSpecialAttackWithEnemyCollision(SpecialAttack specialAttack) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            CollisionInfo collisionInfo = collisionDetector.detectCollision(enemy, specialAttack);
            if (collisionInfo != null) {
                specialAttack.tryDealDamageAndApplyEffects(enemy);
            }
        }
    }


    // Checks collision between special attacks and enemy missiles
    private void checkSpecialAttackWithEnemyMissileCollision(SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty() &&
                (specialAttack.isDestroysMissiles() || specialAttack.isDamagesMissiles())) {
            for (Missile missile : missiles) {
                if (!missile.isFriendly() && collisionDetector.detectCollision(missile, specialAttack) != null) {
                    if (specialAttack.isDestroysMissiles() && missile.isDestructable()) {
                        missile.destroyMissile();
                    } else if (specialAttack.isDamagesMissiles() && missile.isDamageable()) {
                        missile.takeDamage(getSpecialAttackMissileDamage(specialAttack, missile)); //min 1 for flamethrower/mutalisk interaction
                    }
                }

            }
        }
    }

    private float getSpecialAttackMissileDamage(SpecialAttack specialAttack, Missile missile) {
        if((specialAttack instanceof FlameThrower || specialAttack instanceof FireShield) && missile.getMissileEnum().equals(MissileEnums.ReflectiveBlocks)) {
            return Math.max(1, missile.getMaxHitPoints() * (specialAttack.getMaxHPDamagePercentageForMissiles() * 0.2f));
        }

        return Math.max(1, missile.getMaxHitPoints() * specialAttack.getMaxHPDamagePercentageForMissiles());
    }

    private void checkMissileCollisionWithEnemies(Missile missile) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, enemy);
            if (collisionInfo != null) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                    ((TazerProjectile) missile).applyTazerMissileEffect(enemy);
                } else { //It's a player missile
                    if (!missile.hasCollidedBeforeWith(enemy)) {
                        missile.applyBeforeCollisionAttackModifyingItemEffects(enemy);
                        missile.handleCollision(enemy);
                        missile.applyAfterCollisionItemEffects(enemy);
                    }
                }

            }
        }
    }


    private void checkMissileCollisionWithPlayer(Missile missile) {
        SpaceShip spaceship = playerManager.getSpaceship();
        CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, spaceship);
        if (collisionInfo != null) {
            ReflectiveShielding reflectiveShielding = (ReflectiveShielding) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ReflectiveShielding);

            if (reflectiveShielding != null && reflectiveShielding.attemptToReflectMissile(missile)
                    && PlayerManager.getInstance().getSpaceship().getCurrentShieldPoints() > 0) {
                ThornsDamageDealer.getInstance().reflectMissile(collisionInfo.getCollisionPoint(), missile);
                return; //don't want to continue since we reflected/blocked the missile
            }

            if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                ((TazerProjectile) missile).applyTazerMissileEffect(spaceship);
            }

            missile.handleCollision(spaceship);

            if (missile.getKnockbackStrength() > 0) {
                spaceship.applyKnockback(collisionInfo, missile.getKnockbackStrength());
            }
        }
    }

    private void checkMissileCollisionWithDrones(Missile missile) {
        for (Drone drone : FriendlyManager.getInstance().getAllProtossDrones()) {
            CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, drone);
            if (collisionInfo != null) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                    ((TazerProjectile) missile).applyTazerMissileEffect(drone);
                }

                missile.setShowDamage(false);
                missile.handleCollision(drone);
                drone.setShowHealthBar(true);
                ProtossUtils.getInstance().handleProtossThorns(missile.getOwnerOrCreator());
            }
        }


    }


    private void checkMissileCollisionWithMissiles(Missile missile) {
        if (missile.interactsWithMissiles()) {
            if (missile.isFriendly()) {
                //Check for all non-friendly missiles in the missile list, this is used by the player
                for (Missile enemyMissile : missiles) {
                    if (!enemyMissile.isFriendly()) {
                        CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, enemyMissile);
                        if (collisionInfo != null) {

                            if (enemyMissile instanceof ReflectiveBlocks reflectiveBlocks) {
                                handleReflection(missile, reflectiveBlocks);
                            } else if (missile instanceof ReflectiveBlocks reflectiveBlocks) {
                                handleReflection(enemyMissile, reflectiveBlocks);
                            } else if (missile.isDeletesMissiles() && enemyMissile.isDestructable()) {
                                enemyMissile.destroyMissile();
                                addHitToStatsTracker(missile);
                            } else if (missile.isDamageable()) {
                                enemyMissile.dealDamageToGameObject(missile);
                                enemyMissile.destroyMissile();
                                addHitToStatsTracker(missile);
                            }
                        }
                    }

                }
            } else {
                //Check for all friendly missiles in the missile list, this is used by the enemies
                for (Missile friendlyMissile : missiles) {
                    if (friendlyMissile.isFriendly()) {
                        CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, friendlyMissile);
                        if (collisionInfo != null) {

                            if (friendlyMissile instanceof ReflectiveBlocks reflectiveBlocks) {
                                handleReflection(missile, reflectiveBlocks);
                            } else if (missile instanceof ReflectiveBlocks reflectiveBlocks) {
                                handleReflection(friendlyMissile, reflectiveBlocks);
                            } else if (missile.isDeletesMissiles() && friendlyMissile.isDestructable()) {
                                friendlyMissile.destroyMissile();
                            } else if (missile.isDamageable()) {
                                friendlyMissile.dealDamageToGameObject(missile);
//                                missile.setShowHealthBar(true);
                                friendlyMissile.destroyMissile();
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleReflection(Missile nonReflectiveMissile, Missile reflectiveMissile) {
        if (reflectiveMissile instanceof ReflectiveBlocks reflectiveBlocks) {
            reflectiveBlocks.reflectMissile(nonReflectiveMissile);
        }
    }

    private void addHitToStatsTracker(Missile missile) {
        if (missile.getOwnerOrCreator().equals(PlayerManager.getInstance().getSpaceship())) {
            GameStatsTracker.getInstance().addShotHit(1);
        }
    }


    private void triggerMissileActions() {
        for (Missile missile : missiles) {
            missile.missileAction();
        }

        for (SpecialAttack specialAttack : specialAttacks) {
            specialAttack.updateSpecialAttackEnemyCollisionList();
            specialAttack.updateSpecialAttack();
        }
    }

    public void addSpecialAttack(SpecialAttack specialAttack) {
        this.specialAttacks.add(specialAttack);
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public List<Missile> getMissilesByAllegiance(boolean friendly) {
        return missiles.stream().filter(missile -> missile.isFriendly() == friendly).toList();
    }

    public List<SpecialAttack> getSpecialAttacks() {
        return specialAttacks;
    }

    public List<SpecialAttack> getSpecialAttacksByAnimationLayer(VisualLayer layer) {
        return specialAttacks.stream().filter(specialAttack -> specialAttack.getVisualLayer().equals(layer)).collect(Collectors.toList());
    }

    public void addExistingMissile(Missile missile) {
        this.missiles.add(missile);
    }

    public CopyOnWriteArrayList<Laserbeam> getLaserbeams() {
        return laserbeams;
    }

    public void addLaserBeam(Laserbeam laserbeam) {
        if (!this.laserbeams.contains(laserbeam)) {
            laserbeams.add(laserbeam);
        }
    }

    public PerformanceLogger getPerformanceLogger() {
        return this.performanceLogger;
    }
}
