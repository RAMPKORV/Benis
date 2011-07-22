package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Removes useless words such as 'Billigt'
 * @author wasd
 */
public class UselessWordFilter implements MessageFilter{
    
<<<<<<< HEAD
    private Pattern buying = Pattern.compile("([^a-zåäöÅÄÖ]| |^)(billigt|sprillans|billigt|liknande|också|lol|nytt|ny|usb)([^a-zåäöÅÄÖ]| |$)", Pattern.CASE_INSENSITIVE);
=======
    private Pattern buying = Pattern.compile("([^a-zåäöÅÄÖ]| |^)(billigt|billigt|liknande|också|lol|nytt|ny|usb)([^a-zåäöÅÄÖ]| |$)", Pattern.CASE_INSENSITIVE);
>>>>>>> 9cb7a05c1c8b605149824869049c7575bca52360
    
    public UselessWordFilter(){
    }

    @Override
    public void filter(Message m) {
        Matcher ma = buying.matcher(m.getMessage());
        m.setMessage(ma.replaceAll(" "));
    }
    
}
