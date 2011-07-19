package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wasd
 */
public class TradingSynonymFilter implements MessageFilter{

    @Override
    public void filter(Message m) {
        String s = m.getMessage();
        
        s = replaceBuyingSynonyms(s);
        s = replaceSellingSynonyms(s);
        
        m.setMessage(s);
    }

    private Pattern buying = Pattern.compile("(jag köper|jag vill ha|köper|wtb)", Pattern.CASE_INSENSITIVE);
    private Pattern buyingBefore = Pattern.compile("(^|\\. )([^\\. ]*) köpes", Pattern.CASE_INSENSITIVE);
    private String replaceBuyingSynonyms(String s){
        Matcher m = buying.matcher(s);
        s = m.replaceAll("WTB");
        String thing = null;
        m = buyingBefore.matcher(s);
        while(m.find()) {
            s = s.replaceAll(m.group(0), m.group(1) + "WTB " + m.group(2));
        }
        return s;
    }
    
    private Pattern selling = Pattern.compile("(jag säljer|jag vill sälja|säljer|wts)", Pattern.CASE_INSENSITIVE);
    private Pattern sellingBefore = Pattern.compile("(^|\\.)([^\\. ]*) köpes", Pattern.CASE_INSENSITIVE);
    private String replaceSellingSynonyms(String s){
        Matcher m = selling.matcher(s);
        s = m.replaceAll("WTS");
        String thing = null;
        m = sellingBefore.matcher(s);
        while(m.find()) {
            s = s.replaceAll(m.group(0), m.group(1) + "WTS " + m.group(2));
        }
        return s;
    }
    
}
