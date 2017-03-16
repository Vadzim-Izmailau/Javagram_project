package misc;

import javax.swing.*;
import java.awt.*;

/**
 * Created by i5 on 20.10.2016.
 */
public class OverlayBackground extends JPanel
{
    private Color overlayColor;

    public OverlayBackground(Color overlayColor) {
        super();
        this.overlayColor = overlayColor;
    }

    public OverlayBackground() {
        this(GuiHelper.makeTransparent(Color.black, 0.7f));
    }

    public Color getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(Color overlayColor) {
        if(this.overlayColor == overlayColor) {
            this.overlayColor = overlayColor;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(overlayColor != null) {
            g.setColor(overlayColor);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }
}
