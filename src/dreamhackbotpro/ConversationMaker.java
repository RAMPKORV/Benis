package dreamhackbotpro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author wasd
 */
public class ConversationMaker {

    private long lastConversationMade = 0;
    private List<Conversation> conversations = new ArrayList<Conversation>();

    public ConversationMaker() {
    }

    /**
     * @param users Should be users.values() from Bot
     */
    public void check(Collection<User> users, UserInfo bot){

        if(lastConversationMade>System.currentTimeMillis()-waitTime()){
            return;
        }
        lastConversationMade = System.currentTimeMillis();

        //TODO make the calculation of Certainty more advanced for Interests for better result. preDefined items should have more certainty

        User bestBuyer = null;
        User bestSeller = null;

        for(User u : users){
            if(u.getConversation() != null)
                continue;
//            if(u.isInactive())
//                continue;
            Interest bestInterest;
            // Use random strategy to determine best interest
            if(Utils.random.nextBoolean()) {
                bestInterest = u.getMostCertainInterest();
                if(bestInterest==null)
                    bestInterest = u.getPrioritizedInterest();
            } else {
                bestInterest = u.getPrioritizedInterest();
                if(bestInterest==null)
                    bestInterest = u.getMostCertainInterest();
            }
            if(bestInterest==null){
                continue;
            }
            if(bestInterest.isBuying()){
                if(bestBuyer==null){
                    bestBuyer=u;
                }
                else if(bestInterest.getCertainty()>bestBuyer.getMostCertainInterest().getCertainty()){
                    bestBuyer=u;
                }
            }
            else{
                if(bestSeller==null){
                    bestSeller=u;
                }
                else if(bestInterest.getCertainty()>bestSeller.getMostCertainInterest().getCertainty()){
                    bestSeller=u;
                }
            }

        }

        if(bestBuyer!=null && bestSeller!=null){
            Conversation con = new Conversation(bestBuyer, bestSeller);
            String buyerGreeting = Greeting.getGreeting(con.getSellerThing(), con.getSellerPrice(), true);
            String sellerGreeting = Greeting.getGreeting(con.getBuyerThing(), con.getBuyerPrice(), false);
            con.addGreeting(buyerGreeting);
            con.addGreeting(sellerGreeting);
            greetLater(5000L + Utils.random.nextInt(4000), con, bestBuyer, bestSeller, buyerGreeting, sellerGreeting, bot);
        } else {
            //reset timer to try on next message recieved instead of waiting 30 seconds
            lastConversationMade = 0;
        }
    }

    private void greetLater(final long delay, final Conversation con, final User buyer, final User seller, final String buyerGreeting, final String sellerGreeting, final UserInfo bot) {
       new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(delay);
                        //Send greeting
                        con.onMessage(buyer, new Message(buyer.getUserInfo(), buyerGreeting, seller.getUserInfo(), bot), false);
                        con.onMessage(seller, new Message(seller.getUserInfo(), sellerGreeting, buyer.getUserInfo(), bot), false);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
       }).start();
    }

    private long waitTime(){
        return Options.getInstance().getSecondsBetweenNewConversation()*1000L;
    }

}
