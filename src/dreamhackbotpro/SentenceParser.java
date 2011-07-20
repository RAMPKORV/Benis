package dreamhackbotpro;

/**
 *
 * @author wasd
 */
public class SentenceParser {
    
    private static SentenceParser instance;
    
    private SentenceParser() {}
    
    public static synchronized SentenceParser getInstance(){
        if(instance==null)
            instance = new SentenceParser();
        return instance;
    }
    
    /**
     * Parse a sentence to see what the user wants to buy or sell
     * @param u The User who said the sentence
     * @param s The sentence to parse
     * @return The the Interest parsed. null if no Interest found
     */
    public Interest parseSentences(String s){
        
        
        //Problem:
        //A user may send "WTB snus. 50kr" in two sentences. In that case the two sentences would be parsed separatly
        //Possible solution: Send in the entire message the user sent. Then return an array of Interests that Bot then adds to the User.
        
        /**psuedo code:
         * if matches pattern: "(WTB|WTS) [A-Za-zÅÄÖåäö]+ \d+kr" Example: WTB snus 50kr
         *      return new Interest(secondWord, parseInt(thirdWord without kr), firstWord==WTB)
         * 
         * if matches pattern: "(WTB|WTS) [A-Za-zÅÄÖåäö]+" Example: WTB snus
         *      return new Interest(secondWord, firstWord==WTB)
         */
        
        
        //user parsePrice to parse the price if an item is found
        
        return null;
    }
    
    /**
     * Parses the price from a sentence
     * @param s The sentence
     * @return The price. -1 if not found
     */
    public int parsePrice(String s){
        
        //if \d+kr not found, check for any separate number
        //side effect: model numbers may be only numbers
        //weak solution: Only interpret separate numbers that ends with 0 as prices.
        
        //problem: if "orginalpris 100kr" found, it will think that that's the price that the item is being sold for
        
        return -1;
    }
    
}
