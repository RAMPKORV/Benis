package dreamhackbotpro;

import dreamhackbotpro.gui.GUI;
import dreamhackbotpro.gui.TextGUI;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

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
        String server = Options.getInstance().getServer();
        String channel = Options.getInstance().getChannel();
        String name = "Bot_" + System.getProperty("user.name");
        name = "random"+Utils.random.nextInt(100);
        try {
            name = JOptionPane.showInputDialog("Enter name for bot");
        } catch (Exception e) {
        }
        IrcHandler irc = new IrcHandler(name, server, channel);
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

