package net.riezebos.bruus.tbd.visualsandaudio.objects;

import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.*;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;

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
    protected boolean shouldChangeTransparancy;
    protected float transparancyStepSize;


    //Game logic variables
    protected int xOffset;
    protected int yOffset;
    protected Rectangle bounds;
    protected ImageEnums imageEnum;

    protected SpriteConfiguration spriteConfiguration;

    public Sprite (SpriteConfiguration spriteConfiguration) {
        this.xCoordinate = spriteConfiguration.getxCoordinate();
        this.yCoordinate = spriteConfiguration.getyCoordinate();
        this.scale = spriteConfiguration.getScale();
        this.visible = true;
        this.bounds = new Rectangle();


        this.transparancyAlpha = spriteConfiguration.getTransparancyAlpha();
        this.transparancyStepSize = spriteConfiguration.getTransparancyStepSize();
        this.shouldChangeTransparancy = spriteConfiguration.isIncreaseTransparancy();
        this.imageEnum = spriteConfiguration.getImageType();
        setImage(spriteConfiguration.getImageType());
        this.spriteConfiguration = spriteConfiguration;

    }

    protected void setImage (ImageEnums imageName) {
        this.image = imgDatabase.getImage(imageName);
        this.imageEnum = imageName;
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
        this.image = imageRotator.rotate(image, rotation, true);
        configureImageDimensions();
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

    public void setCenterCoordinates (float newXCoordinate, float newYCoordinate) {
        this.xCoordinate = Math.round(newXCoordinate - (this.width / 2));
        this.yCoordinate = Math.round(newYCoordinate - (this.height / 2));
    }

    public void setCenterYCoordinate(int newYCoordinate){
        this.yCoordinate = newYCoordinate - (this.height / 2);
    }

    public void setCenterXCoordinate(int newXCoordinate){
        this.xCoordinate = newXCoordinate - (this.width / 2);
    }

    public int getWidth () {
        return this.width;
    }

    public int getHeight () {
        return this.height;
    }

    public BufferedImage getImage () {
        if (this.shouldChangeTransparancy &&
                this.transparancyAlpha + this.transparancyStepSize < 1.0f &&
                this.transparancyAlpha + this.transparancyStepSize > 0.0f) {
            this.transparancyAlpha += this.transparancyStepSize;
        }

        if(this.transparancyAlpha <= 0.05f && this.transparancyStepSize < 0.0f) {
            this.setVisible(false);
        }
        return image;
    }

    public int getXCoordinate () {
        return xCoordinate + xOffset;
    }

    public int getYCoordinate () {
        return yCoordinate + yOffset;
    }

    public void setXCoordinate (int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setYCoordinate (int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public boolean isVisible () {
        return visible;
    }

    public void setVisible (boolean visible) {
        this.visible = visible;
    }

    public void addXOffset (int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset () {
        return this.xOffset;
    }

    public int getYOffset(){
        return this.yOffset;
    }

    public void addYOffset (int yoffset) {
        this.yOffset = yoffset;
    }

    public void resetOffset(){
        this.yOffset = 0;
        this.xOffset = 0;
    }

    public void setImageDimensions (int newWidth, int newHeight) {
        if (this.image.getWidth() != newWidth && this.image.getHeight() != newHeight) {
            this.image = imageResizer.resizeImageToDimensions(this.originalImage, newWidth, newHeight);
            configureImageDimensions();
        }
    }

    public void cropWidth (float cropPercentage) {
        ImageCropper imageCropper = ImageCropper.getInstance();
        this.image = imageCropper.cropImage(this.originalImage, cropPercentage);
    }


    public Rectangle getBounds () {
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
        this.shouldChangeTransparancy = shouldIncrease;
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

    protected void recalculateBoundsAndSize () {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.bounds = new Rectangle(xCoordinate, yCoordinate, this.width, this.height);
    }

    public float getScale () {
        return scale;
    }
}