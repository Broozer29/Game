package net.riezebos.bruus.tbd.game.UI;

import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageResizer;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageRotator;
import net.riezebos.bruus.tbd.visuals.objects.Sprite;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

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