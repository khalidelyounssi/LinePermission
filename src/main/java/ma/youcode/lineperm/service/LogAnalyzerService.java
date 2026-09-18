package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import  java.util.Map;

import ma.youcode.lineperm.model.AccessLog;

public class LogAnalyzerService {


    private final List<AccessLog> logs = new ArrayList<>();

    private final Path logFile = Path.of("data","access.log");

    public  LogAnalyzerService(){
        loadLogs();
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
            System.out.println("errur dont le fiche");

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

        AccessLog logLine = new AccessLog(parts[0],parts[1],parts[2],parts[3],parts[4],parts[5]);
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
}
