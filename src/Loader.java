import org.javagram.dao.AbstractTelegramDAO;
import org.javagram.dao.ApiBridgeTelegramDAO;
import org.javagram.dao.DebugTelegramDAO;
import org.javagram.dao.TelegramDAO;
import resources.Config;

import javax.swing.*;

/**
 * Created by i5 on 10.04.2016.
 */
public class Loader
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    TelegramDAO telegramDAO = new ApiBridgeTelegramDAO(Config.SERVER, Config.APP_ID, Config.APP_HASH);
                            new DebugTelegramDAO();
                    JFrame myFrame = new MyFrame(telegramDAO);
                    myFrame.setUndecorated(true);
                    myFrame.setLocationRelativeTo(null);
                    myFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}
