package dreamhackbotpro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class SeatReader implements ConversationsListener, ChatListener {

    private static Pattern pattern = Pattern.compile("(^|\\s)([a-d])(\\:)?[ ]*([1-9][0-9]?)([ ,]*(plats)?(\\:)?[ ]*)([1-9][0-9]?)", Pattern.CASE_INSENSITIVE);

    public SeatReader() {
    }

    

    public void alarm(Message m) {
        System.out.println("RED ALERT! FROM: "+m.getFrom()+". TO: "+m.getTo()+". MESSAGE: " + m.getMessage());
    }

    public static String getSeat(String message) {
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            if(matcher.group(5).length() < 1)
                continue;
            return matcher.group(2)+matcher.group(4)+':'+matcher.group(8);
        }
        return null;
    }

    public void scanSeats(Message m) {
        String seat = getSeat(m.getMessage());
        if(seat != null) {
            alarm(m);
        }
    }

    public void onConversationMessage(Message m) {
        scanSeats(m);
    }

    public void onMessage(Message m) {
        scanSeats(m);
    }

    public void onPrivateMessage(Message m) {}
    public void onError(String error) {}
    public void onServerMessage(String message) {}
    public void onNameChange(UserInfo olduser, UserInfo newuser) {}
    public void onQuit(UserInfo userInfo) {}
    public void onUserInfo(UserInfo ui) {}

    public void onConversationClose(Conversation c) {
    }
}
