package overlays;

import misc.GuiHelper;
import misc.ImageButton;
import resources.Images;

import javax.swing.*;
import java.awt.*;

/**
 * Created by i5 on 23.06.2016.
 */
public class AddButton extends JPanel
{
    private JPanel rootPanel;
    private JButton addButton;

    private void createUIComponents() {
        rootPanel = this;
        addButton = new ImageButton(Images.getIconPlus());
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        Color color = Color.white;
        g.setColor(GuiHelper.makeTransparent(color, 0.0f));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public JButton getAddButton() {
        return addButton;
    }
}
