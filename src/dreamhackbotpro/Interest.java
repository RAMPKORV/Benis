/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class Interest {

    private String thing;
    private int price;
    private boolean wtb;

    public Interest(String thing, boolean wtb){
        this(thing, -1, wtb);
    }

    public Interest(String thing, int price, boolean wtb) {
        this.thing = thing;
        this.price = price;
        this.wtb = wtb;
    }

    public boolean isBuying() {
        return wtb;
    }
    
}
