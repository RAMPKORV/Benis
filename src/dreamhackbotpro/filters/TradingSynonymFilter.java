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

    private Pattern buying = Pattern.compile("(någon som säljer|någon som har|jag köper|jag vill köpa|vill köpa|jag vill ha|köper|^köpes|buying|wtb)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private Pattern buyingBefore = Pattern.compile("(^|\\. )([^\\.]+) köpes", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private String replaceBuyingSynonyms(String s){
        Matcher m = buying.matcher(s);
        s = m.replaceAll("WTB");
        m = buyingBefore.matcher(s);
        while(m.find()) {
            s = s.replaceAll(m.group(0), m.group(1) + "WTB " + m.group(2));
        }
        return s;
    }
    
    private Pattern selling = Pattern.compile("(någon som köper|jag säljer|jag vill sälja|säljer|^säljes|selling|wts)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private Pattern sellingBefore = Pattern.compile("(^|\\. )([^\\.]+) (säljes|till salu)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private String replaceSellingSynonyms(String s){
        Matcher m = selling.matcher(s);
        s = m.replaceAll("WTS");
        m = sellingBefore.matcher(s);
        while(m.find()) {
            s = s.replaceAll(m.group(0), m.group(1) + "WTS " + m.group(2));
        }
        return s;
    }
    
}
