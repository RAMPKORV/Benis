package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;

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
        logReader.addChatListener(seatReader);
        Conversation.addConversationsListener(seatReader);
//        logReader.read("log4000.txt");
    }
}
