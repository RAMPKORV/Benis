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
public class MessageFilterTest {

    private MessageFilter filter = new MessageFilter();

    public MessageFilterTest() {
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

    private void checkFromTo(String from, String to) {
        Message original;
        Message filtered;
        original = new Message(from);
        filtered = filter.getFiltered(original);
        assertEquals(filtered.getMessage(), to);
    }

    // Check that good messages remain the same
    private void checkUnfiltered(String message) {
        checkFromTo(message, message);
    }

    @Test
    public void testWTBSimple() {
        checkFromTo("wtb snus", "WTB snus");
        checkFromTo("köper snus", "WTB snus");
        checkFromTo("jag köper snuss", "WTB snus");
        checkFromTo("Jag vill ha snus", "WTB snus");
    }

    @Test
    public void testWTSSimple() {
        checkFromTo("wts snus", "WTs snus");
        checkFromTo("säljer snus", "WTS snus");
        checkFromTo("jag säljer snuss", "WTS snus");
        checkFromTo("snus säljes", "WTS snus");
    }
    
    @Test
    public void testWTBNormal() {
        checkFromTo("||WTB CIGG & TÄNDARE||SÄLJER SKIN TILL TRYNDA OCH SINGED||", "WTB CIGG & TÄNDARE");
    }
    
    @Test
    public void testWTSNormal() {
        checkFromTo("Säljer Snus! Original portion!", "WTS Snus");
        checkFromTo("WTB CIGG PAKET. OÖPPNAT!!!!! DU FÅR 60kr!!!", "WTB CIGG PAKET 60kr");
    }

    @Test
    public void testWTBComplex() {
        checkFromTo("Någon som säljer<--------> WOW GULD?!!!!!", "WTB wow guld");
    }

    @Test
    public void testWTSComplex() {
        checkFromTo("säljer wow acc med spel tid biligt har 3 85 warrior,hunter,shamman alla har fult pvp gear men shamman och warriorn har dugligt gear för pve runt 353+ IL  ",
                "WTS wow acc");
        checkFromTo("-------| STEELSERIES SIBERIA NECKBAND HEADSET TILL SALU, SKRIV BUD! |-----------", "WTS STEELSERIES SIBERIA NECKBAND HEADSET");
        checkFromTo("SÄLJER ICECOFFEE  , Sockerdricka och dextro mint BILLIGT!!!!!!!!!", "WTS ICECOFFEE. WTS Sockerdricka. WTS dextro mint");
        checkFromTo("musmatta steelseries 4HD Small s?ljes 100kr!", "WTS musmatta steelseries 4HD Small 100kr");
        checkFromTo("ACE EDGE3200 gaming mouse säljes eller bytes mot en mionix naos5000 PM!", "WTS ACE EDGE3200 gaming mouse");
        checkFromTo("WTS min ljusblå mössa/min rosa mössa 50 kr st", "WTS ljusblå mössa 50kr. WTS rosa mössa 50kr");
        checkFromTo("Säljer Kämd Razer mus, den som ger bud får köpa innom 1 minut!!!!!!", "WTS Kämd Razer mus");
        checkFromTo("säljer ett steel series shift tangent bord 400", "WTS steel series shift tangent bord 400");
        checkFromTo("Säljer Keyboard Ace AC-KC6 100kr. och Ikari optical mus! 150kr", "WTS Keyboard Ace AC-KC6 100kr. WTS Ikari optical mus 150kr");
    }

    @Test
    public void testCrapRemoval() {
        // Check that bad characters are removed
        checkFromTo("Jag  köper  snus",       "WTB snus");
        checkFromTo("Jag   köper   snus",     "WTB snus");
        checkFromTo("Jag    köper    snus",   "WTB snus");
        checkFromTo("Jag     köper     snus", "WTB snus");
        checkFromTo("Säljer xperia x1 >300kr<", "WTS xperia x1 300kr");
        checkFromTo("WTB FNATIC MATTA!!!", "WTB FNATIC MATTA");
        checkFromTo("wts powerkings pm", "WTS powerkings");
    }

}