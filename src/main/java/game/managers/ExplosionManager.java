package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.GameObject;
import game.objects.neutral.Explosion;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.utils.BoundsCalculator;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class ExplosionManager {

    private static ExplosionManager instance = new ExplosionManager();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private List<Explosion> explosionList = new ArrayList<Explosion>();

    private ExplosionManager () {

    }

    public void updateGametick () {
        removeFinishedExplosions();
        updateExplosions();
    }

    private void updateExplosions () {
        for (Explosion explosion : explosionList) {
            explosion.updateAllowedToDealDamage();
            checkExplosionCollisions(explosion);
        }
    }

    public static ExplosionManager getInstance () {
        return instance;
    }


    public void addExistingExplosion (Explosion explosion) {
        if (!this.explosionList.contains(explosion)) {
            this.explosionList.add(explosion);
        }
    }

    public List<Explosion> getExplosions () {
        return this.explosionList;
    }

    private void removeFinishedExplosions () {
        for (int i = 0; i < explosionList.size(); i++) {
            if (!explosionList.get(i).getAnimation().isVisible()) {
                explosionList.get(i).setVisible(false);
                explosionList.get(i).getAnimation().setVisible(false);
                explosionList.remove(i);
            }
        }
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
                    explosion.addCollidedSprite(enemy);
                }
            }
        }
    }

    private void checkHostileExplosionCollision (Explosion explosion) {
        if (!explosion.dealtDamageToTarget(friendlyManager.getSpaceship()) && CollisionDetector.getInstance().detectCollision(explosion, friendlyManager.getSpaceship())) {
            friendlyManager.getSpaceship().takeDamage(explosion.getDamage());
            explosion.addCollidedSprite(friendlyManager.getSpaceship());
        }
    }

    public void resetManager () {
        this.explosionList = new ArrayList<Explosion>();
    }

}
