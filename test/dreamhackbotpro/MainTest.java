/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class MainTest {
    public static void main(String[] args) {
        LogReader logReader = new LogReader();
        Bot bot = new Bot();
        logReader.addChatListener(bot);
        GUI gui = new GUI();
        logReader.addChatListener(gui);
        Conversation.addConversationsListener(gui);
        SeatReader seatReader = new SeatReader();
        Conversation.addConversationsListener(seatReader);
        logReader.read("log4000.txt");
    }
}
