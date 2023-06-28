package menuscreens;

import data.image.ImageEnums;
import data.image.ImageLoader;
import visual.objects.Sprite;

public class MenuFunctionImageCoupler {

	private static MenuFunctionImageCoupler instance = new MenuFunctionImageCoupler();

	private MenuFunctionImageCoupler() {

	}

	public static MenuFunctionImageCoupler getInstance() {
		return instance;
	}

	public ImageEnums getImageByMenuType(MenuObjectEnums menuFunction) {
		switch (menuFunction) {
		case Cursor_Image:
			return ImageEnums.Player_Spaceship_Model_3;
		case Text_Block:
			break;
		case Title_Image:
			return ImageEnums.Title_Image;
		}

		return null;
	}
}