package net.riezebos.bruus.tbd.guiboards.boards;

import net.riezebos.bruus.tbd.controllerInput.ControllerNotices;
import net.riezebos.bruus.tbd.controllerInput.MenuAction;
import net.riezebos.bruus.tbd.controllerInput.MenuInput;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.util.OnScreenText;
import net.riezebos.bruus.tbd.game.util.OnScreenTextManager;
import net.riezebos.bruus.tbd.guiboards.TimerHolder;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundManager;
import net.riezebos.bruus.tbd.guiboards.background.BackgroundObject;
import net.riezebos.bruus.tbd.guiboards.guicomponents.GUIComponent;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuCursor;
import net.riezebos.bruus.tbd.guiboards.guicomponents.MenuGrid;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.Sprite;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Base of the menu screens: owns the timer, the frame order, the drawing order, the error handling,
// the drawing helpers and the input loop. A screen fills in its own buttons and behaviour through the hooks.
public abstract class MenuBoard extends JPanel implements TimerHolder {

    protected final BackgroundManager backgroundManager = BackgroundManager.getInstance();
    protected final AnimationManager animationManager = AnimationManager.getInstance();

    protected final List<GUIComponent> offTheGridObjects = new ArrayList<>();
    // Drawn in list order; the default onDirection moves the first one
    protected final List<MenuGrid> grids = new ArrayList<>();
    protected MenuCursor menuCursor;
    // Created here, started by the screen itself
    private final Timer timer;

    protected MenuBoard() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight()));

        timer = new Timer(GameState.getInstance().getDELAY(), e -> repaint(0, 0, DataClass.getInstance().getWindowWidth(), DataClass.getInstance().getWindowHeight() + 5));
    }

    /*------------------------Hooks for the screens--------------------------------*/

    // Activate the selected menu tile
    protected abstract void onConfirm();

    protected void onBack() {
    }

    // Called once after a poll that moved the cursor, confirmed or went back
    protected void onInputHandled() {
    }

    // Moves the first grid; a screen overrides this for its own jumps and calls super for the rest
    protected void onDirection(MenuAction action) {
        if (grids.isEmpty()) {
            return;
        }
        MenuGrid grid = grids.get(0);
        switch (action) {
            case LEFT:
                grid.moveLeft();
                break;
            case RIGHT:
                grid.moveRight();
                break;
            case UP:
                grid.moveUp();
                break;
            case DOWN:
                grid.moveDown();
                break;
        }
    }

    // Drawn right after the grids and before the cursor
    protected void drawScreenContent(Graphics2D g) {
    }

    // Drawn after the on screen texts and before the controller notices
    protected void drawScreenExtras(Graphics2D g) {
    }

    public abstract void recreateWindow();

    /*---------------------------Input-------------------------------*/

    // Acts on what MenuInput reports: keys and the main controller, one set of rules for every menu screen
    private void executeMenuInput() {
        boolean needsUpdate = false;
        for (MenuAction action : MenuInput.getInstance().poll()) {
            switch (action) {
                case LEFT:
                case RIGHT:
                case UP:
                case DOWN:
                    onDirection(action);
                    needsUpdate = true;
                    break;
                case CONFIRM:
                    onConfirm();
                    onInputHandled();
                    return; // selecting can change the screen
                case BACK:
                    onBack();
                    onInputHandled();
                    return; // the screen has changed
            }
        }

        if (needsUpdate) {
            onInputHandled(); // Update the GUI only if there was an action that requires it
        }
    }

    /*---------------------------Drawing methods-------------------------------*/
    @Override
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create(); // Create a copy to avoid modifying the original graphics context

            try {
                // Draws all background objects
                for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
                    drawImage(g2d, bgObject);
                }

                for (SpriteAnimation animation : animationManager.getLowerAnimations()) {
                    drawAnimation(g2d, animation);
                }

                for (GUIComponent component : offTheGridObjects) {
                    if (component != null) {
                        drawGUIComponent(g2d, component);
                    }
                }

                drawGrids(g2d);
                drawScreenContent(g2d);
                drawImage(g2d, menuCursor);

                for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
                    drawAnimation(g2d, animation);
                }

                for (OnScreenText text : OnScreenTextManager.getInstance().getOnScreenTexts()) {
                    drawText(g2d, text);
                }

                drawScreenExtras(g2d);

                ControllerNotices.getInstance().draw(g2d);
            } finally {
                g2d.dispose(); // Ensure resources are released
            }

            animationManager.updateGameTick();
            backgroundManager.updateGameTick();
            Toolkit.getDefaultToolkit().sync();

            executeMenuInput();
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    private void handleError(Exception ex) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("error_log.txt", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);
            pw.println("=== Error at " + java.time.LocalDateTime.now() + " ===");
            ex.printStackTrace(pw);
            pw.println();
            pw.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        System.exit(1);
    }

    private void drawGrids(Graphics2D g) {
        for (MenuGrid grid : grids) {
            for (List<GUIComponent> list : grid.getRows()) {
                for (GUIComponent component : list) {
                    drawGUIComponent(g, component);
                }
            }
        }
    }

    protected void drawGUIComponent(Graphics2D g, GUIComponent component) {
        g.drawImage(component.getImage(), component.getXCoordinate(), component.getYCoordinate(), this);
    }

    protected void drawImage(Graphics g, Sprite sprite) {
        if (sprite.getImage() != null) {
            g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
        }
    }

    protected void drawAnimation(Graphics2D g, SpriteAnimation animation) {
        if (animation.getCurrentFrameImage(false) != null) {
            g.drawImage(animation.getCurrentFrameImage(true), animation.getXCoordinate(), animation.getYCoordinate(), this);
        }
    }

    protected void drawText(Graphics2D g, OnScreenText text) {
        // Ensure that transparency value is within the appropriate bounds.
        float transparency = Math.max(0, Math.min(1, text.getTransparencyValue()));
        Color originalColor = g.getColor(); // store the original color
        Font originalFont = g.getFont();

        // Set the color with the specified transparency.
        Color colorWithTransparency = new Color(
                text.getColor().getRed(),
                text.getColor().getGreen(),
                text.getColor().getBlue(),
                (int) (transparency * 255) // alpha value must be between 0 and 255
        );

        g.setColor(colorWithTransparency);
        g.setFont(new Font("Helvetica", Font.PLAIN, text.getFontSize()));
        // Draw the text at the current coordinates.
        g.drawString(text.getText(), text.getXCoordinate(), text.getYCoordinate());

        // Update the Y coordinate of the text to make it scroll upwards.
        text.setYCoordinate(text.getYCoordinate() - 1);

        // Decrease the transparency for the next draw
        text.setTransparency(transparency - text.getTransparancyStepSize()); // decrease transparency

        g.setColor(originalColor); // restore the original color
        g.setFont(originalFont);
    }

    /*------------------------------End of Drawing methods-------------------------------*/

    @Override
    public Timer getTimer() {
        return this.timer;
    }
}
