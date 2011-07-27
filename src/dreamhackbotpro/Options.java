package dreamhackbotpro;

/**
 *
 * @author wasd
 */
public class Options {
    
    private int maxActiveConversations=20;
    private int secondsBetweenNewConversation=15;
    private long inactiveTimeLimit=2*60; //2 minutes
    private String version = "mIRC v6.17 Khaled Mardam-Bey";
    private String finger = "Toe";

    private static Options instance = null;

    
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
      
}
