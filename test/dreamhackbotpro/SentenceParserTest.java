package dreamhackbotpro;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patrik
 */
public class SentenceParserTest {

    private SentenceParser parser = SentenceParser.getInstance();

    public SentenceParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testParseSimple() {
        expectedInterest("WTB snus 50kr", "snus", 50, true);
        expectedInterest("WTS snus 50kr", "snus", 50, false);
        expectedInterest("WTB snus", "snus", -1, true);
    }
    
    @Test
    public void testParseCommon() {
        expectedInterest("WTS wow acc med två 80s", "wow acc", -1, false);
        expectedInterest("WTS powerking billigt", "powerking", -1, false); //Should a filter remove 'billigt'?
    }
    
    @Test
    public void testParseComplex() {
        expectedInterest("WTS ett paket cigg. Du får den för 50kr om du kommer snabbt.", "cigg", 50, false);
        expectedInterest("WTB CIGG PAKET. OÖPPNAT. DU FÅR 60kr.", "cigg", 60, true);
    }
    
    @Test
    public void testParseHard() {
        expectedInterest("WTS ett steel series shift tangent bord 400.", "tangentbord", 400, false);
        expectedInterest("För endast 45kr så WTS jag ett paket cigg.", "cigg", 45, false);
    }
    
    private void expectedInterest(String sentence, String thing, int price, boolean wtb){
        checkFromTo(sentence, thing+'-'+price+'-'+wtb);
    }
    
    private void checkFromTo(String from, String to) {
        assertEquals(to, parser.parseSentences(from).toString());
    }

}