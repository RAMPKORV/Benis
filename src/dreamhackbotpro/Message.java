package dreamhackbotpro;

public class Message {

    private String from;
    private String message;
    private String to;

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

    @Override
    public String toString() {
        return from+": "+message;
    }

    public void setTo(String to) {
        this.to=to;
    }
    
}
