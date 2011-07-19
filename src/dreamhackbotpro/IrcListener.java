package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public interface IrcListener {

    public void onMessage(Message m);
    public void onNameChange(String oldName, String newName);
    public void onPrivateMessage(Message m);
    public void onQuit(String user);
    public void onError(String error);
}
