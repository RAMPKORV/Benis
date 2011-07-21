package dreamhackbotpro;

/**
 * Listens to chats through ChatObservable
 * @author patrik
 */
public interface ChatListener {

    public void onMessage(Message m);
    public void onNameChange(String oldName, String newName);
    public void onPrivateMessage(Message m);
    public void onQuit(String user);
    public void onError(String error);
}
