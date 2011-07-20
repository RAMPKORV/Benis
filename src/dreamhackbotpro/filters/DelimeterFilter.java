/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replaces delimeters with periods and adds space where it is missing
 * @author patrik
 */
public class DelimeterFilter implements MessageFilter {
    Pattern pattern = Pattern.compile("[\\.]([a-zA-Z0-9])");
    public void filter(Message m) {
        m.setMessage(m.getMessage().replaceAll("[\\(\\)\\{\\}/'\\\":!?\\.]+", "."));
        Matcher matcher = pattern.matcher(m.getMessage());
        while(matcher.find()) {
            m.setMessage(matcher.replaceAll(". " + matcher.group(1)));
        }
    }

}
