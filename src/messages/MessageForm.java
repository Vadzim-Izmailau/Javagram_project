package messages;

import javax.swing.*;
import java.awt.*;

/**
 * Created by i5 on 08.06.2016.
 */
public class MessageForm extends JPanel
{
    private JEditorPane textPane = new JEditorPane();
    private JLabel dateLabel = new JLabel();
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    private Color color;
    private final int MARGIN = 5;
    private final int RADIUS = 10;

    public MessageForm(String text, String date, int width, Color color) {
        setLayout(boxLayout);
        textPane.setAlignmentX(0.05f);
        textPane.setForeground(new Color(255, 255, 255));
        add(textPane);
        dateLabel.setAlignmentX(0.0f);
        add(dateLabel);
        textPane.setSize(width, Short.MAX_VALUE);
        textPane.setText(text);
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setMargin(new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
        dateLabel.setText(date);
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRoundRect(textPane.getX(), textPane.getY(), textPane.getWidth(), textPane.getHeight(), RADIUS, RADIUS);
    }
}
