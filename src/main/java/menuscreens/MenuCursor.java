package menuscreens;

import data.image.enums.ImageEnums;
import visual.objects.Sprite;

public class MenuCursor extends Sprite {

	MenuTile selectedMenuTile;

	public MenuCursor(int xCoordinate, int yCoordinate, float scale) {
		super(xCoordinate, yCoordinate,  scale);
		initCursorImage();
	}

	private void initCursorImage() {
		loadImage(ImageEnums.Player_Spaceship_Model_3);
		getImageDimensions();
	}

	public MenuTile getSelectedMenuTile() {
		return selectedMenuTile;
	}

	public void setSelectedMenuTile(MenuTile selectedMenuTile) {
		this.selectedMenuTile = selectedMenuTile;
	}

}
