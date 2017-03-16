package overlays;

import misc.Helper;
import misc.ImageButton;
import misc.ImagePanel;
import misc.OverlayBackground;
import resources.Images;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

/**
 * Created by i5 on 23.05.2016.
 */
public class AddContact extends OverlayBackground
{
    private JPanel rootPanel;
    private JTextPane addPane;
    private JTextPane enter;
    private JFormattedTextField number;
    private JTextField name;
    private JTextField surname;
    private JButton addContact;
    private JButton back;
    private JPanel phoneIcon;

    {
        setContactData(new ContactData());
        number.setBorder(BorderFactory.createEmptyBorder());
        name.setBorder(BorderFactory.createEmptyBorder());
        surname.setBorder(BorderFactory.createEmptyBorder());
        Helper.adjustTextPane(enter);
        try {
            MaskFormatter mask = new MaskFormatter("+*** ** ***-**-**");
            mask.setValidCharacters(" 0123456789");
            mask.setPlaceholderCharacter(' ');
            mask.setOverwriteMode(true);
            number.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        rootPanel = this;
        phoneIcon = new ImagePanel(Images.getIconPhone(), false, true, 0);
        back = new ImageButton(Images.getIconBack());
    }

    public JButton getBack() {
        return back;
    }

    public JButton getAddContact() {
        return addContact;
    }

    private String getPhoneNumber() {
        try {
            number.commitEdit();
            return number.getValue().toString();
        } catch (ParseException e) {
            return null;
        }
    }
    public void setPhoneNumber(String number) {
        this.number.setText(number);
    }

    public ContactData getContactData() {
        return new ContactData(name.getText().trim(), surname.getText().trim(), getPhoneNumber());
    }
    public void setContactData(ContactData data) {
        name.setText(data.getName());
        surname.setText(data.getSurname());
        number.setText(data.getNumber());
    }
}
