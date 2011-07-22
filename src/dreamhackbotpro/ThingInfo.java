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
public class ThingInfo implements Comparable<ThingInfo> {
    private String thing = null;
    private int counter = 0;
    private List<Integer> prices = new ArrayList<Integer>();
    private int minPrice = 0;
    private int maxPrice = 0;
    private int buyers = 0;
    private int sellers = 0;
    private boolean medianCalculated = false;
    private boolean stdDevCalculated = false;
    private float median = 0;
    private float stdDev = 0;

    public String getThing() {
        return thing;
    }

    public ThingInfo(Interest i) {
        thing = i.getThing();
        try {
            addInterest(i);
        } catch(Exception ex) {}
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

    public int compareTo(ThingInfo ti) {
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


}
