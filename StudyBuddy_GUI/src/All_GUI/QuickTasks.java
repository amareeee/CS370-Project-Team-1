package All_GUI;
//import Timer. add function call to addListeners

import javax.swing.*;
import java.awt.*;

public class QuickTasks extends JPanel{
    private Image backgroundImage;
    private JButton addTaskButton;
    private JButton startSessionButton;
    private JButton calendarButton;
    private TimerPanel timerPanel;
    private TaskManager taskManager;
    private DefaultListModel<Tasks> listModel;
    private JList<Tasks> taskList;
    private AccountManager accountManager;

    public QuickTasks(TaskManager taskManager, TimerPanel timerPanel, AccountManager accountManager) {
        this.taskManager = taskManager;
        this.timerPanel = timerPanel;
        this.accountManager = accountManager;

        backgroundImage = new ImageIcon(getClass().getResource("/resources/quicktasksBG.png")).getImage();
        //add any assets here
        setUpPanel();
        createParts();
        components();
    }
    private void openTaskCreation() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        TaskCreation dialog = new TaskCreation(parent);
        dialog.setVisible(true);

        dialog.getTaskTitle();
        dialog.getTaskDescription();
        dialog.getDueDateInput();

        if (dialog.isConfirmed()) {
            Tasks task = new Tasks(dialog.getTaskTitle(),
                    dialog.getTaskDescription(),
                    dialog.getDueDateInput());
            taskManager.addTask(task);
            if (listModel != null) listModel.addElement(task);
        }
    }
    //want this panel to function more like a widget
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

        Dimension buttonSize = new Dimension(200,40);
        //calendar connect/status button
        calendarButton = new JButton(getCalendarButtonLabel());
        calendarButton.setOpaque(true);
        calendarButton.setPreferredSize(buttonSize);
        calendarButton.setMaximumSize(buttonSize);
        calendarButton.setBackground(accountManager.getCalendarSync().isConnected()
                ? new Color(34, 139, 34) : new Color(100, 149, 237));
        calendarButton.setForeground(Color.WHITE);
        calendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarButton.setBorderPainted(false);
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        calendarButton.addActionListener(e -> handleCalendarButton());



        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setOpaque(true);
        logoutBtn.setPreferredSize(buttonSize);
        logoutBtn.setMaximumSize(buttonSize);
        logoutBtn.setBackground(new Color(200,100,30));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Sign out of Study Buddy?", "Sign out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                accountManager.logout();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.dispose();
                SwingUtilities.invokeLater(() ->
                    new LoginPage(accountManager,
                            () -> SwingUtilities.invokeLater(() -> new GUI(accountManager))));
            }
        });

        add(title);
        add(Box.createVerticalStrut(15));
        add(addTaskButton);
        add(Box.createVerticalStrut(15));
        add(startSessionButton);
        add(Box.createVerticalGlue());//pushes logout to bottom
        add(calendarButton);
        add(Box.createVerticalStrut(15));
        add(logoutBtn);
        add(Box.createVerticalStrut(15));
    }

    private void handleCalendarButton() {
        GoogleCalendarSync sync = accountManager.getCalendarSync();
        String username = accountManager.getCurrentUser().getUsername();

        if (sync.isConnected()) {

            String[] options = {"Open Calendar", "Disconnect", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this, "Google Calender is connected."
            , "Google Calendar", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame instanceof GUI) ((GUI)frame).onCalendarSelected();
            } else if (choice == 1) {
                sync.disconnect(username);
                refreshCalendarButton();
            }
        } else{
            calendarButton.setEnabled(false);
            calendarButton.setText("Connecting...");

            SwingWorker<String, Void> worker = new SwingWorker<>(){
                @Override
                protected String doInBackground() {
                    return sync.connect(username);
                }
                @Override
                protected void done() {
                    try{
                        String error = get();
                        if (error != null) {
                            JOptionPane.showMessageDialog(QuickTasks.this, error, "Connection Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            sync.syncAllTasks(taskManager);
                        }
                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(QuickTasks.this, ex, "Connection Error", JOptionPane.ERROR_MESSAGE);
                    }
                    refreshCalendarButton();
                }
            };
            worker.execute();
        }
    }

    //update the calendar button text
    public void refreshCalendarButton() {
        boolean connected = accountManager.getCalendarSync().isConnected();
        calendarButton.setText(getCalendarButtonLabel());
        calendarButton.setBackground(connected ? new Color(34, 139, 34) : new Color(100, 149, 237));
        calendarButton.setEnabled(true);
        revalidate();
        repaint();
    }

    private String getCalendarButtonLabel() {
        return accountManager.getCalendarSync().isConnected()
                ? "✔ Google Calendar"
                : "Connect Google Calendar";
    }

    private void addListeners() {
        startSessionButton.addActionListener(e -> {
            timerPanel.startSession();
            System.out.println("Session has begun");
        });

        addTaskButton.addActionListener(e -> openTaskCreation());
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
