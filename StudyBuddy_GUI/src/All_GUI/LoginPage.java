package All_GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Full-screen login / register panel shown before the main app.
 * On successful auth it calls the provided Runnable to launch the main GUI.
 */
public class LoginPage extends JFrame {

    private final AccountManager accountManager;
    private final Runnable onLoginSuccess;

    // Shared UI fields
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMe;
    private JLabel messageLabel;

    // Toggle between login and register
    private boolean isLoginMode = true;

    // Extra field for register
    private JPasswordField confirmPasswordField;
    private JPanel confirmPanel;

    public LoginPage(AccountManager accountManager, Runnable onLoginSuccess) {
        super("Study Buddy – Sign In");
        this.accountManager   = accountManager;
        this.onLoginSuccess   = onLoginSuccess;

        setUndecorated(true);
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(buildContent());
        setVisible(true);
    }

    // ------------------------------------------------------------------- UI build

    private JPanel buildContent() {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // warm gradient background
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 230, 190),
                        0, getHeight(), new Color(255, 200, 130));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // card panel
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 248, 235));
        card.setBorder(new EmptyBorder(36, 40, 36, 40));
        card.setPreferredSize(new Dimension(360, 460));

        // --- logo / title ---
        JLabel logo = new JLabel("📚");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Study Buddy");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(new Color(90, 50, 10));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- mode label ---
        JLabel modeLabel = new JLabel("Sign in to your account");
        modeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        modeLabel.setForeground(new Color(150, 100, 40));
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- form ---
        usernameField = styledField("Username");
        passwordField = styledPassword("Password");
        confirmPasswordField = styledPassword("Confirm Password");

        confirmPanel = new JPanel();
        confirmPanel.setOpaque(false);
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
        confirmPanel.add(Box.createVerticalStrut(8));
        confirmPanel.add(labelFor("Confirm Password"));
        confirmPanel.add(Box.createVerticalStrut(4));
        confirmPanel.add(confirmPasswordField);
        confirmPanel.setVisible(false); // hidden in login mode

        rememberMe = new JCheckBox("Keep me signed in");
        rememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberMe.setForeground(new Color(100, 60, 10));
        rememberMe.setOpaque(false);
        rememberMe.setSelected(true);
        rememberMe.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- action button ---
        JButton actionButton = new JButton("Sign In");
        actionButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionButton.setBackground(Utilities.ORANGE_MEDIUM);
        actionButton.setForeground(Color.WHITE);
        actionButton.setOpaque(true);
        actionButton.setBorderPainted(false);
        actionButton.setFocusPainted(false);
        actionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        actionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- toggle link ---
        JLabel toggleLabel = new JLabel("Don't have an account? Register");
        toggleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toggleLabel.setForeground(new Color(180, 110, 20));
        toggleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- message label for errors / info ---
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(180, 60, 40));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- drag to move ---
        enableDrag(card);
        enableDrag(root);

        // --- wire actions ---
        actionButton.addActionListener(e -> handleAction());
        // Enter key submits
        KeyAdapter enterKey = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleAction();
            }
        };
        usernameField.addKeyListener(enterKey);
        passwordField.addKeyListener(enterKey);
        confirmPasswordField.addKeyListener(enterKey);

        toggleLabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                isLoginMode = !isLoginMode;
                confirmPanel.setVisible(!isLoginMode);
                rememberMe.setVisible(isLoginMode);
                if (isLoginMode) {
                    modeLabel.setText("Sign in to your account");
                    actionButton.setText("Sign In");
                    toggleLabel.setText("Don't have an account? Register");
                } else {
                    modeLabel.setText("Create a new account");
                    actionButton.setText("Register");
                    toggleLabel.setText("Already have an account? Sign In");
                }
                messageLabel.setText(" ");
                card.revalidate();
                card.repaint();
            }
        });

        // --- assemble card ---
        card.add(logo);
        card.add(Box.createVerticalStrut(6));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(modeLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(labelFor("Username"));
        card.add(Box.createVerticalStrut(4));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(10));
        card.add(labelFor("Password"));
        card.add(Box.createVerticalStrut(4));
        card.add(passwordField);
        card.add(confirmPanel);
        card.add(Box.createVerticalStrut(10));
        card.add(rememberMe);
        card.add(Box.createVerticalStrut(18));
        card.add(actionButton);
        card.add(Box.createVerticalStrut(10));
        card.add(messageLabel);
        card.add(Box.createVerticalGlue());
        card.add(toggleLabel);

        root.add(card);
        return root;
    }

    // ---------------------------------------------------------------- helpers

    private void handleAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (isLoginMode) {
            String error = accountManager.login(username, password, rememberMe.isSelected());
            if (error != null) {
                showMessage(error, true);
            } else {
                dispose();
                onLoginSuccess.run();
            }
        } else {
            String confirm = new String(confirmPasswordField.getPassword());
            if (!password.equals(confirm)) {
                showMessage("Passwords do not match.", true);
                return;
            }
            String error = accountManager.register(username, password);
            if (error != null) {
                showMessage(error, true);
            } else {
                showMessage("Account created! Signing you in…", false);
                accountManager.login(username, password, true);
                Timer delay = new Timer(800, e -> { dispose(); onLoginSuccess.run(); });
                delay.setRepeats(false);
                delay.start();
            }
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setForeground(isError ? new Color(180, 60, 40) : new Color(40, 140, 80));
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        styleInput(f);
        return f;
    }

    private JPasswordField styledPassword(String placeholder) {
        JPasswordField f = new JPasswordField();
        styleInput(f);
        return f;
    }

    private void styleInput(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(255, 240, 215));
        f.setForeground(new Color(60, 40, 10));
        f.setCaretColor(new Color(60, 40, 10));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Utilities.ORANGE_MEDIUM, 1),
                new EmptyBorder(6, 10, 6, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel labelFor(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(100, 60, 10));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private Point _dragStart;
    private void enableDrag(JComponent comp) {
        comp.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { _dragStart = e.getPoint(); }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - _dragStart.x,
                        loc.y + e.getY() - _dragStart.y);
            }
        });
    }
}
