package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;

/**
 *
 * @author patrik
 */
public class MainTestLogReaderAndIrcHandler {
    public static void main(String[] args) {
        LogReader logReader = new LogReader();
        IrcHandler irc = new IrcHandler("Bot_"+System.getProperty("user.name"),"irc.esper.net",new String[]{"#trade_test"});
        Bot bot = new Bot();
        irc.addChatListener(bot);
        Conversation.addConversationsListener(irc);
        logReader.addChatListener(bot);
        GUI gui = new GUI();
        irc.addChatListener(gui);
        logReader.addChatListener(gui);
        Conversation.addConversationsListener(gui);
        SeatReader seatReader = new SeatReader();
        Conversation.addConversationsListener(seatReader);
//        logReader.read("log4000.txt");
        irc.connect();
    }
}
