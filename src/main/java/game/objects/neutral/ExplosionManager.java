package game.objects.neutral;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.items.Item;
import game.items.PlayerInventory;
import game.items.effects.EffectActivationTypes;
import game.items.enums.ItemApplicationEnum;
import game.items.effects.EffectInterface;
import game.managers.AnimationManager;
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
        for (Explosion explosion : explosionList) {
            if (explosion.isVisible()) {
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
            AnimationManager.getInstance().addUpperAnimation(explosion.getAnimation());
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
                    applyDamageModification(explosion, enemy);
                    enemy.takeDamage(explosion.getDamage());

                    if(explosion.isApplyOnHitEffects()) {
                        applyPlayerOnHitEffects(enemy);
                    }

                    applyExplosionEffects(explosion, enemy);
                    explosion.addCollidedSprite(enemy);
                }
            }
        }
    }

    private void applyPlayerOnHitEffects (Enemy enemy) {
        List<Item> onHitItems = PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.AfterCollision);
        for (Item item : onHitItems) {
            item.applyEffectToObject(enemy); // Assuming applyEffect adds the effect to the GameObject
        }
    }


    private void applyExplosionEffects (Explosion explosion, GameObject target) {
        for (EffectInterface effect : explosion.getEffectsToApply()) {
            EffectInterface effectCopy = effect.copy();
            if (effectCopy != null) {
                target.addEffect(effectCopy);
            } else target.addEffect(effect);
        }
    }

    private void checkHostileExplosionCollision (Explosion explosion) {
        if (!explosion.dealtDamageToTarget(friendlyManager.getSpaceship()) &&
                CollisionDetector.getInstance().detectCollision(explosion, friendlyManager.getSpaceship())) {
            friendlyManager.getSpaceship().takeDamage(explosion.getDamage());
            applyPlayerTakeDamageOnHitEffects();
            explosion.addCollidedSprite(friendlyManager.getSpaceship());
        }
    }

    private void applyPlayerTakeDamageOnHitEffects(){
        List<Item> onHitItems = PlayerInventory.getInstance().getItemByActivationTypes(EffectActivationTypes.OnPlayerHit);
        for (Item item : onHitItems) {
            item.applyEffectToObject(PlayerManager.getInstance().getSpaceship());
        }
    }

    private void applyDamageModification (GameObject attack, GameObject target) {
        for (Item item : PlayerInventory.getInstance().getItemsByApplicationMethod(ItemApplicationEnum.BeforeCollision)) {
            item.modifyAttackValues(attack, target);
        }
    }


}
