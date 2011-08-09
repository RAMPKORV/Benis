package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Removes useless words such as 'Billigt'
 * @author wasd
 */
public class UselessWordFilter implements MessageFilter{
    
    private String[] colors = {
        "röd","röda","rött","orange","oranga","gul","gula","gult",
        "grön","gröna","grönt","blå","blåa","blått","rosa","grå","gråa","grått",
        "vit","vita","vitt","svart","svarta",
        "ljusröd","ljusröda","ljusrött","ljusorange","ljusoranga","ljusgul","ljusgula","ljusgult",
        "ljusgrön","ljusgröna","ljusgrönt","ljusblå","ljusblåa","ljusblått","ljusrosa", 
        "lustgrå","lustgråa","ljusgrått",
        "mörkröd","mörkröda","mörkrött","mörkorange","mörkoranga","mörkgul","mörkgula","mörkgult",
        "mörkgrön","mörkgröna","mörkgrönt","mörkblå","mörkblåa","mörkblått","mörkrosa",
        "mörkgrå","mörkgråa","mörkgrått"};

    private String[] adjectivesOther = {
        "stor","stora","större","störst",
        "liten","mindre","minst", "små",
        "söt","snygg","ursöt","ursnygg",
        "vacker","sexig","rolig","roligt",
        "vanligt","vanlig","vattenfast"
    };

    private String[] priceStuff = {
        "billig", "billigt","billiga","billigare","billigast","bara",
        "enbart", "för","uppskattat","uppskattningsvis"
    };

    private String[] quality = {
        "sprillans","liknande","fräsh","fräs",
        "fräsha","fräsch","fräscha",
        "fräsht","fräscht","ny","nya","nytt", "öppnad",
        "fraesha","fraesh","fraesht","oöppnad","oöppnat",
        "oöpnnat","oöpnnad","saknas","medföljer","inklusive","exklusive"
    };

    private String[] other = {
        "fråga","ifall","också","lol","usb","gärna","intresserad","mycket","nästan","pma","pm",
                "uppskattas", "även", "levererar","sätter", "installerat","något","följer",
                "annan","orginal"
    };

    private static Pattern useless = null;

    public UselessWordFilter() {
        if(useless == null) {
            StringBuilder sb = new StringBuilder("");
            for(String s : colors)
                sb.append("|"+s);
            for(String s : adjectivesOther)
                sb.append("|"+s);
            for(String s : priceStuff)
                sb.append("|"+s);
            for(String s : quality)
                sb.append("|"+s);
            for(String s : other)
                sb.append("|"+s);
            String list = sb.toString().substring(1);
            useless = Pattern.compile("([^a-zåäöÅÄÖ]| |^)("+list+")([^a-zåäöÅÄÖ]| |$)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }
    }

    @Override
    public void filter(Message m) {
        String msg = m.getMessage();
        Matcher ma = useless.matcher(msg);
        while(ma.find()) {
            msg = msg.replace(ma.group(2), "");
        }
        m.setMessage(msg);
    }
    
}
