package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.objects.enemies.enums.EnemyEnums;
import game.spawner.EnemyFormation;
import game.spawner.EnemySpawnTimer;
import game.spawner.FormationCreator;
import game.spawner.enums.SpawnFormationEnums;
import VisualAndAudioData.DataClass;

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

	public EnclosingFromAboveAndBelow(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed){
		createTimers(activationTime, enemyType, scale, xMovementSpeed, yMovementSpeed);
	}
	
	private void createTimers(int activationTime, EnemyEnums enemyType, float scale, int xMovementSpeed, int yMovementSpeed) {
	    boolean loopable = false;
	    EnemySpawnTimer timer = null;
	    EnemyFormation formation = null;

	    // Calculate proportional offsets based on window dimensions
//	    int offsetY = (int) (dataClass.getWindowHeight() * 0.229); // 22.9% of window height (equivalent to -200 when windowHeight = 875)
		int offsetY = 1;
	    int offsetX1 = 0;
	    int offsetX2 = (int) (dataClass.getWindowWidth() * 0.382); // 38.2% of window width (equivalent to 550 when windowWidth = 1440)
	    int offsetX3 = (int) (dataClass.getWindowWidth() * 0.764); // 76.4% of window width (equivalent to 1100 when windowWidth = 1440)

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.DOWN, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, offsetX1, -offsetY);
	    allTimers.add(timer);

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.DOWN, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.V, enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, offsetX2, -offsetY);
	    allTimers.add(timer);
	    
	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.DOWN, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, offsetX3, -offsetY);
	    allTimers.add(timer);

	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.UP, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, offsetX1, dataClass.getWindowHeight());
	    allTimers.add(timer);
	    
	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.UP, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, offsetX2, dataClass.getWindowHeight());
	    allTimers.add(timer);
	    
	    timer = new EnemySpawnTimer(activationTime, 1, enemyType, loopable, Direction.UP, scale,  xMovementSpeed, yMovementSpeed);
	    formation = formCreator.createFormation(SpawnFormationEnums.Reverse_V,  enemyType.getFormationWidthDistance(), enemyType.getFormationHeightDistance());
	    timer.setFormation(formation, offsetX3, dataClass.getWindowHeight());
	    allTimers.add(timer);
	}


	@Override
	public List<EnemySpawnTimer> getTimers() {
		return allTimers;
	}

	
}
