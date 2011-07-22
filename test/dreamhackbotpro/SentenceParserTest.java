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

    private void checkThingFromTo(String from, String to) {
        assertEquals(to, parser.parseThing(from));
    }

    @Test
    public void testParseThing() {
        checkThingFromTo("stor snus","snus");
        checkThingFromTo("snus billigt","snus");
        checkThingFromTo("fnatic musmatta","musmatta");
        checkThingFromTo("en razer goliath musmatta","musmatta");
        checkThingFromTo("min mus Microsoft Intellimouse 3.0","mus");
        checkThingFromTo("cigg paket","cigg");
        checkThingFromTo("cigg pm","cigg");
        checkThingFromTo("steelseries siberia neckband headset","headset");
        checkThingFromTo("Fifa11 till Xbox360","Fifall");
        checkThingFromTo("en burg ansjovis","ansjovis");
        checkThingFromTo("sprit/tusch pennor eller något liknande","pennor");
    }

    @Test
    public void testParsePrice() {
        SentenceParser p = SentenceParser.getInstance();
        assertEquals(100, p.parsePrice("Du får 100 för dem."));
        assertEquals(-1, p.parsePrice("Jag sitter på D23:12"));
        assertEquals(-1, p.parsePrice("Jag sitter på D23 12"));
        assertEquals(-1, p.parsePrice("Jag sitter på D 23 12"));
        assertEquals(-1, p.parsePrice("Jag sitter på Rad D23 Plats 12"));
        assertEquals(-1, p.parsePrice("Jag sitter på Rad D 23 Plats 12"));
        assertEquals(150, p.parsePrice("150"));
        assertEquals(300, p.parsePrice("Kom till Rad D 23 Plats 12. Ta med 300kr så är den din."));
        assertEquals(100, p.parsePrice("Kom till Rad D 23 Plats 12. Ta med 100:- så är den din."));
        assertEquals(100, p.parsePrice("Kom till Rad D 23 Plats 12. Ta med 100 kr så är den din."));
        assertEquals(200, p.parsePrice("Kom till Rad D 23 Plats 12. Ta med 200 så är den din."));
        assertEquals(100, p.parsePrice("Kom till Rad D 23 Plats 12. Ta med en hundring så är den din."));
    }

}