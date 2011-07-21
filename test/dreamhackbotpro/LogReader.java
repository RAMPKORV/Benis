/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author patrik
 */
public class LogReader implements ChatObservable {

    Set<ChatListener> listeners = new HashSet<ChatListener>();

    public LogReader(String textfile) {
        new Thread(new Runnable() {

            public void run() {
                //TODO: Read textfile line by line with delay
            }

        }).start();
    }

    public void addChatListener(ChatListener l) {
        listeners.add(l);
    }

    public void removeChatListener(ChatListener l) {
        listeners.remove(l);
    }

}
