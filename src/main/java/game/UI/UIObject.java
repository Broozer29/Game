package game.UI;

import gamedata.image.ImageEnums;
import gamedata.image.ImageResizer;
import visual.objects.Sprite;

public class UIObject extends Sprite {
	
	public UIObject(int x, int y, float scale, ImageEnums imageType) {
		super(x, y, scale);
		loadImage(imageType);
	}

	public void resizeToDimensions(int width, int height) {
		if (height > 0 && width > 0) {
			ImageResizer imageResizer = ImageResizer.getInstance();
			this.image = imageResizer.resizeImageToDimensions(this.image, width, height);
		}
	}

}