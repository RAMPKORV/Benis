/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

/**
 *
 * @author patrik
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import dreamhackbotpro.Message;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author patrik
 */
public abstract class MessageFilterTest {

    protected MessageFilter mf;

    public MessageFilterTest(MessageFilter mf) {
        this.mf = mf;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception{}

    @Before
    public abstract void setUp();

    @After
    public abstract void tearDown();

    protected final void checkFromTo(String from, String to) {
        Message m = new Message(from);
        mf.filter(m);
        assertEquals(to, m.getMessage());
    }

    // Check that good messages remain the same
    protected final void checkUnfiltered(String message) {
        checkFromTo(message, message);
    }
    
}
