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
public class WhiteSpaceCleanerFilterTest extends MessageFilterTest {

    public WhiteSpaceCleanerFilterTest() {
        super(new WhiteSpaceCleanerFilter());
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
        checkFromTo(" wtb     snus ","wtb snus");
        checkFromTo("      wtb    kuk           snus ","wtb kuk snus");
    }

}