package dreamhackbotpro;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.IrcUser;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author patrik
 */
public class IrcHandler extends PircBot implements ChatObservable, ConversationsListener {

    private String ircNick;
    private String ircServer;
    private String ircChannel;
    private volatile BotInfo info = null;
    private PreviousMessageChecker pMC = new PreviousMessageChecker();
    private Set<String> opUsers = new HashSet<String>();
    private Set<ChatListener> listeners = new HashSet<ChatListener>();
    private Set<String> leftUsers = new TreeSet<String>();

    public IrcHandler(String nick, String ircServer, String ircChannel) {
        this.ircNick = nick;
        this.ircServer = ircServer;
        this.ircChannel = ircChannel;
        info = new BotInfo(ircNick);
        setAutoNickChange(true);
        setLogin(System.getProperty("user.name"));
        try {
            setEncoding("ISO-8859-1");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            //happens when encoding not found
        }
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
        for(ChatListener l : listeners) {
            l.onError(msg);
        }
    }

    public void connect() throws InterruptedException {
        this.setName(ircNick);
        try {
            connect(ircServer);
            joinChannel(ircChannel);
            createNickChangeThread();
        } catch(Exception ex) {
            error(ex.getMessage());
        }
    }

    @Override
    public void addChatListener(ChatListener l) {
        listeners.add(l);
    }

    @Override
    public void removeChatListener(ChatListener l) {
        listeners.remove(l);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if(opUsers.contains(sender))
            return;

        if(pMC.contains(sender, message))
            return;
        pMC.add(sender, message);

        message = recode(message);
        
        for(ChatListener l : listeners) {
            l.onMessage(new Message(sender, message, null, info));
        }
    }

    private String recode(String message) {
        try {
            String recoded = new String(message.getBytes("ISO-8859-1"));
            if(recoded.length()!=message.length()){
                message=recoded;
            }
        } catch (Exception ex) {
            //happens when ISO is not found
            error(ex.getMessage());
        }
        return message;
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if(opUsers.contains(sender))
            return;
        message = recode(message);
        for(ChatListener l : listeners) {
            l.onPrivateMessage(new Message(sender, message, null, info));
        }
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if(oldNick.equals(info.getNick())) {
            updateBotNick(newNick);
            return;
        }
        if(opUsers.contains(oldNick)) {
            opUsers.remove(oldNick);
            opUsers.add(newNick);
        } else {
            for(ChatListener l : listeners) {
                l.onNameChange(oldNick,newNick);
            }
        }
    }

    private void onQuit(String nick) {
        if(opUsers.contains(nick)) {
            opUsers.remove(nick);
        } else {
            for(ChatListener l : listeners) {
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
        leftUsers.add(sender);
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
        String botNick = m.getBotInfo().getNick();
        if(botNick.equals(info.getNick())) {
            if(opUsers.contains(m.getTo())) {
                return;
            }
            this.sendMessage(m.getTo(), m.getMessage());
        }
    }

    public void updateBotNick(String newNick) {
        info = new BotInfo(newNick);
    }

    private void createNickChangeThread() {
        new Thread(new Runnable(){
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(20 * 60 * 1000);
                    } catch (InterruptedException ex) {
                        return;
                    }
                    String nickBefore = IrcHandler.this.getNick();
                    List<String> leftUsersCopy = null;
                    synchronized (leftUsers) {
                        leftUsersCopy = new ArrayList<String>(leftUsers);
                    }
                    if(!leftUsersCopy.isEmpty()) {
                        for(String nick : leftUsersCopy) {
                            IrcHandler.this.changeNick(nick);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                return;
                            }
                            if(IrcHandler.this.getNick().equals(nickBefore)) {
                                leftUsers.remove(nick);
                            } else {
                                leftUsers.remove(nickBefore);
                                break;
                            }
                        }
                    }
                }
            }
        }).start();
    }
}
