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

	public EnclosingSidewaysV(int activationTime, EnemyEnums enemyType, float scale) {
		createTimers(activationTime, enemyType, scale);
	}

	private void createTimers(int activationTime, EnemyEnums enemyType, float scale) {
		boolean loopable = false;
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_UP, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 100);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT_DOWN, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 400);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_UP, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 100);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT_DOWN, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen,
				enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 400);
		allTimers.add(timer);
	}

	@Override
	public List<EnemySpawnTimer> getTimers() {
		return this.allTimers;
	}
}
