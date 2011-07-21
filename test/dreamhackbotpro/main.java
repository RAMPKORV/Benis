/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class main {
    public static void main(String[] args) {
        ChatObservable logReader = new LogReader();
        Bot bot = new Bot();
        logReader.addChatListener(bot);
        GUI gui = new GUI();
        logReader.addChatListener(gui);
        Conversation.addConversationsListener(gui);
        SeatReader seatReader = new SeatReader();
        Conversation.addConversationsListener(seatReader);
    }
}
