/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class SeatReader implements ConversationsListener {

    private static Pattern pattern = null;

    public SeatReader() {
        pattern = Pattern.compile("([a-d][0-9]{1,2})(\\:| )(Plats)?[: ]*([0-9]{1,2})", Pattern.CASE_INSENSITIVE);
    }

    public void onConversationMessage(Message m) {
        String seat = getSeat(m.getMessage());
        if(seat != null) {
            alarm(seat);
        }
    }

    public void alarm(String seat) {
        System.out.println("RED ALERT! " + seat);
    }

    public static String getSeat(String message) {
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()) {
            return matcher.group(1)+':'+matcher.group(4);
        }
        return null;
    }
}
