import org.apache.commons.collections4.MultiValuedMap;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class AudienceApp {
    public static void main(String[] args) throws IOException {
        InputStatementGenerator generator = new InputStatementGenerator();
        AudienceAppUtil audienceAppUtil = new AudienceAppUtil();
        Path inputPath;
        if(args.length>0 && args[0]!=null){
             inputPath = Paths.get(args[0]);
        }else{
            Path outputPath = Paths.get("src\\main\\resources\\StatementsDescending.txt");
            generator.generate(5000000, outputPath);
            inputPath = Paths.get("src\\main\\resources\\StatementsDescending.txt");
        }
        Calendar c1 =Calendar.getInstance();
        System.out.println("read data ...");
        MultiValuedMap<Integer, Statement> map= audienceAppUtil.readStatements(inputPath);
        Calendar c2 =Calendar.getInstance();
        System.out.print("data read time: ");
        System.out.println(c2.getTimeInMillis()-c1.getTimeInMillis());
        audienceAppUtil.calculateAudience(map);
        Calendar c3 =Calendar.getInstance();
        System.out.print("overall time: ");
        System.out.println(c3.getTimeInMillis()-c1.getTimeInMillis());
    }
}
