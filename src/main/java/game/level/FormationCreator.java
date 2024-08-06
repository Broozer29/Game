package game.level;

import game.level.enums.SpawnFormationEnums;

public class FormationCreator {

	public EnemyFormation createFormation(SpawnFormationEnums spawnFormation, int widthDistance, int heightDistance) {
		EnemyFormation newFormation = null;
		String[][] pattern = null;
		switch (spawnFormation) {
		case Cross:
			pattern = createPatternFromStrings(
					"X...X",
					".X.X.", 
					"..A..",
					".X.X.", 
					"X...X");
			break;
		case Large_greaterthen:
			pattern = createPatternFromStrings(
					"X......", 
					".X.....", 
					"..X....", 
					"...X...", 
					"....A..",
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
					"..A....",
					"...X...",
					"....X..", 
					".....X.", 
					"......X");
			break;
		case Small_greaterthen:
			pattern = createPatternFromStrings(
					"X....", 
					".X...", 
					"..A..",
					".X...", 
					"X....");
			break;
		case Small_smallerthen:
			pattern = createPatternFromStrings(
					"....X", 
					"...X.", 
					"..A..",
					"...X.", 
					"....X");
			break;
		case Square:
			pattern = createPatternFromStrings(
					"XXXXXXX", 
					"X.....X",
					"X..A..X",
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
					"..A..",
					".X...", 
					"X....");
			break;
		case Reverse_Divide:
			pattern = createPatternFromStrings(
					"X....", 
					".X...",
					"..A..",
					"...X.", 
					"....X");
			break;
		case Line:
			pattern = createPatternFromStrings(
					".....", 
					".....",
					"XXAXX",
					".....", 
					".....");
			break;
		case Reverse_Line:
			pattern = createPatternFromStrings(
					"..X..", 
					"..X..",
					"..A..",
					"..X..", 
					"..X..");
			break;
		case V:
			pattern = createPatternFromStrings(
					"X...........X", 
					"..X...A...X..",
					"....X...X....",
					"......X......");
			break;
		case Reverse_V:
			pattern = createPatternFromStrings(
					"......X......",
			        "....X...X....",
			        "..X...A...X..",
			        "X...........X"
			);
			break;
//		case Dot:
//			pattern = createPatternFromStrings(
//			        "X"
//					);
//			break;
		}
		newFormation = new EnemyFormation(pattern, heightDistance, widthDistance);
		return newFormation;
	}

	public String[][] createPatternFromStrings(String... lines) {
		String[][] pattern = new String[lines.length][];
		for (int i = 0; i < lines.length; i++) {
			pattern[i] = new String[lines[i].length()];
			for (int j = 0; j < lines[i].length(); j++) {
				pattern[i][j] = String.valueOf(lines[i].charAt(j));
			}
		}
		return pattern;
	}
	
}
