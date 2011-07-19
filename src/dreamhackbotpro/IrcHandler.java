/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dreamhackbotpro;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author patrik
 */
public class IrcHandler extends PircBot implements ConversationsListener {

    private String ircNick;
    private String ircServer;
    private String ircChannel;
    
    private Set<IrcListener> listeners = new HashSet<IrcListener>();

    public IrcHandler(String nick, String ircServer, String ircChannel) {
        this.ircNick = nick;
        this.ircServer = ircServer;
        this.ircChannel = ircChannel;
    }

    public String getChannel() {
        return ircChannel;
    }

    public void setChannel(String ircChannel) {
        this.ircChannel = ircChannel;
    }

    public String getIrcServer() {
        return ircServer;
    }

    public void setIrcServer(String ircServer) {
        this.ircServer = ircServer;
    }

    public String getIrcNick() {
        return ircNick;
    }

    public void setIrcNick(String ircNick) {
        this.ircNick = ircNick;
    }




    private void error(String msg) {
        for(IrcListener l : listeners) {
            l.onError(msg);
        }
    }

    public void connect() throws InterruptedException {
        int connectionAttempts = 0;
        String attemptedNick = null;
        do {
            attemptedNick = ircNick + (connectionAttempts > 0 ? connectionAttempts : "");
            this.setName(attemptedNick);
            try {
                try {
                    connect(ircServer);
                } catch (IOException ex) {
                    error("IOException");
                } catch (NickAlreadyInUseException ex) {
                    error("Användarnamnet \""+attemptedNick+"\" är upptaget");
                } catch (IrcException ex) {
                    error("IRC Exception");
                }
            }  finally {
                connectionAttempts++;
            }
        } while (this.isConnected() == false);
    }

    public void addIrcListener(IrcListener l) {
        listeners.add(l);
    }

    public void removeIrcListener(IrcListener l) {
        listeners.remove(l);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        //TODO return if sender is OP
        for(IrcListener l : listeners) {
            l.onMessage(new Message(sender, message, null));
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        //TODO return if sender is OP
        for(IrcListener l : listeners) {
            l.onPrivateMessage(new Message(sender, message, null));
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        //TODO return if oldNick/newNick is OP
        for(IrcListener l : listeners) {
            l.onNameChange(oldNick,newNick);
        }
    }

    private void onQuit(String nick) {
        //TODO return if nick is OP
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
        this.sendMessage(m.getTo(), m.getMessage());
    }
}
