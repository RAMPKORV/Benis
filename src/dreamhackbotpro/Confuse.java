/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class Confuse {
    public static String negateBlames(String msg) {
        msg = msg.replaceAll("(?i)du skrev (till|te|til) (mig|mej)","jag skrev till dig");
        msg = msg.replaceAll("(?i)du började","jag började");
        msg = msg.replaceAll("(?i)du skrev[a-zåäöÅÄÖ ]*först","jag skrev först");
        msg = msg.replaceAll("(?i)du startade","jag startade");
        msg = msg.replaceAll("(?i)du kontaktade (mig|mej)","jag kontaktade dig");
        msg = msg.replaceAll("(?i)(varför|vrf) (skrev/skriver) du (till|te|til) mig","jag skrev till dig!");
        msg = msg.replaceAll("(?i)jag skrev inte","jag skrev");
        msg = msg.replaceAll("(?i)jag inte","jag");
        msg = msg.replaceAll("(?i)inte jag","jag");
        return msg;
    }

    public static String negateDoNots(String msg) {
        msg = msg.replaceAll("(?i)är inte intresserad","är intresserad");
        msg = msg.replaceAll("(?i)vill inte ha","vill ha");
        msg = msg.replaceAll("(?i)vill inte köpa","köper");
        msg = msg.replaceAll("(?i)köper inte","köper");
        msg = msg.replaceAll("(?i)säljer inte","säljer");
        msg = msg.replaceAll("(?i)nej tack","ja tack");
        msg = msg.replaceAll("(?i)^glöm( det)?(\\!|\\.)*$",getSoundsGood());
        return msg;
    }

    private static String[] soundGoods = {
        "Låter bra", "Ok", ":)", ":-)", "Najs",
        "Underbart", "Toppen", "Lysande"
    };
    private static String getSoundsGood() {
        return soundGoods[Utils.random.nextInt(soundGoods.length)];
    }

    public static String negateConfusion(String msg) {
        msg = msg.replaceAll("(?i)^(ha)*(h | )?(va|what|vafan|wtf|va i helvete|lol)[\\!\\?]*$", getSoundsGood());
        msg = msg.replaceAll("(?i)^\\?+$", getSoundsGood());
        return msg;
    }

}

