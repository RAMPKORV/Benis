/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter consisting of several filters that are looped through
 * @author patrik
 */
public class CompositeMessageFilter implements MessageFilter {

    protected List<MessageFilter> filters = new ArrayList<MessageFilter>();

    public void addMessageFilter(MessageFilter filter) {
        filters.add(filter);
    }

    public void removeMessageFilter(MessageFilter filter) {
        filters.remove(filter);
    }

    public void filter(Message m) {
        for(MessageFilter mf : filters) {
            mf.filter(m);
        }
    }

}
