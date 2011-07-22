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

    private static Map<String,ThingInfo> interestsMap = new HashMap<String,ThingInfo>();
    private static volatile List<ThingInfo> interestsSorted = new ArrayList<ThingInfo>();

    static{
        loadPredefineInterests();
    }

    public static Map<String, ThingInfo> getInterestsMap() {
        return interestsMap;
    }

    public static List<ThingInfo> getInterestsSorted() {
        return interestsSorted;
    }
    
    public Interest(String thing, boolean wtb, float certainty){
        this(thing, -1, wtb, certainty);
    }

    public Interest(String thing, int price, boolean wtb, float certainty) {
        this.thing = thing.toLowerCase();
        this.price = price;
        this.wtb = wtb;
        this.certainty = certainty;
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
        return thing+'-'+price+'-'+wtb;
    }
    
    public void addToInterestsMap(){
        try {
            ThingInfo ti = interestsMap.get(thing);
            if(ti!=null){
                ti.addInterest(this);
            }
            else{
                ti = new ThingInfo(this);
                interestsMap.put(thing, ti);
                interestsSorted.add(ti);
                Collections.sort(interestsSorted);
            }
        } catch(Exception ex) {
                // Do nothing, for now
        }
    }

    /**
     * Used when a User says he wants to buy or sell the same thing twice
     */
    public void update(Interest theInterest) {
        if(theInterest.price>this.price){
            this.price=theInterest.price;
            //TODO also update ThingInfo with the new price
        }
    }

    private static void loadPredefineInterests(){
        newPredefineInterest("snus", 50);
        newPredefineInterest("cigg", 60);
    }

    private static void newPredefineInterest(String thing, int price){
        Interest i = new Interest(thing, price, true, 1f);
        ThingInfo ti = new ThingInfo(i, true);
        interestsMap.put(thing, ti);
        interestsSorted.add(ti);
    }
    
    
}
