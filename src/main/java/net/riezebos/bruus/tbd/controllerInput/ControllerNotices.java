package net.riezebos.bruus.tbd.controllerInput;

import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Short white notices ("CONTROLLER 2 CONNECTED"), drawn by the screens as their last drawing step
public class ControllerNotices {
    private static ControllerNotices instance = new ControllerNotices();
    private static final long NOTICE_DURATION_MILLIS = 3000;
    private final List<String> texts = new ArrayList<>();
    private final List<Long> expiryTimes = new ArrayList<>();

    private ControllerNotices() {
    }

    public static ControllerNotices getInstance() {
        return instance;
    }

    public synchronized void addNotice(String text) {
        texts.add(text);
        expiryTimes.add(System.currentTimeMillis() + NOTICE_DURATION_MILLIS);
    }

    public synchronized void draw(Graphics g) {
        long now = System.currentTimeMillis();
        for (int i = texts.size() - 1; i >= 0; i--) {
            if (expiryTimes.get(i) <= now) {
                texts.remove(i);
                expiryTimes.remove(i);
            }
        }
        if (texts.isEmpty()) {
            return;
        }

        Font oldFont = g.getFont();
        Color oldColor = g.getColor();
        float resolutionFactor = DataClass.getInstance().getResolutionFactor();
        g.setFont(new Font(DataClass.getInstance().getTextFont(), Font.BOLD, Math.round(30 * resolutionFactor)));
        g.setColor(Color.WHITE);
        FontMetrics metrics = g.getFontMetrics();
        int y = metrics.getAscent() + Math.round(10 * resolutionFactor);
        for (String text : texts) {
            int x = (DataClass.getInstance().getWindowWidth() - metrics.stringWidth(text)) / 2;
            g.drawString(text, x, y);
            y += metrics.getHeight();
        }
        g.setFont(oldFont);
        g.setColor(oldColor);
    }
}
