package overlays;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by i5 on 18.06.2016.
 */
public class MyOverlayDialog extends JPanel
{
    private Component background;
    private Component[] foregrounds;
    private int index;
    private OverlayLayout overlayLayout;
    private BufferedImage image;

    {
        overlayLayout = new OverlayLayout(this);
        setLayout(overlayLayout);
    }

    private JPanel fakeBackground = new JPanel(){
        @Override
        public void paint(Graphics g) {
            //super.paint(g);
            if (image == null || image.getWidth() != background.getWidth()
                    || image.getHeight() != background.getHeight()) {
                image = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2D = image.createGraphics();
                try {
                    background.paint(g2D);
                } finally {
                    g2D.dispose();
                }
            }
            g.drawImage(image, 0, 0, null);
        }
    };

    public MyOverlayDialog(Component background, Component... foregrounds) {
        this.foregrounds = Arrays.copyOf(foregrounds, foregrounds.length);
        for (Component foreground : foregrounds) {
            foreground.setVisible(false);
            add(foreground);
        }
        fakeBackground.setVisible(false);
        add(fakeBackground);
        this.background = background;
        this.background.setVisible(true);
        add(background);
        index = -1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (index < 0)
            index = -1;
        else
            foregrounds[index] = foregrounds[index];

        if (this.index != index) {
            if (this.index >= 0 && index >= 0) {
                foregrounds[this.index].setVisible(false);
                foregrounds[index].setVisible(true);
                this.index = index;
            } else if (this.index >= 0) {
                foregrounds[this.index].setVisible(false);
                fakeBackground.setVisible(false);
                background.setVisible(true);
                this.index = -1;
                image = null;
            } else if (index >= 0) {
                image = null;
                foregrounds[index].setVisible(true);
                fakeBackground.setVisible(true);
                this.background.setVisible(false);
                this.index = index;
            }
        }
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }
}
