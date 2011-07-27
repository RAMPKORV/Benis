/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class TimestampedMessage {
    private final Message message;
    private final long time;

    public TimestampedMessage(Message m) {
        message = m;
        time = System.currentTimeMillis();
    }

    public Message getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

}
