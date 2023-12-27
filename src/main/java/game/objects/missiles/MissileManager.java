package game.objects.missiles;

import java.util.ArrayList;
import java.util.List;

import game.managers.AnimationManager;
import game.managers.CollisionDetector;
import game.objects.player.PlayerManager;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.player.specialAttacks.SpecialAttack;
import game.objects.missiles.missiletypes.Rocket1;


public class MissileManager {

    private static MissileManager instance = new MissileManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private PlayerManager playerManager = PlayerManager.getInstance();
    private CollisionDetector collisionDetector = CollisionDetector.getInstance();
    private List<Missile> missiles = new ArrayList<Missile>();
    private List<SpecialAttack> specialAttacks = new ArrayList<SpecialAttack>();

    private MissileManager () {
    }

    public static MissileManager getInstance () {
        return instance;
    }

    public void resetManager () {
        missiles = new ArrayList<Missile>();
        specialAttacks = new ArrayList<SpecialAttack>();
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
        checkMissileCollisions();
        triggerMissileActions();
    }

    private void moveMissiles () {
        for (int i = missiles.size() - 1; i >= 0; i--) {
            Missile missile = missiles.get(i);
            if (missile.isVisible()) {
                missile.move();
            } else {
                missiles.remove(i);
            }
        }
    }

    private void checkMissileCollisions () {
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                if (missile.isFriendly()) {
                    checkMissileCollisionWithEnemies(missile);
                } else {
                    checkMissileCollisionWithPlayer(missile);
                }
            }
        }

        // Handle special attacks
        for (SpecialAttack specialAttack : specialAttacks) {
            if (specialAttack.getAnimation().isVisible()) {
                checkSpecialAttackWithEnemyCollision(specialAttack);
                checkSpecialAttackWithEnemyMissileCollision(specialAttack);
            }
        }
    }

    // Checks collision between special attacks and enemies
    private void checkSpecialAttackWithEnemyCollision (SpecialAttack specialAttack) {
        for (Missile missile : specialAttack.getSpecialAttackMissiles()) {
            for (Enemy enemy : enemyManager.getEnemies()) {
                if (collisionDetector.detectCollision(missile, enemy)) {
                    enemy.takeDamage(specialAttack.getDamage());
                }
            }
        }

        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(enemy, specialAttack)) {
                enemy.takeDamage(specialAttack.getDamage());
            }
        }
    }

    // Checks collision between special attacks and enemy missiles
    private void checkSpecialAttackWithEnemyMissileCollision (SpecialAttack specialAttack) {
        if (specialAttack.getSpecialAttackMissiles().size() == 0) {
            for (Missile missile : missiles) {
                if (collisionDetector.detectCollision(missile, specialAttack)) {
                    handleMissileDestruction(missile);
                }
            }
        }
    }

    private void checkMissileCollisionWithEnemies (Missile missile) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(missile, enemy)) {
                handleCollision(enemy, missile);
            }
        }
    }

    private void checkMissileCollisionWithPlayer (Missile missile) {
        if (collisionDetector.detectCollision(missile, playerManager.getSpaceship())) {
            playerManager.getSpaceship().takeHitpointDamage(missile.getDamage());
            handleMissileDestruction(missile);
        }
    }

    private void handleCollision (Enemy enemy, Missile missile) {
        if (missile instanceof Rocket1) {
            missile.missileAction();
            missile.setVisible(false);
        } else {
            enemy.takeDamage(missile.getDamage());
            missile.setVisible(false);
            if (missile.getDestructionAnimation() != null) {
                animationManager.addUpperAnimation(missile.getDestructionAnimation());
            }
        }
    }

    private void handleMissileDestruction (Missile missile) {
        missile.setVisible(false);
        if (missile.getDestructionAnimation() != null) {
            animationManager.addUpperAnimation(missile.getDestructionAnimation());
        }
    }

    private void triggerMissileActions () {
        for (Missile missile : missiles) {
            if (missile instanceof Rocket1) {
                missile.missileAction();
            }
        }
    }

    public void addSpecialAttack (SpecialAttack specialAttack) {
        this.specialAttacks.add(specialAttack);
    }
}
