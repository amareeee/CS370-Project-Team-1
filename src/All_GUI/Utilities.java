package All_GUI;

import javax.swing.*;
import java.awt.*;

public class Utilities {
    //helper function to make labels
    public static JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));
        label.setForeground(color);
        label.setVisible(true);
        return label;
    }
    //commonly used fonts
    public static final Font WINDOWS_BUTTONS_FONT = new Font("SansSerif", Font.BOLD, 16);

    //color palette
    public static final Color ORANGE_MEDIUM = new Color(255, 167, 66);
}
