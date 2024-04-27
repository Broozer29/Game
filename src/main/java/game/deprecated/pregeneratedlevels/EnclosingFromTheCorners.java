package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.objects.enemies.enums.EnemyEnums;
import game.spawner.EnemyFormation;
import game.objects.powerups.timers.DeprecatedEnemySpawnTimer;
import game.spawner.FormationCreator;
import game.spawner.enums.SpawnFormationEnums;
import VisualAndAudioData.DataClass;

//Not to scale, just a representation of the pattern
/*- ...X......................X...
 * ..X........................X..
 * .X..........................X.
 * X............................X
 * ..............................
 * ..............................
 * ..............................
 * X............................X
 * .X..........................X.
 * ..X........................X..
 * ...X......................X...
 */

//Copy behaviour from the Furi level here, so it can be re-used for random selection
public class EnclosingFromTheCorners implements PregeneratedFormation {

	private List<DeprecatedEnemySpawnTimer> allTimers = new ArrayList<DeprecatedEnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public EnclosingFromTheCorners(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
		createTimers(activationTime, enemyType, scale, xMovementSpeed, yMovementSpeed);
	}

	private void createTimers(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
	    boolean loopable = false;
	    DeprecatedEnemySpawnTimer timer = null;
	    EnemyFormation formation = null;

	    // Calculate proportional offsets based on window dimensions
	    int offsetX1 = (int) (dataClass.getWindowWidth() * 0.347); // 34.7% of window width
	    int offsetX2 = (int) (dataClass.getWindowWidth() * 0.138); // 13.8% of window width

	    int offsetY1 = (int) (dataClass.getWindowHeight() * 1.114); // 111.4% of window height
	    int offsetY2 = (int) (dataClass.getWindowHeight() * 0.4); // 40% of window height

	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_UP, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, -offsetX1, offsetY1);
	    allTimers.add(timer);
	    
	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_DOWN, scale, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_Divide, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetX2, -offsetY2);
	    allTimers.add(timer);

	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_DOWN, scale, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Divide, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, -offsetX1, -offsetY2);
	    allTimers.add(timer);

	    timer = new DeprecatedEnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_UP, scale, xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Divide, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, dataClass.getWindowWidth() + offsetX2, offsetY1);
	    allTimers.add(timer);
	}


	@Override
	public List<DeprecatedEnemySpawnTimer> getTimers() {
		return this.allTimers;
	}

}
