package dreamhackbotpro;

import dreamhackbotpro.filters.MasterFilter;
import dreamhackbotpro.filters.MessageFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that finds out what users are interested in based on messages and connects users together
 * @author wasd
 */
public class Bot implements ChatListener{
    
    private Map<String, User> users = new HashMap<String, User>();
    private User orphanUser = null;
    private MessageFilter messageFilter = new MasterFilter();
    private SentenceParser parser = SentenceParser.getInstance();

    public Bot(){
    }

    @Override
    public void onMessage(final Message m) {
        User user = users.get(m.getFrom());
        if(user==null){
            user = new User(m.getFrom());
            users.put(m.getFrom(), user);
        }
        else
            user.updateActivity();
        
        messageFilter.filter(m);
        
        //handle each sentence individually
        for(String s : m.getMessage().split("\\.(?![0-9])")){
            Interest i = parser.parseInterest(s);
            if(i!=null){         
                user.addInterest(i);
                parser.parseBuzzWords(i, s);
            }
            else{
                //uncomment when running InterestParsingTest4000
//                System.out.println("failed to parse: "+s);
            }
        }
    }

    @Override
    public void onNameChange(String oldName, String newName) {
        User user = users.get(oldName);
        if(user==null){
            user = new User(newName);
            users.put(newName, user);
            return;
        }
        else
            user.setName(newName);
        user.updateActivity();
        users.remove(oldName);
        users.put(newName, user);
    }

    @Override
    public void onPrivateMessage(Message m) {
        User user = users.get(m.toString());
        if(user==null){
            user = new User(m.getFrom());
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
        
        user.messageConversationBuddy(m);
    }

    @Override
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

    @Override
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
    @Deprecated
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
