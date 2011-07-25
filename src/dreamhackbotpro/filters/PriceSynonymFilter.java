package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
class PriceSynonymFilter implements MessageFilter {

    private Pattern pattern = Pattern.compile("([1-9][0-9]*)[ ]*(lax|spänn|kr|hundring|hundralapp|hundralappar|kronor|riksdaler|sek|\\:-|tjuga|tjugor|tjugolapp|tjugolappar)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public void filter(Message m) {
        Matcher matcher = pattern.matcher(m.getMessage());
        while(matcher.find()) {
            String value = matcher.group(1);
            String unit = matcher.group(2);
            if(unit.equals("lax")) {
                m.setMessage(matcher.replaceAll(1000 * Integer.parseInt(value) + "kr"));
            } else if(unit.equals("sek") ||
                      unit.equals("kr") ||
                      unit.equals("spänn") ||
                      unit.equals("kronor") ||
                      unit.equals("riksdaler") ||
                      unit.equals(":-")) {
                m.setMessage(matcher.replaceAll(value + "kr"));
            } else if(unit.equals("tjuga") ||
                      unit.equals("tjugor") ||
                      unit.equals("tjugolapp") || 
                      unit.equals("tjugolappar")) {
                m.setMessage(matcher.replaceAll(20 * Integer.parseInt(value) + "kr"));
            } else if(unit.equals("hundring") ||
                      unit.equals("hundralapp") ||
                      unit.equals("hundralappar")) {
                m.setMessage(matcher.replaceAll(100 * Integer.parseInt(value) + "kr"));
            }
        }
    }
    
}
