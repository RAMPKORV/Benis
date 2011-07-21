package dreamhackbotpro;


import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.IrcUser;
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

    private Set<String> opUsers = new HashSet<String>();
    private Set<IrcListener> listeners = new HashSet<IrcListener>();
    

    public IrcHandler(String nick, String ircServer, String ircChannel) {
        this.ircNick = nick;
        this.ircServer = ircServer;
        this.ircChannel = ircChannel;
    }

    @Override
    protected void onUserList(String channel, IrcUser[] users) {
        for(IrcUser u : users) {
            if(u.isOp()) {
                opUsers.add(u.getNick());
            }
        }
    }

    @Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        opUsers.add(recipient);
    }

    @Override
    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        //opUsers.remove(recipient);
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
        joinChannel(ircChannel);
    }

    public void addIrcListener(IrcListener l) {
        listeners.add(l);
    }

    public void removeIrcListener(IrcListener l) {
        listeners.remove(l);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if(opUsers.contains(sender))
            return;
        
        //TODO see BenchmarkPreviousMessageChecker
        
        for(IrcListener l : listeners) {
            l.onMessage(new Message(sender, message, null));
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if(opUsers.contains(sender))
            return;
        for(IrcListener l : listeners) {
            l.onPrivateMessage(new Message(sender, message, null));
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if(opUsers.contains(oldNick)) {
            opUsers.remove(oldNick);
            opUsers.add(newNick);
        } else {
            for(IrcListener l : listeners) {
                l.onNameChange(oldNick,newNick);
            }
        }
    }

    private void onQuit(String nick) {
        if(opUsers.contains(nick)) {
            opUsers.remove(nick);
        } else {
            for(IrcListener l : listeners) {
                l.onQuit(nick);
            }
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
    protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001VERSION " + Options.getInstance().getVersion() + "\u0001");
    }

    @Override
    protected void onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001FINGER " + Options.getInstance().getFinger() + "\u0001");
    }

    @Override
    public void onConversationMessage(Message m) {
        if(opUsers.contains(m.getTo()))
            return;
        this.sendMessage(m.getTo(), m.getMessage());
    }
}
