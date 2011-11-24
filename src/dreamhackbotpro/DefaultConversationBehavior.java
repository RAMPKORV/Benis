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

    private DefaultConversationBehavior() {
    }

    public static synchronized DefaultConversationBehavior getInstance() {
        if (instance == null) {
            instance = new DefaultConversationBehavior();
        }
        return instance;
    }

    @Override
    public Message transformMessage(final Conversation c, Message m) {
        SentenceParser p = SentenceParser.getInstance();
        String msg = m.getMessage();

        // Remove immediate affirmations like "Japp"
        if (c.getNumMessages() <= 3) {
            if (Greeting.isAffirmation(msg)) {
                m.setMessage("");
                return m;
            }
        }

        // Remove references to original greetings
        List<String> greetings = c.getGreetings();
        for (String greeting : greetings) {
            if (msg.contains(greeting)) {
                m.setMessage("");
                return m;
            }
        }

        // Remove simple greetings
        if (Greeting.isSimpleGreeting(msg)) {
            m.setMessage("");
            return m;
        }

        // Delete greetings after the first two messages of the conversation        
        String greeting;
        do {
            greeting = Greeting.hasGreeting(msg);
            if (greeting != null) {
                msg = msg.replaceAll("(?i)" + greeting + "[^ ]*", "");
            }
        } while (greeting != null);

        //Convenience
        final UserInfo bot = m.getBotInfo();
        final UserInfo buyer = c.getBuyer().getUserInfo();
        final UserInfo seller = c.getSeller().getUserInfo();
        String buyerThing = c.getBuyerThing();
        String sellerThing = c.getSellerThing();
        final String buyerBrand = ThingInfo.getBrandByItem(buyerThing);
        final String sellerBrand = ThingInfo.getBrandByItem(sellerThing);
        int buyerPrice = c.getBuyerPrice();
        int sellerPrice = c.getSellerPrice();
        float buyerSTD = c.getBuyerSTD();
        float sellerSTD = c.getSellerSTD();
        String[] words = msg.split("[ \\.\\!\\?\\,](?![0-9])");
        boolean hasSeat = SeatReader.getSeat(m.getMessage()) != null;

        // Message originated from buyer
        if (m.getFrom().nick.equals(c.getBuyer().getName())) {

            // Replace brands
            if (buyerBrand != null && sellerBrand != null) {
                msg = msg.replace(buyerBrand, sellerBrand);
            } else {
                if (buyerBrand != null && sellerBrand == null) {
                    msg = msg.replace(buyerBrand, sellerThing);
                }
            }

            // Handle brand questions
            if (buyerBrand != null && ThingInfo.isBrandQuestion(msg, buyerThing)) {
                m.setMessage("");
                send(3000L + Utils.random.nextInt(3000), c, new Message(seller, buyerBrand, buyer, bot));
                return m;
            }

            // Handle "X or Y" questions
            String[] xy = ThingInfo.isXorYQuestion(msg);
            if (xy != null) {
                m.setMessage("");
                send(3000L + Utils.random.nextInt(3000), c, new Message(seller, xy[Utils.random.nextInt(xy.length)], buyer, bot));
                return m;
            }

            // Handle confirm questions
            if (ThingInfo.isConfirmQuestion(msg)) {
                m.setMessage("");
                send(3000L + Utils.random.nextInt(3000), c, new Message(seller, Greeting.getYes(), buyer, bot));
                return m;
            }

            // Handle "How many" questions
            if (ThingInfo.isHowManyQuestion(msg)) {
                m.setMessage("");
                send(3000L + Utils.random.nextInt(3000), c, new Message(seller, "" + (Utils.random.nextInt(4) + 1), buyer, bot));
                return m;
            }

            // Handle prices
            List<String> priceStrings = p.parsePriceStrings(msg);
            int i = 0;
            List<String> newPrices = new ArrayList<String>();
            msg = msg.replace(SEPARATOR, "\\" + SEPARATOR);
            for (String priceString : priceStrings) {
                if (buyerPrice <= 0) {
                    continue;
                }

                int price = p.parsePrice(priceString);
                if (price <= 0) {
                    continue;
                }

                if (price == buyerPrice) {
                    String newPriceString = priceString.replace(price + "", sellerPrice + "");
                    newPrices.add(newPriceString);
                    msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR + (i++) + SEPARATOR);
                } else {
                    int newPrice = convertPrice(price, sellerPrice, buyerPrice, sellerSTD, buyerSTD, true);
                    if (!hasSeat && newPrice > 0) {
                        String newPriceString = priceString.replace(price + "", newPrice + "");
                        newPrices.add(newPriceString);
                        msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR + (i++) + SEPARATOR);
                    }
                }
            }
            while (i > 0) {
                i--;
                msg = msg.replace(SEPARATOR + i + SEPARATOR, newPrices.get(i));
            }
            msg = msg.replace("\\" + SEPARATOR, SEPARATOR);

            // Iterate each word
            for (String s : words) {

                // Replace mentions to own and bot nick, ident, host or IP.
                if (Utils.getLevenshteinDistance(s.toLowerCase(), buyer.nick.toLowerCase()) <= Math.max(s.length(), buyer.nick.length()) / 4) {
                    msg = msg.replace(s, bot.nick);
                }
                if (!buyer.requireWhois()) {
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), buyer.ident.toLowerCase()) <= Math.max(s.length(), buyer.ident.length()) / 4) {
                        msg = msg.replace(s, bot.ident);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), buyer.host.toLowerCase()) <= Math.max(s.length(), buyer.host.length()) / 4) {
                        msg = msg.replace(s, bot.host);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), buyer.ip.toLowerCase()) <= Math.max(s.length(), buyer.ip.length()) / 4) {
                        msg = msg.replace(s, bot.ip);
                    }
                }
                if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.nick.toLowerCase()) <= Math.max(s.length(), bot.nick.length()) / 4) {
                    msg = msg.replace(s, seller.nick);
                }
                if (!bot.requireWhois()) {
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.ident.toLowerCase()) <= Math.max(s.length(), bot.ident.length()) / 4) {
                        msg = msg.replace(s, seller.ident);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.host.toLowerCase()) <= Math.max(s.length(), bot.host.length()) / 4) {
                        msg = msg.replace(s, seller.host);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.ip.toLowerCase()) <= Math.max(s.length(), bot.ip.length()) / 4) {
                        msg = msg.replace(s, seller.ip);
                    }
                }

                if (Utils.getLevenshteinDistance(s.toLowerCase(), buyerThing.toLowerCase()) <= Math.max(s.length(), buyerThing.length()) / 4) {
                    msg = msg.replace(s, sellerThing);
                } else {
                    if (s.toLowerCase().contains(buyerThing)) {
                        msg = msg.replace(s, sellerThing);
                    }
                }

                // Handle buzz words; words mentioned in conjunction with buyerthing/sellerthing
                String translated = ThingInfo.translateBuzzWord(buyerThing, sellerThing, s);
                if (!translated.equals(s)) {
                    msg = msg.replace(s, translated);
                }

                String brandfix = brandToCorrectBrand(s, buyerThing);
                if (brandfix != null) {
                    msg = msg.replace(s, brandfix);
                }
            }
        } else { // Message originated from seller

            // Replace brands
            if (buyerBrand != null && sellerBrand != null) {
                msg = msg.replace(sellerBrand, buyerBrand);
            } else {
                if (sellerBrand != null && buyerBrand == null) {
                    msg = msg.replace(sellerBrand, buyerThing);
                }
            }

            // Handle "X or Y" questions
            String[] xy = ThingInfo.isXorYQuestion(msg);
            if (xy != null) {
                m.setMessage("");
                send(5000L, c, new Message(buyer, xy[Utils.random.nextInt(xy.length)], seller, bot));
                return m;
            }

            // Handle confirm questions
            if (ThingInfo.isConfirmQuestion(msg)) {
                m.setMessage("");
                send(5000L, c, new Message(buyer, Greeting.getYes(), seller, bot));
                return m;
            }

            // Handle "How many" questions
            if (ThingInfo.isHowManyQuestion(msg)) {
                m.setMessage("");
                send(5000L, c, new Message(buyer, "" + (Utils.random.nextInt(4) + 1), seller, bot));
                return m;
            }

            // Handle prices
            List<String> priceStrings = p.parsePriceStrings(msg);
            int i = 0;
            List<String> newPrices = new ArrayList<String>();
            msg = msg.replace(SEPARATOR, "\\" + SEPARATOR);
            for (String priceString : priceStrings) {
                if (sellerPrice <= 0) {
                    continue;
                }
                int price = p.parsePrice(msg);
                if (price <= 0) {
                    continue;
                }
                if (price == sellerPrice) {
                    String newPriceString = priceString.replace(price + "", buyerPrice + "");
                    newPrices.add(newPriceString);
                    msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR + (i++) + SEPARATOR);
                } else {
                    int newPrice = convertPrice(price, sellerPrice, buyerPrice, sellerSTD, buyerSTD, false);
                    if (!hasSeat && newPrice > 0) {
                        String newPriceString = priceString.replace(price + "", newPrice + "");
                        newPrices.add(newPriceString);
                        msg = msg.replaceFirst(Pattern.quote(priceString), SEPARATOR + (i++) + SEPARATOR);
                    }
                }
            }
            while (i > 0) {
                i--;
                msg = msg.replace(SEPARATOR + i + SEPARATOR, newPrices.get(i));
            }
            msg = msg.replace("\\" + SEPARATOR, SEPARATOR);

            // Iterate each word
            for (String s : words) {

                // Replace mentions to own and bot nick, ident, host or IP.
                if (Utils.getLevenshteinDistance(s.toLowerCase(), seller.nick.toLowerCase()) <= Math.max(s.length(), seller.nick.length()) / 4) {
                    msg = msg.replace(s, bot.nick);
                }
                if (!seller.requireWhois()) {
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), seller.ident.toLowerCase()) <= Math.max(s.length(), seller.ident.length()) / 4) {
                        msg = msg.replace(s, bot.ident);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), seller.host.toLowerCase()) <= Math.max(s.length(), seller.host.length()) / 4) {
                        msg = msg.replace(s, bot.host);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), seller.ip.toLowerCase()) <= Math.max(s.length(), seller.ip.length()) / 4) {
                        msg = msg.replace(s, bot.ip);
                    }
                }
                if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.nick.toLowerCase()) <= Math.max(s.length(), bot.nick.length()) / 4) {
                    msg = msg.replace(s, buyer.nick);
                }
                if (!bot.requireWhois()) {
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.ident.toLowerCase()) <= Math.max(s.length(), bot.ident.length()) / 4) {
                        msg = msg.replace(s, buyer.ident);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.host.toLowerCase()) <= Math.max(s.length(), bot.host.length()) / 4) {
                        msg = msg.replace(s, buyer.host);
                    }
                    if (Utils.getLevenshteinDistance(s.toLowerCase(), bot.ip.toLowerCase()) <= Math.max(s.length(), bot.ip.length()) / 4) {
                        msg = msg.replace(s, buyer.ip);
                    }
                }
                if (Utils.getLevenshteinDistance(s.toLowerCase(), sellerThing.toLowerCase()) <= Math.max(s.length(), sellerThing.length()) / 4) {
                    msg = msg.replace(s, buyerThing);
                } else {
                    if (s.toLowerCase().contains(sellerThing)) {
                        msg = msg.replace(s, buyerThing);
                    }
                }

                // Handle buzz words; words mentioned in conjunction with buyerthing/sellerthing
                String translated = ThingInfo.translateBuzzWord(sellerThing, buyerThing, s);
                if (!translated.equals(s)) {
                    msg = msg.replace(s, translated);
                }

                String brandfix = brandToCorrectBrand(s, sellerThing);
                if (brandfix != null) {
                    msg = msg.replace(s, brandfix);
                }

            }
        }

        // Handle blames
        msg = Confuse.negateBlames(msg);
        // Handle mismatched interests
        msg = Confuse.negateDoNots(msg);
        // Handle confusion
        msg = Confuse.negateConfusion(msg);
        m.setMessage(msg);
        return m;
    }

    // Wait for a while, then send a message
    private void send(final long delay, final Conversation c, final Message m) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(delay);
                    c.onMessage(null, m, false);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }).start();
    }

    // Translate prices between seller and buyer
    private int convertPrice(int price, int sellerPrice, int buyerPrice, float sellerSTD, float buyerSTD, boolean wtb) {
        float sellerPriceFloat = (float) sellerPrice;
        float buyerPriceFloat = (float) buyerPrice;
        float STDs = 0;
        float converted;
        if (sellerSTD == 0) {
            sellerSTD = sellerPrice * 0.341f;
        }
        if (buyerSTD == 0) {
            buyerSTD = buyerPrice * 0.341f;
        }
        if (wtb) {
            STDs = (price - buyerPrice) / buyerSTD;
            converted = sellerPriceFloat + sellerSTD * STDs;
        } else {
            STDs = (price - sellerPrice) / sellerSTD;
            converted = buyerPriceFloat + buyerSTD * STDs;
        }

        return Utils.roundPrice((int) converted);
    }

    /**
     * Tries to convert an incorrect brand to a correct one
     */
    public String brandToCorrectBrand(String sentbrand, String item) {

        if (ThingInfo.brandToItem.get(sentbrand) == null) {
            return null; //not a known brand
        }
        String newbrand = ThingInfo.itemToBrand.get(item);
        if (newbrand == null) {
            return null;
        }

        return newbrand;
    }
}
