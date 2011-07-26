package dreamhackbotpro;

public class Message {

    private String from;
    private String message;
    private String to;
    private BotInfo bot;

    public Message(String from, String message, String to, BotInfo bot) {
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
    public Message(String from, String message, BotInfo bot) {
        this(from, message, null, bot);
    }

    /**
     * Convenience constructor for quick testing
     * @param message
     */
    public Message(String message, BotInfo bot) {
        this(null, message, null, bot);
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

    public BotInfo getBotInfo() {
        return bot;
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
