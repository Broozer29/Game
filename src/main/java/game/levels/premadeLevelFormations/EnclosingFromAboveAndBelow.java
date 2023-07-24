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
/*- X.......XX.........X etc etc
* ....X...X....X....X...........
* ......X........X..............
* ..............................
* ..............................
* ......X.........X..............
* ....X...X....X.....X...........
* ..X.......XX..........X etc etc
* .............................
*/



public class EnclosingFromAboveAndBelow implements PregeneratedFormation{
	private List<EnemySpawnTimer> allTimers = new ArrayList<EnemySpawnTimer>();
	private FormationCreator formCreator = new FormationCreator();
	private DataClass dataClass = DataClass.getInstance();

	public EnclosingFromAboveAndBelow(int activationTime, EnemyEnums enemyType, float scale){
		createTimers(activationTime, enemyType, scale);
	}
	
	private void createTimers(int activationTime, EnemyEnums enemyType, float scale) {
		boolean loopable = false;
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;
		
		
		timer =  new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.DOWN, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, 0, -200);
		allTimers.add(timer);

		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.DOWN, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, 550, -200);
		allTimers.add(timer);
		
		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.DOWN, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, 1100, -200);
		allTimers.add(timer);
		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.UP, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, 0, dataClass.getWindowHeight());
		allTimers.add(timer);
		
		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.UP, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, 550, dataClass.getWindowHeight());
		allTimers.add(timer);
		
		timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.UP, scale, 0);
		formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
		timer.setFormation(formation, 1100, dataClass.getWindowHeight());
		allTimers.add(timer);
	}

	@Override
	public List<EnemySpawnTimer> getTimers() {
		return allTimers;
	}

	
}
