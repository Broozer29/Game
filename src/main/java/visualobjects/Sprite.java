package visualobjects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.movement.Direction;
import game.objects.missiles.missiletypes.SeekerProjectile;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import VisualAndAudioData.image.ImageCropper;
import VisualAndAudioData.image.ImageDatabase;
import VisualAndAudioData.image.ImageEnums;
import VisualAndAudioData.image.ImageResizer;
import VisualAndAudioData.image.ImageRotator;

public class Sprite {
    protected ImageDatabase imgDatabase = ImageDatabase.getInstance();
    protected ImageRotator imageRotator = ImageRotator.getInstance();
    protected ImageResizer imageResizer = ImageResizer.getInstance();
    protected int xCoordinate;
    protected int yCoordinate;
    protected int width;
    protected int height;
    protected boolean visible;
    protected BufferedImage image;
    protected BufferedImage originalImage;
    protected float scale;

    protected float transparancyAlpha;
    protected boolean increaseTransparancy;
    protected float transparancyStepSize;


    //Game logic variables
    protected int xOffset;
    protected int yOffset;
    protected Rectangle bounds;
    protected ImageEnums imageType;

    protected SpriteConfiguration spriteConfiguration;

    public Sprite (SpriteConfiguration spriteConfiguration) {
        this.xCoordinate = spriteConfiguration.getxCoordinate();
        this.yCoordinate = spriteConfiguration.getyCoordinate();
        this.scale = spriteConfiguration.getScale();
        this.visible = true;
        this.bounds = new Rectangle();


        this.transparancyAlpha = spriteConfiguration.getTransparancyAlpha();
        this.transparancyStepSize = spriteConfiguration.getTransparancyStepSize();
        this.increaseTransparancy = spriteConfiguration.isIncreaseTransparancy();
        this.imageType = spriteConfiguration.getImageType();
        loadImage(spriteConfiguration.getImageType());
        this.spriteConfiguration = spriteConfiguration;

//		this.transparancyAlpha = (float) 1.0;
//		this.increaseTransparancy = false;
//		this.transparancyStepSize = 0;

    }

    protected void loadImage (ImageEnums imageName) {
        this.image = imgDatabase.getImage(imageName);
        if (this.image == null) {
            System.out.println("Crashed because getting " + imageName + " returned an empty/null image");
        }
        if (scale != 1 && this.image != null) {
            this.image = imageResizer.getScaledImage(image, scale);
        }
        this.originalImage = image;
        configureImageDimensions();
//		 Zet collision ook op die getallen en shits & giggles
    }

    protected void configureImageDimensions () {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    // Only used to re-use objects and change the image. Currently only used for
    // background planets.
    protected void setImage (BufferedImage image) {
        this.image = image;
        configureImageDimensions();
    }

    protected void rotateImage (Direction rotation) {
        if (rotation != Direction.LEFT) {
            this.image = imageRotator.rotate(image, rotation);
            configureImageDimensions();
        }
    }

    protected void setScale (float newScale) {
        if (this.scale != newScale) {
            this.scale = newScale;

            if (this.image == null) {
                System.out.println("Crashed because resizing an image that was null/empty");
            }
            this.image = imageResizer.getScaledImage(image, scale);
            configureImageDimensions();
        }

    }

    public int getCenterXCoordinate () {
        return xCoordinate + xOffset + (width / 2);
    }

    public int getCenterYCoordinate () {
        return yCoordinate + yOffset + (height / 2);
    }

    public void setCenterCoordinates (int newXCoordinate, int newYCoordinate) {
        this.xCoordinate = newXCoordinate - (this.width / 2);
        this.yCoordinate = newYCoordinate - (this.height / 2);
    }

    public int getWidth () {
        return this.width;
    }

    public int getHeight () {
        return this.height;
    }

    public BufferedImage getImage () {
        return image;
    }

    public int getXCoordinate () {
        return xCoordinate + xOffset;
    }

    public int getYCoordinate () {
        return yCoordinate + yOffset;
    }

    public void setX (int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setY (int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public boolean isVisible () {
        return visible;
    }

    public void setVisible (Boolean visible) {
        this.visible = visible;
    }

    public void addXOffset (int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset () {
        return this.xOffset;
    }

    public void addYOffset (int yoffset) {
        this.yOffset = yoffset;
    }

    public void setImageDimensions (int newWidth, int newHeight) {
        ImageResizer imageResizer = ImageResizer.getInstance();
        this.image = imageResizer.resizeImageToDimensions(this.originalImage, newWidth, newHeight);
        configureImageDimensions();

    }

    public void cropWidth (float cropPercentage) {
        ImageCropper imageCropper = ImageCropper.getInstance();
        this.image = imageCropper.cropImage(this.originalImage, cropPercentage);
    }


    public Rectangle getBounds () {
        if (this instanceof SpriteAnimation) {
            System.out.println("I returned Sprite bounds for a SpriteAnimation!");
        }
        if (this.bounds.getHeight() == 0 || this.bounds.getWidth() == 0) {
            setBounds(this.xCoordinate + xOffset, this.yCoordinate + yOffset, this.width, this.height);
        }
        return this.bounds;
    }

    protected void setBounds (int xCoordinate, int yCoordinate, int width, int height) {
        this.bounds.setBounds(xCoordinate, yCoordinate, width, height);
    }

    // Sets the new transparancy values, also tells the managers to increase the
    // transparancy or not
    public void setTransparancyAlpha (boolean shouldIncrease, float newAlphaTransparancy, float transparacyStepSize) {
        this.increaseTransparancy = shouldIncrease;
        this.transparancyAlpha = newAlphaTransparancy;
        this.transparancyStepSize = transparacyStepSize;
    }

    public float getTransparancyAlpha () {
        return this.transparancyAlpha;
    }

    public SpriteConfiguration getSpriteConfiguration () {
        return spriteConfiguration;
    }

    public BufferedImage getOriginalImage () {
        return this.originalImage;
    }
}