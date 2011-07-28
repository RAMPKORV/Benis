package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Conversation behavior which replaces buyerthing/sellerthing etc.
 * @author patrik
 */
public class DefaultConversationBehavior implements ConversationBehavior {

    private static DefaultConversationBehavior instance = null;
    private static final String SEPARATOR = "%";
    private DefaultConversationBehavior() {}

    public static synchronized DefaultConversationBehavior getInstance() {
        if(instance == null)
            instance = new DefaultConversationBehavior();
        return instance;
    }

    @Override
    public Message transformMessage(final Conversation c, Message m) {

        SentenceParser p = SentenceParser.getInstance();
        String msg = m.getMessage();

        // Remove immediate affirmations like "Japp"
        if(c.getNumMessages() == 3) {
            if(Greeting.isAffirmation(msg)) {
                m.setMessage("");
                return m;
            }
        }

        // Remove references to original greetings
        List<String> greetings = c.getGreetings();
        for(String greeting : greetings) {
            if(msg.contains(greeting)) {
                m.setMessage("");
                return m;
            }
        }

        // Remove simple greetings
        if(Greeting.isSimpleGreeting(msg)) {
            m.setMessage("");
            return m;
        }

        // Delete greetings after the first two messages of the conversation        
        String greeting;
        do {
            greeting = Greeting.hasGreeting(msg);
            if(greeting != null) {
                msg = msg.replaceAll("(?i)"+greeting+"[^ ]*", "");
            }
        } while(greeting != null);

        final BotInfo bot = m.getBotInfo();
        String botNick = bot.getNick();
        final String buyer = c.getBuyer().getName();
        final String seller = c.getSeller().getName();
        String buyerThing = c.getBuyerThing();
        String sellerThing = c.getSellerThing();
        final String buyerBrand = ThingInfo.getBrandByItem(buyerThing);
        final String sellerBrand = ThingInfo.getBrandByItem(sellerThing);
        int buyerPrice = c.getBuyerPrice();
        int sellerPrice = c.getSellerPrice();
        float buyerSTD = c.getBuyerSTD();
        float sellerSTD = c.getSellerSTD();
        String[] words = msg.split("[ \\.\\!\\?\\,]");
        if(m.getFrom().equals(c.getBuyer().getName())) {
            if(buyerBrand != null && sellerBrand != null) {
                msg = msg.replace(buyerBrand, sellerBrand);
            }
            if(buyerBrand != null && sellerBrand == null) {
                msg = msg.replace(buyerBrand, sellerThing);
            }
            if(buyerBrand != null && ThingInfo.isBrandQuestion(msg)) {
                    m.setMessage("");
                    send(5000L, c, new Message(seller,buyerBrand,buyer,bot));
                    return m;
            }
            String[] xy = ThingInfo.isXorYQuestion(msg);
            if(xy != null) {
                m.setMessage("");
                send(5000L, c, new Message(seller,xy[Utils.random.nextInt(xy.length)],buyer,bot));
                return m;
            }
            if(ThingInfo.isConfirmQuestion(msg)) {
                m.setMessage("");
                send(5000L, c, new Message(seller,Greeting.getYes(),buyer,bot));
                return m;
            }
            if(ThingInfo.isHowManyQuestion(msg)) {
                m.setMessage("");
                send(5000L, c, new Message(seller,""+(Utils.random.nextInt(4)+1),buyer,bot));
                return m;
            }
             List<String> priceStrings = p.parsePriceStrings(msg);
             int i = 0;
             List<String> newPrices = new ArrayList<String>();
             msg = msg.replace(SEPARATOR, "\\"+SEPARATOR);
             for(String priceString : priceStrings) {
                int price = p.parsePrice(priceString);
                if(price == buyerPrice) {
                    String newPriceString = priceString.replace(price+"", sellerPrice+"");
                    newPrices.add(newPriceString);
                    msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR+(i++)+SEPARATOR);
                } else {
                    int newPrice = convertPrice(price, sellerPrice, buyerPrice, sellerSTD, buyerSTD, true);
                    String newPriceString = priceString.replace(price+"", newPrice+"");
                    newPrices.add(newPriceString);
                    msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR+(i++)+SEPARATOR);
                }
            }
            while(i > 0) {
                i--;
                msg = msg.replace(SEPARATOR+i+SEPARATOR, newPrices.get(i));
            }
            msg = msg.replace("\\"+SEPARATOR, SEPARATOR);
            for(String s : words) {
                if(Utils.getLevenshteinDistance(s.toLowerCase(), buyer.toLowerCase()) <= Math.max(s.length(),buyer.length())/4)
                    msg = msg.replace(s, botNick);
                if(Utils.getLevenshteinDistance(s.toLowerCase(), botNick.toLowerCase()) <= Math.max(s.length(),botNick.length())/4)
                    msg = msg.replace(s, seller);
                if(Utils.getLevenshteinDistance(s.toLowerCase(), buyerThing.toLowerCase()) <= Math.max(s.length(),buyerThing.length())/4) {
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
            if(buyerBrand != null && sellerBrand != null) {
                msg = msg.replace(sellerBrand, buyerBrand);
            }
            if(seller != null && buyerBrand == null) {
                msg = msg.replace(sellerBrand, buyerThing);
            }
            String[] xy = ThingInfo.isXorYQuestion(msg);
            if(xy != null) {
                m.setMessage("");
                send(5000L, c, new Message(buyer,xy[Utils.random.nextInt(xy.length)],seller,bot));
                return m;
            }
            if(ThingInfo.isConfirmQuestion(msg)) {
                m.setMessage("");
                send(5000L, c, new Message(buyer,Greeting.getYes(),seller,bot));
                return m;
            }
            if(ThingInfo.isHowManyQuestion(msg)) {
                m.setMessage("");
                send(5000L, c, new Message(buyer,""+(Utils.random.nextInt(4)+1),seller,bot));
                return m;
            }
             List<String> priceStrings = p.parsePriceStrings(msg);
             int i = 0;
             List<String> newPrices = new ArrayList<String>();
             msg = msg.replace(SEPARATOR, "\\"+SEPARATOR);
             for(String priceString : priceStrings) {
                int price = p.parsePrice(msg);
                if(price == sellerPrice) {
                    String newPriceString = priceString.replace(price+"", buyerPrice+"");
                    newPrices.add(newPriceString);
                    msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR+(i++)+SEPARATOR);
                } else {
                    int newPrice = convertPrice(price, sellerPrice, buyerPrice, sellerSTD, buyerSTD, false);
                    String newPriceString = priceString.replace(price+"", newPrice+"");
                    newPrices.add(newPriceString);
                    msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR+(i++)+SEPARATOR);
                }
            }
            while(i > 0) {
                i--;
                msg = msg.replace(SEPARATOR+i+SEPARATOR, newPrices.get(i));
            }
            msg = msg.replace("\\"+SEPARATOR, SEPARATOR);
            for(String s : words) {
                if(Utils.getLevenshteinDistance(s.toLowerCase(), seller.toLowerCase()) <= Math.max(s.length(),seller.length())/4)
                    msg = msg.replace(s, botNick);
                if(Utils.getLevenshteinDistance(s.toLowerCase(), botNick.toLowerCase()) <= Math.max(s.length(),botNick.length())/4)
                    msg = msg.replace(s, buyer);
                if(Utils.getLevenshteinDistance(s.toLowerCase(), sellerThing.toLowerCase()) <= Math.max(s.length(),sellerThing.length())/4) {
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
        msg = Blame.negateBlames(msg);
        m.setMessage(msg);
        return m;
    }

    private void send(long delay, final Conversation c, final Message m) {
        new Thread(new Runnable(){
                        public void run() {
                            try {
                                Thread.sleep(5000L);
                                c.onMessage(null,m, false);
                            } catch (InterruptedException ex) {
                                return;
                            }
                        }
       }).start();
    }

    private int convertPrice(int price, int sellerPrice, int buyerPrice, float sellerSTD, float buyerSTD, boolean wtb) {
        float priceFloat = (float)price;
        float sellerPriceFloat = (float)sellerPrice;
        float buyerPriceFloat = (float)buyerPrice;
        float STDs = 0;
        float converted;
        if(sellerSTD == 0)
                sellerSTD = sellerPrice*0.341f;
        if(buyerSTD == 0)
                buyerSTD = buyerPrice*0.341f;
        if(wtb) {
            STDs = (price-buyerPrice)/buyerSTD;
            converted = sellerPriceFloat + sellerSTD*STDs;
        } else {
            STDs = (price-sellerPrice)/sellerSTD;
            converted = buyerPriceFloat + buyerSTD*STDs;
        }
        return Utils.roundPrice((int)converted);
    }
}
