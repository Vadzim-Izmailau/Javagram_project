package misc;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by i5 on 23.06.2016.
 */
public class GhostText implements FocusListener, DocumentListener, PropertyChangeListener
{
    private final JTextComponent textComponent;
    private final String ghostText;
    private boolean isEmpty;
    private Color ghostColor;
    private Color foregroundColor;

    public GhostText(final JTextComponent textComponent, String ghostText) {
        super();
        this.textComponent = textComponent;
        this.ghostText = ghostText;
        this.ghostColor = Color.LIGHT_GRAY;
        textComponent.addFocusListener(this);
        registerListeners();
        updateState();
        if (!this.textComponent.hasFocus())
            focusLost(null);
    }

    public void delete() {
        unregisterListeners();
        textComponent.removeFocusListener(this);
    }

    private void registerListeners() {
        textComponent.getDocument().addDocumentListener(this);
        textComponent.addPropertyChangeListener("foreground", this);
    }

    private void unregisterListeners() {
        textComponent.getDocument().removeDocumentListener(this);
        textComponent.removePropertyChangeListener("foreground", this);
    }

    private void updateState() {
        isEmpty = textComponent.getText().length() == 0;
        foregroundColor = textComponent.getForeground();
    }

    public Color getGhostColor() {
        return ghostColor;
    }

    public void setGhostColor(Color ghostColor) {
        this.ghostColor = ghostColor;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (isEmpty) {
            unregisterListeners();
            try {
                textComponent.setText("");
                textComponent.setForeground(foregroundColor);
            } finally {
                registerListeners();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (isEmpty) {
            unregisterListeners();
            try {
                textComponent.setText(ghostText);
                textComponent.setForeground(ghostColor);
            } finally {
                registerListeners();
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateState();
    }
}
