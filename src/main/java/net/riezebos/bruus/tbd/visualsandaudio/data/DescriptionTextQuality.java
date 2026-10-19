package net.riezebos.bruus.tbd.visualsandaudio.data;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

//Draws the description text (item descriptions in the shop and the ARMORY page, the relic choice, the class descriptions) smoother and sharper.
//Not used for the bitmap menu buttons (MenuText), which stay as they were.
public class DescriptionTextQuality {

    //The one switch: true draws the description text with antialiasing on, false sets no hints at all (the drawing from before the crisp text)
    public static final boolean CRISP_DESCRIPTION_TEXT = true;

    private final Object oldAntialiasing;
    private final Object oldTextAntialiasing;

    private DescriptionTextQuality(Object oldAntialiasing, Object oldTextAntialiasing) {
        this.oldAntialiasing = oldAntialiasing;
        this.oldTextAntialiasing = oldTextAntialiasing;
    }

    //Sets the hints (when the switch is on) and returns what restore needs; call restore with the result when the description is drawn
    public static DescriptionTextQuality apply(Graphics2D g) {
        if (!CRISP_DESCRIPTION_TEXT) {
            return new DescriptionTextQuality(null, null);
        }
        DescriptionTextQuality previous = new DescriptionTextQuality(
                g.getRenderingHint(RenderingHints.KEY_ANTIALIASING),
                g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return previous;
    }

    public void restore(Graphics2D g) {
        if (oldAntialiasing != null) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
        }
        if (oldTextAntialiasing != null) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldTextAntialiasing);
        }
    }
}
