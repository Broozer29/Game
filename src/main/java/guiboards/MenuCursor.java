package guiboards;

import guiboards.boardEnums.MenuFunctionEnums;
import guiboards.boardEnums.MenuObjectEnums;

public class MenuCursor extends MenuObjectCollection {

	private MenuObjectCollection selectedMenuTile;
	private int xDistanceToKeep = 75;

	public MenuCursor(int xCoordinate, int yCoordinate, float scale) {
		super(xCoordinate, yCoordinate, scale, null, MenuObjectEnums.Cursor_Image, MenuFunctionEnums.Menu_Cursor);
	}

	public MenuObjectCollection getSelectedMenuTile() {
		return selectedMenuTile;
	}

	public void setSelectedMenuTile(MenuObjectCollection selectedMenuTile) {
		this.selectedMenuTile = selectedMenuTile;
	}

	public int getxDistanceToKeep() {
		return xDistanceToKeep;
	}

}