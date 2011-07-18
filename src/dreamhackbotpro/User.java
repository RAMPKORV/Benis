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


    public User getConversationBuddy(){
        if(conversation==null)
            return null;
        if(conversation.getBuyer()==this)
            return conversation.getSeller();
        return conversation.getBuyer();
    }


}
