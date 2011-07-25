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
public class DefaultConversationBehaviorTest {

    private ConversationBehavior behavior = DefaultConversationBehavior.getInstance();
    private Conversation conversation;
    private String from;
    private String to;

    public DefaultConversationBehaviorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        from = "WASD";
        to = "RAMPKORV";
        //WASD köper cigg för 50kr
        //RAMPKORV säljer headset för 200kr
        conversation = new Conversation(new User(from), new User(to), "cigg", "headset", 50, 200);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testTransformMessage() {
        assertTransform("Du heter Monsquaz","Du heter RAMPKORV"); //behaviour uses Monsquaz as the name of the bot
        assertTransform("Jag köper cigg", "Jag köper headset");
        assertTransform("Du får 50 för den", "Du får 200 för den"); //"50kr" blir "200" utan kr
    }

    private void assertTransform(String input, String expected) {
        assertEquals(expected, behavior.transformMessage(conversation, new Message(from, input, to)).getMessage());
    }

}