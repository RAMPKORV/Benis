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

    Pattern pattern = null;

    public SeatReader() {
        pattern = Pattern.compile("[a-d][0-9]+\\:[0-9]+", Pattern.CASE_INSENSITIVE);
    }

    public void onConversationMessage(Message m) {
        Matcher matcher = pattern.matcher(m.getMessage());
        while(matcher.find()) {
            alarm(matcher.group());
        }
    }

    public void alarm(String seat) {
        System.out.println("RED ALERT! " + seat);
    }
}
