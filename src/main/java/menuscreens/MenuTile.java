package menuscreens;

import data.DataClass;
import data.image.enums.ImageEnums;
import visual.objects.Sprite;

public class MenuTile extends Sprite {

	private DataClass dataClass = DataClass.getInstance();
	private ImageEnums tileType;

	public MenuTile(ImageEnums tileType, int xCoordinate, int yCoordinate, float scale) {
		super(xCoordinate, yCoordinate, scale);
		this.tileType = tileType;
		initTile();
	}

	private void initTile() {
		loadImage(tileType);
		getImageDimensions();
	}

	public void menuTileAction() {
		//Starts the game from the main menu
		if (this.tileType.equals(ImageEnums.Start_Game)) {
			BoardManager.getInstance().initGame();

		//Opens the user menu from the main menu
		} else if (this.tileType.equals(ImageEnums.Select_User_Menu)) {
			BoardManager.getInstance().initUserSelection();

		//Changes the user selection board to the main menu board.
		} else if (tileType.equals(ImageEnums.Select_User_Menu)) {
			BoardManager.getInstance().userSelectionToMainMenu();
		}
	}

	public ImageEnums getTileType() {
		return tileType;
	}

	public void setTileType(ImageEnums tileType) {
		this.tileType = tileType;
	}

}
