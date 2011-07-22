package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;

/**
 *
 * @author patrik
 */
public class MainTestLogReaderAndIrcHandler {
    public static void main(String[] args) {
        LogReader logReader = new LogReader();
        IrcHandler irc = new IrcHandler("Monsquaz","irc.esper.net","#trade_test");
        Bot bot = new Bot();
        irc.addChatListener(bot);
        logReader.addChatListener(bot);
        GUI gui = new GUI();
        irc.addChatListener(gui);
        logReader.addChatListener(gui);
        Conversation.addConversationsListener(gui);
        SeatReader seatReader = new SeatReader();
        Conversation.addConversationsListener(seatReader);
        logReader.read("log4000.txt");
        try {
            irc.connect();
        } catch (InterruptedException ex) {
            return;
        }
    }
}
