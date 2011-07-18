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
public class Conversation {

    private User buyer;
    private User seller;
    private String buyerThing;
    private String sellerThing;

    private static List<ConversationsListener> listeners = new ArrayList<ConversationsListener>();

    public Conversation(User buyer, User seller, String buyerThing, String sellerThing) {
        this.buyer = buyer;
        this.seller = seller;
        this.buyerThing = buyerThing;
        this.sellerThing = sellerThing;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public static void addConversationsListener(ConversationsListener l){
        listeners.add(l);
    }

    public static void removeConversationsListener(ConversationsListener l){
        listeners.remove(l);
    }

}
