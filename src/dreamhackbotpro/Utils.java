package dreamhackbotpro;

import java.util.Random;

/**
 * Class with static stuff that is used by many classes
 * @author wasd
 */
public class Utils {

    public static final Random random = new Random();

    /**
     * Rounds a price into a human appreciated form
     */
    public static int roundPrice(int price){
        if(price<20)
            return price;
        if(price<100)
            return Math.round(price/5f)*5;
        if(price<500)
            return Math.round(price/10f)*10;
        if(price<1000)
            return Math.round(price/25f)*25;
        return Math.round(price/50f)*50;
    }

}
