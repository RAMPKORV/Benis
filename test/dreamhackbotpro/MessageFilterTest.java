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

    private MessageFilter filter;

    public MessageFilterTest(MessageFilter implementation) {
        filter = implementation;
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
        assertEquals(filtered, to);
    }

    private void checkUnfiltered(String message) {
        checkFromTo(message, message);
    }

    @Test
    public void testWTBSimple() {
        // Check that good messages remain the same
        checkUnfiltered("wtb snus");
        checkUnfiltered("köper snus");
        checkUnfiltered("jag köper snus");
        checkUnfiltered("Jag vill ha snus");
    }

    @Test
    public void testWTSSimple() {
        // Check that good messages remain the same
        checkUnfiltered("wts snus");
        checkUnfiltered("säljer snus");
        checkUnfiltered("jag säljer snus");
        checkUnfiltered("snus säljes");
    }

    @Test
    public void testWTBComplex() {
    }

    @Test
    public void testWTSComplex() {
    }

    @Test
    public void testCrapRemoval() {
        // Check that bad characters are removed
        checkFromTo("Jag  köper  snus",       "Jag köper snus");
        checkFromTo("Jag   köper   snus",     "Jag köper snus");
        checkFromTo("Jag    köper    snus",   "Jag köper snus");
        checkFromTo("Jag     köper     snus", "Jag köper snus");
    }

}