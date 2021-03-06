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
public class Interest {

    private String thing;
    private int price;
    private boolean wtb;
    private float certainty;
    private static Map<String, ThingInfo> interestsMap = new HashMap<String, ThingInfo>();
    private static volatile List<ThingInfo> interestsSorted = new ArrayList<ThingInfo>();

    public static Map<String, ThingInfo> getInterestsMap() {
        return interestsMap;
    }

    public static synchronized List<ThingInfo> getInterestsSorted() {
        return interestsSorted;
    }

    public Interest(String thing, boolean wtb, float certainty) {
        this(thing, -1, wtb, certainty);
    }

    public Interest(String thing, int price, boolean wtb, float certainty) {
        String item = ThingInfo.getItemByBrand(thing);
        if (item == null) {
            this.thing = thing.toLowerCase();
        } else {
            this.thing = item;
        }
        this.price = price;
        this.wtb = wtb;
        this.certainty = certainty;

        if (price <= 0) {
            certainty--;
        }

        synchronized (interestsSorted) {
            for (ThingInfo ti : interestsSorted) {
                if (ti.getThing().equals(thing)) {
                    certainty += (ti.getBuyers() + ti.getSellers());
                    break;
                }
            }
        }

        //addToInterestsMap();
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
        return thing + '-' + price + '-' + wtb;
    }

    public void addToInterestsMap() {
        try {
            ThingInfo ti = interestsMap.get(thing);
            if (ti != null) {
                ti.addInterest(this);
            } else {
                ti = new ThingInfo(this);
                interestsMap.put(thing, ti);
                interestsSorted.add(ti);
                Collections.sort(interestsSorted);
            }
        } catch (Exception ex) {
            // Do nothing, for now
        }
    }

    /**
     * Used when a User says he wants to buy or sell the same thing twice
     */
    public void update(Interest theInterest) {
        //TODO find out why the if-statenent is there
        if (theInterest.price > this.price) {
            this.price = theInterest.price;
            //TODO also update ThingInfo with the new price
        }
    }

    public float getCertainty() {
        return certainty;
    }
}
