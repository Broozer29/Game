package menuscreens;

import gamedata.DataClass;
import gamedata.image.ImageEnums;
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
		configureImageDimensions();
	}

	public void menuTileAction() {
		//Starts the game from the main menu
		if (this.tileType.equals(ImageEnums.Start_Game)) {
			BoardManager.getInstance().initGame();
		}
	}

	public ImageEnums getTileType() {
		return tileType;
	}

	public void setTileType(ImageEnums tileType) {
		this.tileType = tileType;
	}

}
