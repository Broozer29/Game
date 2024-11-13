package net.riezebos.bruus.tbd.game.UI;

import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageResizer;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageRotator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

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
		return this.imageEnum;
	}

	public void changeImage(ImageEnums newImage){
		this.imageEnum = newImage;
		this.loadImage(newImage);
	}

	@Override
	public void rotateImage(Direction direction){
		if(this.imageEnum == ImageEnums.peepoSkillIssue){
			return;
		}

		if (this.image != null) {
			this.image = ImageRotator.getInstance().rotate(originalImage, direction, false);
			super.recalculateBoundsAndSize();
		}
	}


}