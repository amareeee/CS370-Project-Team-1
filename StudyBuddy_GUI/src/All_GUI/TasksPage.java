package All_GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TasksPage extends JPanel {
    private TaskManager taskManager;
    private DefaultListModel<Tasks> listModel;
    private JList<Tasks> taskList;

    public TasksPage(TaskManager taskManager) {
        this.taskManager = taskManager;

        setLayout(new BorderLayout());
        setBackground(Utilities.ORANGE_MEDIUM);

        JLabel title = new JLabel("Your Tasks:");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        refreshTaskList();

        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Tasks selected = taskList.getSelectedValue();
                    if (selected != null) {
                        if (!selected.isCompleted()) {
                            //routes through TaskManager so hp gain/loss runs
                            taskManager.completeTask(selected);
                        } else {
                            //if already completed, no hp change
                            selected.setCompleted(false);
                        }
                        refreshTaskList();
                    }
                }


            }
        });
    }

    public void refreshTaskList() {
        listModel.clear();
        for (Tasks t : taskManager.getAllTasks()) {
            listModel.addElement(t);
        }
    }
}
