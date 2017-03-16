package resources;

import java.awt.*;
import java.io.InputStream;

/**
 * Created by i5 on 18.10.2016.
 */
public class Fonts
{
    private static Font nameFont;

    public static Font getNameFont() {
        if (nameFont == null)
            nameFont = loadFont("OpenSansRegular.ttf");
        return nameFont;
    }

    private static Font loadFont(String name) {
        try(InputStream inputStream = Fonts.class.getResourceAsStream("/fonts/" + name)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("serif", Font.PLAIN, 24);
        }
    }
}