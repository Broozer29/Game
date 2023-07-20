package menuscreens;

import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class MenuObjectPart extends Sprite {

	private ImageEnums tileType;
	private SpriteAnimation tileAnimation;

	public MenuObjectPart(ImageEnums tileType, int xCoordinate, int yCoordinate, float scale) {
		super(xCoordinate, yCoordinate, scale);
		this.tileType = tileType;
		initTile();
	}

	private void initTile() {
		loadImage(tileType);
		getImageDimensions();
	}
	
	public void setTileAnimation(ImageEnums tileAnimation) {
		if(tileAnimation == ImageEnums.Highlight) {
			this.tileAnimation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Highlight, true, 1);
		}
	}
	
	public SpriteAnimation getAnimation() {
		return this.tileAnimation;
	}

	public ImageEnums getTileType() {
		return tileType;
	}

	public void setTileType(ImageEnums tileType) {
		this.tileType = tileType;
	}

}