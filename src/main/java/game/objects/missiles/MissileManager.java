package game.objects.missiles;

import java.util.ArrayList;
import java.util.List;

import game.managers.AnimationManager;
import game.objects.GameObject;
import game.objects.player.PlayerStats;
import game.items.Item;
import game.items.enums.ItemApplicationEnum;
import game.util.CollisionDetector;
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

        for(Missile missile : missiles){
            missile.setVisible(false);
        }

        for(SpecialAttack specialAttack: specialAttacks){
            specialAttack.setVisible(false);
        }

        removeInvisibleProjectiles();

        missiles = new ArrayList<Missile>();
        specialAttacks = new ArrayList<SpecialAttack>();
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
    }

    private void removeInvisibleProjectiles(){
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
        for (int i = missiles.size() - 1; i >= 0; i--) {
            if (missiles.get(i).isVisible()) {
                missiles.get(i).move();
                missiles.get(i).updateGameObjectEffects();
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
//        for (Missile missile : specialAttack.getSpecialAttackMissiles()) {
//            for (Enemy enemy : enemyManager.getEnemies()) {
//                if (collisionDetector.detectCollision(missile, enemy)) {
//                    enemy.takeDamage(specialAttack.getDamage());
//                }
//            }
//        }


        for (Enemy enemy : enemyManager.getEnemies()) {
            if (collisionDetector.detectCollision(enemy, specialAttack)) {
                applyDamageModification(specialAttack, enemy);
                enemy.takeDamage(specialAttack.getDamage());
                applyPlayerOnHitEffects(enemy);
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
                applyDamageModification(missile, enemy);
                handleCollision(enemy, missile);
                applyPlayerOnHitEffects(enemy);
            }
        }
    }



    private void checkMissileCollisionWithPlayer (Missile missile) {
        if (collisionDetector.detectCollision(missile, playerManager.getSpaceship())) {
            playerManager.getSpaceship().takeDamage(missile.getDamage());
            handleMissileDestruction(missile);
        }
    }

    private void applyPlayerOnHitEffects(Enemy enemy){
        List<Item> onHitItems = PlayerStats.getInstance().getPlayerInventory().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
        for (Item item : onHitItems) {
            item.applyEffectToObject(enemy); // Assuming applyEffect adds the effect to the GameObject
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
//            if (missile instanceof Rocket1) {
                missile.missileAction();
//            }
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

    private void applyDamageModification(GameObject attack, GameObject target){
        for(Item item : PlayerStats.getInstance().getPlayerInventory().getItemsByApplicationMethod(ItemApplicationEnum.BeforeCollision)){
            item.modifyAttackValues(attack, target);
        }
    }
}
