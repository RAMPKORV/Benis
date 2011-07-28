package dreamhackbotpro;

import java.util.Calendar;
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

    /**
     * Calculates the number of changes needed to turn one string into another string.
     * @param s
     * @param t
     * @return
     */
    public static int getLevenshteinDistance (String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length();
        int m = t.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n+1];
        int d[] = new int[n+1];
        int _d[];
        int i;
        int j;
        char t_j;
        int cost;

        for (i = 0; i<=n; i++) {
            p[i] = i;
        }

        for (j = 1; j<=m; j++) {
            t_j = t.charAt(j-1);
            d[0] = j;
            for (i=1; i<=n; i++) {
                cost = s.charAt(i-1)==t_j ? 0 : 1;
                d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
            }
            _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }

    public static String timeStamp(){
        Calendar cal = Calendar.getInstance();
        return String.format("(%02d:%02d:%02d) ", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }

}
