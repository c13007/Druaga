package System;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class DFont {

    private Font myfont;

    public DFont(String filename, float size) {
        this.myfont = createFont(filename);
        this.myfont = myfont.deriveFont(size);
    }

    public Font createFont(String filename) {

        Font font = null;
        InputStream is = null;

        try {
            is = getClass().getResourceAsStream(filename);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        return font;

    }

    public Font getFont() {
        return this.myfont;
    }

}
