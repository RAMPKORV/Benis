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
    private String buyer;
    private String seller;

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
        buyer = "WASD";
        seller = "RAMPKORV";
        //WASD köper cigg för 50kr
        //RAMPKORV säljer headset för 200kr
        conversation = new Conversation(new User(buyer), new User(seller), "cigg", "headset", 50, 200);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testBuyerMessages() {
        //assertTransformBuyer("Du heter Monsquaz","Du heter RAMPKORV"); //behaviour uses Monsquaz as the name of the bot
        //assertTransformBuyer("Jag heter WASD","Jag heter Monsquaz");
        assertTransformBuyer("Jag köper cigg", "Jag köper headset");
        assertTransformBuyer("Jag köper ciggen", "Jag köper headset");
        assertTransformBuyer("Jag köper cig", "Jag köper headset");
        assertTransformBuyer("Du får 50 för den", "Du får 200 för den"); //"50kr" blir "200" utan kr
        assertTransformBuyer("Du får 50kr för den", "Du får 200kr för den");
        assertTransformBuyer("Du får 50 kr för den", "Du får 200 kr för den");
        assertTransformBuyer("Du får 100 för den", "Du får 400 för den");
        assertTransformBuyer("Du får 25 för den", "Du får 100 för den");
        conversation = new Conversation(new User(buyer), new User(seller), "cigg", "headset", 60, 400);
        assertTransformBuyer("60kr låter som ett riktigt kap. 70kr skulle vara bra med.", "400kr låter som ett riktigt kap. 470kr skulle vara bra med.");
    }

    @Test
    public void testSellerMessages() {
        assertTransformSeller("Jag säljer headset","Jag säljer cigg");
        assertTransformSeller("Bud på headsetet?","Bud på cigg?");
        assertTransformSeller("Kom hit med 200", "Kom hit med 50"); //"200kr" blir "50" utan kr
    }

    private void assertTransformBuyer(String input, String expected) {
        assertEquals(expected, behavior.transformMessage(conversation, new Message(buyer, input, seller)).getMessage());
    }

    private void assertTransformSeller(String input, String expected) {
        assertEquals(expected, behavior.transformMessage(conversation, new Message(seller, input, buyer)).getMessage());
    }

}