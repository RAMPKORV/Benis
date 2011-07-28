/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class Blame {
    public static String negateBlames(String msg) {
        msg = msg.replaceAll("(?i)du skrev till mig","jag skrev till dig");
        msg = msg.replaceAll("(?i)du började","jag började");
        msg = msg.replaceAll("(?i)du skrev först","jag skrev först");
        msg = msg.replaceAll("(?i)du startade","jag startade");
        msg = msg.replaceAll("(?i)du kontaktade mig","jag kontaktade dig");
        msg = msg.replaceAll("(?i)varför skrev du till mig?","jag skrev till dig!");
        return msg;
    }
}
