/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author patrik
 */
public class IrcHandler extends PircBot implements ConversationsListener {
    //TODO extend PircBot
    
    private List<IrcListener> listeners = new ArrayList<IrcListener>();

    public IrcHandler() {
    }

    public void addIrcListener(IrcListener l) {
        listeners.add(l);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        for(IrcListener l : listeners) {
            l.onMessage(new Message(sender, message, null));
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        for(IrcListener l : listeners) {
            l.onPrivateMessage(new Message(sender, message, null));
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        for(IrcListener l : listeners) {
            l.onNameChange(oldNick,newNick);
        }
    }

    private void onQuit(String nick) {
        for(IrcListener l : listeners) {
            l.onQuit(nick);
        }
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        onQuit(sourceNick);
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        onQuit(sender);
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        onQuit(recipientNick);
    }

    @Override
    public void onConversationMessage(Message m) {
        this.sendMessage(m.to, m.message);
    }
}
