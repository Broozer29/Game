package game.gameobjects.neutral;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.managers.AnimationManager;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.spaceship.SpaceShip;
import game.util.collision.CollisionDetector;
import game.util.ThornsDamageDealer;
import game.util.collision.CollisionInfo;

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

            if (!explosion.getAnimation().isPlaying()) {
                explosion.setVisible(false);
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
                CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(explosion, enemy);
                if (collisionInfo != null) {
                    explosion.applyBeforeCollisionEffects(enemy);


                    if (explosion.isApplyOnHitEffects()) {
                        explosion.applyAfterCollisionEffects(enemy);
                    }

                    explosion.applyExplosionEffects(enemy);
                    explosion.addCollidedSprite(enemy);
                    explosion.dealDamageToGameObject(enemy);
                }
            }
        }
    }

    private void checkHostileExplosionCollision (Explosion explosion) {
        SpaceShip spaceship = friendlyManager.getSpaceship();
        if (!explosion.dealtDamageToTarget(spaceship)) {
            CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(explosion, spaceship);
            if (collisionInfo != null) {
                //if thorns: reflect damage
                ThornsDamageDealer.getInstance().dealThornsDamageTo(explosion.getOwnerOrCreator());
                explosion.dealDamageToGameObject(spaceship);
                explosion.addCollidedSprite(spaceship);
            }
        }
    }

}
