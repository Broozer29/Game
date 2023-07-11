package game.levels;

import game.spawner.EnemyFormation;
import game.spawner.SpawnFormationEnums;

public class FormationCreator {

	public EnemyFormation createFormation(SpawnFormationEnums spawnFormation, int widthDistance, int heightDistance) {
		EnemyFormation newFormation = null;
		boolean[][] pattern = null;
		switch (spawnFormation) {
		case Cross:
			pattern = createPatternFromStrings(
					"X...X",
					".X.X.", 
					"..XX.", 
					".X.X.", 
					"X...X");
			break;
		case Large_greaterthen:
			pattern = createPatternFromStrings(
					"X......", 
					".X.....", 
					"..X....", 
					"...X...", 
					"....X..", 
					"...X...",
					"..X....", 
					".X.....", 
					"X......");

			break;
		case Large_smallerthen:
			pattern = createPatternFromStrings("......X", 
					".....X.", 
					"....X..", 
					"...X...", 
					"..X....", 
					"...X...",
					"....X..", 
					".....X.", 
					"......X");
			break;
		case Small_greaterthen:
			pattern = createPatternFromStrings(
					"X....", 
					".X...", 
					"..X..", 
					".X...", 
					"X....");
			break;
		case Small_smallerthen:
			pattern = createPatternFromStrings(
					"....X", 
					"...X.", 
					"..X..",
					"...X.", 
					"....X");
			break;
		case Square:
			pattern = createPatternFromStrings(
					"XXXXXXX", 
					"X.....X",
					"X.....X",
					"X.....X", 
					"XXXXXXX");
			break;
		default:
			// Empty pattern, no enemies.
			pattern = createPatternFromStrings(
					".....", 
					".....",
					".....",
					".....", 
					".....");

			pattern = createPatternFromStrings(
					".......", 
					".......", 
					".......",
					".......", 
					".......", 
					".......",
					".......", 
					".......",
					".......");
			break;
		case Divide:
			pattern = createPatternFromStrings(
					"....X", 
					"...X.",
					"..X..",
					".X...", 
					"X....");
			break;
		case Reverse_Divide:
			pattern = createPatternFromStrings(
					"X....", 
					".X...",
					"..X..",
					"...X.", 
					"....X");
			break;
		}
		newFormation = new EnemyFormation(pattern, widthDistance, heightDistance);
		return newFormation;
	}

	public boolean[][] createPatternFromStrings(String... lines) {
		boolean[][] pattern = new boolean[lines.length][];
		for (int i = 0; i < lines.length; i++) {
			pattern[i] = new boolean[lines[i].length()];
			for (int j = 0; j < lines[i].length(); j++) {
				pattern[i][j] = lines[i].charAt(j) == 'X';
			}
		}
		return pattern;
	}
	
}
