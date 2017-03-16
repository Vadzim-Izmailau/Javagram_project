package undecorated;

import misc.ImageButton;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by i5 on 04.05.2016.
 */
public class BaseForm extends JPanel
{
    private JPanel rootBasePanel;
    private JPanel topPanel;
    private JButton hide;
    private JButton close;
    private JPanel contentPanel;

    private ComponentMover mover;
    private ComponentResizerAbstract resizer;
    private String title;

    public static final int CLOSE_BUTTON = 1, MINIMIZE_BUTTON = 4;
    public static final int BIND_CLOSE_BUTTON = 16, BIND_MINIMIZE_BUTTON = 64;

    public static final int MINIMIZE_CLOSE_BUTTONS = CLOSE_BUTTON | MINIMIZE_BUTTON;
    public static final int BIND_MINIMIZE_CLOSE_BUTTONS = BIND_CLOSE_BUTTON | BIND_MINIMIZE_BUTTON;

    public static final int FRAME_DEFAULT_RESIZE_POLICY = ComponentResizerAbstract.SIMPLE;
    public static final int DIALOG_DEFAULT_RESIZE_POLICY = -1;

    public static final int FRAME_DEFAULT_BUTTONS= MINIMIZE_CLOSE_BUTTONS | BIND_MINIMIZE_CLOSE_BUTTONS;
    public static final int DIALOG_DEFAULT_BUTTONS = CLOSE_BUTTON | BIND_CLOSE_BUTTON;

    public BaseForm(JFrame window, int resizePolicy, int buttons){
        init(window, resizePolicy, buttons);
        initFrame(window, buttons);
        initContentPane(window);
        initValidation();
    }
    public BaseForm(JFrame window, int resizePolicy) {
        this(window, resizePolicy, FRAME_DEFAULT_BUTTONS);
    }
    public BaseForm(JFrame window) {
        this(window, FRAME_DEFAULT_RESIZE_POLICY);
    }

    public BaseForm(JDialog window, int resizePolicy, int buttons){
        init(window, resizePolicy, buttons);
        initDialog(window);
        initContentPane(window);
        initValidation();
    }
    public BaseForm(JDialog window, int resizePolicy) {
        this(window, resizePolicy, DIALOG_DEFAULT_BUTTONS);
    }
    public BaseForm(JDialog window) {
        this(window, DIALOG_DEFAULT_RESIZE_POLICY);
    }

    public JPanel getRootBasePanel() {
        return rootBasePanel;
    }

    public void setContentPanel(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void createUIComponents() {
        rootBasePanel = this;
        close = new ImageButton(Images.getIconClose());
        hide = new ImageButton(Images.getIconHide());
        topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Font font = getFont();
                graphics.setFont(font);
                FontMetrics fontMetrics = graphics.getFontMetrics();
                int height = fontMetrics.getAscent() + fontMetrics.getDescent();
                int pos = (this.getHeight() + height) / 2;
                int start = 4;
                int end = this.getWidth();
                for(Component component : getComponents()) {
                    if(component.getX() < end)
                        end = component.getX();
                }
                end -= 4;
                String text = title;
                while(fontMetrics.stringWidth(text) > end - start) {
                    int len = text.length() - 4;
                    if(len < 0)
                        return;
                    else
                        text = text.substring(0, len) + "...";
                }
                graphics.setColor(Color.black);
                graphics.setFont(graphics.getFont().deriveFont(Font.ITALIC));
                graphics.drawString(text, start, pos);
            }
        };
    }

    private void init(Window window, int resizePolicy, int buttons) {
        setPreferredSize(null);
        setMinimumSize(null);
        setMaximumSize(null);
        if((buttons & CLOSE_BUTTON) == 0)
            topPanel.remove(close);
        else if((buttons & BIND_CLOSE_BUTTON) != 0)
            addActionListenerForClose(e -> window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)));
        if((buttons & MINIMIZE_BUTTON) == 0)
            topPanel.remove(hide);
        mover = new ComponentMover(window, topPanel);
        if(resizePolicy < 0)
            return;
        resizer = new ComponentResizerAbstract(resizePolicy, window) {
            @Override
            protected int getExtraHeight() {
                return rootBasePanel.getHeight() - contentPanel.getHeight();
            }
            @Override
            protected int getExtraWidth() {
                return rootBasePanel.getWidth() - contentPanel.getWidth();
            }
        };

    }
    private void initFrame(Frame window, int buttons) {
        title = window.getTitle();
        if((buttons & MINIMIZE_BUTTON) != 0 && (buttons &  BIND_MINIMIZE_BUTTON) != 0) {
            addActionListenerForHide(e -> window.setState(Frame.ICONIFIED));
        }
        window.setUndecorated(true);
    }
    private void initDialog(Dialog window) {
        title = window.getTitle();
        window.setUndecorated(true);
    }

    private void initContentPane(RootPaneContainer window) {
        window.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setContentPanel(window.getContentPane());
        window.setContentPane(this);
    }
    private void initValidation() {
        revalidate();
        repaint();
    }

    public void addActionListenerForClose(ActionListener listener) {
        close.addActionListener(listener);
    }
    public void addActionListenerForHide(ActionListener listener) {
        hide.addActionListener(listener);
    }

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, options, initialValue);
    }
    private static int showDialog(JDialog dialog, Object message, int messageType, int optionType, Icon icon,
                                  Object[] options, Object initialValue) {
        JOptionPane optionPane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
        dialog.setModal(true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        new BaseForm(dialog);
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getParent());
        Map<ActionListener, AbstractButton> listeners = new HashMap<>();
        if(options != null) {
            for (Object option : options) {
                if(option instanceof AbstractButton) {
                    AbstractButton abstractButton = (AbstractButton)option;
                    ActionListener actionListener = actionEvent -> optionPane.setValue(option);
                    abstractButton.addActionListener(actionListener);
                    listeners.put(actionListener, abstractButton);
                }
            }
        }
        PropertyChangeListener propertyChangeListener = propertyChangeEvent -> dialog.setVisible(false);
        optionPane.addPropertyChangeListener("value", propertyChangeListener);
        dialog.setVisible(true);
        optionPane.removePropertyChangeListener("value", propertyChangeListener);
        for(Map.Entry<ActionListener, AbstractButton> entry : listeners.entrySet())
            entry.getValue().removeActionListener(entry.getKey());
        Object selectedValue = optionPane.getValue();
        if(selectedValue == null)
            return JOptionPane.CLOSED_OPTION;

        if(options == null) {
            if(selectedValue instanceof Integer)
                return ((Integer)selectedValue);
            else
                return JOptionPane.CLOSED_OPTION;
        }

        for(int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
            if(options[counter].equals(selectedValue))
                return counter;
        }
        return JOptionPane.CLOSED_OPTION;
    }
}
