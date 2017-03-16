package gui;

import misc.GhostText;
import misc.Helper;
import misc.ImagePanel;
import resources.Images;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * Created by i5 on 23.04.2016.
 */
public class Registration extends ImagePanel
{
    private JPanel rootPanelThird;
    private JPanel logoPanel;
    private JTextPane enter;
    private JTextField name;
    private JTextField surname;
    private JButton complete;
    private GhostText firstName, lastName;

    public Registration() {
        super(Images.getBackground(), true, false, 0);
        name.setBorder(BorderFactory.createEmptyBorder());
        surname.setBorder(BorderFactory.createEmptyBorder());
        Helper.adjustTextPane(enter);
        firstName = new GhostText(name, "Имя");
        lastName = new GhostText(surname, "Фамилия");
    }

    public JPanel getRootPanelThird() {
        return rootPanelThird;
    }

    public JTextField getFirstName() {
        return name;
    }

    public JTextField getSurname() {
        return surname;
    }

    public JButton getComplete() {
        return complete;
    }

    private void createUIComponents() {
        rootPanelThird = this;
        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
    }
}
