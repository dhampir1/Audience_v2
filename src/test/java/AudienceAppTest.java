import static org.junit.Assert.*;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AudienceAppTest
{
    @Spy
    private final AudienceAppUtil audienceAppUtil = new AudienceAppUtil();

    MultiValuedMap<Integer, Statement> exampleInput1 = new ArrayListValuedHashMap<>();
    MultiValuedMap<Integer, Statement> exampleInput2;
    MultiValuedMap<Integer, Statement> exampleInput3;
    Statement statement1 = new Statement(1, 101, LocalDateTime.of(2020, Month.of(5), 10, 10, 12, 20 ), "Live");
    Statement statement2 = new Statement(1, 102, LocalDateTime.of(2020, Month.of(5), 10, 10, 13, 20 ), "Playback");
    Statement statement3 = new Statement(1, 103, LocalDateTime.of(2020, Month.of(5), 10, 10, 14, 20 ), "Live");
    Statement statement4 = new Statement(2, 101, LocalDateTime.of(2020, Month.of(5), 10, 10, 10, 20 ), "Live");
    Statement statement5 = new Statement(2, 102, LocalDateTime.of(2020, Month.of(5), 10, 11, 10, 20 ), "Playback");
    Statement statement6 = new Statement(2, 103, LocalDateTime.of(2020, Month.of(5), 11, 10, 10, 20 ), "Live");

    List<OutputStatement> expectedOutput1 = new ArrayList<>();
    OutputStatement outputStatement1 = new OutputStatement(1, 101, LocalDateTime.of(2020, Month.of(5), 10, 10, 12, 20 ),
            LocalDateTime.of(2020, Month.of(5), 10, 10, 13, 20 ), "Live", 60);
    OutputStatement outputStatement2 = new OutputStatement(1, 102, LocalDateTime.of(2020, Month.of(5), 10, 10, 13, 20 ),
            LocalDateTime.of(2020, Month.of(5), 10, 10, 14, 20 ), "Playback", 60);
    OutputStatement outputStatement3 = new OutputStatement(1, 103, LocalDateTime.of(2020, Month.of(5), 10, 10, 14, 20 ),
            LocalDateTime.of(2020, Month.of(5), 11, 0, 0, 0 ), "Live", 49540);

    @Test
    public void calculate() {
        //given
        exampleInput1.put(1, statement1);
        exampleInput1.put(1, statement2);
        exampleInput1.put(1, statement3);
        expectedOutput1.add(outputStatement1);
        expectedOutput1.add(outputStatement2);
        expectedOutput1.add(outputStatement3);

        //when
        List<OutputStatement> outputStatements = audienceAppUtil.calculateAudience(exampleInput1);

        //then
        assertEquals(expectedOutput1.toString(), outputStatements.toString());
    }

    @Test
    public void calculateReversedInput() {
        //given
        exampleInput1.put(1, statement3);
        exampleInput1.put(1, statement2);
        exampleInput1.put(1, statement1);
        expectedOutput1.add(outputStatement1);
        expectedOutput1.add(outputStatement2);
        expectedOutput1.add(outputStatement3);

        //when
        List<OutputStatement> outputStatements = audienceAppUtil.calculateAudience(exampleInput1);

        //then
        assertEquals(expectedOutput1.toString(), outputStatements.toString());
    }
}