package dreamhackbotpro;

public class Message {

    private String from;
    private String message;
    private String to;
    private String botNick;

    public Message(String from, String message, String to, String botNick) {
        this.from = from;
        this.message = message;
        this.to = to;
        this.botNick = botNick;
    }
    
    /**
     * Used when the receiver is unknown or the message is in the global chat
     * @param from
     * @param message
     */
    public Message(String from, String message, String botNick) {
        this(from, message, null, botNick);
    }

    /**
     * Convenience constructor for quick testing
     * @param message
     */
    public Message(String message, String botNick) {
        this(null, message, null, botNick);
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

    public String getBotNick() {
        return botNick;
    }

    @Override
    public String toString() {
        return from+": "+message;
    }

    public void setTo(String to) {
        this.to=to;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
