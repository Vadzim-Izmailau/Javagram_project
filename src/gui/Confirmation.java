package gui;

import misc.Helper;
import misc.ImagePanel;
import misc.MaxLengthDocumentFilter;
import resources.Images;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

/**
 * Created by i5 on 10.04.2016.
 */
public class Confirmation extends ImagePanel
{
    private JPanel rootPanelSecond;
    private JPanel logoPanel;
    private JTextPane number;
    private JTextPane enter;
    private JPasswordField passField;
    private JButton proceed;
    private JPanel lockIcon;

    public Confirmation() {
        super(Images.getBackground(), true, false, 0);
        passField.setBorder(BorderFactory.createEmptyBorder());
        if (passField.getDocument() instanceof AbstractDocument)
            ((AbstractDocument) passField.getDocument()).setDocumentFilter(new MaxLengthDocumentFilter(5));
        Helper.adjustTextPane(enter);
    }

    public JPanel getRootPanelSecond() {
        return rootPanelSecond;
    }

    public JPasswordField getPassField() {
        return passField;
    }

    public JButton getProceed() {
        return proceed;
    }

    public String getPass() {
        return new String(passField.getPassword());
    }

    public JTextPane getNumber() {
        return number;
    }

    private void createUIComponents() {
        rootPanelSecond = this;
        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
        lockIcon = new ImagePanel(Images.getIconLock(), false, true, 0);
    }

    public void clear() {
        passField.setText("");
        number.setText("");
    }
}
