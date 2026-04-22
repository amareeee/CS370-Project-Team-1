import All_GUI.AccountManager;
import All_GUI.GUI;
import All_GUI.LoginPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AccountManager accountManager = new AccountManager();

            if (accountManager.isLoggedIn()) {
                //saved session found
                System.out.println("Welcome back," + accountManager.getCurrentUser().getUsername() + "!");

                new GUI(accountManager);
            } else {
                //show login page
                new LoginPage(accountManager, () -> SwingUtilities.invokeLater(() ->new GUI(accountManager)));
            }
        });
    }
}
