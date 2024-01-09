package game.objects.background;


import java.util.Random;

public enum SpaceThemeEnums {
	Default,
	Three_Random_Planets;

	private static final Random random = new Random();

	public static SpaceThemeEnums selectRandomSpaceTheme() {
		SpaceThemeEnums[] enums = SpaceThemeEnums.values();
		return enums[random.nextInt(enums.length)];
	}
}