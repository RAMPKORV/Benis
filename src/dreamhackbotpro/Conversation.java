package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author patrik
 */
public class Conversation implements Comparable<Conversation> {

    private User buyer;
    private User seller;
    private String buyerThing;
    private String sellerThing;
    private int buyerPrice;
    private int sellerPrice;
    private int numMessages = 0;
    private List<String> greetings = new ArrayList<String>();

    public int getNumMessages() {
        return numMessages;
    }

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

    public int getBuyerPrice() {
        return buyerPrice;
    }

    public int getSellerPrice() {
        return sellerPrice;
    }

    public Conversation(User buyer, User seller) {
        this(buyer, seller, null, null, -1, -1);
        //TODO perhaps getPrioritizedInterest should be used?
        Interest buyerInterest = buyer.getMostCertainInterest();
        Interest sellerInterest = seller.getMostCertainInterest();
        buyerThing = buyerInterest.getThing();    
        sellerThing = sellerInterest.getThing();
        Map<String, ThingInfo> iMap = Interest.getInterestsMap();
        sellerPrice = sellerInterest.getPrice();
        if(sellerPrice == -1) {
            ThingInfo sInfo = iMap.get(sellerThing);
            sellerPrice = Utils.roundPrice((int) (sInfo.getMedian() - sInfo.getStdDev()*0.5));
        }
        buyerPrice = buyerInterest.getPrice();
        if(buyerPrice == -1) {
            ThingInfo sInfo = iMap.get(buyerThing);
            buyerPrice = Utils.roundPrice((int) (sInfo.getMedian() + sInfo.getStdDev()*0.5));
        }
        
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
        onMessage(u, m, true);
    }

    public void onMessage(User u, Message m, boolean transform){
        numMessages++;
        if(u==buyer)
            m.setTo(seller.getName());
        else
            m.setTo(buyer.getName());

        //do all the logic. Replace buyerThing with sellerThing etc.
        if(transform)
            behavior.transformMessage(this, m);

        if(m.getMessage().length() > 0) {
            for(ConversationsListener l : listeners){
                l.onConversationMessage(m);
            }
        }
    }

    public int compareTo(Conversation t) {
        if(numMessages > t.numMessages)
            return 1;
        if(numMessages < t.numMessages)
            return -1;
        if(buyerPrice > t.buyerPrice)
            return 1;
        if(buyerPrice < t.buyerPrice)
            return -1;
        if(sellerPrice > t.sellerPrice)
            return 1;
        if(sellerPrice < t.sellerPrice)
            return -1;
        return 0;
    }

    public void addGreeting(String greeting) {
        greetings.add(greeting);
    }

    public List<String> getGreetings() {
        return greetings;
    }
    
}
