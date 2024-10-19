package net.riezebos.bruus.tbd.game.gameobjects.background;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum NebulaThemeEnums {
	Blue_Nebula_1,
	Blue_Nebula_2,
	Blue_Nebula_3,
	Blue_Nebula_4,
	Blue_Nebula_5,
	Blue_Nebula_6,
	Green_Nebula_1,
	Green_Nebula_2,
	Green_Nebula_3,
	Green_Nebula_4,
	Green_Nebula_5,
	Green_Nebula_6,
	Green_Nebula_7,
	Purple_Nebula_1,
	Purple_Nebula_2,
	Purple_Nebula_3,
	Purple_Nebula_4,
	Purple_Nebula_5,
	Purple_Nebula_6,
	Purple_Nebula_7;
//	HDNebula1,
//	HDNebula2,
//	HDNebula3,
//	HDNebula4,
//	HDNebula5;

	private static final Random random = new Random();

	public static NebulaThemeEnums selectRandomNebulaScene() {
		NebulaThemeEnums[] enums = NebulaThemeEnums.values();
		return enums[random.nextInt(enums.length)];
	}

	public static NebulaThemeEnums selectNebulaByColour(SpaceThemeEnums spaceThemeEnums) {
		// Convert the SpaceThemeEnum to a String and prepare it for comparison
		String colour = spaceThemeEnums.name();

		// Filter NebulaThemeEnums to only those that match the specified color
		List<NebulaThemeEnums> matchingEnums = List.of(NebulaThemeEnums.values()).stream()
				.filter(e -> e.name().startsWith(colour))
				.collect(Collectors.toList());

		// Return a random Nebula from the filtered list
		if (!matchingEnums.isEmpty()) {
			return matchingEnums.get(random.nextInt(matchingEnums.size()));
		} else {
			// Fallback or error handling if no matching enums are found
			// This case should not happen unless there is a mismatch in naming conventions
			throw new IllegalStateException("No matching Nebula found for color: " + colour);
		}
	}
}