package dreamhackbotpro;

/**
 *
 * @author wasd
 */
public class Options {
    
    private int maxActiveConversations=20;
    private int secondsBetweenNewConversation=60;
    private long inactiveTimeLimit=60; //1 minute
    private long conversationTimeout=9000; //do not timeout as of now, not implemented properly
    private String server = "irc.quakenet.org";
    private String channel = "#dreamhack.trade";
    private String version = "mIRC v6.17 Khaled Mardam-Bey";
    private String finger = "Toe";
    private long inactivityTimeout = 5 * 60 * 1000;
    private static Options instance = null;
    private boolean seatPopupEnabled = true;

    
    private Options(){}
    
    public static synchronized Options getInstance(){
        if(instance==null)
            instance=new Options();
        return instance;
    }

    public int getMaxActiveConversations() {
        return maxActiveConversations;
    }

    public synchronized void setMaxActiveConversations(int maxActiveConversations) {
        this.maxActiveConversations = maxActiveConversations;
    }

    public long getInactivityTimeout() {
        return inactivityTimeout;
    }

    public void setInactivityTimeout(long inactivityTimeout) {
        this.inactivityTimeout = inactivityTimeout;
    }

    public long getInactiveTimeLimit() {
        return inactiveTimeLimit;
    }

    public synchronized void setInactiveTimeLimit(long inactiveTimeLimit) {
        this.inactiveTimeLimit = inactiveTimeLimit;
    }

    public String getVersion() {
        return version;
    }

    public String getFinger() {
        return finger;
    }

    public int getSecondsBetweenNewConversation() {
        return secondsBetweenNewConversation;
    }

    public synchronized void setSecondsBetweenNewConversation(int secondsBetweenNewConversation) {
        this.secondsBetweenNewConversation = secondsBetweenNewConversation;
    }

    public boolean isSeatPopupEnabled() {
        return seatPopupEnabled;
    }

    public void setSeatPopupEnabled(boolean seatPopupEnabled) {
        this.seatPopupEnabled = seatPopupEnabled;
    }

    public long getConversationTimeout() {
        return conversationTimeout;
    }

    public void setConversationTimeout(long conversationTimeout) {
        this.conversationTimeout = conversationTimeout;
    }

    public String getServer() {
        return server;
    }

    public String getChannel() {
        return channel;
    }
      
}
