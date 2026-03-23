package All_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TitlePanel extends JPanel {
    private MenuListener listener;
    private Point initialClick;
    public TitlePanel(MenuListener listener) {
        //set up listener
        this.listener = listener;

        //set up
        setLayout(new BorderLayout());
        setBackground(new Color (250,200,152));

        add(createMenu(), BorderLayout.WEST);
        add(createWindowsButtons(), BorderLayout.EAST);

        enableDragtoMove();
    }

    private JPanel createWindowsButtons() {

        //EXIT BUTTON
        JButton closeButton = new JButton("X");
        closeButton.setFocusable(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);;
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(Utilities.WINDOWS_BUTTONS_FONT);
        //actionListener for close button
        closeButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
        });
        //MINIMIZE BUTTON
        JButton minButton = new JButton("-");
        minButton.setFocusable(false);
        minButton.setBorderPainted(false);
        minButton.setContentAreaFilled(false);
        minButton.setForeground(Color.WHITE);
        minButton.setFont(Utilities.WINDOWS_BUTTONS_FONT);
        //actionListener for min button
        minButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setState(JFrame.ICONIFIED);
        });

        //MAXIMIZE BUTTON
        JButton maxButton = new JButton ("□");
        maxButton.setFocusable(false);
        maxButton.setBorderPainted(false);
        maxButton.setContentAreaFilled(false);
        maxButton.setForeground(Color.WHITE);
        maxButton.setFont(Utilities.WINDOWS_BUTTONS_FONT);
        //add action listener for maxButton
        maxButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                frame.setExtendedState(JFrame.NORMAL); //restore
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //maximize
            }
        });


        //wrapping everything in the panel
        JPanel rightButtons = new JPanel();
        rightButtons.setOpaque(false);
        rightButtons.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));

        rightButtons.add(minButton);
        rightButtons.add(maxButton);
        rightButtons.add(closeButton);

        return rightButtons;
    }
    private void enableDragtoMove() {
        //adding functionality to make everything draggable
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed (MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(TitlePanel.this);
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                frame.setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
    }

    private JPanel createMenu() {
        //load image and scale it
        ImageIcon icon = new ImageIcon(getClass().getResource("/hamburgermenu.png"));
        Image scaled = icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        //Make button and align
        JButton button = new JButton(scaledIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        //menu creation and buttons
        JPopupMenu menu = new JPopupMenu();
        JMenuItem home = new JMenuItem("Home");
        JMenuItem tasks = new JMenuItem("Tasks");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem timer = new JMenuItem("Timer");
        //adding buttons to menu
        menu.add(home);
        menu.add(tasks);
        menu.add(settings);
        menu.add(timer);
        //set up listener
        home.addActionListener(e -> listener.onHomeSelected());
        tasks.addActionListener(e -> listener.onTasksSelected());
        settings.addActionListener(e -> listener.onSettingsSelected());
        timer.addActionListener(e -> listener.onTimerSelected());

        //add action Listener
        button.addActionListener(e ->
                menu.show(button, 0, button.getHeight()));

        JPanel leftWrapper = new JPanel();
        leftWrapper.setOpaque(false);
        leftWrapper.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        leftWrapper.add(button);
        return leftWrapper;
    }
}
