package game.spawner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum SpawnFormationEnums {
	Square, //[]
	Small_smallerthen, //<
	Large_smallerthen, //<
	Small_greaterthen, //> 
	Large_greaterthen, //>
	Cross, //X
	Divide, // /
	Reverse_Divide, // \
	Line, // -
	Reverse_Line,// |
	V, //V
	Reverse_V, // ^ 
	Dot; //.

	public static SpawnFormationEnums getRandomFormation () {
		List<SpawnFormationEnums> values = Arrays.stream(SpawnFormationEnums.values())
				.collect(Collectors.toList());
		Random random = new Random();
		return values.get(random.nextInt(values.size()));
	}
	
}

