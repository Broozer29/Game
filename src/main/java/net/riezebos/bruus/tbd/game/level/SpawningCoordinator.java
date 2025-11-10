package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.guiboards.background.BGOEnums;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawningCoordinator {

    private static SpawningCoordinator instance = new SpawningCoordinator();
    private Random random = new Random();

    // Al deze ranges moeten eigenlijk dynamisch berekend worden, want nu is het
    // niet resizable


    private int maximumBGOWidthRange = DataClass.getInstance().getWindowWidth() + 200;
    private int minimumBGOWidthRange = -200;
    private int maximumBGOHeightRange = DataClass.getInstance().getWindowHeight();
    private int minimumBGOHeightRange = 0;


    private int maximumBombEnemyHeightDownRange = 0;
    private int minimumBombEnemyHeightDownRange = -200;

    //Left Spawning block
    private int leftEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() - 100;
    private int leftEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMinHeight();
    private int leftEnemyMaxWidthRange = 500;
    private int leftEnemyMinWidthRange = 250;

    private int bottomLeftEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() - 100;
    private int bottomLeftEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() + 50;

    private int topLeftEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMinHeight();
    private int topLeftEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMinHeight() + 100;

    //Right Spawning block
    private int rightEnemyMaxWidthRange = DataClass.getInstance().getWindowWidth() + 200;
    private int rightEnemyMinWidthRange = DataClass.getInstance().getWindowWidth();
    private int rightEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() - 100;
    private int rightEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMinHeight() + 100;

    private int bottomRightEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() - 100;
    private int bottomRightEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() + 50;


    private int topRightEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMinHeight() + 100;
    private int topRightEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMinHeight() + 200;

    //Up spawning block
    private int upEnemyMaxWidthRange = DataClass.getInstance().getWindowWidth() - 50;
    private int upEnemyMinWidthRange = 100;
    private int upEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMinHeight() + 150;
    private int upEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMinHeight();

    //Down spawning block
    private int downEnemyMaxWidthRange = DataClass.getInstance().getWindowWidth() - 50;
    private int downEnemyMinWidthRange = 50;
    private int downEnemyMaxHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() + 200;
    private int downEnemyMinHeightRange = DataClass.getInstance().getPlayableWindowMaxHeight() + 50;

    private SpawningCoordinator () {

    }

    public static SpawningCoordinator getInstance () {
        return instance;
    }

    //Function used to prevent enemies of the same type of stacking on top of each other when being spawned in
    public boolean checkValidEnemyXCoordinate (List<Enemy> listToCheck, int xCoordinate, int minimumRange) {
        for (Enemy enemy : listToCheck) {
            if (!enemy.getEnemyType().equals(EnemyEnums.Alien_Bomb)) {
                int testX = enemy.getXCoordinate();
                if (Math.abs(testX - xCoordinate) < minimumRange) {
                    return false;
                }
            }
        }
        return true;
    }

    //Function used to prevent enemies of the same type of stacking on top of each other when being spawned in
    public boolean checkValidEnemyYCoordinate (List<Enemy> listToCheck, int yCoordinate, int minimumRange) {
        for (Enemy enemy : listToCheck) {
            if (!enemy.getEnemyType().equals(EnemyEnums.Alien_Bomb)) {
                int testY = enemy.getYCoordinate();
                if (Math.abs(testY - yCoordinate) < minimumRange) {
                    return false;
                }
            }
        }
        return true;
    }


    public int getRightBlockXCoordinate () {
        return random.nextInt((rightEnemyMaxWidthRange - rightEnemyMinWidthRange) + 1) + rightEnemyMinWidthRange;
    }

    public int getRightBlockYCoordinate () {
        return random.nextInt((rightEnemyMaxHeightRange - rightEnemyMinHeightRange) + 1) + rightEnemyMinHeightRange;
    }

    public int getBottomRightBlockYCoordinate () {
        return random.nextInt((bottomRightEnemyMaxHeightRange - bottomRightEnemyMinHeightRange) + 1) + bottomRightEnemyMinHeightRange;
    }

    public int getTopRightBlockYCoordinate () {
        return -random.nextInt((topRightEnemyMaxHeightRange - topRightEnemyMinHeightRange) + 1) + topRightEnemyMinHeightRange;
    }

    public int getLeftBlockXCoordinate () {
        return -(random.nextInt((leftEnemyMaxWidthRange - leftEnemyMinWidthRange) + 1) + leftEnemyMinWidthRange);
    }

    public int getLeftBlockYCoordinate () {
        return random.nextInt((leftEnemyMaxHeightRange - leftEnemyMinHeightRange) + 1) + leftEnemyMinHeightRange;
    }

    public int getBottomLeftBlockYCoordinate () {
        return random.nextInt((bottomLeftEnemyMaxHeightRange - bottomLeftEnemyMinHeightRange) + 1) + bottomLeftEnemyMinHeightRange;
    }

    public int getTopLeftBlockYCoordinate () {
        return 0 - random.nextInt((topLeftEnemyMaxHeightRange - topLeftEnemyMinHeightRange) + 1) + topLeftEnemyMinHeightRange;
    }

    public int getUpBlockXCoordinate () {
        return random.nextInt((upEnemyMaxWidthRange - upEnemyMinWidthRange) + 1) + upEnemyMinWidthRange;
    }

    public int getUpBlockYCoordinate () {
        return 0 - random.nextInt((upEnemyMaxHeightRange - upEnemyMinHeightRange) + 1) + upEnemyMinHeightRange;
    }

    public int getDownBlockXCoordinate () {
        return random.nextInt((downEnemyMaxWidthRange - downEnemyMinWidthRange) + 1) + downEnemyMinWidthRange;
    }

    public int getDownBlockYCoordinate () {
        return random.nextInt((downEnemyMaxHeightRange - downEnemyMinHeightRange) + 1) + downEnemyMinHeightRange;
    }


    public Direction upOrDown () {
        int randInt = random.nextInt((1 - 0) + 1) + 0;
        switch (randInt) {
            case (0):
                return Direction.DOWN;
            case (1):
                return Direction.UP;
        }
        return Direction.UP;
    }

    //Unbelievably computationally expensive this shit, need to rework it to save performance
    public boolean checkValidBGOCoordinates(List<BackgroundObject> listToCheck, BackgroundObject bgObjectToCheck){
        for(BackgroundObject bgObject : listToCheck){
            if(CollisionDetector.getInstance().detectCollision(bgObject, bgObjectToCheck) != null){
                return false;
            }
        }
        return true;
    }

    public int getRandomXBGOCoordinate (BGOEnums bgoType) {
        int randomNumber = 0;
        if(bgoType == BGOEnums.Clouds){
            randomNumber = random.nextInt(((maximumBGOWidthRange * 3) - minimumBGOWidthRange) + 1) + minimumBGOWidthRange;
        } else {
            randomNumber =random.nextInt((maximumBGOWidthRange - minimumBGOWidthRange) + 1) + minimumBGOWidthRange;
        }

        return randomNumber;
    }

    public int getRandomYBGOCoordinate (BGOEnums bgoType) {
        int randomNumber = 0;
        if(bgoType == BGOEnums.Clouds){
            int modifiedMin = - (int) (maximumBGOHeightRange * 0.5); // 25% of the max height shifted negatively
            int modifiedMax = (int) (maximumBGOHeightRange * 0.5);    // 50% of the max height
            randomNumber = random.nextInt((modifiedMax - modifiedMin) + 1) + modifiedMin;
        } else {
            randomNumber = random.nextInt((maximumBGOHeightRange - minimumBGOHeightRange) + 1) + minimumBGOHeightRange;
        }
        return randomNumber;
    }

    public List<Integer> getSpawnCoordinatesByDirection (Direction direction) {
        List<Integer> coordinatesList = new ArrayList<Integer>();
        if (direction.equals(Direction.LEFT)) {
            coordinatesList.add(getRightBlockXCoordinate());
            coordinatesList.add(getRightBlockYCoordinate());
        } else if (direction.equals(Direction.RIGHT)) {
            coordinatesList.add(getLeftBlockXCoordinate());
            coordinatesList.add(getLeftBlockYCoordinate());
        } else if (direction.equals(Direction.DOWN)) {
            coordinatesList.add(getUpBlockXCoordinate());
            coordinatesList.add(getUpBlockYCoordinate());
        } else if (direction.equals(Direction.UP)) {
            coordinatesList.add(getDownBlockXCoordinate());
            coordinatesList.add(getDownBlockYCoordinate());
        } else if (direction.equals(Direction.LEFT_UP)) {
            coordinatesList.add(getRightBlockXCoordinate());
            coordinatesList.add(getBottomRightBlockYCoordinate());
        } else if (direction.equals(Direction.LEFT_DOWN)) {
            coordinatesList.add(getRightBlockXCoordinate());
            coordinatesList.add(getTopRightBlockYCoordinate());
        } else if (direction.equals(Direction.RIGHT_UP)) {
            coordinatesList.add(getLeftBlockXCoordinate());
            coordinatesList.add(getBottomLeftBlockYCoordinate());
        } else if (direction.equals(Direction.RIGHT_DOWN)) {
            coordinatesList.add(getLeftBlockXCoordinate());
            coordinatesList.add(getTopLeftBlockYCoordinate());
        }
        return coordinatesList;
    }

}