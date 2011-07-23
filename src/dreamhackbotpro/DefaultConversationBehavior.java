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
        String msg = m.getMessage();
        String buyerThing = c.getBuyerThing();
        String sellerThing = c.getSellerThing();
        String[] words = msg.split(" ");
        if(m.getFrom().equals(c.getBuyer().getName())) {
            m.setMessage(msg.replaceAll("(?i)"+Pattern.quote(c.getBuyer().getName()), botNick));
            m.setMessage(msg.replaceAll("(?i)"+Pattern.quote(botNick), c.getSeller().getName()));
            for(String s : words) {
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), buyerThing.toLowerCase()) < s.length()/4) {
                    msg = msg.replace(s, sellerThing);
                }
            }
        } else {
            m.setMessage(msg.replaceAll("(?i)"+Pattern.quote(c.getSeller().getName()), botNick));
            m.setMessage(msg.replaceAll("(?i)"+Pattern.quote(botNick), c.getBuyer().getName()));
            for(String s : words) {
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), sellerThing.toLowerCase()) < s.length()/4) {
                    msg = msg.replace(s, buyerThing);
                }
            }
        }
        m.setMessage(msg);
    }
}
