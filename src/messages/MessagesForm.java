package messages;

import misc.GuiHelper;
import org.javagram.dao.Me;
import org.javagram.dao.Message;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by i5 on 07.06.2016.
 */
public class MessagesForm extends JPanel
{
    private JPanel rootPanel;
    private JScrollPane scrollPane;
    private JPanel scrollPanel;

    private final int width = 250;
    private final int messagesCount = 100;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm dd MMM yyyy");

    private TelegramProxy telegramProxy;
    private Person person;

    {
        GuiHelper.decorateScrollPane(scrollPane);
    }

    public MessagesForm(TelegramProxy telegramProxy) {
        this(telegramProxy, null);
    }

    public MessagesForm(TelegramProxy telegramProxy, Person person) {
        this.telegramProxy = telegramProxy;
        display(person);
    }

    public void display(Person person) {
        scrollPanel.removeAll();
        this.person = person;
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
        scrollPanel.add(Box.createGlue());

        if (person == null)
            return;

        List<Message> messages = telegramProxy.getMessages(person, messagesCount);
        for (int i = messages.size() - 1; i >= 0; i--) {
            JPanel panel = new JPanel() {
                @Override
                public Dimension getMaximumSize() {
                    Dimension maxSize = super.getMaximumSize();
                    Dimension prefSize = super.getPreferredSize();
                    return new Dimension(maxSize.width, prefSize.height);
                }
            };
            Message message = messages.get(i);
            int alignment;
            Color color;
            if (message.getReceiver() instanceof Me) {
                alignment = FlowLayout.LEFT;
                color = new Color(0, 178, 229);
            } else if (message.getSender() instanceof Me) {
                alignment = FlowLayout.RIGHT;
                color = new Color(74, 68, 167);
            } else {
                alignment = FlowLayout.CENTER;
                color = Color.red;
            }
            panel.setLayout(new FlowLayout(alignment));
            panel.add(new MessageForm(message.getText(), dateFormat.format(message.getDate()), width, color));
            scrollPanel.add(panel);
        }
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private void createUIComponents() {
        rootPanel = this;
    }

    public TelegramProxy getTelegramProxy() {
        return telegramProxy;
    }

    public Person getPerson() {
        return person;
    }
}
