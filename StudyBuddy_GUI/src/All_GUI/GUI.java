package All_GUI;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements MenuListener{
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private TaskManager taskManager;
    private final AccountManager accountManager;
    private TasksPage tasksPage;

    public GUI(AccountManager accountManager) {
        super("Study Buddy");
        this.accountManager = accountManager;

        setUndecorated(true); //remove title bar and making custom
        setLayout(new BorderLayout());
        TimerPanel timerPanel = new TimerPanel();

        BuddyPage buddyPage = new BuddyPage();
        taskManager = buddyPage.getTaskManager();

        //make cardlayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(buddyPage, "HOME");
        this.tasksPage = new TasksPage(taskManager);
        contentPanel.add(this.tasksPage, "TASKS");
        contentPanel.add(new SettingsPage(), "SETTINGS");
        contentPanel.add(timerPanel, "TIMER");

        //add contentpanel to frame
        add(contentPanel, BorderLayout.CENTER);

        //add panels
        QuickTasks quickTasks = new QuickTasks(taskManager, timerPanel);
        add(quickTasks, BorderLayout.EAST);
        add(new TitlePanel(this, accountManager), BorderLayout.NORTH);

        setSize(1200, 800);
        setExtendedState(JFrame.NORMAL);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void onHomeSelected() {
        cardLayout.show(contentPanel, "HOME");
    }
    public void onTasksSelected() {
        tasksPage.refreshTaskList();
        cardLayout.show(contentPanel, "TASKS");
    }
    public void onSettingsSelected() {
        cardLayout.show(contentPanel, "SETTINGS");
    }
    public void onTimerSelected() {
        cardLayout.show(contentPanel, "TIMER");
    }
}


