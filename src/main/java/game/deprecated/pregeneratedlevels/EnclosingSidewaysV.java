package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.powerups.timers.DeprecatedEnemySpawnTimer;
import game.spawner.FormationCreator;
import VisualAndAudioData.DataClass;

public class EnclosingSidewaysV implements PregeneratedFormation {
	// Not to scale, just a representation of the pattern
	/*- .............................
	 * ..X........................X..
	 * .X..........................X.
	 * X............................X
	 * .X..........................X.
	 * ..X........................X..
	 * ..X........................X..
	 * .X..........................X.
	 * X............................X
	 * .X..........................X.
	 * ..X........................X..
	 */

	// Copy behaviour from the Furi level here, so it can be re-used for random selection

	private List<DeprecatedEnemySpawnTimer> allTimers = new ArrayList<DeprecatedEnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public EnclosingSidewaysV(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
//		createTimers(activationTime, enemyType, scale, xMovementSpeed, yMovementSpeed);
	}

//	private void createTimers(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
//	    boolean loopable = false;
//	    DeprecatedEnemySpawnTimer timer = null;
//	    EnemyFormation formation = null;
//
//	    // Calculate proportional offsets based on window dimensions
//	    int offsetY1 = (int) (dataClass.getWindowHeight() * 0.094); // 11.4% of window height (equivalent to -100 when windowHeight = 875)
//	    int offsetY2 = (int) (dataClass.getWindowHeight() * 0.457); // 45.7% of window height (equivalent to -400 when windowHeight = 875)
//
//	    int offsetXRight = (int) (dataClass.getWindowWidth() * 0.174); // 17.4% of window width (equivalent to +250 when windowWidth = 1440)
//	    int offsetXLeft = (int) (dataClass.getWindowWidth() * 0.382); // 38.2% of window width (equivalent to -550 when windowWidth = 1440)
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_UP, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, dataClass.getWindowHeight() / 2 - offsetY1);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_DOWN, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, dataClass.getWindowHeight() / 2 - offsetY2);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_UP, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetXLeft, dataClass.getWindowHeight() / 2 - offsetY1);
//	    allTimers.add(timer);
//
//	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_DOWN, scale,  xMovementSpeed, yMovementSpeed);
//	    formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen,
//	            enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
//	    timer.setFormation(formation, -offsetXLeft, dataClass.getWindowHeight() / 2 - offsetY2);
//	    allTimers.add(timer);
//	}


	@Override
	public List<DeprecatedEnemySpawnTimer> getTimers() {
		return this.allTimers;
	}
}
