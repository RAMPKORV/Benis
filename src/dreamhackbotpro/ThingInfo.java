package dreamhackbotpro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //TODO
    //brandToItem should be used when people announce brands in #trade to get which item they are talking about
    //itemToBrand should be used when buyer asks "vilket märke?" or similar in private chat. If brand found, block the message and reply with brand
    private static Map<String, String> brandToItem = new HashMap<String, String>();
    private static Map<String, String> itemToBrand = new HashMap<String, String>();

    static{
        loadPredefineInterests();
    }

    public String getThing() {
        return thing;
    }

    public ThingInfo(Interest i) {
        this(i, false);
    }
    
    private ThingInfo(Interest i, boolean preDefined) {
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
        newBrands("snus","rapé","portion", "general", "grov");
        newBrands("cigg", "john silver", "marlboro", "black devil", "blend", "level", "maryland", "winston");
        newBrands("tangentbord", "saitek eclipse", "microsoft sidewinder", "steelseries", "steelkeys", "zboard");
        newBrands("mus", "deathadder", "steelseries", "intellimouse", "logitech", "mx518", "naga", "g400", "g500", "g700", "performance mx", "xai");
        newBrands("musmatta", "fnatic", "razer", "steelseries", "qpad");
        newBrands("headset", "siberia v2", "siberia", "steelseries", "logitech", "koss", "sennheiser");
        newBrands("datorskärm", "dell", "lg", "benq", "samsung");
        newBrands("skärm", "dell", "lg", "benq", "samsung");
        newBrands("plattskärm", "dell", "lg", "benq", "samsung");
        newBrands("smartphone", "xperia");
        newBrands("jolt", "jolt");
        newBrands("läsk","jolt","cola","fanta","sprite","dr pepper");
        newBrands("energidryck", "powerking","redbull");

        newPredefineInterest("snus", 50);
        newPredefineInterest("cigg", 60);
        newPredefineInterest("tangentbord", 250);
        newPredefineInterest("mus", 250);
        newPredefineInterest("jolt", 10);
        newPredefineInterest("keps", 50);
    }

    /**
     * @param item Name of the item
     * @param brands Possible brands, the first brand will be used in itemToBrand
     */
    private static void newBrands(String item, String... brands){
        itemToBrand.put(item, brands[0]);
        for(String brand : brands){
            brandToItem.put(brand, item);
        }
    }

    private static void newPredefineInterest(String thing, int price){
        Interest i = new Interest(thing, price, true, 1f);
        ThingInfo ti = new ThingInfo(i, true);
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
            if(Utils.getLevenshteinDistance(fromWord.getWord(), word) < Math.max(word.length(),fromWord.getWord().length())/4) {
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
            if(Utils.getLevenshteinDistance(w, word) <= Math.max(w.length(),word.length())/4)
                return true;
        }
        return false;
    }

    public String getDefaultBrand() {
        return itemToBrand.get(thing);
    }

    public static String getItemByBrand(String brand) {
        return brandToItem.get(brand);
    }

    public static String getBrandByItem(String item) {
        return itemToBrand.get(item);
    }


    private static String[] brandQuestions = {
        "^märke\\?$","vad för märke\\?","vad är det för märke\\?","vilket märke","vilket märke\\?","^modell\\?$",
        "^vilken modell\\?","^tillverkare\\?","^vilken tillverkare"
    };

    public static boolean isBrandQuestion(String msg) {
        msg = msg.toLowerCase();
        for(String q : brandQuestions) {
            if(msg.matches(q))
                return true;
        }
        return false;
    }

    private static Pattern xy = Pattern.compile("(?i)^([a-zA-Z0-9åäöÅÄÖ-]+)[ ]+eller[ ]+([a-zA-Z0-9åäöÅÄÖ-]+)($|\\?)");
    public static String[] isXorYQuestion(String msg) {
        Matcher m = xy.matcher(msg);
        if(m.find()) {
            return new String[]{m.group(1),m.group(2)};
        }
        return null;
    }

    private static String[] confirmQuestions = {
        "^är det [a-zåäöÅÄÖ]+[ ]*(\\?|$)", "skall vi säga så[a-zA-ZåäöÅÄÖ ]*\\?"
    };
    public static boolean isConfirmQuestion(String msg) {
        msg = msg.toLowerCase();
        for(String q : confirmQuestions) {
            if(msg.matches(q))
                return true;
        }
        return false;
    }

    private static String[] howmanyQuestions = {
        "^hur många\\??$","^hur många vill du ha","^hur många skall du ha"
    };
    public static boolean isHowManyQuestion(String msg) {
        msg = msg.toLowerCase();
        for(String q : howmanyQuestions) {
            if(msg.matches(q))
                return true;
        }
        return false;
    }

}
