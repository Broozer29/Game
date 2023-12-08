package game.managers;

import java.util.ArrayList;
import java.util.List;

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
        checkExplosionPlayerCollision();
        checkExplosionEnemyCollision();
    }

    private void updateExplosions () {
        for (Explosion explosion : explosionList) {
            explosion.updateAllowedToDealDamage();
        }
    }

    public static ExplosionManager getInstance () {
        return instance;
    }

    public void createAndAddExplosion (int xCoordinate, int yCoordinate, ImageEnums explosionType, float scale,
                                       float damage, boolean friendly) {
        SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, explosionType, false, scale);
        animation.setX(xCoordinate - animation.getWidth() / 2);
        animation.setY(yCoordinate - animation.getHeight() / 2);
        Explosion explosion = new Explosion(xCoordinate, yCoordinate, scale, animation, damage, friendly);
        explosion.setX(xCoordinate - animation.getWidth() / 2);
        explosion.setY(yCoordinate - animation.getHeight() / 2);
        addExistingExplosion(explosion);
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

    // used for explosions of the enemy
    // Broken intentionally, rework this manually
    private void checkExplosionPlayerCollision () {
        if (friendlyManager == null) {
            friendlyManager = PlayerManager.getInstance();
        }
        for (Explosion explosion : explosionList) {
            // Check if the explosion should deal damage at all
            if (explosion.isAllowedToDealDamage()) {
                // Check if the explosion is visible and hostile
                if (explosion.isVisible() && !explosion.isFriendly()) {
                    // Check if the explosion already did damage/collided to the player
                    if (!explosion.getCollidedSprites().contains(friendlyManager.getSpaceship())) {
                        // If the explosion is close to the player
                        if (isNearby(explosion, friendlyManager.getSpaceship())) {
                            // If the explosion intersects with the player
                            if (BoundsCalculator.getGameObjectBounds(explosion)
                                    .intersects(BoundsCalculator.getGameObjectBounds(friendlyManager.getSpaceship()))) {
                                // Collision!
                                friendlyManager.getSpaceship().takeHitpointDamage(explosion.getDamage());
                                explosion.addCollidedSprite(friendlyManager.getSpaceship());
                            }
                        }
                    }
                }
            }
        }
    }

    // used for explosions of the player
    private void checkExplosionEnemyCollision () {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }
        for (Explosion explosion : explosionList) {
            if (explosion.isAllowedToDealDamage()) {
                if (explosion.isVisible() && explosion.isFriendly()) {
                    for (Enemy enemy : enemyManager.getEnemies()) {
                        if (!explosion.getCollidedSprites().contains(enemy)) {
                            if (isNearby(explosion, enemy)) {
                                if (BoundsCalculator.getGameObjectBounds(explosion)
                                        .intersects(BoundsCalculator.getGameObjectBounds(enemy))) {
                                    enemy.takeDamage(explosion.getDamage());
                                    explosion.addCollidedSprite(enemy);
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private boolean isWithinBoardBlockThreshold (Sprite sprite1, Sprite sprite2) {
        int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
        return blockDifference <= 4;
    }

    private boolean isNearby (Sprite sprite1, Sprite sprite2) {
        if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
            return false;
        }

        double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
                sprite1.getYCoordinate() - sprite2.getYCoordinate());
        return distance < 400;
    }

    public void resetManager () {
        this.explosionList = new ArrayList<Explosion>();
    }

}
