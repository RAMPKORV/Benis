package dreamhackbotpro.gui;

import dreamhackbotpro.ChatListener;
import dreamhackbotpro.Conversation;
import dreamhackbotpro.ConversationsListener;
import dreamhackbotpro.Message;
import dreamhackbotpro.UserInfo;
import dreamhackbotpro.Utils;

/**
 * A ConversationsListener that outputs conversations in stdout.
 * 
 * @author wasd
 */
public class TextGUI implements ConversationsListener, ChatListener {


    @Override
    public void onConversationMessage(Message m) {
        System.out.println(Utils.timeStamp()+m.getFrom().nick+" -> "+m.getTo().nick+": "+m.getMessage());
    }

    @Override
    public void onConversationInactive(Conversation c) {
    }
    
    @Override
    public void onError(String error) {
        System.err.println(Utils.timeStamp()+error);  
    }

    // Empty ChatListener methods. These would bloat down the display.
    @Override
    public void onMessage(Message m) {}
    @Override
    public void onNameChange(UserInfo olduser, UserInfo newuser) {}
    @Override
    public void onPrivateMessage(Message m) {}
    @Override
    public void onQuit(UserInfo userInfo) {}
    @Override
    public void onServerMessage(String message) {}
    @Override
    public void onUserInfo(UserInfo ui) {}
    @Override
    public void onBotNickChange() {}
    
}
