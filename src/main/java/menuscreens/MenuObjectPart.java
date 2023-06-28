package menuscreens;

import data.DataClass;
import data.image.ImageEnums;
import visual.objects.Sprite;

public class MenuObjectPart extends Sprite {

	private ImageEnums tileType;

	public MenuObjectPart(ImageEnums tileType, int xCoordinate, int yCoordinate, float scale) {
		super(xCoordinate, yCoordinate, scale);
		this.tileType = tileType;
		initTile();
	}

	private void initTile() {
		loadImage(tileType);
		getImageDimensions();
	}

	public ImageEnums getTileType() {
		return tileType;
	}

	public void setTileType(ImageEnums tileType) {
		this.tileType = tileType;
	}

}