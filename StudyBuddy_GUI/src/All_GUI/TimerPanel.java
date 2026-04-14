package All_GUI;

import javax.swing.*;
import java.awt.*;

public class TimerPanel extends JPanel {

    private int totalSeconds = 0;
    private int remaining    = 0;
    private boolean running  = false;
    private Timer swingTimer;

    // --- UI components ---
    private JLabel  displayLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JSpinner hoursSpinner, minutesSpinner, secondsSpinner;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    public TimerPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        buildUI();
        buildTimer();
    }

    // ------------------------------------------------------------------ UI --

    private void buildUI() {
        // Title
        JLabel title = new JLabel("Session Timer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(12));

        // Big time display
        displayLabel = new JLabel("00:00:00");
        displayLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        displayLabel.setForeground(new Color(60, 40, 10));
        displayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(displayLabel);
        add(Box.createVerticalStrut(8));

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(Utilities.ORANGE_MEDIUM);
        progressBar.setBackground(new Color(240, 210, 170));
        progressBar.setBorderPainted(false);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(progressBar);
        add(Box.createVerticalStrut(16));

        // Spinner row  (h : m : s)
        add(buildSpinnerRow());
        add(Box.createVerticalStrut(14));

        // Buttons
        add(buildButtonRow());
        add(Box.createVerticalStrut(10));

        // Status
        statusLabel = new JLabel("ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(180, 130, 60));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(statusLabel);
    }

    private JPanel buildSpinnerRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        hoursSpinner   = makeSpinner(0, 99, 0);
        minutesSpinner = makeSpinner(0, 59, 25);   // default 25-min Pomodoro
        secondsSpinner = makeSpinner(0, 59, 0);

        row.add(labeledSpinner(hoursSpinner,   "hr"));
        row.add(colonLabel());
        row.add(labeledSpinner(minutesSpinner, "min"));
        row.add(colonLabel());
        row.add(labeledSpinner(secondsSpinner, "sec"));
        return row;
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        Dimension btnSize = new Dimension(80, 36);

        startButton = makeButton("Start",  true,  btnSize);
        stopButton  = makeButton("Stop",   false, btnSize);
        resetButton = makeButton("Reset",  false, btnSize);

        startButton.addActionListener(e -> handleStart());
        stopButton .addActionListener(e -> handleStop());
        resetButton.addActionListener(e -> handleReset());

        row.add(startButton);
        row.add(stopButton);
        row.add(resetButton);
        return row;
    }

    // --------------------------------------------------------------- helpers -

    private JSpinner makeSpinner(int min, int max, int value) {
        JSpinner s = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        s.setPreferredSize(new Dimension(52, 30));
        s.setOpaque(false);
        JFormattedTextField tf =
                ((JSpinner.DefaultEditor) s.getEditor()).getTextField();
        tf.setFont(new Font("Monospaced", Font.PLAIN, 14));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBackground(new Color(255, 235, 200));
        tf.setForeground(new Color(60, 40, 10));
        tf.setBorder(BorderFactory.createLineBorder(Utilities.ORANGE_MEDIUM, 1));
        return s;
    }

    private JPanel labeledSpinner(JSpinner spinner, String unit) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(spinner);
        JLabel lbl = new JLabel(unit);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(new Color(160, 110, 40));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(lbl);
        return p;
    }

    private JLabel colonLabel() {
        JLabel c = new JLabel(":");
        c.setFont(new Font("Monospaced", Font.BOLD, 20));
        c.setForeground(new Color(200, 150, 80));
        return c;
    }

    private JButton makeButton(String text, boolean primary, Dimension size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setPreferredSize(size);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (primary) {
            btn.setBackground(Utilities.ORANGE_MEDIUM);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        } else {
            btn.setBackground(new Color(255, 235, 200));
            btn.setForeground(new Color(60, 40, 10));
            btn.setBorder(BorderFactory.createLineBorder(Utilities.ORANGE_MEDIUM, 1));
        }
        return btn;
    }

    // ---------------------------------------------------------- timer logic --

    private void buildTimer() {
        swingTimer = new Timer(1000, e -> tick());
    }

    private int readInputs() {
        int h = (int) hoursSpinner.getValue();
        int m = (int) minutesSpinner.getValue();
        int s = (int) secondsSpinner.getValue();
        return h * 3600 + m * 60 + s;
    }

    private void handleStart() {
        if (running) return;
        if (remaining == 0 || remaining == totalSeconds) {
            totalSeconds = readInputs();
            if (totalSeconds == 0) return;
            remaining = totalSeconds;
        }
        running = true;
        startButton.setText("Running…");
        startButton.setEnabled(false);
        setStatus("running", new Color(29, 158, 117));
        updateDisplay();
        swingTimer.start();
    }

    private void handleStop() {
        if (!running) return;
        swingTimer.stop();
        running = false;
        startButton.setText("Resume");
        startButton.setEnabled(true);
        setStatus("paused", new Color(180, 130, 60));
    }

    private void handleReset() {
        swingTimer.stop();
        running    = false;
        remaining  = 0;
        totalSeconds = 0;
        displayLabel.setText("00:00:00");
        displayLabel.setForeground(new Color(60, 40, 10));
        progressBar.setValue(100);
        progressBar.setForeground(Utilities.ORANGE_MEDIUM);
        startButton.setText("Start");
        startButton.setEnabled(true);
        setStatus("ready", new Color(180, 130, 60));
    }

    private void tick() {
        remaining--;
        updateDisplay();
        if (remaining <= 0) {
            swingTimer.stop();
            running = false;
            startButton.setText("Start");
            startButton.setEnabled(true);
            displayLabel.setForeground(new Color(180, 60, 40));
            progressBar.setValue(0);
            setStatus("done!", new Color(180, 60, 40));
        }
    }

    private void updateDisplay() {
        int h   = remaining / 3600;
        int m   = (remaining % 3600) / 60;
        int s   = remaining % 60;
        displayLabel.setText(String.format("%02d:%02d:%02d", h, m, s));

        if (totalSeconds > 0) {
            int pct = (int) ((remaining / (double) totalSeconds) * 100);
            progressBar.setValue(pct);
            // colour shifts: orange → amber → red
            if (pct > 50) {
                progressBar.setForeground(Utilities.ORANGE_MEDIUM);
                displayLabel.setForeground(new Color(60, 40, 10));
            } else if (pct > 20) {
                progressBar.setForeground(new Color(200, 130, 20));
                displayLabel.setForeground(new Color(160, 100, 10));
            } else {
                progressBar.setForeground(new Color(180, 60, 40));
                displayLabel.setForeground(new Color(180, 60, 40));
            }
        }
    }

    private void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    // --------------------------------- public hook for "Start Session" button -
//called from Quick Tasks
    public void startSession() {
        handleReset();
        swingTimer.start();
        handleStart();
    }
}

