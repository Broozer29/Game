package game.UI;

import VisualAndAudioData.image.ImageEnums;
import VisualAndAudioData.image.ImageResizer;
import VisualAndAudioData.image.ImageRotator;
import game.movement.Direction;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.Sprite;

public class UIObject extends Sprite {
	
	public UIObject(SpriteConfiguration spriteConfiguration) {
		super(spriteConfiguration);
	}

	public void resizeToDimensions(int width, int height) {
		if (height > 0 && width > 0) {
			ImageResizer imageResizer = ImageResizer.getInstance();
			this.image = imageResizer.resizeImageToDimensions(this.originalImage, width, height);
			configureImageDimensions();
		}
	}

	public ImageEnums getImageEnum(){
		return this.imageType;
	}

	public void changeImage(ImageEnums newImage){
		this.imageType = newImage;
		this.loadImage(newImage);
	}

	public void rotateImage(Direction direction){
		if(this.imageType == ImageEnums.peepoSkillIssue){
			return;
		}

		if (this.image != null) {
			this.image = ImageRotator.getInstance().rotate(originalImage, direction, false);
			super.recalculateBoundsAndSize();
		}
	}


}