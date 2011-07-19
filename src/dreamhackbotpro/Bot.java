package dreamhackbotpro;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that finds out what users are interested in based on messages and connects users together
 * @author wasd
 */
public class Bot implements IrcListener{
    
    private Map<String, User> users = new HashMap<String, User>();
    private User orphanUser = null;
    private MessageFilter filter = new MessageFilter();

    public Bot(){

    }

    public void onMessage(final Message m) {
        User user = users.get(m.getFrom());
        if(user==null){
            user = new User();
            users.put(m.getFrom(), user);
        }
        else
            user.updateActivity();
        
        final Message filtered = filter.getFiltered(m);
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onNameChange(String oldName, String newName) {
        User user = users.get(oldName);
        if(user==null){
            user = new User();
            users.put(newName, user);
            return;
        }
        user.updateActivity();
        users.remove(oldName);
        users.put(newName, user);
    }

    public void onPrivateMessage(Message m) {
        User user = users.get(m.toString());
        if(user==null){
            user = new User();
            users.put(m.getFrom(), user);
        }
        else
            user.updateActivity();
        User buddy = user.getConversationBuddy();
        if(buddy==null){
            if(orphanUser==null){
                orphanUser=user;
                return;
            }
            createConversation(user, orphanUser, true);
            orphanUser=null;
        }
        
        //TODO tell users Conversation that he sent a message to his buddy
    }

    public void onQuit(String userName) {
        User user = users.get(userName);
        if(user==null)
            return;
        
        User buddy = user.getConversationBuddy();
        if(buddy==null)
            return; //user was not in any chat
        if(buddy.isInactive())
            return; //do not connect inactive buddy to anyone
        
        if(orphanUser==null || removeOrphanIfInactive()){
            //no orphan or inactive orphan
            orphanUser=buddy;
            return;
        }
        
        //buddy and orphan is active
        createConversation(buddy, orphanUser, true);
        orphanUser=null;
        
    }

    public void onError(String error) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Creates a conversation between u1 and u2
     * 
     * @param u1 The first user
     * @param u2 The second user
     * @param calculateBuyerAndSeller If false, u1 will be buyer and u2 will be seller.
     * If true, calculates who is seller and buyer based on u1 and u2 Interest
     * @return The Conversation
     */
    private Conversation createConversation(User u1, User u2, boolean calculateBuyerAndSeller){
        if(!calculateBuyerAndSeller){
            Conversation con = new Conversation(u1, u2);
            return con;
        }
        
        if(u1.isMostlyBuying() && !u2.isMostlyBuying()){
            return createConversation(u1, u2, false);
        }
        if(!u1.isMostlyBuying() && u2.isMostlyBuying()){
            return createConversation(u2, u1, false);
        }
        
        //TODO check who is most interested in buying and selling
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Removes orphanUser if he is inactive
     * @return true if orphanUser is removed
     */
    private boolean removeOrphanIfInactive(){
        if(orphanUser==null)
            return false;
        if(orphanUser.isInactive()){
            orphanUser=null;
            return true;
        }
        return false;
    }

}
