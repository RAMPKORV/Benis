/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author patrik
 */
public class ThingInfo implements Comparable {
    private String thing = null;
    private long counter = 0;
    private List<Integer> prices = new ArrayList<Integer>();
    private int buyers = 0;
    private int sellers = 0;
    private boolean changed = true;
    private float median = 0;

    ThingInfo(Interest i) {
        thing = i.getThing();
        try {
            addInterest(i);
        } catch(Exception ex) {}
    }

    public int getBuyers() {
        return buyers;
    }

    public long getCounter() {
        return counter;
    }

    public int getSellers() {
        return sellers;
    }

    public float getMedian() {
        if(!changed)
            return median;
        Collections.sort(prices);
        median = med(prices);
        changed = false;
        return median;
    }

    public void addInterest(Interest i) throws Exception {
        if(thing != null && !i.getThing().equals(thing)) {
            throw new Exception("Wrong interest");
        }
        prices.add(i.getPrice());
        counter++;
        if(i.isBuying())
            buyers++;
        else
            sellers++;
        changed = true;
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

    public int compareTo(Object t) {
        if(!(t instanceof ThingInfo))
            throw new ClassCastException();
        ThingInfo ti = (ThingInfo)t;
        if(this.counter > ti.getCounter())
            return 1;
        if(this.counter == ti.getCounter())
            return 0;
        else
            return -1;
    }


}
