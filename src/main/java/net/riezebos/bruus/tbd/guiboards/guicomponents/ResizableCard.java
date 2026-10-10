package net.riezebos.bruus.tbd.guiboards.guicomponents;

import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;

//A card that can be made any height without the border getting thicker: the source image is cut into nine pieces
//The corners keep a fixed size, the edges only stretch along their length and the middle stretches both ways
public class ResizableCard extends DisplayOnly {

    private static final int CORNER_SIZE = 80; //In pixels of the source image, large enough to hold the rounded border and corner

    public ResizableCard (SpriteConfiguration spriteConfiguration, int targetWidth, int targetHeight) {
        super(spriteConfiguration);
        //Cut the unscaled image from the database, since CORNER_SIZE is measured in its pixels
        setImage(createNinePieceImage(imgDatabase.getImage(spriteConfiguration.getImageType()), targetWidth, targetHeight));
    }

    private BufferedImage createNinePieceImage (BufferedImage source, int targetWidth, int targetHeight) {
        BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        float scale = (float) targetWidth / sourceWidth; //Same scale for both directions so the corners stay undistorted
        int corner = Math.min(Math.round(CORNER_SIZE * scale), Math.min(targetWidth, targetHeight) / 2);

        int[] sourceX = {0, CORNER_SIZE, sourceWidth - CORNER_SIZE, sourceWidth};
        int[] sourceY = {0, CORNER_SIZE, sourceHeight - CORNER_SIZE, sourceHeight};
        int[] targetX = {0, corner, targetWidth - corner, targetWidth};
        int[] targetY = {0, corner, targetHeight - corner, targetHeight};

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                g.drawImage(source,
                        targetX[column], targetY[row], targetX[column + 1], targetY[row + 1],
                        sourceX[column], sourceY[row], sourceX[column + 1], sourceY[row + 1], null);
            }
        }
        g.dispose();
        return result;
    }
}
