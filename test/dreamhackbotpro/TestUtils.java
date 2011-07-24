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
 * @author wasd
 */
public class TestUtils {

    public TestUtils() {
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
    public void testRoundPrice() {
        assertEquals(7, Utils.roundPrice(7));
        assertEquals(25, Utils.roundPrice(26));
        assertEquals(40, Utils.roundPrice(42));
        assertEquals(70, Utils.roundPrice(68));
        assertEquals(160, Utils.roundPrice(155));
        assertEquals(750, Utils.roundPrice(742));
        assertEquals(1650, Utils.roundPrice(1666));
    }
}
