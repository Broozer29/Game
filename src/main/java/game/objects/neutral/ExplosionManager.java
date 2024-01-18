package game.objects.neutral;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import game.items.Item;
import game.items.ItemApplicationEnum;
import game.items.ItemEnums;
import game.items.effects.EffectInterface;
import game.objects.GameObject;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.player.PlayerManager;
import game.objects.player.PlayerStats;
import game.util.CollisionDetector;

public class ExplosionManager {

    private static ExplosionManager instance = new ExplosionManager();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
//    private List<Explosion> explosionList = new ArrayList<Explosion>();
private CopyOnWriteArrayList<Explosion> explosionList = new CopyOnWriteArrayList<>();

    private ExplosionManager () {

    }

    public void updateGametick () {
        updateExplosions();
    }

    private void updateExplosions () {
        List<Explosion> toRemove = new ArrayList<>();
        for(Explosion explosion : explosionList){
            if(explosion.isVisible()){
                explosion.updateAllowedToDealDamage();
                checkExplosionCollisions(explosion);
            } else {
                explosion.deleteObject();
                toRemove.add(explosion);
            }
        }

        explosionList.removeAll(toRemove);
    }


    public void resetManager () {
        for (Explosion explosion : explosionList) {
            explosion.setVisible(false);
            explosion.deleteObject();
        }

        explosionList = new CopyOnWriteArrayList<>();
    }

    public static ExplosionManager getInstance () {
        return instance;
    }


    public void addExplosion (Explosion explosion) {
        if (!this.explosionList.contains(explosion)) {
            this.explosionList.add(explosion);
        }
    }

    public List<Explosion> getExplosions () {
        return this.explosionList;
    }


    private void checkExplosionCollisions (Explosion explosion) {
        if (explosion.isAllowedToDealDamage() && explosion.isVisible()) {
            if (explosion.isFriendly()) {
                checkFriendlyExplosionCollision(explosion);
            } else {
                checkHostileExplosionCollision(explosion);
            }
        }
    }

    private void checkFriendlyExplosionCollision (Explosion explosion) {
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (!explosion.dealtDamageToTarget(enemy)) {
                if (CollisionDetector.getInstance().detectCollision(explosion, enemy)) {
                    enemy.takeDamage(explosion.getDamage());
                    applyPlayerOnHitEffects(enemy);
                    applyExplosionEffects(explosion, enemy);
                    explosion.addCollidedSprite(enemy);
                }
            }
        }
    }

    private void applyPlayerOnHitEffects (Enemy enemy) {
        List<Item> onHitItems = PlayerStats.getInstance().getPlayerInventory().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
        for (Item item : onHitItems) {
            item.applyEffectToObject(enemy); // Assuming applyEffect adds the effect to the GameObject
        }
    }

    private void applyExplosionEffects (Explosion explosion, GameObject target) {
        for (EffectInterface effect : explosion.getEffectsToApply()) {
            target.addEffect(effect);
            System.out.println("Added: " + effect);
        }
    }

    private void checkHostileExplosionCollision (Explosion explosion) {
        if (!explosion.dealtDamageToTarget(friendlyManager.getSpaceship()) && CollisionDetector.getInstance().detectCollision(explosion, friendlyManager.getSpaceship())) {
            friendlyManager.getSpaceship().takeDamage(explosion.getDamage());
            explosion.addCollidedSprite(friendlyManager.getSpaceship());
        }
    }


}
