package dreamhackbotpro;

import java.util.Collection;

/**
 *
 * @author wasd
 */
public class ConversationMaker {

    public ConversationMaker() {
    }

    /**
     * @param users Should be users.values() from Bot
     */
    public void makeConversation(Collection<User> users){

        //TODO make the calculation of Certainty more advanced for Interests for better result. preDefined items should have more certainty

        User bestBuyer = null;
        User bestSeller = null;

        //TODO perhaps getPrioritizedInterest should be used?
        for(User u : users){
            Interest bestInterest = u.getMostCertainInterest();
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
            //TODO send greeting
        }
    }

}
