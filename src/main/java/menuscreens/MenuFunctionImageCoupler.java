package menuscreens;

import gamedata.image.ImageEnums;
import gamedata.image.ImageLoader;
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
		case Long_Card:
			return ImageEnums.Long_Card;
		case Wide_Card:
			return ImageEnums.Long_Card;
		case Square_Card:
			return ImageEnums.Square_Card;
		case EMP_Icon:
			return ImageEnums.Starcraft2_Electric_Field;
		case Firewall_Icon:
			return ImageEnums.Starcraft2_Firebat_Weapon;
		case Flamethrower_Icon:
			return ImageEnums.Starcraft2_Flame_Turret;
		case Highlight_Animation:
			return ImageEnums.Highlight;
		case Laserbeam_Icon:
			return ImageEnums.Starcraft2_Pulse_Laser;
		case Rocket_Launcher_Icon:
			return ImageEnums.Starcraft2_Dual_Rockets;
		default:
			break;
			
		}

		return null;
	}
}