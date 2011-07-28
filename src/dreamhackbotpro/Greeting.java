package dreamhackbotpro;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class Greeting {
    private Greeting() {}
    private static String[] iceBreakers = {
        "","hej. ", "hej! ","hejsan. ",
        "tjo! ","tjo ","tja!","tja... ",
        "hallå ","yo ","tjena! ","tjenare! "
    };
    private static String[] iceBreakersClean = {
        "hejsan","hej","tjo","tja",
        "hallå","tjena","tjenare","goddag",
        "gokväll"
    };
    private static String[] buyingProposals = {
        "%price%%currency% för %thing%?",
        "Köper %thing% för %price%%currency%",
        "Säljer du %thing% för %price%%currency%?",
        "Får jag köpa %thing% för %price%%currency%?",
        "Jag köper %thing%",
    };
    private static String[] buyingProposalsNoPrice = {
        "Jag är intresserad av %thing%",
        "Köper %thing%",
        "Jag vill köpa %thing%"
    };
    private static String[] sellingProposals = {
        "Säljer %thing% för %price%%currency%",
        "Köper du %thing% för %price%%currency%?",
        "Vill du köpa %thing% för %price%%currency%?",
        "Du får köpa %thing% för %price%%currency%",
        "Jag säljer %thing%"
    };
    private static String[] sellingProposalsNoPrice = {
        "Är du intresserad av %thing%",
        "Säljer %thing%",
        "Jag vill sälja %thing%"
    };
    private static String[] currencies = {
        "kr"," kronor"," spänn",":-",""," kr"
    };
    private static Random random = Utils.random;
    public static String getGreeting(String thing, int price, boolean wtb) {
        String iceBreaker = pickRandom(iceBreakers);
        if(random.nextInt(100) > 40) { // Capitalize most times.
            if(iceBreaker.length() > 0)
                iceBreaker = Character.toUpperCase(iceBreaker.charAt(0))+iceBreaker.substring(1);
        }
        String proposal;
        if(price != -1) {
            proposal = wtb ? pickRandom(buyingProposals) : pickRandom(sellingProposals);
            proposal = proposal.replace("%price%",""+price);
            proposal = proposal.replace("%currency%",pickRandom(currencies));
        } else {
            proposal = wtb ? pickRandom(buyingProposalsNoPrice) : pickRandom(sellingProposalsNoPrice);
        }
        proposal = proposal.replace("%thing%",thing);
        if(random.nextInt(100) > 80) {
            proposal += " " + getSmiley();
        }
        return iceBreaker + proposal;
    }

    private static String getSmiley() {
        String ret = "";
        ret += pickRandom(new String[]{":",";"});
        ret += pickRandom(new String[]{")",")",")","D","d","P","p"});
        return ret;
    }

    private static String pickRandom(String[] s) {
        return s[random.nextInt(s.length)];
    }

    public static boolean isSimpleGreeting(String s) {
        String greeting = hasGreeting(s);
        if(greeting == null || greeting.length() < 2)
            return false;
        return s.length() < 18;
    }

    private static String[] affirmations = {
        "ja","japp","javisst","jajamensan",
        "jajjamen","jajjamensan","visst",
        "yeah","jopp","jaa","yes",
        "yep","yepp","sure","sure bacon",
        "jo","jovisst","jadå","jodå",
        "m","mm","mmm","a",
        "aa","aaa","gärna","jättegärna",
        "självklart","oja","ohja","o ja",
        "jepp","ok","okej","gott",
        "gött","najs","nice"
    };
    
    public static boolean isAffirmation(String s) {
        s = s.toLowerCase();
        int sLen = s.length();
        for(String affirmation : affirmations) {
            if(s.startsWith(affirmation) && Math.abs(affirmation.length() - sLen) < 2)
                return true;
        }
        return false;
    }

    public static String getYes() {
        return "Ja"; // TODO: Alternative answers
    }

    public static String hasGreeting(String s) {
        String filtered = s.replaceAll("[^a-zA-ZåäöÅÄÖ ]", "");
        for(String w : iceBreakersClean) {
            if(w.length() < 2)
                continue;
            Pattern p = Pattern.compile("^"+w+"[^ ]*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher m = p.matcher(s);
            if(m.find()) {
                return m.group(0);
            }
        }
        String[] words = filtered.split(" ");
        int i = 1;
        for(String word: words) {
            for(String w : iceBreakersClean) {
                if(Utils.getLevenshteinDistance(word, w) < 2) {
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
