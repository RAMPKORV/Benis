package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IrcHandler irc = new IrcHandler("Monsquaz","irc.quakenet.org","#dreamhack.trade");
        Conversation.addConversationsListener(irc);
        Bot bot = new Bot();
        irc.addChatListener(bot);
        GUI gui = new GUI();
        irc.addChatListener(gui);
        Conversation.addConversationsListener(gui);
        SeatReader seatReader = new SeatReader();
        Conversation.addConversationsListener(seatReader);
        try {
            irc.connect();
        } catch (InterruptedException ex) {
            return;
        }
    }

}

