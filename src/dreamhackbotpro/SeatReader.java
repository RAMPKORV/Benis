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
public class SeatReader implements ConversationsListener, ChatListener {

    private static Pattern pattern = null;

    public SeatReader() {
        pattern = Pattern.compile("(^|\\s)([a-d])[ ]*([1-9][0-9]?)([ ,]*(plats)?(\\:)?[ ]*)([1-9][0-9]?)", Pattern.CASE_INSENSITIVE);
    }

    

    public void alarm(String seat) {
        System.out.println("RED ALERT! " + seat);
    }

    public static String getSeat(String message) {
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            if(matcher.group(4).length() < 1)
                continue;
            return matcher.group(2)+matcher.group(3)+':'+matcher.group(7);
        }
        return null;
    }

    public void scanSeats(Message m) {
        String seat = getSeat(m.getMessage());
        if(seat != null) {
            alarm(m.getMessage());
        }
    }

    public void onConversationMessage(Message m) {
        scanSeats(m);
    }

    public void onMessage(Message m) {
        scanSeats(m);
    }

    public void onNameChange(String oldName, String newName) {}
    public void onPrivateMessage(Message m) {}
    public void onQuit(String user) {}
    public void onError(String error) {}
}
