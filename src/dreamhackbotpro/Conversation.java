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
    private int buyerPrice;
    private int sellerPrice;

    private ConversationBehavior behavior = DefaultConversationBehavior.getInstance();

    private static List<ConversationsListener> listeners = new ArrayList<ConversationsListener>();

    public Conversation(User buyer, User seller, String buyerThing, String sellerThing, int buyerPrice, int sellerPrice) {
        this.buyer = buyer;
        this.seller = seller;
        this.buyerThing = buyerThing;
        this.sellerThing = sellerThing;
        this.buyerPrice = buyerPrice;
        this.sellerPrice = sellerPrice;
        buyer.setConversation(this);
        seller.setConversation(this);
        
        //TODO send a greeting to buyer and seller
        //Problem: Do not send greeting before buyerThing and sellerThing has been generated in the other constructor
        //Possible solution: add a public sendGreeting method called after Conversation has been constructed
    }

    public Conversation(User buyer, User seller) {
        this(buyer, seller, null, null, -1, -1);
        //TODO perhaps getPrioritizedInterest should be used?
        Interest buyerInterest = buyer.getMostCertainInterest();
        Interest sellerInterest = seller.getMostCertainInterest();
        buyerThing = buyerInterest.getThing();
        buyerPrice = buyerInterest.getPrice();
        sellerThing = sellerInterest.getThing();
        sellerPrice = sellerInterest.getPrice();
    }
    

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public String getBuyerThing() {
        return buyerThing;
    }

    public String getSellerThing() {
        return sellerThing;
    }

    public static void addConversationsListener(ConversationsListener l){
        listeners.add(l);
    }

    public static void removeConversationsListener(ConversationsListener l){
        listeners.remove(l);
    }

    public void onMessage(User u, Message m) {
        if(u==buyer)
            m.setTo(seller.getName());
        else
            m.setTo(buyer.getName());
        
        //do all the logic. Replace buyerThing with sellerThing etc.
        behavior.transformMessage(this, m);
        
        for(ConversationsListener l : listeners){
            l.onConversationMessage(m);
        }
    }

}
