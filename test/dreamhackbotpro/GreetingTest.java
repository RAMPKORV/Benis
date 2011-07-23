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
public class GreetingTest {

    public GreetingTest() {
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
     * Test of getGreeting method, of class Greeting.
     */
    @Test
    public void testGetGreeting() {
    }

    /**
     * Test of isGreeting method, of class Greeting.
     */
    @Test
    public void testIsGreeting() {
        assertEquals(null, Greeting.hasGreeting(""));
        assertEquals(null, Greeting.hasGreeting("Vad vill du?"));
        assertEquals(null, Greeting.hasGreeting("Vill du ha den eller?"));
        assertEquals(null, Greeting.hasGreeting("Varför skriver du till mig för?"));
        assertEquals("Hej", Greeting.hasGreeting("Hej"));
        assertEquals("Hej", Greeting.hasGreeting("Hej!"));
        assertEquals("Hej", Greeting.hasGreeting("Hej!!"));
        assertEquals("Hej", Greeting.hasGreeting("Hej!!!"));
        assertEquals("Hej", Greeting.hasGreeting("Hej!!!!"));
        assertEquals("Hej", Greeting.hasGreeting("Hej på dig."));
        assertEquals("tjenare", Greeting.hasGreeting("Nejmen tjenare."));
        assertEquals(true, Greeting.isSimpleGreeting("Hej"));
        assertEquals(true, Greeting.isSimpleGreeting("Hej på dig."));
        assertEquals(true, Greeting.isSimpleGreeting("Nejmen tjenare."));
        assertEquals(true, Greeting.isSimpleGreeting("Nejmen tjenare."));
        assertEquals(false, Greeting.isSimpleGreeting("Hej. Intresserad av av köpa chassit?"));
    }

}