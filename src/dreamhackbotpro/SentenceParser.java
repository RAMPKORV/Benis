package dreamhackbotpro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class SentenceParser {

    private Pattern withoutPrice = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ ]{3,})");
    private Pattern withPrice = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ ]{3,}?) ([1-9][0-9]*)kr", Pattern.CASE_INSENSITIVE);
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
        float certainty = 0;

        Matcher matcher = withPrice.matcher(s);
        while (matcher.find()) {
            thing = matcher.group(2).trim();
            words = thing.split(" ");
            if (words.length > 1) {
                thing = parseThing(matcher.group(2));
//                System.out.println("\n\nSENTENCE\n"+matcher.group(2)+"\nPARSED\n"+thing);
            }
            else{
                String familiar = familiarWord(thing);
                if(familiar!=null)
                    thing=familiar;
            }
            // We return the first result, but create the others anyway.
            certainty = 1 / words.length;
            found = new Interest(thing, Integer.parseInt(matcher.group(3)), matcher.group(1).equals("WTB"), certainty);
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
            thing = matcher.group(2).trim();
            words = thing.split(" ");
            if (words.length > 1) {
                thing = parseThing(matcher.group(2));
//                System.out.println("\n\nSENTENCE\n"+matcher.group(2)+"\nPARSED\n"+thing);
            }
            else{
                String familiar = familiarWord(thing);
                if(familiar!=null)
                    thing=familiar;
            }
            // We return the first result, but create the others anyway.
            certainty = 1 / words.length;
            found = new Interest(thing, matcher.group(1).equals("WTB"), certainty);
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

    public String parseThing(String sentence) {
        sentence = sentence.toLowerCase();
        String[] words = sentence.split(" ");
        if(words.length==1){
            return words[0];
        }
        
        //check if the sentence contains any word from other known items. Needs testing.
        for(ThingInfo ti : Interest.getInterestsSorted()){
            String thing = ti.getThing();
            for(String word : words)
                if(thing.equals(word)) {
                    return thing;
                }
        }

        //check if the sentence contains any word with low levenstein distance from known items.
        for(ThingInfo ti : Interest.getInterestsSorted()){
            String thing = ti.getThing();
            for(String word : words) {
                if(getLevenshteinDistance(thing, word) < word.length()/4) {
                    return word;
                }
            }
        }
        
        //second attempt. Checks if word and thing has someting similar. Musmatta would return Matta if that is a known item
        //same alrogitm as familiarWord but for the entire sentence
        for(ThingInfo ti : Interest.getInterestsSorted()) {
            String thing = ti.getThing();
            for(String word : words)
                if(word.length() > 3 && (word.contains(thing) || thing.contains(word))) {
                    return thing;
                }
        }

        // Pick the longest word
        String longest = "";
        for(String word: words) {
            if(word.length() > longest.length())
                longest = word;
        }
        
        // If the longest is less than 4, return whole sentence
        int length = longest.length();
        if(length < 4) {
            if(length == 1) {
                return sentence.replaceAll(" ", "");
            }
            return sentence;
        }

        return longest;

        // If nothing found:
        // For now, simply return the first word.
        //System.out.println("THING3\n"+words[0]);
        //return words[0];
    }

    /**
     * Calculates the number of changes needed to turn one string into another string.
     * @param s
     * @param t
     * @return
     */
    private static int getLevenshteinDistance (String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length();
        int m = t.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n+1];
        int d[] = new int[n+1];
        int _d[];
        int i;
        int j;
        char t_j;
        int cost;

        for (i = 0; i<=n; i++) {
            p[i] = i;
        }

        for (j = 1; j<=m; j++) {
            t_j = t.charAt(j-1);
            d[0] = j;
            for (i=1; i<=n; i++) {
                cost = s.charAt(i-1)==t_j ? 0 : 1;
                d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
            }
            _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }
    
    /**
     * Checks for familiar words. "cigggggg" would return "cigg" since cigg is a known word.
     */
    private String familiarWord(String word){
        word = word.toLowerCase();
        for(ThingInfo ti : Interest.getInterestsSorted()) {
            String thing = ti.getThing();
            if(word.length() > 3 && (word.contains(thing) || thing.contains(word))) {
                return thing;
            }
        }
        return null;
    }

}
