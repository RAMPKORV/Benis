/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author patrik
 */
public class Bot implements IrcListener{
    
    private Map<String, User> users = new HashMap<String, User>();
    private User orphanUser = null;
    private MessageFilter filter = new MessageFilter();

    public Bot(){

    }

    public void onMessage(Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onNameChange(String oldName, String newName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPrivateMessage(Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onQuit(String user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onError(String error) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
