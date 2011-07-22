/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author patrik
 */
public class DelimeterFilterTest extends MessageFilterTest {

    public DelimeterFilterTest() {
        super(new DelimeterFilter());
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
     * Test of filter method, of class DelimeterFilter.
     */
    @Test
    public void testFilter() {
        checkFromTo("Jag vill ha snus!!","Jag vill ha snus.");
        checkFromTo("Jag vill ha snus..","Jag vill ha snus.");
        checkFromTo("Jag vill ha snus!! Och cigg.","Jag vill ha snus. Och cigg.");
        checkFromTo("Jag vill ha snus.. Och cigg.","Jag vill ha snus. Och cigg.");
        checkFromTo("Jag vill ha snus(ja, det vill jag)","Jag vill ha snus. ja, det vill jag.");
    }

}