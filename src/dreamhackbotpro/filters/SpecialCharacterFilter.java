package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class SpecialCharacterFilter implements MessageFilter{

    Pattern pattern = Pattern.compile("[\\&|\\!]{2,}");
    @Override
    public void filter(Message m) {
        String s = m.getMessage();
        s = s.replaceAll("[^a-zA-ZåäöÅÄÖ0-9\\:\\.\\-\\&\\!\\, ]", "");
        s = s.replaceAll("\\:(?!\\-)","");
        s = s.replaceAll("(?<!\\:)\\-(?![a-zA-Z0-9])","");
        Matcher matcher = pattern.matcher(s);
        while(matcher.find()) {
            s = matcher.replaceAll("" + matcher.group().charAt(0));
        }
        m.setMessage(s);
    }
    
}
