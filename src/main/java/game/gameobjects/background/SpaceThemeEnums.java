package game.gameobjects.background;


import java.util.Random;

public enum SpaceThemeEnums {

	Blue, Green, Purple;

	private static final Random random = new Random();

	public static SpaceThemeEnums selectRandomSpaceTheme() {
		SpaceThemeEnums[] enums = SpaceThemeEnums.values();
		return enums[random.nextInt(enums.length)];
	}
}