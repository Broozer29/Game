package net.riezebos.bruus.tbd.guiboards.guicomponents;

//Menu letters placed by their centre or their left edge
public class MenuText {

    public static final float TEXT_SCALE = 1.6f;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;

    //Menu letters of the given scale, placed so that x is the left edge (ALIGN_LEFT) or the middle (ALIGN_CENTER) and centerY the middle of the letters
    public static GUITextCollection createText (String text, int x, int centerY, float textScale, int align) {
        GUITextCollection collection = new GUITextCollection(0, 0, text);
        collection.setScale(textScale);
        int left = align == ALIGN_CENTER ? x - collection.getWidth() / 2 : x;
        int top = centerY - collection.getHeight() / 2;
        int shiftX = left - collection.getComponents().get(0).getXCoordinate();
        for (GUIComponent letter : collection.getComponents()) {
            letter.setXCoordinate(letter.getXCoordinate() + shiftX);
            letter.setYCoordinate(top);
        }
        return collection;
    }
}
