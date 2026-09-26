package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import  java.util.Map;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

import ma.youcode.lineperm.model.AccessLog;

public class LogAnalyzerService {


    private final List<AccessLog> logs = new ArrayList<>();

    private final Path logFile = Path.of("data","access.log");

    public  LogAnalyzerService(){
        loadLogs();
    }

    public void seveLog(String user, String action, String file, String result) throws IOException {
    AccessLog log = new AccessLog(LocalDate.now(),LocalTime.now(),user,action,file,result
    );

    logs.add(log);
    String heure = log.getHeure().format(DateTimeFormatter.ofPattern("HH:mm"));

    String line = log.getDate() + ";"+ heure + ";"+ log.getUtilisateur() + ";"+ log.getAction() + ";"+ log.getFichier() + ";"+ log.getResultat()+ System.lineSeparator();

    
                Files.writeString(
                    logFile,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
    
}


    private void loadLogs(){
        if(!Files.exists(logFile)){
            return;
        }

        try{
            List<AccessLog> loadLogs = Files.readAllLines(logFile).stream()
                                                                .map(this::parseLine).filter(l->l!=null).collect(Collectors.toList());
                    logs.addAll(loadLogs);

        }catch(IOException e){
            System.out.println("Impossible de lire le fichier access.log.");

        }
    }

    public AccessLog parseLine(String line){
        if (line==null){
            return null;
        }
        line=line.trim();
        if(line.isEmpty()){
            return  null;
        }

        String parts[] = line.split(";",6);
        if(parts.length!=6){
            return null;
        }

        AccessLog logLine = new AccessLog(LocalDate.parse(parts[0]),LocalTime.parse(parts[1]),parts[2],parts[3],parts[4],parts[5]);
        return logLine;

        
    }

    public long totalActione(){
          long totalAc = logs.stream().count();
          return totalAc;
    }

    public long totalRefuse(){
        long totalRe = logs.stream().filter(l->"REFUSE".equals(l.getResultat())).count();
        return totalRe;
    }
    public List<String> getDestincUser(){
        List<String> destincUser = logs.stream().map(l->l.getUtilisateur()).distinct().sorted().collect(Collectors.toList());
        return destincUser;
    }

    public Map<String, Long> actionByUser(){

                 Map<String, Long> totalAcByUser = logs.stream().collect(Collectors.groupingBy(l->l.getUtilisateur(),Collectors.counting()));

                 return totalAcByUser;
    }
    public List<Map.Entry<String, Long>>  actionByFile(){
        Map<String, Long> totalByFile = logs.stream().collect(Collectors.groupingBy(l->l.getFichier(),Collectors.counting()));

        List<Map.Entry<String, Long>> top3File =totalByFile.entrySet().stream().sorted((f1,f2)->Long.compare(f2.getValue(),f1.getValue())).limit(3).collect(Collectors.toList());

        return top3File;
    }

    public  List<AccessLog> getReAccessByUser(String u){

        if(u==null){
            return new ArrayList<>();
        }
        String user = u.trim();

         if(user.isEmpty()){

        return new ArrayList<>();
            }
       

        List<AccessLog> accessByUser = logs.stream().filter(l-> l.getUtilisateur().equalsIgnoreCase(user)).filter(l->"REFUSE".equals(l.getResultat())).collect(Collectors.toList());
        return accessByUser;
    }

    public Optional<Map.Entry<String, Long>>getMostActiveUser() {

    Map<String, Long> actionsByUser =
            logs.stream().collect(Collectors.groupingBy(AccessLog::getUtilisateur,Collectors.counting()));

    return actionsByUser.entrySet().stream().max((user1, user2) ->Long.compare(user1.getValue(),user2.getValue())
            );
}

        public Map<String, Long> actionByType() {

    Map<String, Long> totalByAction =logs.stream().collect(Collectors.groupingBy(log -> log.getAction(),Collectors.counting()));

                    return totalByAction;
}
}
