package game.gameobjects.missiles;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.gameobjects.missiles.laserbeams.Laserbeam;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatsTracker;
import game.managers.AnimationManager;
import game.gameobjects.missiles.missiletypes.TazerProjectile;
import game.gameobjects.player.spaceship.SpaceShip;
import game.util.collision.CollisionDetector;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.missiles.specialAttacks.SpecialAttack;
import game.util.ThornsDamageDealer;
import game.util.collision.CollisionInfo;


public class MissileManager {

    private static MissileManager instance = new MissileManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private PlayerManager playerManager = PlayerManager.getInstance();
    private CollisionDetector collisionDetector = CollisionDetector.getInstance();
    private CopyOnWriteArrayList<Missile> missiles = new CopyOnWriteArrayList<Missile>();
    private CopyOnWriteArrayList<SpecialAttack> specialAttacks = new CopyOnWriteArrayList<SpecialAttack>();
    private CopyOnWriteArrayList<Laserbeam> laserbeams = new CopyOnWriteArrayList<>();


    private MissileManager () {
    }

    public static MissileManager getInstance () {
        return instance;
    }

    public void resetManager () {

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
    }


    private void initManagersIfNull () {
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

    public void updateGameTick () {
        initManagersIfNull();
        moveMissiles();
        removeInvisibleProjectiles();
        checkMissileCollisions();
        triggerMissileActions();
        updateLaserBeams();
    }

    private void updateLaserBeams () {
        for (Laserbeam laserbeam : laserbeams) {
            laserbeam.update();
            SpaceShip spaceship = PlayerManager.getInstance().getSpaceship();
            CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(spaceship, laserbeam);
            if (collisionInfo.isCollided()) {
                if(laserbeam.isBlocksMovement()) {
                    spaceship.resetToPreviousPosition();
                    spaceship.applyKnockback(collisionInfo, laserbeam.getKnockBackStrength());
                }
                spaceship.takeDamage(laserbeam.getDamage());
            }

            if(!laserbeam.isVisible()){
                laserbeams.remove(laserbeam);
            }
        }

    }

    private void removeInvisibleProjectiles () {
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

    private void moveMissiles () {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                missile.move();
                missile.updateGameObjectEffects();
            }
        }
    }

    private void checkMissileCollisions () {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) { //Check special interactions first currently only tazers
                    checkMissileCollisionWithPlayer(missile);
                    checkMissileCollisionWithEnemies(missile);
                } else if (missile.isFriendly()) {  //Then generic friendly missiles on enemies
                    checkMissileCollisionWithEnemies(missile);
                } else { // Then generic enemy missiles on friendlies
                    checkMissileCollisionWithPlayer(missile);
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


    private void checkEnemySpecialAttackMissileCollision (SpecialAttack specialAttack) {
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
    private void checkSpecialAttackWithEnemyCollision (SpecialAttack specialAttack) {
        boolean hasAppliedEffects = false;
        for (Enemy enemy : enemyManager.getEnemies()) {
            CollisionInfo collisionInfo = collisionDetector.detectCollision(enemy, specialAttack);
            if (collisionInfo != null) {
                specialAttack.applyBeforeCollisionEffects(enemy);

                if (specialAttack.isAllowOnHitEffects()
                        && specialAttack.canApplyEffectAgain()
                ) {
                    specialAttack.applyAfterCollisionItemEffectsToObject(enemy);
                    hasAppliedEffects = true;
                }

                specialAttack.dealDamageToGameObject(enemy);

            }
        }

        if (hasAppliedEffects) {
            specialAttack.setLastOnHitInterval(GameStateInfo.getInstance().getGameSeconds());
        }
    }


    // Checks collision between special attacks and enemy missiles
    private void checkSpecialAttackWithEnemyMissileCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().isEmpty() && specialAttack.isDestroysMissiles()) {
            for (Missile missile : missiles) {
                if (!missile.isFriendly()) {
                    CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, specialAttack);
                    if (collisionInfo != null) {
                        missile.destroyMissile();
                    }
                }
            }
        }
    }

    private void checkMissileCollisionWithEnemies (Missile missile) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, enemy);
            if (collisionInfo != null) {
                if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                    ((TazerProjectile) missile).handleTazerMissile(missile, enemy);
                } else { //It's a player missile
                    missile.applyBeforeCollisionEffects(enemy);
                    missile.handleCollision(enemy);
                    missile.applyAfterCollisionItemEffectsToObject(enemy);

                }

            }
        }
    }


    private void checkMissileCollisionWithPlayer (Missile missile) {
        SpaceShip spaceship = playerManager.getSpaceship();
        CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, spaceship);
        if (collisionInfo != null) {
            if (missile.getMissileEnum().equals(MissileEnums.TazerProjectile)) {
                ((TazerProjectile) missile).handleTazerMissile(missile, spaceship);
            }
            //if deflect: deflect missile, else:
            missile.handleCollision(spaceship);

            //if thorns & not explosive (explosive reflection is in explosion manager): reflect damage
            if (!missile.isExplosive()) {
                ThornsDamageDealer.getInstance().dealThornsDamageTo(missile.getOwnerOrCreator());
            }
        }
    }

    private void checkMissileCollisionWithMissiles (Missile missile) {
        if (missile.interactsWithMissiles()) {
            if (missile.isFriendly()) {
                //Check for all non-friendly missiles in the missile list, this is used by the player
                for (Missile enemyMissile : missiles) {
                    if (!enemyMissile.isFriendly()) {
                        CollisionInfo collisionInfo = collisionDetector.detectCollision(missile, enemyMissile);
                        if (collisionInfo != null) {
                            if (missile.isDeletesMissiles()) {
                                enemyMissile.destroyMissile();
                                addHitToStatsTracker(missile);
                            } else if (missile.isDestructable()) {
                                enemyMissile.dealDamageToGameObject(missile);
                                missile.setShowHealthBar(true);
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
                            if (missile.isDeletesMissiles()) {
                                friendlyMissile.destroyMissile();
                            } else if (missile.isDestructable()) {
                                friendlyMissile.dealDamageToGameObject(missile);
                                missile.setShowHealthBar(true);
                                friendlyMissile.destroyMissile();
                            }
                        }
                    }
                }
            }
        }
    }

    private void addHitToStatsTracker (Missile missile) {
        if (missile.getOwnerOrCreator().equals(PlayerManager.getInstance().getSpaceship())) {
            GameStatsTracker.getInstance().addShotHit(1);
        }
    }


    private void triggerMissileActions () {
        for (Missile missile : missiles) {
            missile.missileAction();
        }
    }

    public void addSpecialAttack (SpecialAttack specialAttack) {
        this.specialAttacks.add(specialAttack);
    }

    public List<Missile> getMissiles () {
        return missiles;
    }

    public List<SpecialAttack> getSpecialAttacks () {
        return specialAttacks;
    }

    public void addExistingMissile (Missile missile) {
        this.missiles.add(missile);
    }

    public CopyOnWriteArrayList<Laserbeam> getLaserbeams () {
        return laserbeams;
    }

    public void addLaserBeam (Laserbeam laserbeam) {
        if (!this.laserbeams.contains(laserbeam)) {
            laserbeams.add(laserbeam);
        }
    }
}
