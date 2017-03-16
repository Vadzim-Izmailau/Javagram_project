package resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by i5 on 25.05.2016.
 */
public class Images
{
    private Images(){

    }

    private static BufferedImage background;
    private static BufferedImage logo;
    private static BufferedImage logoMini;
    private static BufferedImage iconPhone;
    private static BufferedImage iconLock;
    private static BufferedImage iconBack;
    private static BufferedImage iconClose;
    private static BufferedImage iconHide;
    private static BufferedImage iconTrash;
    private static BufferedImage smallUser;
    private static BufferedImage largeUser;
    private static BufferedImage buttonSend;
    private static BufferedImage iconSettings;
    private static BufferedImage iconPlus;
    private static BufferedImage iconSearch;
    private static BufferedImage iconEdit;
    private static BufferedImage blueButton;
    private static Icon warningIcon;
    private static Icon questionIcon;
    private static Icon errorIcon;
    private static Icon infoIcon;

    public synchronized static BufferedImage getBackground() {
        if (background == null) {
            background = getImage("background.png");
        }
        return background;
    }

    public synchronized static BufferedImage getLogo() {
        if (logo == null) {
            logo = getImage("logo.png");
        }
        return logo;
    }

    public synchronized static BufferedImage getLogoMini() {
        if (logoMini == null) {
            logoMini = getImage("logo-mini.png");
        }
        return logoMini;
    }

    public synchronized static BufferedImage getIconPhone() {
        if (iconPhone == null) {
            iconPhone = getImage("icon-phone.png");
        }
        return iconPhone;
    }

    public synchronized static BufferedImage getIconLock() {
        if (iconLock == null) {
            iconLock = getImage("icon-lock.png");
        }
        return iconLock;
    }

    public synchronized static BufferedImage getIconBack() {
        if (iconBack == null) {
            iconBack = getImage("icon-back.png");
        }
        return iconBack;
    }

    public synchronized static BufferedImage getIconClose() {
        if (iconClose == null) {
            iconClose = getImage("icon-close.png");
        }
        return iconClose;
    }

    public synchronized static BufferedImage getIconHide() {
        if (iconHide == null) {
            iconHide = getImage("icon-hide.png");
        }
        return iconHide;
    }

    public synchronized static BufferedImage getIconTrash() {
        if (iconTrash == null) {
            iconTrash = getImage("icon-trash.png");
        }
        return iconTrash;
    }

    public synchronized static BufferedImage getButtonSend() {
        if (buttonSend == null) {
            buttonSend = getImage("button-send.png");
        }
        return buttonSend;
    }

    public synchronized static BufferedImage getIconSettings() {
        if (iconSettings == null) {
            iconSettings = getImage("icon-settings.png");
        }
        return iconSettings;
    }

    public synchronized static BufferedImage getIconPlus() {
        if (iconPlus == null) {
            iconPlus = getImage("icon-plus.png");
        }
        return iconPlus;
    }

    public synchronized static BufferedImage getIconSearch() {
        if (iconSearch == null) {
            iconSearch = getImage("icon-search.png");
        }
        return iconSearch;
    }

    public synchronized static BufferedImage getIconEdit() {
        if (iconEdit == null) {
            iconEdit = getImage("icon-edit.png");
        }
        return iconEdit;
    }

    public synchronized static BufferedImage getSmallUser() {
        if (smallUser == null) {
            smallUser = getImage("User-small.jpg");
        }
        return smallUser;
    }

    public synchronized static BufferedImage getLargeUser() {
        if (largeUser == null) {
            largeUser = getImage("User-icon.png");
        }
        return largeUser;
    }

    public static BufferedImage getBlueButton() {
        if(blueButton == null)
            blueButton = getImage("button-background.png");
        return blueButton;
    }

    public static Icon getWarningIcon() {
        if(warningIcon == null)
            warningIcon = scaleImageToIcon(getImage("warning-icon.png"));
        return warningIcon;
    }

    public static Icon getErrorIcon() {
        if(errorIcon == null)
            errorIcon = scaleImageToIcon(getImage("error-icon.png"));
        return errorIcon;
    }

    public static Icon getQuestionIcon() {
        if(questionIcon == null)
            questionIcon = scaleImageToIcon(getImage("question-icon.png"));
        return questionIcon;
    }

    public static Icon getInfoIcon() {
        if(infoIcon == null)
            infoIcon = scaleImageToIcon(getImage("info-icon.png"));
        return infoIcon;
    }

    public static BufferedImage getUserImage(boolean small) {
        return small ? getSmallUser() : getLargeUser();
    }

    private static BufferedImage getImage(String name) {
        try {
            return ImageIO.read(Images.class.getResource("/img/" + name));
        } catch (Exception e) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = result.createGraphics();
        try {
            g2d.drawImage(image, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        return result;
    }

    private static Icon scaleImageToIcon(BufferedImage image) {
        return new ImageIcon(scaleImage(image, 50, 50));
    }
}
