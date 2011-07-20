package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class SpecialCharacterFilter implements MessageFilter{
    
    private Pattern pattern = Pattern.compile("(!a-zåäö\\.\\d,-&\\!)", Pattern.CASE_INSENSITIVE);

    @Override
    public void filter(Message m) {
        String s = m.getMessage();
//        Matcher matcher = pattern.matcher(m.getMessage());
        //FIXME
        s = s.replaceAll("(!a-zA-ZåäöÅÄÖ\\.\\d,-&\\!)", " ");
        
        m.setMessage(s);
    }
    
}
