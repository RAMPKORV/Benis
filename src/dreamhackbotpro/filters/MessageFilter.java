package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 * This class filters a Message so that Bot can read it easier
 * @author patrik
 */
public interface MessageFilter {

    /**
     * Filters the message to make it easier for the bot to interpret
     * @param m The message to be filtered
     */
    public void filter(Message m);
}
