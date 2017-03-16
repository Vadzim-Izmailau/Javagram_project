package overlays;

import misc.GuiHelper;
import misc.ImageButton;
import misc.ImagePanel;
import misc.OverlayBackground;
import resources.Images;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by i5 on 24.05.2016.
 */
public class EditContact extends OverlayBackground
{
    private JPanel rootPanel;
    private JTextPane editPane;
    private JTextField name;
    private JTextPane number;
    private JButton delete;
    private JButton save;
    private JButton back;
    private JPanel photoPanel;
    private int id;

    {
        setContactData(new ContactData());
        setPhoto(null);
        name.setBorder(BorderFactory.createEmptyBorder());
        Color color = new Color(235, 74, 75);
        Border border = BorderFactory.createLineBorder(color);
        delete.setBorder(border);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        rootPanel = this;
        photoPanel = new ImagePanel(null, true, false, 0);
        back = new ImageButton(Images.getIconBack());
        delete = new JButton(){
            @Override
            protected void paintComponent(Graphics g) {
                //super.paintComponent(g);
                g.drawImage(Images.getIconTrash(), 5, 3, null);
                g.drawString("удалить пользователя", 30, 17);
            }
        };
    }

    public void setPhoto(Image photo) {
        ((ImagePanel)photoPanel).setImage(photo);
    }

    public Image getPhoto() {
        return ((ImagePanel)photoPanel).getImage();
    }

    public JButton getBack() {
        return back;
    }

    public JButton getDelete() {
        return delete;
    }

    public JButton getSave() {
        return save;
    }

    public ContactData getContactData() {
        String text = name.getText().trim();
        String[] words = text.split("\\s+");
        return new ContactData(words[0], words[1], number.getText().trim(), id);
    }
    public void setContactData(ContactData data) {
        name.setText(data.getName() + " " + data.getSurname());
        number.setText(data.getNumber());
        id = data.getId();
    }
}
