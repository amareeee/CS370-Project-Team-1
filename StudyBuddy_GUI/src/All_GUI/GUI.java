package All_GUI;

import javax.swing.*;
import java.awt.*;

public class GUI {
    JFrame frame = new JFrame("Study Buddy");
    public GUI() {
        frame.add(new QuickTasks(), BorderLayout.EAST);
        frame.add(new BuddyPage(), BorderLayout.CENTER);
        frame.add(new MenuPanel(), BorderLayout.WEST);
        setFrame(frame);
    }
    private void setFrame(JFrame frame) {
        //setting frame to be size of full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}


