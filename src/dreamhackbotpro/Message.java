/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class Message {
	public String from;
	public String message;
	public String to;

    public Message(String from, String message, String to) {
        this.from = from;
        this.message = message;
        this.to = to;
    }

    /**
     * Convenience constructor for quick testing
     * @param message
     */
    public Message(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getTo() {
        return to;
    }
    
}
