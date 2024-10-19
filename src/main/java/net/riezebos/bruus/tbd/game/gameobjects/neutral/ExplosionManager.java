package net.riezebos.bruus.tbd.game.gameobjects.neutral;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.managers.AnimationManager;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
                        explosion.applyAfterCollisionItemEffectsToObject(enemy);
                    }

                    explosion.applyExplosionOnHitEffects(enemy);
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
