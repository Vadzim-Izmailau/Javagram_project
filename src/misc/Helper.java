package misc;

import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Images;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.image.BufferedImage;

/**
 * Created by i5 on 18.10.2016.
 */
public class Helper
{
    private Helper() {}

    public static void adjustTextPane(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public static BufferedImage getPhoto(TelegramProxy telegramProxy, Person person, boolean small) {
        BufferedImage image;
        try {
            image = telegramProxy.getPhoto(person, small);
        } catch (Exception e) {
            e.printStackTrace();
            image = null;
        }
        if(image == null)
            image = Images.getUserImage(small);
        return image;
    }

    public static BufferedImage getPhoto(TelegramProxy telegramProxy, Person person, boolean small, boolean circle) {
        BufferedImage photo = getPhoto(telegramProxy, person, small);
        if(circle)
            photo = GuiHelper.makeCircle(photo);
        return photo;
    }
}
