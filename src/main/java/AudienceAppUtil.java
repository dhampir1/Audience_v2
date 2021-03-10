import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class AudienceAppUtil {

     protected MultiValuedMap<Integer, Statement> readStatements(Path inputPath) throws IOException {
        MultiValuedMap<Integer, Statement> map = new ArrayListValuedHashMap<>();
        Files.lines(inputPath).skip(1).forEach(line-> {
            String[] fields = line.split("\\|");
            Integer homeNumber = Integer.parseInt(fields[0]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(fields[2], formatter);
            map.put(homeNumber, new Statement(homeNumber, Integer.parseInt(fields[1]), dateTime, fields[3]));
        });
        return map;
    }

    public void calculateAudience (MultiValuedMap<Integer, Statement> map) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fileOutputStream != null){
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println("HomeNo|Channel|StartTime|Activity|EndTime|Duration");
            try (ProgressBar pb = new ProgressBar("Calculate", map.keySet().size())) {
                for (Integer key : map.keySet()) {
                    Iterator<Statement> it = map.get(key).stream().sorted().iterator();
                    LocalDateTime endTime;

                    long duration;
                    Statement statement2 = null;
                    Statement statement1 = it.next();

                    while (it.hasNext()) {
                        statement2 = it.next();
                        if (isSameDay(statement1.getStartTime(), statement2.getStartTime())) {
                            endTime = statement2.getStartTime();
                            duration = ChronoUnit.SECONDS.between(statement1.getStartTime(), endTime);
                            OutputStatement outputStatement = new OutputStatement(key, statement1.getChannel(), statement1.getStartTime(), endTime, statement1.getActivity(), duration);
                            printWriter.println(outputStatement);
                        } else {
                            endTime = statement1.getStartTime().plusDays(1).withHour(0).withMinute(0).withSecond(0);
                            duration = ChronoUnit.SECONDS.between(statement1.getStartTime(), endTime);
                            OutputStatement outputStatement = new OutputStatement(key, statement1.getChannel(), statement1.getStartTime(), endTime, statement1.getActivity(), duration);
                            printWriter.println(outputStatement);
                        }
                        statement1 = statement2;
                    }
                    if (statement2 != null) {
                        endTime = statement2.getStartTime().plusDays(1).withHour(0).withMinute(0).withSecond(0);
                        duration = ChronoUnit.SECONDS.between(statement2.getStartTime(), endTime);
                        OutputStatement outputStatement = new OutputStatement(statement2.getHomeNumber(), statement2.getChannel(), statement2.getStartTime(), endTime, statement2.getActivity(), duration);
                        printWriter.println(outputStatement);
                    }
                    pb.step();
                }
            }
            printWriter.close();
        }
    }

    private  static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().equals(date2.toLocalDate());
    }
}
