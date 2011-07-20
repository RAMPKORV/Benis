/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author patrik
 */
class ThingInfo {
    private long counter = 0;
    private List<Integer> prices = new ArrayList<Integer>();
    private int buyers = 0;
    private int sellers = 0;
    private boolean changed = true;
    private float median = 0;

    ThingInfo(Interest i) {
        addInterest(i);
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

    public void addInterest(Interest i) {
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


}
