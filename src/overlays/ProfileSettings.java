package overlays;

import misc.GuiHelper;
import misc.ImageButton;
import misc.OverlayBackground;
import org.javagram.dao.Me;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Images;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by i5 on 22.05.2016.
 */
public class ProfileSettings extends OverlayBackground
{
    private JPanel rootPanel;
    private JPanel bottomPanel;
    private JPanel centralPanel;
    private JTextPane settingsPane;
    private JTextField name;
    private JTextField surname;
    private JButton save;
    private JButton exit;
    private JTextPane number;
    private JButton back;
    private TelegramProxy telegramProxy;

    public ProfileSettings() {
        name.setBorder(BorderFactory.createEmptyBorder());
        surname.setBorder(BorderFactory.createEmptyBorder());
        Color color = new Color(0, 178, 229);
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, color);
        exit.setBorder(border);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JButton getBack() {
        return back;
    }

    public JButton getExit() {
        return exit;
    }

    private void createUIComponents() {
        rootPanel = this;
        back = new ImageButton(Images.getIconBack());
    }

    public TelegramProxy getTelegramProxy() {
        return telegramProxy;
    }

    public void setTelegramProxy(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;
        if (telegramProxy != null) {
            Me me = telegramProxy.getMe();
            name.setText(me.getFirstName());
            surname.setText(me.getLastName());
            number.setText(me.getPhoneNumber());
        } else {
            name.setText("");
            surname.setText("");
            number.setText("");
        }
        repaint();
    }
}
