package dreamhackbotpro;

import java.util.regex.Pattern;

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

    @Override
    public void transformMessage(Conversation c, Message m) {
        // TODO: Fetch botNick from somewhere else
        String botNick = "Monsquaz";
        if(m.getFrom().equals(c.getBuyer().getName())) {
            m.setMessage(m.getMessage().replaceAll("(?i)"+Pattern.quote(c.getBuyer().getName()), botNick));
            m.setMessage(m.getMessage().replaceAll("(?i)"+Pattern.quote(botNick), c.getSeller().getName()));
            m.setMessage(m.getMessage().replaceAll("(?i)"+Pattern.quote(c.getBuyerThing()), c.getSellerThing()));
        } else {
            m.setMessage(m.getMessage().replaceAll("(?i)"+Pattern.quote(c.getSeller().getName()), botNick));
            m.setMessage(m.getMessage().replaceAll("(?i)"+Pattern.quote(botNick), c.getBuyer().getName()));
            m.setMessage(m.getMessage().replaceAll("(?i)"+Pattern.quote(c.getSellerThing()), c.getBuyerThing()));
        }
    }
}
