package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.game.gamestate.shop.ShopFreezeService;
import net.riezebos.bruus.tbd.guiboards.boardcreators.ShopBoardCreator;
import net.riezebos.bruus.tbd.guiboards.guicomponents.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.awt.*;
import java.util.List;

// Everything the shop draws for the freeze: the frost on the items, the thaw, and the Freeze button with its glow. The logic is in ShopFreezeService.
public class ShopFreezeEffect {

    private static final int THAW_CORNERS_HOLD_MS = 125;
    private static final int THAW_CORNERS_TOTAL_MS = 500;
    private static final int THAW_RIM_HOLD_MS = 200;
    private static final int THAW_RIM_TOTAL_MS = ShopFreezeService.THAW_RIM_TOTAL_MS;

    private static ShopFreezeEffect instance = new ShopFreezeEffect();

    private final ShopFreezeService service = ShopFreezeService.getInstance();

    private GUIComponent lastRerollCard;
    private GUIComponent freezeBackgroundCard;
    private GUIComponent freezeButton;
    private GUITextCollection freezeText;

    private ShopFreezeEffect() {

    }

    public static ShopFreezeEffect getInstance() {
        return instance;
    }

    // Puts the Freeze card, button and text on the shop screen; they are built again when the shop screen was built again
    public void addToBoard(ShopBoardCreator creator, GUIComponent rerollCard, List<GUIComponent> firstRow, List<GUIComponent> offTheGridObjects) {
        if (rerollCard != lastRerollCard) {
            lastRerollCard = rerollCard;
            freezeBackgroundCard = creator.createFreezeBackgroundCard(rerollCard);
            freezeButton = creator.createFreezeButton(freezeBackgroundCard);
            freezeText = creator.createFreezeText(freezeBackgroundCard);
        }
        if (!firstRow.contains(freezeButton)) {
            firstRow.add(freezeButton);
        }
        offTheGridObjects.add(freezeBackgroundCard);
        offTheGridObjects.addAll(freezeText.getComponents());
    }

    // Draws the glow behind the Freeze button while the shop is frozen. Returns the alpha the component must be drawn with:
    // the icon is dimmed until the shop is frozen.
    public float drawButtonGlow(Graphics2D g, GUIComponent component) {
        float alpha = component.getTransparancyAlpha();
        if (component != freezeButton) {
            return alpha;
        }
        if (service.isFrozen()) {
            drawFrostGlow(g, component.getXCoordinate(), component.getYCoordinate(), component.getWidth(), component.getHeight(), 6);
            return alpha;
        }
        return alpha * 0.65f;
    }

    // The frost of a frozen Available item, or the thaw of an item that was carried or just unfrozen
    public void drawFreezeEffect(Graphics2D g2d, ShopItem item) {
        service.clearThawIfOver();
        int xCoordinate = item.getXCoordinate();
        int yCoordinate = item.getYCoordinate();
        if (service.isFrozen() && item.getShopItemInformation().isAvailable()) {
            int framePadding = Math.round(3 * DataClass.getInstance().getResolutionFactor());
            drawFrostGlow(g2d, xCoordinate - framePadding, yCoordinate - framePadding,
                    item.getWidth() + framePadding * 2, item.getHeight() + framePadding * 2, 5);
            drawFrostCorners(g2d, xCoordinate, yCoordinate, item.getWidth(), item.getHeight(), 1f, 0);
        } else if (service.getThawStartMillis() != 0 && item.getSlot().isCarried() && item.getShopItemInformation().isAvailable()) {
            drawThaw(g2d, item, Math.round(3 * DataClass.getInstance().getResolutionFactor()));
        }
    }

    // An ice-blue border with a soft glow outside and inside it, around the given rectangle
    private void drawFrostGlow(Graphics2D g, int x, int y, int width, int height, int glowSize) {
        Stroke originalStroke = g.getStroke();
        Color originalColor = g.getColor();
        Shape originalClip = g.getClip();
        Object originalAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Outer glow: wide, faint strokes that get narrower and stronger towards the border
        for (int i = glowSize; i >= 1; i--) {
            g.setStroke(new BasicStroke(i * 2));
            g.setColor(new Color(127, 220, 255, 90 / glowSize));
            g.drawRoundRect(x - 1, y - 1, width + 2, height + 2, 8, 8);
        }

        //Inner glow, kept inside the rectangle
        g.clipRect(x, y, width, height);
        for (int i = glowSize / 2; i >= 1; i--) {
            g.setStroke(new BasicStroke(i * 2));
            g.setColor(new Color(127, 220, 255, 70 / glowSize));
            g.drawRoundRect(x, y, width, height, 8, 8);
        }
        g.setClip(originalClip);

        //The border
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(127, 220, 255));
        g.drawRoundRect(x, y, width, height, 8, 8);

