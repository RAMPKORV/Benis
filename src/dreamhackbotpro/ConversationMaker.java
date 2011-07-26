package dreamhackbotpro;

import java.util.Collection;

/**
 *
 * @author wasd
 */
public class ConversationMaker {

    private long lastConversationMade = 0;

    public ConversationMaker() {
    }

    /**
     * @param users Should be users.values() from Bot
     */
    public void check(Collection<User> users, BotInfo bot){

        //if(lastConversationMade>System.currentTimeMillis()-waitTime()){
        //    return;
        //}
        //lastConversationMade = System.currentTimeMillis();

        //TODO make the calculation of Certainty more advanced for Interests for better result. preDefined items should have more certainty

        User bestBuyer = null;
        User bestSeller = null;

        for(User u : users){
            if(u.getConversation() != null)
                continue;
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
            //Send greeting
            con.onMessage(bestBuyer, new Message(bestBuyer.getName(), Greeting.getGreeting(con.getSellerThing(), con.getSellerPrice(), true), bestSeller.getName(), bot), false);
            con.onMessage(bestSeller, new Message(bestSeller.getName(), Greeting.getGreeting(con.getBuyerThing(), con.getBuyerPrice(), false), bestBuyer.getName(), bot), false);
        }
        else{
            //reset timer to try on next message recieved instead of waiting 30 seconds
            lastConversationMade = 0;
        }
    }

    private long waitTime(){
        return Options.getInstance().getSecondsBetweenNewConversation()*1000L;
    }

}
