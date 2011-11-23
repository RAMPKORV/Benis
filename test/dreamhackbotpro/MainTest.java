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
        Conversation.addConversationsListener(new ConversationsListener(){
            @Override
            public void onConversationMessage(Message m) {
                System.out.println("Message sent:" + m);
            }
            @Override
            public void onConversationClose(Conversation c) {
            }
        });
        logReader.read("log4000.txt");
    }
}
