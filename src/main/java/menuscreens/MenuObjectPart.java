package menuscreens;

import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class MenuObjectPart extends Sprite {

	private SpriteAnimation tileAnimation;

	public MenuObjectPart(SpriteConfiguration spriteConfiguration) {
		super(spriteConfiguration);
	}

//	private void initTile() {
//		loadImage(tileType);
//		configureImageDimensions();
//	}

	public void setTileAnimation(ImageEnums tileAnimation) {
		if(tileAnimation == ImageEnums.Highlight) {
			SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
			spriteConfiguration.setyCoordinate(yCoordinate);
			spriteConfiguration.setxCoordinate(xCoordinate);
			spriteConfiguration.setImageType(ImageEnums.Highlight);
			SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
			this.tileAnimation = new SpriteAnimation(spriteAnimationConfiguration);
		}
	}
	
	public SpriteAnimation getAnimation() {
		return this.tileAnimation;
	}

//	public ImageEnums getTileType() {
//		return tileType;
//	}
//
//	public void setTileType(ImageEnums tileType) {
//		this.tileType = tileType;
//	}

}