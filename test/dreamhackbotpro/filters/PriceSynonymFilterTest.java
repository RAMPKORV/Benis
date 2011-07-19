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
import static org.junit.Assert.*;

/**
 *
 * @author wasd
 */
public class PriceSynonymFilterTest extends MessageFilterTest {

    public PriceSynonymFilterTest() {
        super(new PriceSynonymFilter());
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
     * Test of filter method, of class PriceSynonymFilter.
     */
    @Test
    public void testFilter() {
        checkFromTo("säljer snus 100 spänn!", "säljer snus 100kr!");
        checkFromTo("säljer 24 tum skärm för 2 lax", "säljer 24 tum skärm för 2000kr");
        checkFromTo("köper cigg 50:-", "köper cigg 50kr");
        checkFromTo("säljer 3 burkar läsk för 1 tjuga", "säljer 3 burkar läsk för 20kr");
        checkFromTo("säljer 3 burkar läsk för 3 tjugor", "säljer 3 burkar läsk för 60kr");
        checkFromTo("50 kr", "50kr");
    }

}