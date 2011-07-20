package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 *
 * @author wasd
 */
public class SpecialCharacterFilter implements MessageFilter{

    @Override
    public void filter(Message m) {
        String s = m.getMessage();
        s = s.replaceAll("[^a-zA-ZåäöÅÄÖ0-9\\.\\-\\&\\!\\, ]", "");
        m.setMessage(s);
    }
    
}
