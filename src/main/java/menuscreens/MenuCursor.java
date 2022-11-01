package menuscreens;

import image.objects.Sprite;

public class MenuCursor extends Sprite {

	MenuTile selectedMenuTile;

	public MenuCursor(int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		initCursorImage();
	}

	private void initCursorImage() {
		loadImage("laserbeam");
		getImageDimensions();
	}

	public MenuTile getSelectedMenuTile() {
		return selectedMenuTile;
	}

	public void setSelectedMenuTile(MenuTile selectedMenuTile) {
		this.selectedMenuTile = selectedMenuTile;
	}

}
