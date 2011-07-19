/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 * Remove excessive whitespace
 * @author patrik
 */
public class WhiteSpaceCleanerFilter implements MessageFilter {
    public WhiteSpaceCleanerFilter() {}
    public void filter(Message m) {
        m.setMessage(m.getMessage().trim().replaceAll("\\s+", " "));
    }
    
}
