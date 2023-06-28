package menuscreens;

import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import visual.objects.Sprite;

public class MenuObject {

	private List<MenuObjectPart> menuTiles = new ArrayList<MenuObjectPart>();
	private int xCoordinate;
	private int yCoordinate;

	private String text;
	private MenuObjectEnums menuObjectType;
	private MenuFunctionEnums menuFunctionality;
	private ImageEnums imageType;
	private float scale;

	public MenuObject(int xCoordinate, int yCoordinate, float scale, String text, MenuObjectEnums menuObjectType,
			MenuFunctionEnums menuFunctionality) {
		this.setXCoordinate(xCoordinate);
		this.setYCoordinate(yCoordinate);
		this.text = text;
		this.menuObjectType = menuObjectType;
		this.menuFunctionality = menuFunctionality;
		this.scale = scale;
		this.imageType = MenuFunctionImageCoupler.getInstance().getImageByMenuType(menuObjectType);
		initMenuImages();
	}

	private void initMenuImages() {
		if (!(this.menuObjectType == MenuObjectEnums.Text_Block)) {
			MenuObjectPart newTile = new MenuObjectPart(imageType, xCoordinate, xCoordinate, scale);
			this.menuTiles.add(newTile);
		} else if (this.menuObjectType == MenuObjectEnums.Text_Block) {
			initMenuTextBlock();
		}
	}

	public void menuTileAction() {
		// Starts the game from the main menu
		if (this.menuFunctionality.equals(MenuFunctionEnums.Start_Game)) {
			BoardManager.getInstance().initGame();

			// Opens the user menu from the main menu
		} else if (this.menuFunctionality.equals(MenuFunctionEnums.Select_Setup_Menu)) {
			BoardManager.getInstance().initUserSelection();

			// Changes the user selection board to the main menu board.
		} else if (menuFunctionality.equals(MenuFunctionEnums.Return_To_Main_Menu)) {
			BoardManager.getInstance().userSelectionToMainMenu();
		}
	}

	private void initMenuTextBlock() {
		int startingXCoordinate = this.xCoordinate;
		int startingYCoordinate = this.yCoordinate;
		int kernelDistance = (int) Math.ceil(10 * scale);

		for (int i = 0; i < text.length(); i++) {
			char stringChar = text.charAt(i);
			if (stringChar != ' ') {
				ImageEnums imageType = ImageEnums.fromChar(stringChar); // convert char to Letter enum
				MenuObjectPart newTile = new MenuObjectPart(imageType, startingXCoordinate + (kernelDistance * i),
						startingYCoordinate, scale);

				menuTiles.add(newTile);
			}
		}
	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public void setYCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public List<MenuObjectPart> getMenuImages() {
		return menuTiles;
	}

}