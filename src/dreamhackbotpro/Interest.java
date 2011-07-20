package dreamhackbotpro;

import java.util.Map;
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

    public Interest(String thing, boolean wtb){
        this(thing, -1, wtb);
    }

    public Interest(String thing, int price, boolean wtb) {
        this.thing = thing;
        this.price = price;
        this.wtb = wtb;
        if(interestsMap.containsKey(thing))
            interestsMap.get(thing).addInterest(this);
        else
            interestsMap.put(thing, new ThingInfo(this));
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
