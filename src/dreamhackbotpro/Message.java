package dreamhackbotpro;

public class Message {

    private UserInfo from;
    private String message;
    private UserInfo to;
    private final UserInfo bot;

    public Message(final UserInfo from, final String message, final UserInfo to, final UserInfo bot) {
        this.from = from;
        this.message = message;
        this.to = to;
        this.bot = bot;
    }
    
    /**
     * Used when the receiver is unknown or the message is in the global chat
     * @param from
     * @param message
     */
    public Message(final UserInfo from, final String message, final UserInfo bot) {
        this(from, message, null, bot);
    }

    /**
     * Convenience constructor for quick testing
     * @param message
     */
    public Message(String message, final UserInfo bot) {
        this(null, message, null, bot);
    }

    public UserInfo getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public UserInfo getTo() {
        return to;
    }

    public UserInfo getBotInfo() {
        return bot;
    }

    @Override
    public String toString() {
        return from.nick+": "+message;
    }

    public void setTo(UserInfo to) {
        this.to=to;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
