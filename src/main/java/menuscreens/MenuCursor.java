package menuscreens;

import gamedata.image.ImageEnums;

public class MenuCursor extends MenuObject {

	private MenuObject selectedMenuTile;
	private int xDistanceToKeep = 100;

	public MenuCursor(int xCoordinate, int yCoordinate, float scale) {
		super(yCoordinate, yCoordinate, scale, null, MenuObjectEnums.Cursor_Image, MenuFunctionEnums.Menu_Cursor);
	}

	public MenuObject getSelectedMenuTile() {
		return selectedMenuTile;
	}

	public void setSelectedMenuTile(MenuObject selectedMenuTile) {
		this.selectedMenuTile = selectedMenuTile;
	}

	public int getxDistanceToKeep() {
		return xDistanceToKeep;
	}

}