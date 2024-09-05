package game.gameobjects.enemies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.gameobjects.enemies.enums.EnemyCategory;
import game.gamestate.GameStatsTracker;
import game.managers.AnimationManager;
import game.gameobjects.GameObject;
import game.util.CollisionDetector;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.spaceship.SpaceShip;
import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;

public class EnemyManager {

    private static EnemyManager instance = new EnemyManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private CopyOnWriteArrayList<Enemy> enemyList = new CopyOnWriteArrayList<Enemy>();
    private DataClass dataClass = DataClass.getInstance();

    private EnemyManager () {
    }

    public static EnemyManager getInstance () {
        return instance;
    }

    public void resetManager () {
        for (Enemy enemy : enemyList) {
            enemy.setVisible(false);
        }

        try {
            updateEnemies();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        enemyList = new CopyOnWriteArrayList<Enemy>();
        friendlyManager = PlayerManager.getInstance();
        dataClass = DataClass.getInstance();
        audioManager = AudioManager.getInstance();
    }

    public void updateGameTick () {
        try {
            updateEnemies();
            checkSpaceshipCollisions();
            triggerEnemyActions();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void checkSpaceshipCollisions () throws UnsupportedAudioFileException, IOException {
        SpaceShip spaceship = friendlyManager.getSpaceship();
        for (Enemy enemy : enemyList) {
            if (CollisionDetector.getInstance().detectCollision(enemy, spaceship)) {
                if (enemy.detonateOnCollision) {
                    detonateEnemy(enemy);
                    enemy.dealDamageToGameObject(spaceship);
                } else {
                    spaceship.takeDamage(0.5f);
                }
            }
        }
    }

    private void detonateEnemy (Enemy enemy) throws UnsupportedAudioFileException, IOException {
//        animationManager.createAndAddUpperAnimation(enemy.getXCoordinate(), enemy.getYCoordinate(),
//                enemy.enemyType.getDestructionImageEnum(), false, enemy.getScale());
        animationManager.addLowerAnimation(enemy.getDestructionAnimation());
        audioManager.addAudio(AudioEnums.Alien_Bomb_Impact);
        enemy.setVisible(false);
    }

    private void triggerEnemyActions () {
        for (Enemy enemy : enemyList) {
            enemy.fireAction();
        }
    }

    private void updateEnemies() throws UnsupportedAudioFileException, IOException {
        for (Enemy en : enemyList) {
            if (en.isVisible()) {
                en.move();
                en.updateGameObjectEffects();
            } else {
                en.deleteObject();
                enemyList.remove(en); // Remove directly from CopyOnWriteArrayList
            }
        }
    }

    public void addEnemy (Enemy enemy) {
        if (enemy != null && !enemyList.contains(enemy)) {
            enemy.onCreationEffects();
            enemyList.add(enemy);

            if(enemy.getEnemyType().getEnemyCategory() != EnemyCategory.Summon) {
                GameStatsTracker.getInstance().addEnemySpawned(1);
            }
        }
    }

    public List<Enemy> getEnemies () {
        return this.enemyList;
    }

    public int getEnemyCount () {
        return enemyList.size();
    }

    public boolean enemiesToHomeTo () {
        if (enemyList.isEmpty()) {
            return true;
        } else
            return false;
    }

    public Enemy getClosestEnemy (int xCoordinate, int yCoordinate) {
        Enemy closestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemyList) {
            int enemyXCoordinate = enemy.getCenterXCoordinate();
            int enemyYcoordinate = enemy.getCenterYCoordinate();

            // Compute the distance between player and enemy using Euclidean distance formula
            double distance = Math.sqrt(Math.pow((xCoordinate - enemyXCoordinate), 2)
                    + Math.pow((yCoordinate - enemyYcoordinate), 2));

            // If this enemy is closer than the previous closest enemy, update closestEnemy and
            // minDistance
            if (distance < minDistance) {
                minDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }

    public GameObject getEnemyClosestToGameObject (GameObject gameObject, List<GameObject> objectsToIgnore) {
        GameObject closestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        for (GameObject enemy : enemyList) {
            if (!enemy.equals(gameObject)) {
                if (objectsToIgnore != null && !objectsToIgnore.contains(enemy)) {
                    int enemyXCoordinate = enemy.getCenterXCoordinate();
                    int enemyYcoordinate = enemy.getCenterYCoordinate();

                    // Compute the distance between player and enemy using Euclidean distance formula
                    double distance = Math.sqrt(Math.pow((gameObject.getCenterXCoordinate() - enemyXCoordinate), 2)
                            + Math.pow((gameObject.getCenterYCoordinate() - enemyYcoordinate), 2));

                    // If this enemy is closer than the previous closest enemy, update closestEnemy and
                    // minDistance
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestEnemy = enemy;
                    }
                }
            }
        }

        return closestEnemy;
    }


}