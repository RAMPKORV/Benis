package dreamhackbotpro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class SentenceParser {

    private Pattern withoutPrice = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ\\- ]{3,})");
    private Pattern withPrice = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ\\- ]{3,}?) ([1-9][0-9]*)kr", Pattern.CASE_INSENSITIVE);
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
    public Interest parseInterest(String s) {
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
            if(thing == null) {
                continue;
            }
            if(thing.matches("(?i)([1-9][0-9]+kr|wtb|wts)")) {
                continue;
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
            if(thing == null) {
                continue;
            }
            if(thing.matches("(?i)([1-9][0-9]+kr|wtb|wts)")) {
                continue;
            }
            // We return the first result, but create the others anyway.
            certainty = 1 / words.length;
            found = new Interest(thing, parsePrice(s), matcher.group(1).equals("WTB"), certainty);
            if (result == null) {
                result = found;
            }
        }

        return result;
    }


    Pattern priceSimple = Pattern.compile("([1-9][0-9]+)[ ]?(\\:-|kronor|kr|sp.nn)");
    Pattern seat = Pattern.compile("(^|\\s)([a-d])[ ]*([1-9][0-9]?)([ ,]*(plats)?(\\:)?[ ]*)([1-9][0-9]?)", Pattern.CASE_INSENSITIVE);
    Pattern otherValues1 = Pattern.compile("(orginalpris|orginal pris|nypris|ny pris|model|modell|modellnummer)[ ]+[a-zA-ZåäöÅÄÖ]?[1-9][0-9]+", Pattern.CASE_INSENSITIVE);
    Pattern otherValues2 = Pattern.compile("[0-9]+\\.[0-9]+");
    Pattern eventualPrice = Pattern.compile("[1-9][0-9]+");
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

        //Try parsing simple
        Matcher matcher = priceSimple.matcher(s);
        if(matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch(NumberFormatException ex) {
                //Ignore and go on
            }
        }
        
        //Remove all seats
        matcher = seat.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");
        //Remove other stuff
        matcher = otherValues1.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");
        matcher = otherValues2.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");

        matcher = eventualPrice.matcher(s);
        if(matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(0));
            } catch(NumberFormatException ex) {
                //Ignore and go on
            }
        }

        return -1;
    }
    
        public String parsePriceString(String s) {
        //if \d+kr not found, check for any separate number
        //side effect: model numbers may be only numbers
        //weak solution: Only interpret separate numbers that ends with 0 as prices.

        //problem: if "orginalpris 100kr" found, it will think that that's the price that the item is being sold for

        //Try parsing simple
        Matcher matcher = priceSimple.matcher(s);
        if(matcher.find()) {
            return matcher.group(0);
        }

        //Remove all seats
        matcher = seat.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");
        //Remove other stuff
        matcher = otherValues1.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");
        matcher = otherValues2.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");

        matcher = eventualPrice.matcher(s);
        if(matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    public List<String> parsePriceStrings(String s) {
        List<String> ret = new ArrayList<String>();
        Matcher matcher = priceSimple.matcher(s);
        int index = 0;
        while(matcher.find(index)) {
            ret.add(matcher.group(0));
            index = matcher.start()+matcher.group(0).length();
        }
        //Remove all seats
        matcher = seat.matcher(s);

        while(matcher.find())
            s = matcher.replaceAll("");
        //Remove other stuff
        matcher = otherValues1.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");
        matcher = otherValues2.matcher(s);
        while(matcher.find())
            s = matcher.replaceAll("");

        matcher = eventualPrice.matcher(s);
        index = 0;
        while(matcher.find(index)) {
            ret.add(matcher.group(0));
            index = matcher.start()+matcher.group(0).length();
        }
        return ret;
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
                if(Utils.getLevenshteinDistance(thing, word) <= Math.max(word.length(),thing.length())/4) {
                    return thing;
                }
            }
        }
        for(String item : ThingInfo.getItems()) {
                for(String word : words) {
                if(Utils.getLevenshteinDistance(item, word) <= Math.max(word.length(),item.length())/4) {
                    return item;
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

        // Check if one of the words is a brand. Then return the item om that brand
        Set<String> brands = ThingInfo.getBrands();
        for(String brand : brands) {
            if(sentence.toUpperCase().contains(brand.toUpperCase())) {
                return ThingInfo.getItemByBrand(brand);
            }
        }

        // Check if one of the words is a unit. Then return the item om that unit
        Set<String> units = ThingInfo.getUnits();
        for(String unit : units) {
            if(sentence.toUpperCase().contains(unit.toUpperCase())) {                
                return ThingInfo.getItemByUnit(unit);
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
     * Checks for familiar words. "cigggggg" would return "cigg" since cigg is a known word.
     */
    private String familiarWord(String word){
        if(word.length()<=3)
            return null;
        word = word.toLowerCase();
        for(ThingInfo ti : Interest.getInterestsSorted()) {
            String thing = ti.getThing();
            if(word.length() > 3 && (word.contains(thing) || thing.contains(word))) {
                return thing;
            }
        }
        return null;
    }

    public void parseBuzzWords(Interest i, String s) {
        s = s.replace(i.getThing(), "");
        s = s.replace(""+i.getPrice(), "");
        s = s.replace("WTB", "");
        s = s.replace("WTS", "");
        s = s.replaceAll("(?i)"+Pattern.quote(i.getThing()),"");
        String[] words = s.split(" ");
        for(String word : words) {
            ThingInfo ti = Interest.getInterestsMap().get(i.getThing());
            if(ti != null) {
                ti.mention(word);
            }
        }
        //printMentionsFor(i.getThing()); // For debugging purposes
    }

    private void printMentionsFor(String thing) {
        ThingInfo ti = Interest.getInterestsMap().get(thing);
        if(ti != null) {
            System.out.println("\nMentions for: " + thing);
            List<WordInfo> ws = ti.getMentionedWordsSorted();
            for(WordInfo w : ws) {
                System.out.println(w.getWord());
            }
        }
    }

}
