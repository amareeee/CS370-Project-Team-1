package All_GUI;

import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements MenuListener{
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public GUI() {
        super("Study Buddy");

        setUndecorated(true); //remove title bar and making custom
        setLayout(new BorderLayout());
        TimerPanel timerPanel = new TimerPanel();

        //make cardlayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new BuddyPage(), "HOME");
        contentPanel.add(new TasksPage(), "TASKS");
        contentPanel.add(new SettingsPage(), "SETTINGS");
        contentPanel.add(new SettingsPage(), "TIMER");

        //add contentpanel to fram
        add(contentPanel, BorderLayout.CENTER);

        //add panels
        add(new BuddyPage(), BorderLayout.SOUTH);
        add(new QuickTasks(timerPanel), BorderLayout.EAST);
        add(new TitlePanel(this), BorderLayout.NORTH);

        setSize(1200, 800);
        setExtendedState(JFrame.NORMAL);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void onHomeSelected() {
        cardLayout.show(contentPanel, "HOME");
    }
    public void onTasksSelected() {
        cardLayout.show(contentPanel, "TASKS");
    }
    public void onSettingsSelected() {
        cardLayout.show(contentPanel, "SETTINGS");
    }
    public void onTimerSelected() {
        cardLayout.show(contentPanel, "TIMER");
    }
}


