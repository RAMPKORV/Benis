/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class ListFilter implements MessageFilter {
    Pattern pattern = Pattern.compile("(WTB|WTS) ([a-zA-Z0-9åäöÅÄÖ ]+)(,\\s*[a-zA-Z0-9åäöÅÄÖ ]+)*");
    public void filter(Message m) {
        String msg = m.getMessage();
        msg = msg.replaceAll("(?i)och",",");
        Matcher matcher = pattern.matcher(msg);
        StringBuffer sb = new StringBuffer("");
        if(matcher.find()) {
            sb.append(matcher.group(1) + " " + matcher.group(2) + ". ");
            String[] parts = matcher.group(3).split(",");
            for(String part : parts) {
                sb.append(matcher.group(1) + " " + part + ". ");
            }
        }
        m.setMessage(sb.toString().trim());
    }

}
