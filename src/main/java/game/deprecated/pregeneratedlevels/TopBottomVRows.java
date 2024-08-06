package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.powerups.timers.DeprecatedEnemySpawnTimer;
import game.level.FormationCreator;
import VisualAndAudioData.DataClass;

//Not to scale, just a representation of the pattern
/*- .X...X...X..................
* .....X...X....................
* .......X......................
* ..............................
* ..............................
* ..............................
* ..............................
*..............................
* .......X......................
* .....X...X....................
* ...X...X...X..................
*/

public class TopBottomVRows implements PregeneratedFormation {

	private List<DeprecatedEnemySpawnTimer> allTimers = new ArrayList<DeprecatedEnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public TopBottomVRows(int activationTime, EnemyEnums enemyType, float scale, EnemyEnums centerEnemyType,
			Direction direction, int xMovementSpeed, int yMovementSpeed) {

//		switch (direction) {
//		case LEFT:
//			createLeftDirectionTimers(activationTime, enemyType, scale, centerEnemyType, xMovementSpeed, yMovementSpeed);
//			break;
//		case RIGHT:
//			createRightDirectionTimers(activationTime, enemyType, scale, centerEnemyType, xMovementSpeed, yMovementSpeed);
//			break;
//		default:
//			createLeftDirectionTimers(activationTime, enemyType, scale, centerEnemyType, xMovementSpeed, yMovementSpeed);
//			break;
//
//		}
		
	}

//	private void createLeftDirectionTimers(int activationTime, EnemyEnums enemyType, float scale, EnemyEnums centerEnemyType, int xMovementSpeed, int yMovementSpeed) {
//	    boolean loopable = false;
//	    DeprecatedEnemySpawnTimer timer = null;
//	    EnemyFormation formation = null;
//
//	    int offsetXLeft = (int) (dataClass.getWindowWidth() * 0.173); // 17.3% of window width (equivalent to 250 when windowWidth = 1440)
//	    int offsetXLeftCenter = (int) (dataClass.getWindowWidth() * 0.361); // 36.1% of window width (equivalent to 520 when windowWidth = 1440)
//	    int offsetYLeft = (int) (dataClass.getWindowHeight() * 0.240); // 24% from bottom edge of window (equivalent to 210 when windowHeight = 875)
//	    int offsetYLeftCenter = (int) (dataClass.getWindowHeight() * 0.137); // 13.7% from bottom edge of window (equivalent to 120 when windowHeight = 875)
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXLeft, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXLeftCenter, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXLeft, dataClass.getWindowHeight() - offsetYLeft);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXLeftCenter, dataClass.getWindowHeight() - offsetYLeftCenter);
//	    allTimers.add(timer);
//	}
//
//
//	private void createRightDirectionTimers(int activationTime, EnemyEnums enemyType, float scale, EnemyEnums centerEnemyType, int xMovementSpeed, int yMovementSpeed) {
//	    boolean loopable = false;
//	    DeprecatedEnemySpawnTimer timer = null;
//	    EnemyFormation formation = null;
//
//	    int offsetXRight = (int) (dataClass.getWindowWidth() * 0.416); // 41.6% of window width (equivalent to 600 when windowWidth = 1440)
//	    int offsetYRight = (int) (dataClass.getWindowHeight() * 0.240); // 24% from bottom edge of window (equivalent to 210 when windowHeight = 875)
//	    int offsetYRightCenter = (int) (dataClass.getWindowHeight() * 0.137); // 13.7% from bottom edge of window (equivalent to 120 when windowHeight = 875)
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetXRight, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetXRight, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetXRight, dataClass.getWindowHeight() - offsetYRight);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetXRight, dataClass.getWindowHeight() - offsetYRightCenter);
//	    allTimers.add(timer);
//	}


	@Override
	public List<DeprecatedEnemySpawnTimer> getTimers() {
		return this.allTimers;
	}

}
