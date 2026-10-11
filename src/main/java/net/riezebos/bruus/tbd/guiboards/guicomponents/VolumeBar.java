package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//A volume shown as the game's progress bar, filled to the percentage, with the percentage in menu letters beside it
public class VolumeBar {

    private static final int FILLING_OFFSET = 5; //Where the filling starts inside the bar, in unscaled pixels
    private static final int TEXT_OFFSET = 220; //From the left of the bar to the start of the percentage, in unscaled pixels

    private final ImageDatabase imageDatabase = ImageDatabase.getInstance();
    private final int leftX;
    private final int centerY;
    private final float resolutionFactor;
    private final Picture bar;
    private final Picture filling;
    private GUITextCollection percentageText;

    public VolumeBar (int leftX, int centerY, float resolutionFactor, int percentage) {
        this.leftX = leftX;
        this.centerY = centerY;
        this.resolutionFactor = resolutionFactor;

        BufferedImage barImage = imageDatabase.getImage(ImageEnums.ProgressBar);
        bar = new Picture(ImageEnums.ProgressBar);
        bar.setPicture(Picture.scaled(barImage, scaledSize(barImage.getWidth()), scaledSize(barImage.getHeight())));
        bar.setXCoordinate(leftX);
        bar.setCenterYCoordinate(centerY);

        filling = new Picture(ImageEnums.ProgressBarFilling);
        setPercentage(percentage);
    }

    //Redraws the filling and the text for the new percentage
    public void setPercentage (int percentage) {
        BufferedImage fillingImage = imageDatabase.getImage(ImageEnums.ProgressBarFilling);
        int shownWidth = Math.round(fillingImage.getWidth() * percentage / 100f);
        BufferedImage fillingPicture;
        if (shownWidth <= 0) {
            fillingPicture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); //Nothing to show at 0%
        } else {
            //Cut the image to the percentage instead of squashing it
            BufferedImage cut = fillingImage.getSubimage(0, 0, shownWidth, fillingImage.getHeight());
            fillingPicture = Picture.scaled(cut, scaledSize(shownWidth), scaledSize(fillingImage.getHeight()));
        }
        filling.setPicture(fillingPicture);
        filling.setXCoordinate(leftX + scaledSize(FILLING_OFFSET));
        filling.setCenterYCoordinate(centerY);

        percentageText = MenuText.createText(percentage + "%", leftX + scaledSize(TEXT_OFFSET), centerY, resolutionFactor * MenuText.TEXT_SCALE, MenuText.ALIGN_LEFT);
    }

    private int scaledSize (int size) {
        return Math.round(size * resolutionFactor);
    }

    //The bar, then the filling, then the letters, in the order they are drawn
    public List<GUIComponent> getComponents () {
        List<GUIComponent> components = new ArrayList<>();
        components.add(bar);
        components.add(filling);
        components.addAll(percentageText.getComponents());
        return components;
    }
}
