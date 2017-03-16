package contacts;

import misc.Helper;
import misc.PhotoPanel;
import org.javagram.dao.Dialog;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import java.awt.*;

/**
 * Created by i5 on 13.05.2016.
 */
public class ContactMapping extends JPanel implements ListCellRenderer<Person>
{
    private JPanel rootPanelContact;
    private JPanel photoPanel;
    private JTextPane name;
    private JTextPane lastMessage;
    private TelegramProxy telegramProxy;
    private boolean hasFocus;

    private final int focusMarkerWidth = 4;

    public ContactMapping(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;
    }

    private void createUIComponents() {
        rootPanelContact = this;
        photoPanel = new PhotoPanel(null, true, false, 0, false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.lightGray);
        g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        if (hasFocus) {
            g.setColor(new Color(0, 178, 229));
            g.fillRect(this.getWidth() - focusMarkerWidth, 0, focusMarkerWidth, this.getHeight());
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Person> list,
                                                  Person person,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        Dialog dialog = telegramProxy.getDialog(person);
        this.name.setText(person.getFirstName() + " " + person.getLastName());

        if (dialog != null)
            this.lastMessage.setText(dialog.getLastMessage().getText());
        else
            this.lastMessage.setText("");

        if (isSelected)
            setBackground(Color.white);
        else
            setBackground(Color.lightGray);

        this.hasFocus = cellHasFocus;
        ((PhotoPanel)photoPanel).setImage(Helper.getPhoto(telegramProxy, person, true, true));
        ((PhotoPanel)photoPanel).setOnline(telegramProxy.isOnline(person));
        setPreferredSize(new Dimension(0, 80));
        return this;
    }
}
