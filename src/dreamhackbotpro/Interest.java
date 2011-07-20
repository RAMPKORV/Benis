package dreamhackbotpro;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;

/**
 *
 * @author patrik
 */
public class Interest {

    private String thing;
    private int price;
    private boolean wtb;

    private static Map<String,ThingInfo> interestsMap = new WeakHashMap<String,ThingInfo>();
    private static SortedSet<ThingInfo> interestsSorted = new TreeSet<ThingInfo>();

    public static Map<String, ThingInfo> getInterestsMap() {
        return interestsMap;
    }

    public static SortedSet<ThingInfo> getInterestsSorted() {
        return interestsSorted;
    }
    
    public Interest(String thing, boolean wtb){
        this(thing, -1, wtb);
    }

    public Interest(String thing, int price, boolean wtb) {
        this.thing = thing;
        this.price = price;
        this.wtb = wtb;
        try {
            if(interestsMap.containsKey(thing)) {
                interestsMap.get(thing).addInterest(this);
            } else {
                ThingInfo ti = new ThingInfo(this);
                interestsMap.put(thing, ti);
                interestsSorted.add(ti);
            }
        } catch(Exception ex) {
                // Do nothing, for now
        }
    }

    public boolean isBuying() {
        return wtb;
    }

    public int getPrice() {
        return price;
    }

    public String getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return thing+'-'+price+'-'+wtb;
    }
    
    
}
