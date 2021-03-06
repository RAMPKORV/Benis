package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 * Remove excessive whitespace
 * @author patrik
 */
public class WhiteSpaceCleanerFilter implements MessageFilter {

    public WhiteSpaceCleanerFilter(){
    }
    
    @Override
    public void filter(Message m) {
        m.setMessage(m.getMessage().replaceAll("\\s+", " "));
        m.setMessage(m.getMessage().replaceAll("[ ]*\\.",".").trim());
    }
    
}
