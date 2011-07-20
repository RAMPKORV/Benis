/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
        expectedInterest("WTS wow acc med två 80s", "wow acc", -1, false);
    }
    
    private void expectedInterest(String sentence, String thing, int price, boolean wtb){
        checkFromTo(thing, thing+'-'+price+'-'+wtb);
    }
    
    private void checkFromTo(String from, String to) {
        assertEquals(to, parser.parseSentences(from).toString());
    }

}