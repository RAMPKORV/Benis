package dreamhackbotpro;

/**
 *
 * @author wasd
 */
public class Options {
    
    private int maxActiveConversations=20;
    private long inactiveTimeLimit=2*60; //2 minutes
    
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

    public void setMaxActiveConversations(int maxActiveConversations) {
        this.maxActiveConversations = maxActiveConversations;
    }

    public long getInactiveTimeLimit() {
        return inactiveTimeLimit;
    }

    public void setInactiveTimeLimit(long inactiveTimeLimit) {
        this.inactiveTimeLimit = inactiveTimeLimit;
    }
    
}
