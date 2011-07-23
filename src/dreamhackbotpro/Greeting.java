/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class Greeting {
    private static String[] iceBreakers = {
        "hej","hejsan","tjo","tja","hallå","yo","tjena","tjenare"
    };
    private static Random random = new Random();
    public static String getGreeting(String thing, int price, boolean wtb) {
        String iceBreaker = pickRandom(iceBreakers);
        if(random.nextInt(100) > 40) { // Capitalize most times.
            iceBreaker = Character.toUpperCase(iceBreaker.charAt(0))+iceBreaker.substring(1);
        }
        return iceBreaker + "."; // Placeholder
    }
    
    private static String pickRandom(String[] s) {
        return s[random.nextInt(s.length)];
    }

    public static boolean isSimpleGreeting(String s) {
        return s.length() < 18 && hasGreeting(s) != null;
    }

    public static String hasGreeting(String s) {
        String filtered = s.replaceAll("[^a-zA-ZåäöÅÄÖ ]", "");
        for(String w : iceBreakers) {
            Pattern p = Pattern.compile("^"+Pattern.quote(w), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher m = p.matcher(s);
            if(m.find()) {
                return m.group(0);
            }
        }
        String[] words = filtered.split(" ");
        int i = 1;
        for(String word: words) {
            for(String w : iceBreakers) {
                if(SentenceParser.getLevenshteinDistance(word, w) < 2) {
                    return word;
                }
            }
            i++;
            if(i >= 3)
                break;
        }
        return null;
    }
}
