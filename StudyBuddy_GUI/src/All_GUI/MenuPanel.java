package All_GUI;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        //set up menu button
        setLayout(new BorderLayout());

        //load image and scale it
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/hamburgermenu.png"));
        Image scaled = icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        //make button and align
        JButton button = new JButton(scaledIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        JPanel leftWrapper = new JPanel();
        leftWrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        leftWrapper.add(button);

        add(leftWrapper, BorderLayout.WEST);

        //menu functionality
        JPopupMenu menu = new JPopupMenu();

        JMenuItem home = new JMenuItem("Home");
        JMenuItem tasks = new JMenuItem("Tasks");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem timer = new JMenuItem("Timer");

        menu.add(home);
        menu.add(tasks);
        menu.add(settings);
        menu.add(timer);

        //add action listeners for menu items
        home.addActionListener(e -> System.out.println("Home selected"));
        tasks.addActionListener(e -> System.out.println("Tasks selected"));
        settings.addActionListener(e -> System.out.println("Settings selected"));
        timer.addActionListener(e -> System.out.println("Timer selected"));

        //add action Listener
        button.addActionListener(e ->
                menu.show(button, 0, button.getHeight()));
    }
}
