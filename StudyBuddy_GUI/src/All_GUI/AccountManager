package All_GUI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages user accounts and session persistence.
 *
 * Files written to ~/.studybuddy/
 *   accounts.txt  – plain text, one entry per line: username:salt:hash
 *   session.txt   – username of the logged-in user, empty = no session
 *
 * Uses plain text instead of Java serialization so the files never
 * become unreadable after a recompile.
 */
public class AccountManager {

    private static final Path DATA_DIR      = Paths.get(System.getProperty("user.home"), ".studybuddy");
    private static final Path ACCOUNTS_FILE = DATA_DIR.resolve("accounts.txt");
    private static final Path SESSION_FILE  = DATA_DIR.resolve("session.txt");

    private final Map<String, UserAccount> accounts = new HashMap<>();
    private UserAccount currentUser = null;

    public AccountManager() {
        ensureDataDir();
        loadAccounts();
        restoreSession();
    }

    // ─────────────────────────────────────────── public API ──

    public UserAccount getCurrentUser() { return currentUser; }
    public boolean isLoggedIn()         { return currentUser != null; }

    /** Register a new account. Returns null on success, error string on failure. */
    public String register(String username, String password) {
        username = username.trim();
        if (username.isEmpty())     return "Username cannot be empty.";
        if (password.length() < 6)  return "Password must be at least 6 characters.";
        if (accounts.containsKey(username.toLowerCase()))
            return "Username already taken.";

        UserAccount account = new UserAccount(username, password);
        accounts.put(username.toLowerCase(), account);
        saveAccounts();
        return null;
    }

    /** Log in. Returns null on success, error string on failure. */
    public String login(String username, String password, boolean remember) {
        username = username.trim();
        if (username.isEmpty()) return "Please enter a username.";

        UserAccount account = accounts.get(username.toLowerCase());
        if (account == null)                  return "No account found with that username.";
        if (!account.checkPassword(password)) return "Incorrect password.";

        currentUser = account;
        if (remember) writeFile(SESSION_FILE, account.getUsername());
        return null;
    }

    /** Log out and erase the saved session. */
    public void logout() {
        currentUser = null;
        writeFile(SESSION_FILE, "");
    }

    // ─────────────────────────────────────── persistence ──

    private void ensureDataDir() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("[AccountManager] Cannot create data dir: " + e.getMessage());
        }
    }

    /**
     * Load accounts from accounts.txt.
     * Format per line:  username:salt:hash
     */
    private void loadAccounts() {
        if (!Files.exists(ACCOUNTS_FILE)) return;
        try {
            for (String line : Files.readAllLines(ACCOUNTS_FILE, StandardCharsets.UTF_8)) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(":", 3);
                if (parts.length != 3) {
                    System.err.println("[AccountManager] Skipping malformed line.");
                    continue;
                }
                UserAccount account = UserAccount.fromStored(parts[0], parts[1], parts[2]);
                accounts.put(parts[0].toLowerCase(), account);
            }
            System.out.println("[AccountManager] Loaded " + accounts.size() + " account(s).");
        } catch (IOException e) {
            System.err.println("[AccountManager] Could not read accounts: " + e.getMessage());
        }
    }

    /** Save all accounts to accounts.txt. */
    private void saveAccounts() {
        StringBuilder sb = new StringBuilder();
        for (UserAccount a : accounts.values()) {
            sb.append(a.getUsername()).append(":")
                    .append(a.getSalt()).append(":")
                    .append(a.getPasswordHash())
                    .append(System.lineSeparator());
        }
        writeFile(ACCOUNTS_FILE, sb.toString());
    }

    /** Read session.txt and auto-login the saved user if their account still exists. */
    private void restoreSession() {
        if (!Files.exists(SESSION_FILE)) return;
        try {
            String username = new String(
                    Files.readAllBytes(SESSION_FILE), StandardCharsets.UTF_8).trim();
            if (username.isEmpty()) return;

            UserAccount account = accounts.get(username.toLowerCase());
            if (account != null) {
                currentUser = account;
                System.out.println("[AccountManager] Auto-logged in as: " + account.getUsername());
            } else {
                // Session file pointed to a deleted/missing account – clear it safely
                System.err.println("[AccountManager] Session user not found; clearing session.");
                writeFile(SESSION_FILE, "");
            }
        } catch (IOException e) {
            System.err.println("[AccountManager] Could not read session: " + e.getMessage());
        }
    }

    /** Safely write a string to a file (UTF-8). */
    private void writeFile(Path path, String content) {
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("[AccountManager] Could not write "
                    + path.getFileName() + ": " + e.getMessage());
        }
    }
