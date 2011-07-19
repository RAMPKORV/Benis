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
public class User {
    
    private List<Interest> interests = new ArrayList<Interest>();
    private Conversation conversation = null;
    private long lastMessage;

    public User() {
        this.lastMessage = System.currentTimeMillis();
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
    public void updateActivity(){
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

}
