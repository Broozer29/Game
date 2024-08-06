package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.powerups.timers.DeprecatedEnemySpawnTimer;
import game.level.FormationCreator;
import VisualAndAudioData.DataClass;

public class EnclosingTopBars implements PregeneratedFormation {

	// Not to scale, just a representation of the pattern
	/*-X...........................X
	 * ..X.......................X...
	 * ....X...................X.....
	 * ..X.......................X...
	 * X...........................X.
	 * ..............................
	 * large gap
	 * ..............................
	 * X............................X
	 * ..X........................X..
	 * ....X....................X....
	 * ..X........................x..
	 * X............................X
	 */
	//"Inverse" reverses the arrow formation

	// Copy behaviour from the Furi level here, so it can be re-used for random selection

	private List<DeprecatedEnemySpawnTimer> allTimers = new ArrayList<DeprecatedEnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public EnclosingTopBars(int activationTime, EnemyEnums enemyType, float scale, boolean inverse,int xMovementSpeed, int yMovementSpeed) {
//		if(inverse) {
//			createInverseTimers(activationTime, enemyType, scale, xMovementSpeed, yMovementSpeed);
//		} else {
//			createRegularTimers(activationTime, enemyType, scale, xMovementSpeed, yMovementSpeed);
//		}
	}

//	private void createInverseTimers(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
//	    boolean loopable = false;
//	    DeprecatedEnemySpawnTimer timer = null;
//	    EnemyFormation formation = null;
//
//	    int offsetYInverse = (int) (dataClass.getWindowHeight() * 0.709); // 62.9% of window height (equivalent to 550 when windowHeight = 875)
//	    int offsetX = (int) (dataClass.getWindowWidth() * 0.208); // 20.8% of window width (equivalent to 300 when windowWidth = 1440)
//	    int offsetXRight = (int) (dataClass.getWindowWidth() * 0.069); // 6.9% of window width (equivalent to 100 when windowWidth = 1440)
//
//	    // Timers going from right to left
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetX, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetX, offsetYInverse);
//	    allTimers.add(timer);
//
//	    // Timers going from left to right
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, offsetYInverse);
//	    allTimers.add(timer);
//	}
//
//	private void createRegularTimers(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
//	    boolean loopable = false;
//	    DeprecatedEnemySpawnTimer timer = null;
//	    EnemyFormation formation = null;
//
//	    int offsetYRegular = (int) (dataClass.getWindowHeight() * 0.709); // 70.9% of window height (equivalent to 620 when windowHeight = 875)
//	    int offsetX = (int) (dataClass.getWindowWidth() * 0.208); // 20.8% of window width (equivalent to 300 when windowWidth = 1440)
//	    int offsetXRight = (int) (dataClass.getWindowWidth() * 0.069); // 6.9% of window width (equivalent to 100 when windowWidth = 1440)
//
//	    // Timers going from right to left
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetX, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetX, offsetYRegular);
//	    allTimers.add(timer);
//
//	    // Timers going from left to right
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, 0);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, offsetYRegular);
//	    allTimers.add(timer);
//	}


	@Override
	public List<DeprecatedEnemySpawnTimer> getTimers() {
		return allTimers;
	}
}
