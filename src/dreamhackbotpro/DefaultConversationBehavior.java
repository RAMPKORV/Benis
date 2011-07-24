package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;
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
        SentenceParser p = SentenceParser.getInstance();
        String msg = m.getMessage();
        if(Greeting.isSimpleGreeting(msg)) {
            m.setMessage("");
            return;
        }

        // Delete greetings after the first two messages of the conversation        
        String greeting;
        do {
            greeting = Greeting.hasGreeting(msg);
            if(greeting != null) {
                msg = msg.replaceAll("(?i)"+Pattern.quote(greeting), "");
            }
        } while(greeting != null);
        

        // TODO: Fetch botNick from somewhere else
        String botNick = "Monsquaz";      
        String buyer = c.getBuyer().getName();
        String seller = c.getSeller().getName();
        String buyerThing = c.getBuyerThing();
        String sellerThing = c.getSellerThing();
        int buyerPrice = c.getBuyerPrice();
        int sellerPrice = c.getSellerPrice();
        String[] words = msg.split("[ \\.\\!\\?\\,]");
        if(m.getFrom().equals(c.getBuyer().getName())) {
            int index = 0;
            List<String> priceStrings = new ArrayList<String>();
            while(index < msg.length()-1) {
                String priceString = p.parsePriceString(msg.substring(index, msg.length()-1));
                if(priceString != null) {
                    System.out.println(priceString);
                    priceStrings.add(priceString);
                } else {
                    break;
                }
                index += msg.substring(index).indexOf(priceString);
            }
            for(String priceString : priceStrings) {
                int price = p.parsePrice(priceString);
                if(price == buyerPrice) {
                    msg = msg.replace(priceString, sellerPrice + "");
                } else {
                    int newPrice = (int) ((float)sellerPrice * (1.0f + (Math.abs((float) price - (float) buyerPrice) / (float) buyerPrice)));
                    newPrice = Utils.roundPrice(newPrice);
                    msg = msg.replace(priceString, newPrice + "");
                }
            }
            for(String s : words) {
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), buyer.toLowerCase()) <= s.length()/4)
                    msg = msg.replace(s, botNick);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), botNick.toLowerCase()) <= s.length()/4)
                    msg = msg.replace(s, seller);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), buyerThing.toLowerCase()) <= s.length()/4)
                    msg = msg.replace(s, sellerThing);
                if(s.toLowerCase().contains(buyerThing))
                    msg = msg.replace(s, sellerThing);
                String translated = ThingInfo.translateBuzzWord(buyerThing, sellerThing, s);
                if(!translated.equals(s)) {
                    msg = msg.replace(s, translated);
                }
            }
        } else {
            int index = 0;
            List<String> priceStrings = new ArrayList<String>();
            while(index < msg.length()-1) {
                String priceString = p.parsePriceString(msg.substring(index, msg.length()-1));
                if(priceString != null) {
                    System.out.println(priceString);
                    priceStrings.add(priceString);
                } else {
                    break;
                }
                index += msg.substring(index).indexOf(priceString);
            }
            for(String priceString : priceStrings) {
                int price = p.parsePrice(priceString);
                if(price == sellerPrice) {
                    msg = msg.replace(priceString, buyerPrice + "");
                } else {
                    int newPrice = (int) ((float)buyerPrice * (1.0f - (Math.abs((float) price - (float) sellerPrice) / (float) sellerPrice)));
                    newPrice = Utils.roundPrice(newPrice);
                    msg = msg.replace(priceString, newPrice + "");
                }
            }
            for(String s : words) {
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), seller.toLowerCase()) <= s.length()/4)
                    msg = msg.replace(s, botNick);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), botNick.toLowerCase()) <= s.length()/4)
                    msg = msg.replace(s, buyer);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), sellerThing.toLowerCase()) <= s.length()/4)
                    msg = msg.replace(s, buyerThing);
                if(s.toLowerCase().contains(sellerThing))
                    msg = msg.replace(s, buyerThing);
                String translated = ThingInfo.translateBuzzWord(sellerThing, buyerThing, s);               
                if(!translated.equals(s)) {
                    msg = msg.replace(s, translated);
                }
            }
        }
        m.setMessage(msg);
    }
}
