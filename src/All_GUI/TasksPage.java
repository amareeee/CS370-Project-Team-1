package All_GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TasksPage extends JPanel {
    public TasksPage() {
        setLayout(new BorderLayout());
        setBackground(Color.RED);
        add(new JLabel("Tasks Page"), BorderLayout.CENTER);
    }
}
