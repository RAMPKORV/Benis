/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
interface ChatObservable {
    public void addChatListener(ChatListener l);
    public void removeChatListener(ChatListener l);
}
