package net.riezebos.bruus.tbd.guiboards.background;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum NebulaThemeEnums {
	Blue_Nebula_1(SpaceThemeEnums.Blue),
	Blue_Nebula_2(SpaceThemeEnums.Blue),
	Blue_Nebula_3(SpaceThemeEnums.Blue),
	Blue_Nebula_4(SpaceThemeEnums.Blue),
	Blue_Nebula_5(SpaceThemeEnums.Blue),
	Blue_Nebula_6(SpaceThemeEnums.Blue),
	Blue_Nebula_7(SpaceThemeEnums.Blue),
	Blue_Nebula_8(SpaceThemeEnums.Blue),
	Blue_Nebula_9(SpaceThemeEnums.Blue),
	Green_Nebula_1(SpaceThemeEnums.Green),
	Green_Nebula_2(SpaceThemeEnums.Green),
	Green_Nebula_3(SpaceThemeEnums.Green),
	Green_Nebula_4(SpaceThemeEnums.Green),
	Green_Nebula_5(SpaceThemeEnums.Green),
	Green_Nebula_6(SpaceThemeEnums.Green),
	Green_Nebula_7(SpaceThemeEnums.Green),
	Purple_Nebula_1(SpaceThemeEnums.Purple),
	Purple_Nebula_2(SpaceThemeEnums.Purple),
	Purple_Nebula_3(SpaceThemeEnums.Purple),
	Purple_Nebula_4(SpaceThemeEnums.Purple),
	Purple_Nebula_5(SpaceThemeEnums.Purple),
	Purple_Nebula_6(SpaceThemeEnums.Purple),
	Purple_Nebula_7(SpaceThemeEnums.Purple),
	Purple_Nebula_8(SpaceThemeEnums.Purple),
	Clear_Nebula_1(SpaceThemeEnums.Star_Clear),
	Clear_Nebula_2(SpaceThemeEnums.Star_Clear),
	Clear_Nebula_3(SpaceThemeEnums.Star_Clear),
	Clear_Nebula_4(SpaceThemeEnums.Star_Clear),
	Diverse1(SpaceThemeEnums.Purple),
	RedNebula(SpaceThemeEnums.Red),
	RedNebula2(SpaceThemeEnums.Red),
	RedNebula3(SpaceThemeEnums.Red),
	RedNebula4(SpaceThemeEnums.Red),
	RedNebula5(SpaceThemeEnums.Red);

	SpaceThemeEnums spaceTheme;

	NebulaThemeEnums(SpaceThemeEnums spaceTheme) {
		this.spaceTheme = spaceTheme;
	}

	private static final Random random = new Random();

	public static NebulaThemeEnums selectRandomNebulaScene(SpaceThemeEnums theme) {
		NebulaThemeEnums[] enums = Arrays.stream(NebulaThemeEnums.values()).filter(e -> e.spaceTheme == theme).toArray(NebulaThemeEnums[]::new);
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