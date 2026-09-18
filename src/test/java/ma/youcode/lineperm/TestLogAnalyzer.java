package ma.youcode.lineperm;

import java.util.List;
import java.util.Map;

import ma.youcode.lineperm.model.AccessLog;
import ma.youcode.lineperm.service.LogAnalyzerService;

public class TestLogAnalyzer {

    public static void main(String[] args) {

        LogAnalyzerService service = new LogAnalyzerService();

        String line = "2026-09-16;10:20;khalid;LECTURE;notes.txt;OK";

        AccessLog log = service.parseLine(line);

        // if (log == null) {
        //     System.out.println("Ligne invalide");
        // } else {
        //     System.out.println("Date : " + log.getDate());
        //     System.out.println("Heure : " + log.getHeure());
        //     System.out.println("Utilisateur : " + log.getUtilisateur());
        //     System.out.println("Action : " + log.getAction());
        //     System.out.println("Fichier : " + log.getFichier());
        //     System.out.println("Résultat : " + log.getResultat());
        // }

                AccessLog invalidLog = service.parseLine("ligne;invalide");

                System.out.println(
                        "Ligne invalide retourne null : "
                                + (invalidLog == null));

        long total = service.totalActione();

        System.out.println(
                "Nombre total d'actions : " + total);

        long refused = service.totalRefuse();

        System.out.println(
                "Nombre d'accès refusés : " + refused);

        List<String> users = service.getDestincUser();

        System.out.println(
                "Utilisateurs : " + users);




                List<Map.Entry<String, Long>> topFiles =
        service.actionByFile();

topFiles.forEach(
        file ->
            System.out.println(
                file.getKey() + " : " + file.getValue()
            )
);
            List<AccessLog> refusedLogs =
        service.getReAccessByUser(" KHALID ");

refusedLogs.forEach(
    refusedLog ->
        System.out.println(
            refusedLog.getAction()
            + " - "
            + refusedLog.getFichier()
        )
        
);

    }

    

    

}