package All_GUI;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        //set up menu button
        setLayout(new BorderLayout());

        //load image and scale it
        ImageIcon icon = new ImageIcon(getClass().getResource("/hamburgermenu.png"));
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

        menu.add(home);
        menu.add(tasks);
        menu.add(settings);

        //add action Listener
        button.addActionListener(e ->
            menu.show(button, 0, button.getHeight()));
    }
}
