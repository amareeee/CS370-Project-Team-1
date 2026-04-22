package All_GUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TaskCreation extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private boolean confirmed = false;

    public TaskCreation(JFrame parent) {
        super(parent, "Add Task", true);

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        dueDateField = new JTextField(20);


        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        setLayout(new BorderLayout());
        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.add(new JLabel("Title"));
        fields.add(titleField);
        fields.add(Box.createVerticalStrut(10));

        fields.add(new JLabel("Description:"));
        fields.add(new JScrollPane(descriptionArea));
        fields.add(Box.createVerticalStrut(10));

        fields.add(new JLabel("Due Date (YYYY-MM-DD):"));
        fields.add(dueDateField);

        add(fields, BorderLayout.CENTER);


        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);

    }

    public boolean isConfirmed() { return confirmed; }
    public String getTaskTitle() { return titleField.getText(); }
    public String getTaskDescription() { return descriptionArea.getText(); }
    public LocalDate getDueDateInput() { return LocalDate.parse(dueDateField.getText()); }
}
