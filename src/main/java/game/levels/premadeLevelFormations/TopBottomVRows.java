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

	private List<EnemySpawnTimer> allTimers = new ArrayList<EnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public TopBottomVRows(int activationTime, EnemyEnums enemyType, float scale, EnemyEnums centerEnemyType,
			Direction direction) {

		switch (direction) {
		case LEFT:
			createLeftDirectionTimers(activationTime, enemyType, scale, centerEnemyType);
			break;
		case RIGHT:
			createRightDirectionTimers(activationTime, enemyType, scale, centerEnemyType);
			break;
		default:
			createLeftDirectionTimers(activationTime, enemyType, scale, centerEnemyType);
			break;

		}
		
	}

	private void createLeftDirectionTimers(int activationTime, EnemyEnums enemyType, float scale,
			EnemyEnums centerEnemyType) {
		boolean loopable = false;
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 250, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 520, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() - 210);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.LEFT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, dataClass.getWindowWidth() + 520, dataClass.getWindowHeight() - 120);
		allTimers.add(timer);
	}
	
	
	private void createRightDirectionTimers(int activationTime, EnemyEnums enemyType, float scale,
			EnemyEnums centerEnemyType) {
		boolean loopable = false;
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -600, 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -600 , 0);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -600, dataClass.getWindowHeight() - 210);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, centerEnemyType, loopable, Direction.RIGHT, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, enemyType.getFormationWidthDistance(),
				enemyType.getFormationHeightDistance());
		timer.setFormation(formation, -600, dataClass.getWindowHeight() - 120);
		allTimers.add(timer);

	}


	@Override
	public List<EnemySpawnTimer> getTimers() {
		return this.allTimers;
	}

}
