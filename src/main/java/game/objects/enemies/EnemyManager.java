package game.objects.enemies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.AnimationManager;
import game.util.CollisionDetector;
import game.objects.player.PlayerManager;
import game.objects.enemies.enemytypes.AlienBomb;
import game.objects.player.spaceship.SpaceShip;
import VisualAndAudioData.DataClass;
import VisualAndAudioData.audio.AudioEnums;
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
        for(Enemy enemy : enemyList){
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
                if (enemy instanceof AlienBomb) {
                    detonateAlienBomb(enemy);
                    spaceship.takeDamage(20);
                } else {
                    spaceship.takeDamage(1);
                }
            }
        }
    }

    private void detonateAlienBomb (Enemy enemy) throws UnsupportedAudioFileException, IOException {
        animationManager.createAndAddUpperAnimation(enemy.getXCoordinate(), enemy.getYCoordinate(),
                enemy.enemyType.getDestructionType(), false, 1);
        audioManager.addAudio(AudioEnums.Alien_Bomb_Impact);
        enemy.setVisible(false);
    }

    private void triggerEnemyActions () {
        for (Enemy enemy : enemyList) {
            enemy.fireAction();
        }
    }

    private void updateEnemies () throws UnsupportedAudioFileException, IOException {
        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).isVisible()) {
                enemyList.get(i).move();
                enemyList.get(i).updateGameObjectEffects();
            } else {
                enemyList.get(i).deleteObject();
                enemyList.remove(i);
                i--;
            }
        }
    }

    public void addEnemy (Enemy enemy) {
        if (enemy != null && !enemyList.contains(enemy)) {
            enemy.onCreationEffects();
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

    public Enemy getClosestEnemy () {
        int playerXCoordinate = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
        int playerYCoordinate = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();

        Enemy closestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemyList) {
            int enemyXCoordinate = enemy.getCenterXCoordinate();
            int enemyYcoordinate = enemy.getCenterYCoordinate();

            // Compute the distance between player and enemy using Euclidean distance formula
            double distance = Math.sqrt(Math.pow((playerXCoordinate - enemyXCoordinate), 2)
                    + Math.pow((playerYCoordinate - enemyYcoordinate), 2));

            // If this enemy is closer than the previous closest enemy, update closestEnemy and
            // minDistance
            if (distance < minDistance) {
                minDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }


}