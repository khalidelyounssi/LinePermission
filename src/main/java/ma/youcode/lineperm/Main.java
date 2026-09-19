package ma.youcode.lineperm;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;
import ma.youcode.lineperm.ui.ConsoleApp;
import ma.youcode.lineperm.service.LogAnalyzerService;

public class Main{

    public static void main(String[] args) {

        UserService userService = new UserService();
        FileService fileService = new FileService();
        LogAnalyzerService logAnalyzerService =new LogAnalyzerService();

        ConsoleApp consoleApp =new ConsoleApp(userService, fileService,logAnalyzerService);


        consoleApp.start();


       
    
    }
}