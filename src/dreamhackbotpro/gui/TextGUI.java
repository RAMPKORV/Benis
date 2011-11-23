package dreamhackbotpro.gui;

import dreamhackbotpro.Conversation;
import dreamhackbotpro.ConversationsListener;
import dreamhackbotpro.Message;
import dreamhackbotpro.Utils;

/**
 * A ConversationsListener that outputs conversations in stdout.
 * It does not implement ChatListener since that would make it even harder to read conversations.
 * 
 * @author wasd
 */
public class TextGUI implements ConversationsListener{


    @Override
    public void onConversationMessage(Message m) {
        System.out.println(Utils.timeStamp()+m.getFrom().nick+" -> "+m.getTo().nick+": "+m.getMessage());
    }

    @Override
    public void onConversationClose(Conversation c) {
    }
    
}
