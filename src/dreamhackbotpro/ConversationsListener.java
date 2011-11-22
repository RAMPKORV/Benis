package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public interface ConversationsListener {

    public void onConversationMessage(Message m);
    public void onConversationClose(Conversation c);

}
