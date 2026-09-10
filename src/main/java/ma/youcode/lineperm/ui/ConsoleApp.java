package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {
    private final UserService userService;

    private final Scanner scanner;
    private User currentUser;
    private boolean running;

    public ConsoleApp(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

   
    public void start() {

    running = true;

    System.out.println("Bienvenue dans LinePermission.");
    System.out.println("Ecrivez exit pour quitter.");

    while (running && scanner.hasNextLine()) {

        printPrompt();

        String command =scanner.nextLine().trim().toLowerCase();

        if (command.isEmpty()) {
            continue;
        }

        switch (command) {

    case "signup":
        handleSignup();
        break;

    case "login":
        handleLogin();
        break;

         case "logout":
        handleLogout();
        break;

    case "exit":
        running = false;
        System.out.println("Au revoir");
        break;

    default:
        System.out.println("Commande inconnue.");
}
    }
    scanner.close();
    }
    private void handleSignup() {

    if (currentUser != null) {
        System.out.println("deconnectez vous avant de creer un compte");
        return;
    }

    System.out.print("login : ");
    String login = scanner.nextLine();

    System.out.print("mot passe : ");
    String password = scanner.nextLine();

    boolean created =userService.createUser(login, password);

    if (created) {
        System.out.println("Compte cree avec succes.");
    } else {
        System.out.println(
                "impossible de créer le compte."
        );
    }
    }

    private void handleLogin() {

    if (currentUser != null) {
        System.out.println("Une session est deja ouverte");
        return;
    }

    System.out.print("Login : ");
    String login = scanner.nextLine();

    System.out.print("Mot de passe : ");
    String password = scanner.nextLine();

    User user =userService.authenticate(login, password);

    if (user == null) {
        System.out.println("identifiants incorrects.");
        return;
    }

    currentUser = user;

    System.out.println("connexion reussie.");
}


private void printPrompt() {

    if (currentUser == null) {
        System.out.print("linperm> ");
    } else {
        System.out.print(
                currentUser.getLogin() + "@linperm> "
        );
    }
}
private void handleLogout() {

    if (currentUser == null) {
        System.out.println(
                "Aucun utilisateur connecte"
        );
        return;
    }

    currentUser = null;

    System.out.println("Deconnexion reussie.");
}

   
 
}