package game.objects.enemies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.AnimationManager;
import game.managers.OnScreenTextManager;
import game.objects.GameObject;
import game.util.CollisionDetector;
import game.objects.player.PlayerManager;
import game.objects.player.spaceship.SpaceShip;
import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.AudioManager;

public class EnemyManager {

    private static EnemyManager instance = new EnemyManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private List<Enemy> enemyList = new ArrayList<Enemy>();
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
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        enemyList = new ArrayList<Enemy>();
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
                    spaceship.takeDamage(enemy.getDamage());
                } else {
                    spaceship.takeDamage(1);
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

    private void updateEnemies () throws UnsupportedAudioFileException, IOException {

        Iterator<Enemy> it = enemyList.iterator();
        while (it.hasNext()) {
            Enemy en = it.next();

            if (en.isVisible()) {
                en.move();
                en.updateGameObjectEffects();
            } else {
                en.deleteObject();
                it.remove();
            }
        }
    }

    public void addEnemy (Enemy enemy) {
        if (enemy != null && !enemyList.contains(enemy)) {
            enemyList.add(enemy);
        }
    }

    public List<Enemy> getEnemies () {
        return this.enemyList;
    }

    public int getEnemyCount () {
        return enemyList.size();
    }

    public boolean enemiesToHomeTo () {
        if (enemyList.size() == 0) {
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