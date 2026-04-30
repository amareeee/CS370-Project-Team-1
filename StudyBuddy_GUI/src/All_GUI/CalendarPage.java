package All_GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class CalendarPage extends JPanel {

    private final AccountManager accountManager;
    private final TaskManager taskManager;

    private JLabel statusLabel;
    private JButton connectButton;
    private JButton syncButton;
    private JButton openCalendarButton;

    public CalendarPage(AccountManager accountManager, TaskManager taskManager) {
        this.accountManager = accountManager;
        this.taskManager = taskManager;

        setLayout(new GridBagLayout());
        setBackground(new Color(255,243,220));
        buildUI();
    }

    private void buildUI() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255,243,220));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Utilities.ORANGE_MEDIUM, 1),
                BorderFactory.createEmptyBorder(30,40,30,40)));

        //title
        JLabel title = new JLabel("Calendar");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(new Color(90,50,10));

        //subtitle
        JLabel subtitle = new JLabel("Connect to sync your tasks as calendar events");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(150, 100, 40));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status indicator
        statusLabel = new JLabel(getStatusText());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setForeground(getStatusColor());

        // Connect / Disconnect button
        connectButton = new JButton(getConnectButtonText());
        styleButton(connectButton, accountManager.getCalendarSync().isConnected()
                ? new Color(180, 60, 40) : new Color(34, 139, 34));
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectButton.addActionListener(e -> handleConnectToggle());

        // Sync all tasks button
        syncButton = new JButton("Sync All Tasks Now");
        styleButton(syncButton, Utilities.ORANGE_MEDIUM);
        syncButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        syncButton.setEnabled(accountManager.getCalendarSync().isConnected());
        syncButton.addActionListener(e -> handleSyncAll());

        openCalendarButton = new JButton("Open Calendar");
        styleButton(openCalendarButton, Utilities.ORANGE_MEDIUM);
        openCalendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openCalendarButton.addActionListener(e -> openInBrowser());

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(260, 10));
        separator.setForeground(new Color(220, 180, 120));

        // Info note
        JLabel note = new JLabel("<html><center>Tasks you add will automatically sync.<br>"
                + "Connecting opens a browser window to sign in with Google.</center></html>");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        note.setForeground(new Color(160, 110, 50));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        note.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(connectButton);
        card.add(Box.createVerticalStrut(12));
        card.add(syncButton);
        card.add(Box.createVerticalStrut(12));
        card.add(openCalendarButton);
        card.add(Box.createVerticalStrut(12));
        card.add(separator);
        card.add(Box.createVerticalStrut(12));
        card.add(note);

        add(card);

    }

    private void openInBrowser() {
        try {
            Desktop.getDesktop().browse(new URI("https://calendar.google.com"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not open browser: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleConnectToggle() {
        GoogleCalendarSync sync = accountManager.getCalendarSync();
        String username = accountManager.getCurrentUser().getUsername();

        if (sync.isConnected()) {

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Disconnect your Google Calendar?, Disconnect");
            if (confirm == JOptionPane.YES_OPTION) {
                sync.disconnect(username);
                refresh();
            }
        } else {
            // Connect — runs OAuth in background so UI doesn't freeze
            connectButton.setEnabled(false);
            connectButton.setText("Connecting…");
            statusLabel.setText("Opening browser for Google sign-in…");
            statusLabel.setForeground(new Color(180, 130, 60));

            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return sync.connect(username);
                }
                @Override
                protected void done() {
                    try {
                        String error = get();
                        if (error != null) {
                            JOptionPane.showMessageDialog(CalendarPage.this,
                                    error, "Connection Failed", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Sync existing tasks immediately after connecting
                            sync.syncAllTasks(taskManager);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(CalendarPage.this,
                                "Unexpected error: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    refresh();
                }
            };
            worker.execute();
        }
    }

    private void handleSyncAll() {
        GoogleCalendarSync sync = accountManager.getCalendarSync();
        if (!sync.isConnected()) return;

        syncButton.setEnabled(false);
        syncButton.setText("Sync All Tasks Now");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                sync.syncAllTasks(taskManager);
                return null;
            }
            @Override
            protected void done() {
                syncButton.setEnabled(true);
                syncButton.setText("Sync All Tasks Now");
                JOptionPane.showMessageDialog(CalendarPage.this,
            "All tasks synced to Google Calendar!", "Sync Completed", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        worker.execute();
    }

    public void refresh() {
        boolean connected = accountManager.getCalendarSync().isConnected();
        statusLabel.setText(getStatusText());
        statusLabel.setForeground(getStatusColor());
        connectButton.setText(getConnectButtonText());
        connectButton.setBackground(connected ? new Color(180, 60, 40) : new Color(34, 139, 34));

        connectButton.setEnabled(true);
        syncButton.setEnabled(connected);
        revalidate();
        repaint();
    }

    private String getStatusText() {
        return accountManager.getCalendarSync().isConnected()
                ? "● Connected to Google Calendar"
                : "● Not connected";
    }

    private Color getStatusColor() {
        return accountManager.getCalendarSync().isConnected()
                ? new Color(34, 139, 34)
                : new Color(180, 60, 40);
    }

    private String getConnectButtonText(){
        return accountManager.getCalendarSync().isConnected()
                ? "Disconnect Google Calendar"
                : "Connect Google Calendar";
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(260, 40));
        button.setPreferredSize(new Dimension(260, 40));
    }
}
