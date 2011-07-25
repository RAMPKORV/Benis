/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author patrik
 */
public class ThingInfo implements Comparable<ThingInfo> {
    private String thing = null;
    private int counter = 0;
    private List<Integer> prices = new ArrayList<Integer>();
    private Map<String,WordInfo> buzzWordsMap = new HashMap<String,WordInfo>();
    private volatile List<WordInfo> buzzWordsSorted = new ArrayList<WordInfo>();
    private int minPrice = 0;
    private int maxPrice = 0;
    private int buyers = 0;
    private int sellers = 0;
    private boolean medianCalculated = false;
    private boolean stdDevCalculated = false;
    private float median = 0;
    private float stdDev = 0;
    private boolean predefined;
    private String defaultBrand;

    static{
        loadPredefineInterests();
    }

    public String getThing() {
        return thing;
    }

    public ThingInfo(Interest i) {
        this(i, false, null);
    }
    
    private ThingInfo(Interest i, boolean preDefined, String defaultBrand) {
        this.defaultBrand=defaultBrand;
        thing = i.getThing();
        try {
            addInterest(i);
        } catch(Exception ex) {}
        if(preDefined){
            predefined=true;
            counter = 1;
            buyers=0;
            sellers=0;
        }
    }

    public int getBuyers() {
        return buyers;
    }

    public int getCounter() {
        return counter;
    }

    public int getSellers() {
        return sellers;
    }

    public float getMedian() {
        if(medianCalculated)
            return median;
        if(prices.isEmpty())
            return -1;
        Collections.sort(prices);
        median = med(prices);
        medianCalculated = true;
        return median;
    }

    /**
     * Get standard deviant, where median is taken as average
     * @return
     */
    public float getStdDev() {
        if(stdDevCalculated)
            return stdDev;
        float med = getMedian();
        float result = 0;
        for(Integer i : prices) {
            if(i == -1)
                continue;
            result += ((float)i - med)*((float)i - med);
        }
        result = (float) Math.sqrt(result / counter);
        stdDevCalculated = true;
        stdDev = (float)result;
        return (float)result;
    }

    public void addInterest(Interest i) throws Exception {
        if(thing != null && !i.getThing().equals(thing)) {
            throw new Exception("Wrong interest");
        }
        if(i.getPrice() != -1)
           prices.add(i.getPrice());
        counter++;
        if(i.isBuying())
            buyers++;
        else
            sellers++;
        
        if(i.getPrice()>0){
            if(i.getPrice()>maxPrice)
                maxPrice=i.getPrice();
            if(i.getPrice()<minPrice || minPrice==0)
                minPrice=i.getPrice();
        }
        
        medianCalculated = false;
        stdDevCalculated = false;
    }

    public static float med(List<Integer> data) {
        float result;
        if (data.size() % 2 == 1) {
            result = data.get( (int) Math.floor(data.size()/2) );
        } else {
            float lowerMiddle = data.get( data.size()/2 );
            float upperMiddle = data.get( data.size()/2 - 1 );
            result = (lowerMiddle + upperMiddle) / 2;
        }
        return result;
    }

    @Override
    public int compareTo(ThingInfo ti) {
        if(predefined && !ti.isPredefined())
            return -1;
        if(!predefined && ti.isPredefined())
            return 1;
        if(this.counter < ti.getCounter())
            return 1;
        if(this.counter == ti.getCounter())
            return thing.compareTo(ti.getThing());
        else
            return -1;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    private static void loadPredefineInterests(){
        newPredefineInterest("snus", 50, "rapé");
        newPredefineInterest("cigg", 60, "john silver");
        newPredefineInterest("tangentbord", 250, "saitek eclipse");
        newPredefineInterest("deathadder", 300, null);
        newPredefineInterest("jolt", 10, "jolt");
        newPredefineInterest("keps", 50, null);
    }

    private static void newPredefineInterest(String thing, int price, String defaultBrand){
        Interest i = new Interest(thing, price, true, 1f);
        ThingInfo ti = new ThingInfo(i, true, defaultBrand);
        Interest.getInterestsMap().put(thing, ti);
        Interest.getInterestsSorted().add(ti);
    }

    public boolean isPredefined() {
        return predefined;
    }

    public void mention(String word) {
        word = word.toLowerCase();
        word = word.replaceAll("[^a-zA-ZåäöÅÄÖ0-9]", "");
        if(word.length() < 4)
            return;
        if(useless(word)) {
            return;
        }
        WordInfo wi = buzzWordsMap.get(word);
        int occurances;
        if(wi == null) {
            occurances = 1;
            wi = new WordInfo(word, occurances);
            buzzWordsSorted.add(wi);
        } else {
            occurances = wi.getMentions()+1;
        }
        wi.setMentions(occurances);
        buzzWordsMap.put(word, wi);
    }

    public static String translateBuzzWord(String fromThing, String toThing, String word) {
        try {
        ThingInfo from = Interest.getInterestsMap().get(fromThing);
        ThingInfo to = Interest.getInterestsMap().get(toThing);
        if(from == null || to == null)
            return word;
        List<WordInfo> fromWords = from.getMentionedWordsSorted();
        List<WordInfo> toWords = to.getMentionedWordsSorted();
        if(fromWords == null || toWords == null)
            return word;
        boolean contained = false;
        for(WordInfo fromWord : fromWords) {
            if(SentenceParser.getLevenshteinDistance(fromWord.getWord(), word) < word.length()/4) {
                word = fromWord.getWord();
                contained = true;
                break;
            }
        }
        if(!contained)
            return word;

        // Only use the mostly mentioned third.
        fromWords = fromWords.subList(0, fromWords.size()/3);
        toWords = toWords.subList(0, toWords.size()/3);

        int fromSize = fromWords.size();
        int toSize = fromWords.size();
        if(fromSize == 0 || toSize == 0)
            return word;

        return(toWords.get(fromWords.indexOf(word) % toWords.size()).getWord());
        } catch(Exception ex) {
            ex.printStackTrace();
            return word;
        }
    }

    public List<WordInfo> getMentionedWordsSorted() {
        Collections.sort(buzzWordsSorted);
        return buzzWordsSorted;
    }

    private boolean useless(String word) {
        // Non-special words that should not be translated
        String[] uselessWords = {
            "ingen","någon","hallen","eller",
            "helt","intresserad","båda","annan",
            "vanligt","följer","med","sätter",
            "kostade","kostar","text","olika",
            "till","skriv","aldrig","nästan",
            "använd","annat","kommer","levererar",
            "medföljer","sidan","spänn","halvt",
            "använt","full","endast","från",
            "alla","dugligt","fult","runt",
            "spela","bara","bytes","halv",
            "oöppnat","ursprungspris","något","gamla",
            "grannes","legat","efter","fråga",
            "gärna","mycket","priser","mindre",
            "fungerar","klockrent","köpa","sälja",
            "inom","samt","saknas","dock",
            "about","price","minut","cheap",
            "inte","billigt","innom","nacken",
            "very","andra","ifall","iskall",
            "sånt","uppskattat","nypris","installerat",
            "cirka","bannat","under"
        };
        if(word.matches("[1-9][0-9]*kr"))
            return true;
        for(String w : uselessWords) {
            if(SentenceParser.getLevenshteinDistance(w, word) <= word.length()/4)
                return true;
        }
        return false;
    }

    public String getDefaultBrand() {
        return defaultBrand;
    }

}
