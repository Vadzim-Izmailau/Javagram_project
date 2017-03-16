package gui;

import misc.Helper;
import misc.ImagePanel;
import resources.Images;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.ParseException;

/**
 * Created by i5 on 10.04.2016.
 */
public class InputNumber extends ImagePanel
{
    private JPanel rootPanelFirst;
    private JPanel logoPanel;
    private JFormattedTextField number;
    private JButton proceed;
    private JTextPane enterTextPane;
    private JPanel phoneIcon;

    public InputNumber() {
        super(Images.getBackground(), true, false, 0);
        number.setBorder(BorderFactory.createEmptyBorder());
        Helper.adjustTextPane(enterTextPane);
        try {
            MaskFormatter mask = new MaskFormatter("+*** ** ***-**-**");
            mask.setValidCharacters(" 0123456789");
            mask.setPlaceholderCharacter(' ');
            mask.setOverwriteMode(true);
            number.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    public String getFormattedTextValue() {
        try {
            number.commitEdit();
            return number.getValue().toString();
        } catch (ParseException e) {
            return null;
        }
    }

    public JPanel getRootPanelFirst() {
        return rootPanelFirst;
    }

    public JButton getProceed() {
        return proceed;
    }

    public JFormattedTextField getNumber() {
        return number;
    }

    private void createUIComponents() {
        rootPanelFirst = this;
        logoPanel = new ImagePanel(Images.getLogo(), false, true, 0);
        phoneIcon = new ImagePanel(Images.getIconPhone(), false, true, 0);
    }

    public void clear() {
        number.setText("");
        number.setValue("");
    }
}
