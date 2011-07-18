/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patrik
 */
public class IrcHandler implements ConversationsListener {
    //TODO extend PircBot
    
    private List<IrcListener> listeners = new ArrayList<IrcListener>();

    public IrcHandler() {
    }

    public void addIrcListener(IrcListener l) {
        listeners.add(l);
    }

    @Override
    public void onConversationMessage(Message m) {
    }
}
