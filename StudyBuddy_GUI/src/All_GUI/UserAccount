package All_GUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Holds one user's credentials.
 * Passwords are stored as SHA-256(salt + password), never in plain text.
 *
 * NOT Serializable by design – AccountManager stores accounts as plain text
 * so they survive recompiles without InvalidClassException crashes.
 */
public class UserAccount {

    private final String username;
    private final String salt;
    private final String passwordHash;

    /** Create a brand-new account (hashes the password automatically). */
    public UserAccount(String username, String plainPassword) {
        this.username     = username;
        this.salt         = generateSalt();
        this.passwordHash = hash(plainPassword, this.salt);
    }

    /**
     * Reconstruct an account from already-stored salt + hash
     * (used when loading from accounts.txt).
     */
    public static UserAccount fromStored(String username, String salt, String passwordHash) {
        return new UserAccount(username, salt, passwordHash, true);
    }

    // Private constructor used by fromStored()
    private UserAccount(String username, String salt, String passwordHash, boolean alreadyHashed) {
        this.username     = username;
        this.salt         = salt;
        this.passwordHash = passwordHash;
    }

    // ─────────────────────────────────── accessors ──

    public String getUsername()     { return username; }
    public String getSalt()         { return salt; }
    public String getPasswordHash() { return passwordHash; }

    /** @return true if the supplied plain-text password matches the stored hash. */
    public boolean checkPassword(String plainPassword) {
        return hash(plainPassword, salt).equals(passwordHash);
    }

    // ─────────────────────────────────── crypto ──

    private static String generateSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String hash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] digest = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
