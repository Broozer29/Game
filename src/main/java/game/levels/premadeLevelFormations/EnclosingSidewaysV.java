package game.levels.premadeLevelFormations;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.objects.enemies.EnemyEnums;
import game.spawner.EnemyFormation;
import game.spawner.EnemySpawnTimer;
import game.spawner.FormationCreator;
import game.spawner.SpawnFormationEnums;
import gamedata.DataClass;

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

	private List<EnemySpawnTimer> allTimers = new ArrayList<EnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public EnclosingSidewaysV(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
		createTimers(activationTime, enemyType, scale, xMovementSpeed, yMovementSpeed);
	}

	private void createTimers(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
	    boolean loopable = false;
	    EnemySpawnTimer timer = null;
	    EnemyFormation formation = null;

	    // Calculate proportional offsets based on window dimensions
	    int offsetY1 = (int) (dataClass.getWindowHeight() * 0.094); // 11.4% of window height (equivalent to -100 when windowHeight = 875)
	    int offsetY2 = (int) (dataClass.getWindowHeight() * 0.457); // 45.7% of window height (equivalent to -400 when windowHeight = 875)

	    int offsetXRight = (int) (dataClass.getWindowWidth() * 0.174); // 17.4% of window width (equivalent to +250 when windowWidth = 1440)
	    int offsetXLeft = (int) (dataClass.getWindowWidth() * 0.382); // 38.2% of window width (equivalent to -550 when windowWidth = 1440)

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_UP, scale, 0, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, dataClass.getWindowHeight() / 2 - offsetY1);
	    allTimers.add(timer);

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_DOWN, scale, 0, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetXRight, dataClass.getWindowHeight() / 2 - offsetY2);
	    allTimers.add(timer);

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_UP, scale, 0, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, -offsetXLeft, dataClass.getWindowHeight() / 2 - offsetY1);
	    allTimers.add(timer);

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_DOWN, scale, 0, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen,
	            enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, -offsetXLeft, dataClass.getWindowHeight() / 2 - offsetY2);
	    allTimers.add(timer);
	}


	@Override
	public List<EnemySpawnTimer> getTimers() {
		return this.allTimers;
	}
}