package All_GUI;

import javax.swing.*;
import java.awt.*;

public class QuickTasks extends JPanel{
    public QuickTasks() {
        //creating panel and panel elements
        setLayout(new BorderLayout());
        JLabel label = Utilities.createLabel("Quick Tasks", 15, Color.black);

        //Add task button and elements
        JButton button1 = new JButton("Add Task");
        button1.setPreferredSize(new Dimension(100,50));

        //Start study session button
        JButton button2 = new JButton("Start Session");
        button2.setPreferredSize(new Dimension(100, 50));

        //setting layout and design of panel
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.Y_AXIS));
        buttonHolder.add(button1);
        buttonHolder.add(Box.createVerticalStrut(10));
        buttonHolder.add(button2);

        add(label, BorderLayout.NORTH);
        add(buttonHolder, BorderLayout.CENTER);

        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
        setBackground(Color.lightGray);
    }
}
