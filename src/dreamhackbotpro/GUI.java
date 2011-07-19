/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author patrik
 */
public class GUI extends JFrame implements IrcListener, ConversationsListener{

    public GUI() throws HeadlessException {
        super("Dreamhack Bot Pro");
        //TODO SettingsTab
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

    public void onConversationMessage(Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onError(String error) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
