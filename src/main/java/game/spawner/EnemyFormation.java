package game.spawner;

import game.levels.LevelSpawnerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;

public class EnemyFormation {
	private boolean[][] formationPattern; // Your formation pattern
	private int formationWidth;
	private int formationHeight;
	
	private int heightDistance;
	private int widthDistance;

	public EnemyFormation(boolean[][] formationPattern, int heightDistance, int widthDistance) {
		this.formationPattern = formationPattern;
		this.formationWidth = formationPattern[0].length;
		this.formationHeight = formationPattern.length;
		this.heightDistance = heightDistance;
		this.widthDistance = widthDistance;
	}

	public void spawnFormation(int baseX, int baseY, EnemyEnums timerEnemyType, Direction direction, float scale) {
		for (int i = 0; i < formationHeight; i++) {
			for (int j = 0; j < formationWidth; j++) {
				if (formationPattern[i][j]) { // If there is an enemy at this position in the formation
					int spawnX = baseX + (j * widthDistance);
					int spawnY = baseY + (i * heightDistance);
					
//					System.out.println("Spawning on:" + spawnX + "  " + spawnY);
					// Create the new enemy here and add it to the enemies list
					LevelSpawnerManager.getInstance().spawnEnemy(spawnX, spawnY, timerEnemyType, 1, direction, scale);
				}
			}
		}
	}
}