package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;

/**
 *
 * @author patrik
 */
public class Main {

    /**
     * IrcHandler sends messages to:
     * GUI
     * Bot
     *
     * Bot sends private messages to Conversation
     *
     * Conversation sends private messages to:
     * GUI
     * SeatReader
     * IrcHandler (to send them to the reciever)
     *
     */
    public static void main(String[] args) {
        IrcHandler irc = new IrcHandler("Bot_"+System.getProperty("user.name"),"irc.esper.net","#trade_test");
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

