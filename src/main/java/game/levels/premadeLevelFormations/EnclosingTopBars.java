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

	private List<EnemySpawnTimer> allTimers = new ArrayList<EnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public EnclosingTopBars(int activationTime, EnemyEnums enemyType, float scale, boolean inverse) {
		if(inverse) {
			createInverseTimers(activationTime, enemyType, scale);
		} else {
			createRegularTimers(activationTime, enemyType, scale);
		}
	}

	private void createInverseTimers(int activationTime, EnemyEnums enemyType, float scale) {
		boolean loopable = false;
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -300, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -300, 550);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 100, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 100, 550);
		allTimers.add(timer);
		
	}

	private void createRegularTimers(int activationTime, EnemyEnums enemyType, float scale) {
		boolean loopable = false;
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -300, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_greaterthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -300, 620);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 100, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 100, 620);
		allTimers.add(timer);
	}

	@Override
	public List<EnemySpawnTimer> getTimers() {
		return allTimers;
	}
}
