package game.UI;

import VisualAndAudioData.image.ImageResizer;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.Sprite;

public class UIObject extends Sprite {
	
	public UIObject(SpriteConfiguration spriteConfiguration) {
		super(spriteConfiguration);
	}

	public void resizeToDimensions(int width, int height) {
		if (height > 0 && width > 0) {
			ImageResizer imageResizer = ImageResizer.getInstance();
			this.image = imageResizer.resizeImageToDimensions(this.image, width, height);
		}
	}

}