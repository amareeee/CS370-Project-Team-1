import All_GUI.GUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AccountManager accountManager = new AccountManager();

            if(accountManager.isLoggedIn()) {
                //saved session found
                System.out.print("Welcome back, " + accountManager.getCurrentUser().getUsername() + "!");

                new GUI(accountManager);
            } else {
                //show log in page
                new LoginPage(accountManager, () -> SwingUtilities.invokeLater(() -> new GUI(accountManager)));
            }
        });
    }
}
