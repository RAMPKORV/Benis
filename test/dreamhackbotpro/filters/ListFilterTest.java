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
public class ListFilterTest extends MessageFilterTest {

    public ListFilterTest() {
        super(new ListFilter());
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
        checkFromTo("WTB A, B, C","WTB A. WTB B. WTB C");
        checkFromTo("Hej.","Hej.");
        checkFromTo("Hej","Hej");
        checkFromTo("WTB fint snus, musmattor, WTS cigg, liggunderlag","WTB fint snus. WTB musmattor. WTS cigg. WTS liggunderlag");
    }

}