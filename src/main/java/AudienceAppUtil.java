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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public List<OutputStatement> calculateAudience (MultiValuedMap<Integer, Statement> map) {
         List<OutputStatement> outputStatements = new ArrayList<>();

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
                        outputStatements.add(new OutputStatement(key, statement1.getChannel(), statement1.getStartTime(), endTime, statement1.getActivity(), duration));
                    } else {
                        endTime = statement1.getStartTime().plusDays(1).withHour(0).withMinute(0).withSecond(0);
                        duration = ChronoUnit.SECONDS.between(statement1.getStartTime(), endTime);
                        outputStatements.add(new OutputStatement(key, statement1.getChannel(), statement1.getStartTime(), endTime, statement1.getActivity(), duration));
                    }
                    statement1 = statement2;
                }
                if (statement2 != null) {
                    endTime = statement2.getStartTime().plusDays(1).withHour(0).withMinute(0).withSecond(0);
                    duration = ChronoUnit.SECONDS.between(statement2.getStartTime(), endTime);
                    outputStatements.add(new OutputStatement(statement2.getHomeNumber(), statement2.getChannel(), statement2.getStartTime(), endTime, statement2.getActivity(), duration));
            }
        }
         return outputStatements;
    }

    public void saveToFile (List<OutputStatement> outputStatements){
         System.out.println("Saving output...");
         FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fileOutputStream != null) {
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println("HomeNo|Channel|StartTime|Activity|EndTime|Duration");
            outputStatements.forEach(printWriter::println);
            printWriter.close();
        }
    }

    private  static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().equals(date2.toLocalDate());
    }
}
