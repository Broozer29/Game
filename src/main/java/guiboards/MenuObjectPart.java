package guiboards;

import VisualAndAudioData.image.ImageEnums;
import guiboards.boardEnums.MenuObjectEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.Sprite;
import visualobjects.SpriteAnimation;

public class MenuObjectPart extends Sprite {

	private SpriteAnimation tileAnimation;

	public MenuObjectPart(SpriteConfiguration spriteConfiguration) {
		super(spriteConfiguration);
	}

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
}