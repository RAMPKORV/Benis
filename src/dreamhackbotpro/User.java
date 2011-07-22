package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author patrik
 */
public class User {
    
    private volatile List<Interest> interests = new ArrayList<Interest>();
    private Conversation conversation = null;
    private long lastMessage;
    private String name;

    public User(String name) {
        this.lastMessage = System.currentTimeMillis();
        this.name=name;
    }

    /**
     * Checks if the user is inactive
     * @return true if the user has not said anything in Options.getInactiveTimeLimit() seconds.
     */
    public boolean isInactive(){
        return System.currentTimeMillis()>lastMessage+Options.getInstance().getInactiveTimeLimit()*1000L;
    }
    
    /**
     * Called when the User has done anything
     */
    public synchronized void updateActivity(){
        lastMessage = System.currentTimeMillis();
    }

    /**
     * @return The other User in the Conversation. null if no Conversation
     */
    public User getConversationBuddy(){
        if(conversation==null)
            return null;
        if(conversation.getBuyer()==this)
            return conversation.getSeller();
        return conversation.getBuyer();
    }
    
    /**
     * @return How many interests the User has.
     */
    public int getAmountOfInterests(){
        return interests.size();
    }

    public Interest getPrioritizedInterest() {
        Interest result = null;
        float stdDevsMax = 0;
        float stdDev = -10;
        float median = 0;
        float cmp = 0;
        Map<String, ThingInfo> things = Interest.getInterestsMap();
        for(Interest i : interests) {
            ThingInfo ti = things.get(i.getThing());
            median = ti.getMedian();
            stdDev = ti.getStdDev();
            cmp = ((i.getPrice()-median)/stdDev);
            cmp = i.isBuying() ? cmp : -cmp;
            if(cmp > stdDevsMax) {
                stdDevsMax = Math.abs(stdDev);
                result = i;
            }
        }
        return result;
    }

    /**
     * Calculates if the User is most interested in buying or selling
     * @return true if most of the Users interest is WTB
     */
    public boolean isMostlyBuying(){
        int buy=0;
        int sell=0;
        for(Interest i : interests){
            if(i.isBuying())
                buy++;
            else
                sell++;
        }
        return buy>sell;
    }
    
    /**
     * @return true if the User is interested in buying anything
     */
    public boolean wtbAnything(){
        for(Interest i : interests){
            if(i.isBuying())
                return true;
        }
        return false;
    }
    
    /**
     * @return true if the User is interested in selling anything
     */
    public boolean wtsAnything(){
        for(Interest i : interests){
            if(!i.isBuying())
                return true;
        }
        return false;
    }

    public void messageConversationBuddy(Message m) {
        if(conversation==null)
            throw new NullPointerException();
        conversation.onMessage(this, m);
    }

    /**
     * Should only be called in the Conversation constructor
     */
    public void setConversation(Conversation conversation) {
        this.conversation=conversation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
    
    public void addInterest(Interest theInterest){
        for(Interest i : interests){
            if(theInterest.isBuying()==i.isBuying() && i.getThing().equals(theInterest.getThing())){
                i.update(theInterest);
                return;
            }
        }
        interests.add(theInterest);
        theInterest.addToInterestsMap();
    }

}
