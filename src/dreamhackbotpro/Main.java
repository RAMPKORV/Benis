package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;
import dreamhackbotpro.gui.TextGUI;
import java.awt.HeadlessException;

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
        IrcHandler irc = new IrcHandler("Bot_" + System.getProperty("user.name"), "irc.esper.net", "#trade_test2");
        Conversation.addConversationsListener(irc);
        Bot bot = new Bot();
        irc.addChatListener(bot);
        SeatReader seatReader;
        try {
            GUI gui = new GUI();
            irc.addChatListener(gui);
            Conversation.addConversationsListener(gui);
            seatReader = new SeatReader(true);
            seatReader.addSeatMentionListener(gui);
            
        } catch (HeadlessException ex) {
            System.out.println("No graphics found. Using TextGUI");
            TextGUI textGui = new TextGUI();
            Conversation.addConversationsListener(textGui);
            seatReader = new SeatReader(false);
        }

        Conversation.addConversationsListener(seatReader);
        
        ConversationLogger cl = new ConversationLogger();
        Conversation.addConversationsListener(cl);
        
        irc.connect();
    }
}

