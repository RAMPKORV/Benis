package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;

/**
 *
 * @author patrik
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IrcHandler irc = new IrcHandler("Monsquaz","irc.esper.net","#trade_test");
        Conversation.addConversationsListener(irc);
        Bot bot = new Bot();
        irc.addChatListener(bot);
        GUI gui = new GUI();
        irc.addChatListener(gui);
        Conversation.addConversationsListener(gui);
        SeatReader seatReader = new SeatReader();
        Conversation.addConversationsListener(seatReader);
        irc.connect();
    }

}

