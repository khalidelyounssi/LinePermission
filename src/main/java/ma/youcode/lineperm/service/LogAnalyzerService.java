package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ma.youcode.lineperm.model.AccessLog;

public class LogAnalyzerService {


    private final List<AccessLog> logs = new ArrayList<>();

    private final Path logFile = Path.of("data","access.log");

    private LogAnalyzerService(){
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
    
}
