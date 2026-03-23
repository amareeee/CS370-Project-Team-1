package All_GUI;
//import Timer. add function call to addListeners

import javax.swing.*;
import java.awt.*;

public class QuickTasks extends JPanel{
    private Image backgroundImage;
    private JButton addTaskButton;
    private JButton startSessionButton;
    private TimerPanel timerPanel;
    
    public QuickTasks() {
        backgroundImage = new ImageIcon(getClass().getResource("/quicktasksBG.png")).getImage();
        setUpPanel();
        createParts();
        components();
    }

    //want this panel to function more like a widget than anything else
    //panel layout helper method
    private void setUpPanel() {
        setPreferredSize(new Dimension(260, 0));
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
    }

    private void createParts() {
        //setting button size
        Dimension buttonSize = new Dimension(200,40);

        //when creating the buttons, must be set as opaque because we are overriding background
        addTaskButton = new JButton("Add Task");
        addTaskButton.setOpaque(true);
        addTaskButton.setPreferredSize(buttonSize);
        addTaskButton.setMaximumSize(buttonSize);
        addTaskButton.setBackground(Utilities.ORANGE_MEDIUM);
        addTaskButton.setForeground(Color.BLACK);
        startSessionButton = new JButton("Start Session");
        startSessionButton.setOpaque(true);
        startSessionButton.setPreferredSize(buttonSize);
        startSessionButton.setMaximumSize(buttonSize);
        startSessionButton.setBackground(Utilities.ORANGE_MEDIUM);
        startSessionButton.setForeground(Color.BLACK);

        addTaskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startSessionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //listeners
        addListeners();
    }

    private void components() {
        JLabel title = new JLabel("Quick Tasks");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(15));
        add(addTaskButton);
        add(Box.createVerticalStrut(15));
        add(startSessionButton);
    }
    private void addListeners() {
        startSessionButton.addActionListener(e -> timerPanel.startSession()); {
            //call timer function here

            System.out.println("Session has begun");
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
