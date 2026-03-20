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
}
