package ma.youcode.lineperm.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ma.youcode.lineperm.dao.LogDao;
import ma.youcode.lineperm.model.AccessLog;

public class LogAnalyzerService {

    private final LogDao logDao;

    public LogAnalyzerService() {
        this.logDao = new LogDao();
    }

    public boolean saveLog(String user, String action, String file, String result) {

        String date = LocalDate.now().toString();
        String heure = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        AccessLog log = new AccessLog(date, heure, user, action, file, result);

        return logDao.save(log);
    }

    public long totalActione() {
        return logDao.compterTotal();
    }

    public long totalRefuse() {
        return logDao.compterRefuses();
    }

    public List<String> getDestincUser() {
        return logDao.userDistincts();
    }

    public Map<String, Long> actionByUser() {
        return logDao.actionsByUser();
    }

    public List<Map.Entry<String, Long>> actionByFile() {
        return logDao.topFichiers(3);
    }

    public List<AccessLog> getReAccessByUser(String user) {
        return logDao.refusesByUser(user);
    }

    public Optional<String> getMostActiveUser() {
        return logDao.userPlusActif();
    }

    public Map<String, Long> actionByType() {
        return logDao.repartitionByAction();
    }
}
