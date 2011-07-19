/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
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
public class TradingSynonymFilterTest extends MessageFilterTest {

    public TradingSynonymFilterTest() {
    super(new TradingSynonymFilter());
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

    /**
     * Test of filter method, of class WhiteSpaceCleanerFilter.
     */
    @Test
    public void testFilter() {
        checkFromTo("jag köper snus","WTB snus");
        checkFromTo("jag vill ha snus","WTB snus");
        checkFromTo("köper snus","WTB snus");
        checkFromTo("wtb snus","WTB snus");
        checkFromTo("snus köpes","WTB snus");
        checkFromTo("Hej på er. Snus köpes","Hej på er. WTB Snus");
        checkFromTo("jag säljer snus","WTS snus");
        checkFromTo("jag vill sälja snus","WTS snus");
        checkFromTo("säljer snus","WTS snus");
        checkFromTo("wts snus","WTS snus");
        checkFromTo("snus säljes","WTS snus");
        checkFromTo("Hej på er. Snus säljes","Hej på er. WTS Snus");
    }

}