package menuscreens;

import Data.DataClass;
import gameObjectes.Sprite;
import javafx.scene.image.Image;

public class MenuTile extends Sprite {

	private DataClass dataClass = DataClass.getInstance();
	private String tileType;

	public MenuTile(String tileType, int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		this.tileType = tileType;
		initTile();
	}

	private void initTile() {
		loadImage(tileType);
		getImageDimensions();
	}

	public void menuTileAction() {
		//Starts the game from the main menu
		if (this.tileType.equals("StartGame")) {
			BoardManager.getInstance().initGame();

		//Opens the user menu from the main menu
		} else if (this.tileType.equals("SelectUserMenu")) {
			BoardManager.getInstance().initUserSelection();

		//Selects the first user
		} else if (tileType.equals("UserOne")) {
			dataClass.setCurrentUser("UserOne");

		//Selects the second user
		} else if (tileType.equals("UserTwo")) {
			dataClass.setCurrentUser("UserTwo");

		//Selects the third user
		} else if (tileType.equals("UserThree")) {
			dataClass.setCurrentUser("UserThree");

		//Changes the user selection board to the main menu board.
		} else if (tileType.equals("userMenuToMainMenu")) {
			BoardManager.getInstance().userSelectionToMainMenu();
		}
	}

	public String getTileType() {
		return tileType;
	}

	public void setTileType(String tileType) {
		this.tileType = tileType;
	}

}
