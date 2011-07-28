package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class PriceSynonymFilter implements MessageFilter {

    private Pattern pattern = Pattern.compile("([1-9][0-9]*)[ ]*(lax|spänn|kr|hundring|hundralapp|hundralappar|kronor|riksdaler|sek|\\:-|tjuga|tjugor|tjugolapp|tjugolappar)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public void filter(Message m) {
        Matcher matcher = pattern.matcher(m.getMessage());
        String msg = m.getMessage();
        while(matcher.find()) {
            String value = matcher.group(1);
            String unit = matcher.group(2);
            try {
                if(unit.equals("lax")) {
                    msg = msg.replace(matcher.group(0), 1000 * Integer.parseInt(value) + "kr");
                } else if(unit.equals("sek") ||
                    unit.equals("kr") ||
                    unit.equals("spänn") ||
                    unit.equals("kronor") ||
                    unit.equals("riksdaler") ||
                    unit.equals(":-")) {
                    msg = msg.replace(matcher.group(0), value + "kr");
                } else if(unit.equals("tjuga") ||
                    unit.equals("tjugor") ||
                    unit.equals("tjugolapp") || 
                    unit.equals("tjugolappar")) {
                    msg = msg.replace(matcher.group(0), 20 * Integer.parseInt(value) + "kr");
                } else if(unit.equals("hundring") ||
                    unit.equals("hundralapp") ||
                    unit.equals("hundralappar")) {
                    msg = msg.replace(matcher.group(0), 100 * Integer.parseInt(value) + "kr");
                }
            } catch(NumberFormatException ex) {
                // Could not be converted, so we remove it instead.
                msg = msg.replace(matcher.group(0), "");
            }
        }
        m.setMessage(msg);
    }
    
}
