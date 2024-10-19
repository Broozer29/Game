package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;

public class EnemyFormation {
	private String[][] formationPattern;
	private int formationWidth;
	private int formationHeight;

	private int heightDistance;
	private int widthDistance;

	public EnemyFormation(String[][] formationPattern, int heightDistance, int widthDistance) {
		this.formationPattern = formationPattern;
		this.formationWidth = formationPattern[0].length;
		this.formationHeight = formationPattern.length;
		this.heightDistance = heightDistance;
		this.widthDistance = widthDistance;
	}

	public String[][] getFormationPattern () {
		return formationPattern;
	}

	public void spawnFormation(int baseX, int baseY, EnemyEnums defaultEnemyType, EnemyEnums escortedType, Direction direction,
							   float xMovementSpeed, float yMovementSpeed) {
		for (int i = 0; i < formationHeight; i++) {
			for (int j = 0; j < formationWidth; j++) {
				char type = formationPattern[i][j].charAt(0);
				if (type != '.') { // Assuming '.' means no enemy
					int spawnX = baseX + (j * widthDistance);
					int spawnY = baseY + (i * heightDistance);

					// Check the type of enemy to spawn based on the character
					EnemyEnums enemyTypeToSpawn = (type == 'A') ? escortedType : defaultEnemyType;

					// Create the new enemy here and add it to the enemies list
					LevelManager.getInstance().spawnEnemy(spawnX, spawnY, enemyTypeToSpawn, direction, enemyTypeToSpawn.getDefaultScale(),
							false, xMovementSpeed, yMovementSpeed, false);
				}
			}
		}
	}

	public int getFormationWidth () {
		return formationWidth;
	}

	public int getFormationHeight () {
		return formationHeight;
	}

	public int getHeightDistance () {
		return heightDistance;
	}

	public int getWidthDistance () {
		return widthDistance;
	}
}
