package gui;

import misc.GhostText;
import misc.GuiHelper;
import misc.ImageButton;
import resources.Fonts;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Created by i5 on 11.05.2016.
 */
public class MainScreen extends JPanel
{
    private JPanel rootPanelFourth;
    private JPanel topPanel;
    private JPanel contactPanel;
    private JPanel messagePanel;
    private JScrollPane messageScroll;
    private JTextArea messageArea;
    private JButton sendMessage;
    private JButton settingButton;
    private JScrollPane contactScroll;
    private JButton searchButton;
    private JTextField searchField;
    private JPanel userPanel;
    private JButton editButton;

    private String meText;
    private BufferedImage mePhoto;
    private String userText;
    private BufferedImage userPhoto;

    {
        contactPanel.add(new JPanel());
        messagePanel.add(new JPanel());
        GuiHelper.decorateScrollPane(messageScroll);
        GhostText ghostText = new GhostText(searchField, "Поиск");
        searchField.setBorder(BorderFactory.createEmptyBorder());
    }

    public JPanel getRootPanelFourth() {
        return rootPanelFourth;
    }

    private void createUIComponents() {
        rootPanelFourth = this;
        topPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 178, 229));
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                int leftMostPoint = settingButton.getX();
                int rightMostPoint = 12;
                if (meText != null) {
                    int inset = 25;
                    Font font = Fonts.getNameFont().deriveFont(Font.BOLD, 30);
                    Color color = Color.white;
                    String text = meText;
                    leftMostPoint = GuiHelper.drawText(g, text, color, font, rightMostPoint, 0,
                            leftMostPoint - rightMostPoint, this.getHeight(), inset, true);
                }
                if (mePhoto != null) {
                    int inset = 2;
                    BufferedImage image = mePhoto;
                    leftMostPoint = GuiHelper.drawImage(g, image, rightMostPoint, 0, leftMostPoint - rightMostPoint,
                            this.getHeight(), inset, true);
                }
            }
        };
        userPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int leftMostPoint = editButton.getX();
                int rightMostPoint = 2;
                if (userPhoto != null) {
                    int inset = 2;
                    BufferedImage image = userPhoto;
                    rightMostPoint = GuiHelper.drawImage(g, image, rightMostPoint, 0, leftMostPoint - rightMostPoint,
                            this.getHeight(), inset, false);
                }
                if (userText != null) {
                    int inset = 10;
                    Font font = Fonts.getNameFont().deriveFont(Font.BOLD, 18);
                    Color color = Color.black;
                    String text = userText;
                    rightMostPoint = GuiHelper.drawText(g, text, color, font, rightMostPoint, 0,
                            leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }
            }
        };
        sendMessage = new ImageButton(Images.getButtonSend());
        settingButton = new ImageButton(Images.getIconSettings());
        searchButton = new ImageButton(Images.getIconSearch());
        editButton = new ImageButton(Images.getIconEdit());
    }

    public Component getContactPanel() {
        return this.contactPanel.getComponent(0);
    }

    public void setContactPanel(Component contactPanel) {
        this.contactPanel.removeAll();
        this.contactPanel.add(contactPanel);
    }

    public Component getMessagePanel() {
        return this.messagePanel.getComponent(0);
    }

    public void setMessagePanel(Component messagePanel) {
        this.messagePanel.removeAll();
        this.messagePanel.add(messagePanel);
    }

    public String getMessageText() {
        return messageArea.getText();
    }

    public void setMessageText(String text) {
        this.messageArea.setText(text);
    }

    public String getSearchText() {
        return this.searchField.getText();
    }

    public JButton getSendMessage() {
        return sendMessage;
    }

    public JButton getSettingButton() {
        return settingButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public String getMeText() {
        return meText;
    }

    public void setMeText(String meText) {
        if(!Objects.equals(this.meText, meText)) {
            this.meText = meText;
            repaint();
        }
    }

    public BufferedImage getMePhoto() {
        return mePhoto;
    }

    public void setMePhoto(BufferedImage mePhoto) {
        this.mePhoto = mePhoto;
        repaint();
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        if(!Objects.equals(this.userText, userText)) {
            this.userText = userText;
            repaint();
        }
    }

    public BufferedImage getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(BufferedImage userPhoto) {
        this.userPhoto = userPhoto;
        repaint();
    }

    public boolean isEditEnabled() {
        return editButton.isEnabled();
    }

    public void setEditEnabled(boolean enabled) {
        editButton.setEnabled(enabled);
    }
}
