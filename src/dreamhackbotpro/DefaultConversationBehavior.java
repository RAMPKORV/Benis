package dreamhackbotpro;

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
    public Message transformMessage(Conversation c, Message m) {
        SentenceParser p = SentenceParser.getInstance();
        String msg = m.getMessage();
        if(Greeting.isSimpleGreeting(msg)) {
            m.setMessage("");
            return m;
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
             List<String> priceStrings = p.parsePriceStrings(msg);
             for(String priceString : priceStrings) {
                int price = p.parsePrice(priceString);
                if(price == buyerPrice) {
                    String newPriceString = priceString.replace(price+"", sellerPrice+"");
                    msg = msg.replace(priceString, newPriceString);
                } else {
                    int newPrice = convertPrice(price, sellerPrice, buyerPrice, true);
                    String newPriceString = priceString.replace(price+"", newPrice+"");
                    msg = msg.replace(priceString, newPriceString);
                }
            }
            for(String s : words) {
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), buyer.toLowerCase()) <= Math.max(s.length(),buyer.length())/4)
                    msg = msg.replace(s, botNick);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), botNick.toLowerCase()) <= Math.max(s.length(),botNick.length())/4)
                    msg = msg.replace(s, seller);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), buyerThing.toLowerCase()) <= Math.max(s.length(),buyerThing.length())/4) {
                    msg = msg.replace(s, sellerThing);
                } else {
                    if(s.toLowerCase().contains(buyerThing))
                        msg = msg.replace(s, sellerThing);
                }
                String translated = ThingInfo.translateBuzzWord(buyerThing, sellerThing, s);
                if(!translated.equals(s)) {
                    msg = msg.replace(s, translated);
                }
            }
        } else {
             List<String> priceStrings = p.parsePriceStrings(msg);
             for(String priceString : priceStrings) {
                int price = p.parsePrice(msg);
                if(price == sellerPrice) {
                    String newPriceString = priceString.replace(price+"", buyerPrice+"");
                    msg = msg.replace(priceString, newPriceString);
                } else {
                    int newPrice = convertPrice(price, sellerPrice, buyerPrice, false);
                    String newPriceString = priceString.replace(price+"", newPrice+"");
                    msg = msg.replace(priceString, newPriceString);
                }
            }
            for(String s : words) {
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), seller.toLowerCase()) <= Math.max(s.length(),seller.length())/4)
                    msg = msg.replace(s, botNick);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), botNick.toLowerCase()) <= Math.max(s.length(),botNick.length())/4)
                    msg = msg.replace(s, buyer);
                if(SentenceParser.getLevenshteinDistance(s.toLowerCase(), sellerThing.toLowerCase()) <= Math.max(s.length(),sellerThing.length())/4) {
                    msg = msg.replace(s, buyerThing);
                } else {
                if(s.toLowerCase().contains(sellerThing))
                    msg = msg.replace(s, buyerThing);
                }
                String translated = ThingInfo.translateBuzzWord(sellerThing, buyerThing, s);               
                if(!translated.equals(s)) {
                    msg = msg.replace(s, translated);
                }
            }
        }
        m.setMessage(msg);
        return m;
    }

    private int convertPrice(int price, int sellerPrice, int buyerPrice, boolean wtb) {
        float priceFloat = (float)price;
        float sellerPriceFloat = (float)sellerPrice;
        float buyerPriceFloat = (float)buyerPrice;
        float percentageChange = 0;
        float converted;
        if(wtb) {
            percentageChange = (priceFloat-buyerPrice)/buyerPrice;
            converted = sellerPriceFloat + sellerPriceFloat * percentageChange;
        } else {
            percentageChange = (priceFloat-sellerPrice)/sellerPrice;
            converted = buyerPriceFloat + buyerPriceFloat * percentageChange;
        }
        return Utils.roundPrice((int)converted);
    }
}
