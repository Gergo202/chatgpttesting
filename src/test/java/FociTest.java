import org.example.FociProgram;
import org.example.Match;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FociTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    private final String testFileName = "src/main/resources/meccs_test.txt"; // Módosítva

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
        createTestFile();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalSystemOut);
    }
    private void createTestFile() {
        try {
            File source = new File(testFileName);

            if (!source.exists()) {
                // Kezeljük az esetet, ha a forrásfájl nem létezik
                throw new FileNotFoundException("The source file 'meccs_test.txt' does not exist.");
            }

            File destination = new File(testFileName);
            Path sourcePath = source.toPath();
            Path destinationPath = destination.toPath();
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testMain() {
        FociProgram.main(new String[]{});
        String expectedOutput = "Hungary 3 points\n" +
                "Portugal 1 point\n" +
                "Germany 0 points\n" +
                "France 3 points\n";
        assertEquals(expectedOutput.trim(), outputStream.toString().trim());
    }
    @Test
    public void testMatchClass() {
        // Teszteljük a Match osztályt
        Match match = new Match(1, 2, 0, 1, 0, "TeamA", "TeamB");

        assertEquals(1, match.getRound());
        assertEquals(2, match.getHomeGoalsFullTime());
        assertEquals(0, match.getAwayGoalsFullTime());
        assertEquals(1, match.getHomeGoalsHalfTime());
        assertEquals(0, match.getAwayGoalsHalfTime());
        assertEquals("TeamA", match.getHomeTeam());
        assertEquals("TeamB", match.getAwayTeam());
        assertTrue(match.isWinningAtFullTime());
        assertFalse(match.isLosingAtHalfTime());
        assertFalse(match.isLoss());
        assertTrue(match.isHomeTeam("TeamA"));
        assertFalse(match.isAwayTeam("TeamA"));
        assertEquals("TeamA", match.getWinner());
        assertEquals("2-0", match.getFormattedResult());
        assertEquals("TeamA-TeamB: 2-0 (1-0)", match.toString());
    }

    @Test
    public void testMatchClassEquality() {
        // Tesztelés egy konkrét Match objektummal
        Match match1 = new Match(1, 2, 0, 1, 0, "TeamA", "TeamB");
        Match match2 = new Match(1, 2, 0, 1, 0, "TeamA", "TeamB");

        assertEquals(match1, match2);
    }
}
