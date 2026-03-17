package All_GUI;

import javax.swing.*;
import java.awt.*;

public class BuddyPage extends JPanel{
    public BuddyPage() {
        setLayout(new BorderLayout());
        //add functionality for this later, but add spot to insert name of buddy
        JLabel buddyStats = Utilities.createLabel("Stats", 10, Color.black);
        add(buddyStats, BorderLayout.SOUTH);
    }
}
