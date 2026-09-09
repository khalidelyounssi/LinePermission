package ma.youcode.lineperm;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;;
import ma.youcode.lineperm.ui.ConsoleApp;


public class Main{

    public static void main(String[] args) {

        UserService userService = new UserService();

        ConsoleApp consoleApp = new ConsoleApp(userService);

        consoleApp.start();
    
    }
}