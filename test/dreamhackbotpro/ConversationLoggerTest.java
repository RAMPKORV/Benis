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
public class ConversationLoggerTest {
    
    private ConversationLogger x;

    public ConversationLoggerTest() {
        x = new ConversationLogger();
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
    public void testGetName() {
        assertEquals("RAMPKORV_WASD.txt", x.getName("WASD","RAMPKORV"));
        assertEquals("haX-arn-_ye--ye.txt", x.getName("haX/arn^","ye´_ye"));
    }

}