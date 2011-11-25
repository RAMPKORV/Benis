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
    private MessageFilter messageFilter = new MasterFilter();
    private SentenceParser parser = SentenceParser.getInstance();
    private ConversationMaker conversationMaker = new ConversationMaker();

    public Bot(){
    }

    @Override
    public void onUserInfo(UserInfo ui) {
        User user = users.get(ui.nick);
        if(user == null) { 
            user = new User(ui);
            users.put(ui.nick, user);
        } else { 
            user.setUserInfo(ui);
        }
    }

    @Override
    public void onMessage(final Message m) {
        User user = users.get(m.getFrom().nick);
        if(user==null){
            user = new User(m.getFrom());
            users.put(m.getFrom().nick, user);
        } else 
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
        conversationMaker.check(users.values(), m.getBotInfo());
    }

    @Override
    public void onNameChange(UserInfo olduser, UserInfo newuser) {
        User user = users.get(olduser.nick);
        if(user==null){
            user = new User(newuser);
            users.put(newuser.nick, user);
        }
        else{
            user.setName(newuser.nick);
            user.updateActivity();
            users.remove(olduser.nick);
            users.put(newuser.nick, user);
        }
        conversationMaker.onNickChange(olduser.nick, newuser.nick);
    }

    @Override
    public void onPrivateMessage(Message m) {
        User user = users.get(m.getFrom().nick);
        if(user==null){
            user = new User(m.getFrom());
            users.put(m.getFrom().nick, user);
        }
        else
            user.updateActivity();
        User buddy = user.getConversationBuddy();
        if(buddy==null){
            //message from new user, or user that has been in an inactive Conversation. Not sure what to do with them
            return;
        }
        
        user.messageConversationBuddy(m);
    }

    @Override
    public void onQuit(UserInfo userInfo) {
        User user = users.get(userInfo.nick);
        if(user==null)
            return;
        
        User buddy = user.getConversationBuddy();
        if(buddy==null)
            return; //user was not in any chat
        buddy.setConversation(null);
        
    }

    @Override
    public void onError(String error) {
        //Ignore
    }

    @Override
    public void onServerMessage(String message) {
        //Ignore
    }

    public void onBotNickChange() {
        conversationMaker.onBotNickChange();
    }

    @Override
    public void onBotNickChange(String newName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
