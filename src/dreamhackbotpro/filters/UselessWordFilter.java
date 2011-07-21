package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 * Removes useless words such as 'Billigt'
 * @author wasd
 */
public class UselessWordFilter implements MessageFilter{
    
    public UselessWordFilter(){
        
    }

    @Override
    public void filter(Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
