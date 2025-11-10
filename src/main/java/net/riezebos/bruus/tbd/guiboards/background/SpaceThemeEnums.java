package net.riezebos.bruus.tbd.guiboards.background;


import java.util.Random;

public enum SpaceThemeEnums {

	Blue,
	Green,
	Purple,
	Star_Clear,
	Red;
//	HDNebula;

	private static final Random random = new Random();

	public static SpaceThemeEnums selectRandomSpaceTheme() {
		SpaceThemeEnums[] enums = SpaceThemeEnums.values();
		return enums[random.nextInt(enums.length)];
//		return Star_Clear;
	}

}