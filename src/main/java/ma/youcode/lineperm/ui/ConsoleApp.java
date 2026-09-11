package ma.youcode.lineperm.ui;

import java.text.FieldPosition;
import java.util.Scanner;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;
import ma.youcode.lineperm.service.FileService;
import java.util.List;

public class ConsoleApp {
    private final UserService userService;
    private final FileService fileService;

    private final Scanner scanner;
    private User currentUser;
    private boolean running;

    public ConsoleApp(UserService userService,FileService fileService) {
        this.userService = userService;
    this.fileService = fileService;
    this.scanner = new Scanner(System.in);
    this.currentUser = null;
    }

   
    public void start() {

    running = true;

    System.out.println("Bienvenue dans LinePermission.");
    System.out.println("Ecrivez exit pour quitter.");

    while (running && scanner.hasNextLine()) {

        printPrompt();

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+", 2);

            String command = parts[0].toLowerCase();

            String argument = "";

            if (parts.length == 2) {
                argument = parts[1].trim();
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

        case "touch":
        handleTouch(argument);
        break;
        case "ls":
        handleLs(argument);
        break;
        case "nano":
        handleNano(argument);
        break;
        case "cat":
        handleCat(argument);
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

private void handleTouch(String name) {

    if (currentUser == null) {
        System.out.println("aucun utilisateur connecte");
        return;
    }

    if (name == null || name.isEmpty()) {
        System.out.println("le nom du fichier est obligatoire.");
        return;
    }

    boolean created =fileService.createFile(name,currentUser.getLogin());

    if (created) {
        System.out.println("Fichier cree");
    } else {
        System.out.println("impossible de creer le fichier.");
    }
}

private void handleLs(String option) {

    if (currentUser == null ) {
        System.out.println("Aucun utilisateur connecté.");
        return;
    }

    if (!"-l".equals(option)) {
        System.out.println("Utilisation : ls -l");
        return;
    }

    List<String> files = fileService.listFiles();

    if (files.isEmpty()) {
        System.out.println("Aucun fichier.");
        return;
    }

    for (String file : files) {
        System.out.println(file);
    }
}
private void handleNano(String name) {

    if (currentUser == null) {
        System.out.println("Aucun utilisateur connecté.");
        return;
    }

    if (name == null || name.isEmpty()) {
        System.out.println("utilisation : nano <nomFichier>");
        return;
    }

    if (fileService.findFile(name) == null) {
        System.out.println("Fichier introuvable.");
        return;
    }

    if (!fileService.canWriteFile(
            name,
            currentUser.getLogin()
    )) {
        System.out.println("Permission denied.");
        return;
    }

    System.out.println(
            "ecrivez le contenu. Tapez EOF pour terminer."
    );

    String contenu = "";

    while (scanner.hasNextLine()) {

        String line = scanner.nextLine();

        if (line.equals("EOF")) {
            break;
        }

        contenu = contenu + line + "\n";
    }

    boolean written = fileService.writeFile(name,currentUser.getLogin(),contenu);

    if (written) {
        System.out.println("Fichier modifié.");
    } else {
        System.out.println(
                "impossible de modifier le fichier."
        );
    }
}
private void handleCat(String name) {

    if (currentUser == null) {
        System.out.println("Aucun utilisateur connecté.");
        return;
    }

    if (name == null || name.trim().isEmpty()) {
        System.out.println("Utilisation : cat <nomFichier>");
        return;
    }

    name = name.trim();

    if (fileService.findFile(name) == null) {
        System.out.println("Fichier introuvable.");
        return;
    }

    String contenu = fileService.readFile(name,currentUser.getLogin());

    if (contenu == null) {
        System.out.println("Permission denied.");
    } else {
        System.out.println(contenu);
    }
}
}

   
 
