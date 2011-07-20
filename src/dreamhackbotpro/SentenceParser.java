package dreamhackbotpro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class SentenceParser {

    private Pattern withoutPrice = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ ]+)");
    private Pattern withPrice = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ ]+?) ([1-9][0-9]*)kr");
    private static SentenceParser instance;

    private SentenceParser() {
    }

    public static synchronized SentenceParser getInstance() {
        if (instance == null) {
            instance = new SentenceParser();
        }
        return instance;
    }

    /**
     * Parse a sentence to see what the user wants to buy or sell
     * @param u The User who said the sentence
     * @param s The sentence to parse
     * @return The the Interest parsed. null if no Interest found
     */
    public Interest parseSentences(String s) {
        //Problem:
        //A user may send "WTB snus. 50kr" in two sentences. In that case the two sentences would be parsed separatly
        //Possible solution: Send in the entire message the user sent. Then return an array of Interests that Bot then adds to the User.
        //Possible solution: Add a filter that merges the sentences. Something like "(WTB|WTS) +(!\d+kr) +\. (!WTB|WTS) +\d+kr" then replace . with ,
        
        Interest result = null;
        Interest found = null;
        String thing = null;
        String[] words = null;

        Matcher matcher = withPrice.matcher(s);
        while (matcher.find()) {
            thing = matcher.group(2);
            words = thing.split(" ");
            if (words.length > 1) {
                thing = parseThing(matcher.group(2));
            }
            // We return the first result, but create the others anyway.
            found = new Interest(thing, Integer.parseInt(matcher.group(3)), matcher.group(1).equals("WTB"));
            if (result == null) {
                result = found;
            }
        }

        // Return if we already have our stuff
        if (result != null) {
            return result;
        }

        matcher = withoutPrice.matcher(s);
        while (matcher.find()) {
            thing = matcher.group(2);
            words = thing.split(" ");
            if (words.length > 1) {
                thing = parseThing(matcher.group(2));
            }
            // We return the first result, but create the others anyway.
            found = new Interest(matcher.group(2), matcher.group(1).equals("WTB"));
            if (result == null) {
                result = found;
            }
        }

        //user parsePrice to parse the price if an item is found

        return result;
    }

    /**
     * Parses the price from a sentence
     * @param s The sentence
     * @return The price. -1 if not found
     */
    public int parsePrice(String s) {

        //if \d+kr not found, check for any separate number
        //side effect: model numbers may be only numbers
        //weak solution: Only interpret separate numbers that ends with 0 as prices.

        //problem: if "orginalpris 100kr" found, it will think that that's the price that the item is being sold for

        return -1;
    }

    public String parseThing(String thing) {
        // For now, simply return the first word.
        return thing.split(" ")[0];
    }
}