        g.setStroke(originalStroke);
        g.setColor(originalColor);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, originalAntialiasing);
    }

    // Thin corner lines on the icon box, outside the frost rim: an L per corner, moved `travel` px outward diagonally
    private void drawFrostCorners(Graphics2D g, int x, int y, int width, int height, float alpha, int travel) {
        float factor = DataClass.getInstance().getResolutionFactor();
        float scale = width / 64f;
        int length = Math.round(width * 0.26f);
        int thickness = Math.max(1, Math.round(scale));
        //The rim sits framePadding outside the icon and is 2 px wide (1 px outside its centre); the lines stand 2 px (scaled) beyond that
        int offset = Math.round(3 * factor) + 1 + Math.round(2 * scale) + thickness;
        int glow = Math.max(2, Math.round(3 * scale));

        Composite originalComposite = g.getComposite();
        Color originalColor = g.getColor();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, Math.min(1f, alpha))));
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                int cornerX = (dx < 0 ? x - offset : x + width + offset) + dx * travel;
                int cornerY = (dy < 0 ? y - offset : y + height + offset) + dy * travel;
                int horizontalX = dx < 0 ? cornerX : cornerX - length;
                int horizontalY = dy < 0 ? cornerY : cornerY - thickness;
                int verticalX = dx < 0 ? cornerX : cornerX - thickness;
                int verticalY = dy < 0 ? cornerY : cornerY - length;
                g.setColor(new Color(127, 220, 255, 45));
                for (int i = glow; i >= 1; i--) {
                    g.fillRect(horizontalX - i, horizontalY - i, length + i * 2, thickness + i * 2);
                    g.fillRect(verticalX - i, verticalY - i, thickness + i * 2, length + i * 2);
                }
                g.setColor(new Color(230, 248, 255));
                g.fillRect(horizontalX, horizontalY, length, thickness);
                g.fillRect(verticalX, verticalY, thickness, length);
            }
        }
        g.setComposite(originalComposite);
        g.setColor(originalColor);
    }

    // The break thaw of a carried or just-unfrozen item: the frost holds, the corner lines fly outward and fade, then the rim fades
    private void drawThaw(Graphics2D g, ShopItem object, int framePadding) {
        long elapsed = System.currentTimeMillis() - service.getThawStartMillis();
        int xCoordinate = object.getXCoordinate();
        int yCoordinate = object.getYCoordinate();

        if (elapsed < THAW_RIM_TOTAL_MS) {
            float rimProgress = Math.max(0f, Math.min(1f, (elapsed - THAW_RIM_HOLD_MS) / (float) (THAW_RIM_TOTAL_MS - THAW_RIM_HOLD_MS)));
            float rimAlpha = 1f - rimProgress * rimProgress; //Ease in: slow at first
            Composite originalComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rimAlpha));
            drawFrostGlow(g, xCoordinate - framePadding, yCoordinate - framePadding,
                    object.getWidth() + framePadding * 2, object.getHeight() + framePadding * 2, 5);
            g.setComposite(originalComposite);
        }
        if (elapsed < THAW_CORNERS_TOTAL_MS) {
            float progress = Math.max(0f, Math.min(1f, (elapsed - THAW_CORNERS_HOLD_MS) / (float) (THAW_CORNERS_TOTAL_MS - THAW_CORNERS_HOLD_MS)));
            float eased = 1f - (1f - progress) * (1f - progress); //Ease out: fast at first
            int travel = Math.round(9 * DataClass.getInstance().getResolutionFactor() * eased);
            drawFrostCorners(g, xCoordinate, yCoordinate, object.getWidth(), object.getHeight(), 1f - eased, travel);
        }
    }
}
