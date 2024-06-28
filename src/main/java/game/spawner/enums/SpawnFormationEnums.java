package game.spawner.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum SpawnFormationEnums {
	Square(21), //[]
	Small_smallerthen(5), //<
	Large_smallerthen(8), //<
	Small_greaterthen(5), //>
	Large_greaterthen(8), //>
	Cross(9), //X
	Divide(5), // /
	Reverse_Divide(5), // \
	Line(5), // -
	Reverse_Line(5),// |
	V(8), //V
	Reverse_V(8); // ^
//	Dot; //.

	private int enemyCountInFormation;

	SpawnFormationEnums(int enemyCountInFormation){
		this.enemyCountInFormation = enemyCountInFormation;
	}

	public int getEnemyCountInFormation(){
		return this.enemyCountInFormation;
	}
	public static SpawnFormationEnums getRandomFormation () {
		List<SpawnFormationEnums> values = Arrays.stream(SpawnFormationEnums.values())
				.collect(Collectors.toList());
		Random random = new Random();
		return values.get(random.nextInt(values.size()));
	}
	
}

