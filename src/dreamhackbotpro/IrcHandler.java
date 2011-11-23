package dreamhackbotpro;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private volatile UserInfo info = null;
    private PreviousMessageChecker pMC = new PreviousMessageChecker();
    private final Set<String> opUsers = new HashSet<String>();
    private final Map<String, UserInfo> usersMap = new HashMap<String, UserInfo>();
    private Set<ChatListener> listeners = new HashSet<ChatListener>();
    private final Collection<String> leftUsers = new ArrayList<String>();
    private Thread nickChanger = null;
    private long lastActivity = System.currentTimeMillis();
    private boolean reconnectorStarted = false;

    public IrcHandler(String nick, String ircServer, String ircChannel) {
        this.ircNick = nick;
        this.ircServer = ircServer;
        this.ircChannel = ircChannel;
        setMessageDelay(1500);
        setAutoNickChange(true);
        //setVerbose(true);
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
        synchronized (usersMap) {
            for (IrcUser u : users) {
                if (u.isOp()) {
                    synchronized (opUsers) {
                        opUsers.add(u.getNick());
                    }
                }
                usersMap.put(u.getNick(), new UserInfo(u.getNick()));
            }
        }
    }

    @Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        synchronized (opUsers) {
            opUsers.add(recipient);
        }
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
        for (ChatListener l : listeners) {
            l.onError(msg);
        }
    }

    public void connect() {
        this.setName(ircNick);
        try {
            connect(ircServer);

        } catch (Exception ex) {
            error(ex.getMessage());
        }
        if(!reconnectorStarted) {
            startReconnectorThread();
        }
    }

    @Override
    protected void onConnect() {
        lastActivity = System.currentTimeMillis();
        info = new UserInfo(ircNick, this.getLogin(), getInetAddress().getHostName(), getInetAddress().getHostAddress());
        joinChannel(ircChannel);
        createNickChangeThread();
    }

    @Override
    protected void onDisconnect() {
        while (!isConnected()) {
            try {
                reconnect();
            } catch (Exception e) {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException ex) {
                    return;
                }
            }
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
    protected void onAction(String sender, String login, String hostname, String target, String action) {
        onMessage(target, sender, login, hostname, action);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        lastActivity = System.currentTimeMillis();
        synchronized (opUsers) {
            if (opUsers.contains(sender)) {
                return;
            }
        }

        if (pMC.contains(sender, message)) {
            return;
        }
        pMC.add(sender, message);

        message = recode(message);
        UserInfo senderInfo;
        synchronized (usersMap) {
            senderInfo = usersMap.get(sender);
            if (senderInfo == null || senderInfo.requireWhois()) {
                senderInfo = new UserInfo(sender, login, hostname, ipFromHost(hostname));
                usersMap.put(sender, senderInfo);
            }
        }
        for (ChatListener l : listeners) {
            l.onMessage(new Message(senderInfo, message, null, info));
        }

    }

    private String recode(String message) {
        try {
            String recoded = new String(message.getBytes("ISO-8859-1"));
            if (recoded.length() != message.length()) {
                message = recoded;
            }
        } catch (Exception ex) {
            //happens when ISO is not found
            error(ex.getMessage());
        }
        return message;
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        lastActivity = System.currentTimeMillis();
        if (sender.equals("S")) {
            handleS(message);
            return;
        }
        synchronized (opUsers) {
            if (opUsers.contains(sender)) {
                error("Contacted by OP: " + sender + ": " + message);
                return;
            }
        }
        message = recode(message);
        UserInfo senderInfo;
        synchronized (usersMap) {
            senderInfo = usersMap.get(sender);
            if (senderInfo == null || senderInfo.requireWhois()) {
                senderInfo = new UserInfo(sender, login, hostname, ipFromHost(hostname));
                usersMap.put(sender, senderInfo);
            }
        }
        for (ChatListener l : listeners) {
            l.onPrivateMessage(new Message(senderInfo, message, null, info));
        }
    }

    @Override
    protected void onServerResponse(int code, String response) {
        if (code == RPL_WHOISUSER) {
            String parts[] = response.split(" ");
            String user = parts[1].toLowerCase();
            UserInfo ui = new UserInfo(parts[1], parts[2], parts[3], ipFromHost(parts[3]));
            synchronized (usersMap) {
                usersMap.put(parts[1], ui);
            }
            for (ChatListener l : listeners) {
                l.onUserInfo(ui);
            }
        }
        for (ChatListener l : listeners) {
            l.onServerMessage(code + ": " + response);
        }
    }

    private String ipFromHost(String host) {
        String IP;
        try {
            IP = InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException ex) {
            IP = "74.125.39.105"; // www.google.com
        }
        return IP;
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if (oldNick.equals(info.nick)) {
            updateBotNick(newNick);
            return;
        }
        UserInfo oldInfo;
        synchronized (usersMap) {
            oldInfo = usersMap.get(oldNick);
        }
        if (oldInfo == null) {
            return;
        }
        UserInfo newInfo = new UserInfo(newNick, oldInfo.ident, oldInfo.host, oldInfo.ip);
        synchronized (usersMap) {
            usersMap.put(newNick, newInfo);
        }
        synchronized (opUsers) {
            if (opUsers.contains(oldNick)) {
                opUsers.remove(oldNick);
                opUsers.add(newNick);
            } else {
                synchronized (leftUsers) {
                    leftUsers.add(oldNick);
                }
                for (ChatListener l : listeners) {
                    l.onNameChange(oldInfo, newInfo);
                }
            }
        }
    }

    private void onQuit(String nick) {
        synchronized (usersMap) {
            usersMap.remove(nick);
        }
        synchronized (opUsers) {
            if (opUsers.contains(nick)) {
                opUsers.remove(nick);
            } else {
                synchronized (leftUsers) {
                    leftUsers.add(nick);
                }
                UserInfo quitter;
                synchronized (usersMap) {
                    quitter = usersMap.get(nick);
                }
                if (quitter != null) {
                    for (ChatListener l : listeners) {
                        l.onQuit(quitter);
                    }
                }
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
        String botNick = m.getBotInfo().nick;
        if (botNick.equals(info.nick)) {
            synchronized (opUsers) {
                if (opUsers.contains(m.getTo().nick)) {
                    return;
                }
            }
            this.sendMessage(m.getTo().nick, m.getMessage());
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        UserInfo joined = new UserInfo(sender, login, hostname, ipFromHost(hostname));
        synchronized (usersMap) {
            usersMap.put(sender, joined);
        }
    }

    public void updateBotNick(String newNick) {
        info = new UserInfo(newNick, info.ident, info.host, info.ip);
    }

    private void createNickChangeThread() {
        if (nickChanger != null) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(20 * 60 * 1000);
                    } catch (InterruptedException ex) {
                        return;
                    }
                    boolean empty = true;
                    synchronized (leftUsers) {
                        empty = leftUsers.isEmpty();
                    }
                    if (!empty) {
                        String nickBefore = IrcHandler.this.getNick();
                        List<String> leftUsersCopy = null;
                        synchronized (leftUsers) {
                            leftUsersCopy = new ArrayList<String>(leftUsers);
                        }
                        Collections.shuffle(leftUsersCopy);
                        for (String nick : leftUsersCopy) {
                            if (!IrcHandler.this.isConnected()) {
                                break;
                            }
                            IrcHandler.this.changeNick(nick);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                return;
                            }
                            synchronized (leftUsers) {
                                if (IrcHandler.this.getNick().equals(nickBefore)) {
                                    leftUsers.remove(nick);
                                } else {
                                    leftUsers.remove(nickBefore);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void handleS(String message) {
        final long messageDelayOriginal = this.getMessageDelay();
        setMessageDelay(30 * 1000);
        error("Contacted by S. Staying silent for 30 seconds: " + message);
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException ex) {
                    return;
                } finally {
                    setMessageDelay(messageDelayOriginal);
                }
            }
        }).start();

    }

    private void startReconnectorThread() {

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10 * 1000);
                        if ((System.currentTimeMillis()-lastActivity) > Options.getInstance().getInactivityTimeout()) {
                            lastActivity = System.currentTimeMillis();
                            try {                              
                                for(ChatListener l : listeners) {
                                    l.onError("Too much inactivity. Reconnecting.");
                                }                               
                                disconnect();
                                reconnect();
                            } catch (IOException ex) {
                            } catch (NickAlreadyInUseException ex) {
                            } catch (IrcException ex) {
                            }
                        }
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
            }
        }).start();
    }

    @Override
    public void onConversationClose(Conversation c) {
    }
}
