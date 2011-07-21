package dreamhackbotpro;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author patrik
 */
public class Interest {

    private String thing;
    private int price;
    private boolean wtb;

    private static Map<String,ThingInfo> interestsMap = new HashMap<String,ThingInfo>();
    private static volatile SortedSet<ThingInfo> interestsSorted = new TreeSet<ThingInfo>();

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
            ThingInfo ti = interestsMap.get(thing);
            if(ti!=null){
                ti.addInterest(this);
            }
            else{
                ti = new ThingInfo(this);
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
