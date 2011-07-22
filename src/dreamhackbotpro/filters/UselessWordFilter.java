package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Removes useless words such as 'Billigt'
 * @author wasd
 */
public class UselessWordFilter implements MessageFilter{
    
    private Pattern buying = Pattern.compile("([^a-zåäöÅÄÖ]| |^)(billigt|lol|nytt|ny|usb)([^a-zåäöÅÄÖ]| |$)", Pattern.CASE_INSENSITIVE);
    
    public UselessWordFilter(){
    }

    @Override
    public void filter(Message m) {
        Matcher ma = buying.matcher(m.getMessage());
        m.setMessage(ma.replaceAll(" "));
    }
    
}
