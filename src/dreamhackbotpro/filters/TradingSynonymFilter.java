package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

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
    
    private String replaceBuyingSynonyms(String s){
        //TODO ignore case
        s = s.replaceAll("jag köper", "WTB");
        s = s.replaceAll("jag vill ha", "WTB");
        s = s.replaceAll("köper", "WTB");
        s = s.replaceAll("wtb", "WTB");
        //TODO "x köpes" to "WTB x"
        return s;
    }
    
    private String replaceSellingSynonyms(String s){
        //TODO ignore case
        s = s.replaceAll("jag säljer", "WTS");
        s = s.replaceAll("säljer", "WTS");
        s = s.replaceAll("wts", "WTS");
        //TODO "x säljes" to "WTS x"
        return s;
    }
    
}
