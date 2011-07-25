package dreamhackbotpro;

/**
 * Generates chat messages and forwards them to its listeners
 * @author patrik
 */
public interface ChatObservable {
    public void addChatListener(ChatListener l);
    public void removeChatListener(ChatListener l);
}
