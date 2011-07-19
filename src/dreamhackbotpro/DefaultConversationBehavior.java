/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 * Basic buyer/seller and buyerthing/sellerthing replaces without any analysis.
 * @author patrik
 */
public class DefaultConversationBehavior implements ConversationBehavior {

    private static DefaultConversationBehavior instance = null;

    private DefaultConversationBehavior() {}

    public static synchronized DefaultConversationBehavior getInstance() {
        if(instance == null)
            instance = new DefaultConversationBehavior();
        return instance;
    }

    public void transformMessage(Conversation c, Message m) {
        m.setMessage(m.getMessage().replaceAll(c.getBuyer().getName(), "%SELLER%"));
        m.setMessage(m.getMessage().replaceAll(c.getSeller().getName(), c.getBuyer().getName()));
        m.setMessage(m.getMessage().replaceAll("%SELLER%", c.getSeller().getName()));
        m.setMessage(m.getMessage().replaceAll(c.getBuyerThing(), "%SELLERTHING%"));
        m.setMessage(m.getMessage().replaceAll(c.getSellerThing(), c.getBuyerThing()));
        m.setMessage(m.getMessage().replaceAll("%SELLERTHING%", c.getSellerThing()));
    }
}
