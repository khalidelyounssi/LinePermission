package ma.youcode.lineperm.ui;

import java.util.Scanner;

import java.util.Map;
import java.util.Optional;

import ma.youcode.lineperm.model.AccessLog;
import ma.youcode.lineperm.service.LogAnalyzerService;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;
import ma.youcode.lineperm.service.FileService;
import java.util.List;

public class ConsoleApp {
    private final UserService userService;
    private final FileService fileService;
    private final LogAnalyzerService logAnalyzerService;

    private final Scanner scanner;
    private User currentUser;
    private boolean running;

    public ConsoleApp(UserService userService,FileService fileService,LogAnalyzerService logAnalyzerService) {
        this.userService = userService;
    this.fileService = fileService;
    this.logAnalyzerService = logAnalyzerService;

    this.scanner = new Scanner(System.in);
    this.currentUser = null;
    }

   
    public void start() {

    running = true;

    System.out.println("Bienvenue dans LinePermission.");
    System.out.println("Ecrivez exit pour quitter.");

   while (running) {

    printPrompt();

    if (!scanner.hasNextLine()) {
        break;
    }

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
        case "chmod":
        handleChmod(argument);
        break;
        case "stats":
            handleStats();
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
    System.out.flush();
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

    if (name == null || name.trim().isEmpty()) {
        System.out.println("Utilisation : nano <nomFichier>");
        return;
    }

    name = name.trim();

    if (fileService.findFile(name) == null) {
        System.out.println("Fichier introuvable.");
        return;
    }

    if (!fileService.canWriteFile(name,currentUser.getLogin())) {
        System.out.println("Permission d'écriture refusée.");
        return;
    }

    System.out.println("Écrivez le contenu du fichier.");
    System.out.println("Écrivez EOF dans une nouvelle ligne pour terminer.");

    StringBuilder contenu = new StringBuilder();

    while (scanner.hasNextLine()) {

        String ligne = scanner.nextLine();

        if ("EOF".equals(ligne.trim())) {
            break;
        }

        if (contenu.length() > 0) {
            contenu.append(System.lineSeparator());
        }

        contenu.append(ligne);
    }

    boolean written = fileService.writeFile(name, currentUser.getLogin(),contenu.toString());

    if (written) {
        System.out.println("Fichier modifié.");
    } else {
        System.out.println("Écriture impossible.");
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

    if (fileService.findFile(name) == null) {
        System.out.println("Fichier introuvable.");
        return;
    }

    String contenu = fileService.readFile(name,currentUser.getLogin());

    if (contenu == null) {
        System.out.println("Lecture impossible ou permission refusée.");
        return;
    }

    System.out.println(contenu);
}




private void handleChmod(String argument) {

    if (currentUser == null) {
        System.out.println("Aucun utilisateur connecté.");
        return;
    }

    if (argument == null || argument.trim().isEmpty()) {
        System.out.println("Utilisation : chmod <r|w|d|-r|-w|-d> <nomFichier>");
        return;
    }

    String[] parts = argument.trim().split("\\s+", 2);

    if (parts.length != 2) {
        System.out.println("Utilisation : chmod <r|w|d|-r|-w|-d> <nomFichier>");
        return;
    }

    String permissionArgument = parts[0];
    String fileName = parts[1].trim();

    boolean remove = permissionArgument.startsWith("-");

    String permissionText;

    if (remove) {
        permissionText = permissionArgument.substring(1);
    } else {
        permissionText = permissionArgument;
    }

    if (permissionText.length() != 1) {
        System.out.println("Permission invalide.");
        return;
    }

    char permission = permissionText.charAt(0);

    if (permission != 'r'&& permission != 'w'&& permission != 'd') {
        System.out.println("Permission invalide.");
        return;
    }

    if (fileService.findFile(fileName) == null) {
        System.out.println("Fichier introuvable.");
        return;
    }

    boolean success;

    if (remove) {
        success = fileService.removePermission(fileName,currentUser.getLogin(),permission);
    } else {
        success = fileService.grantPermission(fileName,currentUser.getLogin(),permission);
    }

    if (!success) {
        System.out.println("Permission denied.");
        return;
    }

    if (remove) {
        System.out.println("Droit retiré.");
    } else {
        System.out.println("Droit accordé.");
    }
}

    private void handleStats() {

    boolean statsRunning = true;

    while (statsRunning) {

        System.out.println("\n----- Statistiques ---");
        System.out.println("1 - Total des actions");
        System.out.println("2 - Accès refusés");
        System.out.println("3 - Utilisateurs");
        System.out.println("4 - Actions par utilisateur");
        System.out.println("5 - Top 3 fichiers");
        System.out.println("6 - Refus d'un utilisateur");
        System.out.println("7 - Utilisateur le plus actif");
        System.out.println("8 - Actions par type");
        System.out.println("0 - Retour");

        System.out.print("Choix : ");
        String choix = scanner.nextLine().trim();

        switch (choix) {

            case "1":
                System.out.println("Total : " + logAnalyzerService.totalActione()
                );
                break;

            case "2":
                System.out.println("Refusés : " + logAnalyzerService.totalRefuse()
                );
                break;

            case "3":
                System.out.println(logAnalyzerService.getDestincUser()
                );
                break;

            case "4":
                System.out.println(logAnalyzerService.actionByUser()
                );
                break;

            case "5":
                System.out.println(logAnalyzerService.actionByFile()
                );
                break;

            case "6":
                    System.out.print("Utilisateur : ");
                    String user = scanner.nextLine().trim();

                    List<AccessLog> refus =
                            logAnalyzerService.getReAccessByUser(user);

                    if (refus.isEmpty()) {System.out.println("Aucun refus.");
                    } else {
                        for (AccessLog log : refus) {System.out.println(log.getDate() + " " +log.getHeure() + " " +log.getAction() + " " +log.getFichier() + " " +log.getResultat() );}
                    }
                    break;

            case "7":
                Optional<Map.Entry<String, Long>> mostActive =logAnalyzerService.getMostActiveUser();

                if (mostActive.isPresent()) {
                    Map.Entry<String, Long> result = mostActive.get();
                    System.out.println(result.getKey() + " : " + result.getValue()
                    );
                } else {
                    System.out.println("Aucune donnée.");
                }
                break;

            case "8":
                System.out.println(logAnalyzerService.actionByType()
                );
                break;

            case "0":
                statsRunning = false;
                break;

            default:
                System.out.println("Choix incorrect.");
        }
    }

}


















}





   
 
