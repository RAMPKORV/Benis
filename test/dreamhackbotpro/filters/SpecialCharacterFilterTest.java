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
public class SpecialCharacterFilterTest extends MessageFilterTest {

    public SpecialCharacterFilterTest() {
        super(new SpecialCharacterAndWhitespaceFilter());
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
     * Test of filter method, of class SpecialCharacterFilter.
     */
    @Test
    public void testFilter() {
        checkFromTo("köpes >>SNUS<<","köpes SNUS");
        checkFromTo("-------- SPAM --------","SPAM");
        checkFromTo("-_-_-_-_ SPAM -_-_-_-_","SPAM");
        checkFromTo("Köper ::: snus :::", "Köper snus");
        checkFromTo("Säljer snus: 20:-", "Säljer snus 20:-");
        checkFromTo("Jag säljer en --- >> X-53 << --- 57:-", "Jag säljer en X-53 57:-");
    }

}