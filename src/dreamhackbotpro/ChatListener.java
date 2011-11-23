package dreamhackbotpro;

/**
 * Listens to chats through ChatObservable
 * @author patrik
 */
public interface ChatListener {

    public void onMessage(Message m);
    public void onNameChange(UserInfo olduser, UserInfo newuser);
    public void onPrivateMessage(Message m);
    public void onQuit(UserInfo userInfo);
    public void onError(String error);
    public void onServerMessage(String message);
    public void onUserInfo(UserInfo ui);
    public void onBotNickChange();
}
