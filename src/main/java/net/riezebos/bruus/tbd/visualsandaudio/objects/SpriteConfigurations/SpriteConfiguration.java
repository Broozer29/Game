package net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class SpriteConfiguration {

    private int xCoordinate;
    private int yCoordinate;
    private float scale;
    private int xOffset;
    private int yOffset;

    private float transparancyAlpha;
    private boolean increaseTransparancy;
    private float transparancyStepSize;

    private ImageEnums imageType;

    public SpriteConfiguration (int xCoordinate, int yCoordinate, float scale, ImageEnums imageType, int xOffset, int yOffset, float transparancyAlpha, boolean increaseTransparancy, float transparancyStepSize) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.scale = scale;
        this.imageType = imageType;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.transparancyAlpha = transparancyAlpha;
        this.increaseTransparancy = increaseTransparancy;
        this.transparancyStepSize = transparancyStepSize;
    }

    public SpriteConfiguration(){
        this.transparancyAlpha = 1;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    public int getxCoordinate () {
        return xCoordinate;
    }

    public void setxCoordinate (int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate () {
        return yCoordinate;
    }

    public void setyCoordinate (int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public float getScale () {
        return scale;
    }

    public void setScale (float scale) {
        this.scale = scale;
    }

    public int getxOffset () {
        return xOffset;
    }

    public void setxOffset (int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset () {
        return yOffset;
    }

    public void setyOffset (int yOffset) {
        this.yOffset = yOffset;
    }

    public float getTransparancyAlpha () {
        return transparancyAlpha;
    }

    public void setTransparancyAlpha (float transparancyAlpha) {
        this.transparancyAlpha = transparancyAlpha;
    }

    public boolean isIncreaseTransparancy () {
        return increaseTransparancy;
    }

    public void setIncreaseTransparancy (boolean increaseTransparancy) {
        this.increaseTransparancy = increaseTransparancy;
    }

    public float getTransparancyStepSize () {
        return transparancyStepSize;
    }

    public void setTransparancyStepSize (float transparancyStepSize) {
        this.transparancyStepSize = transparancyStepSize;
    }

    public ImageEnums getImageType () {
        return imageType;
    }

    public void setImageType (ImageEnums imageType) {
        this.imageType = imageType;
    }
}
