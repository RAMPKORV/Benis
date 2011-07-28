package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This filters merges sentences that are related to each other.
 * "WTB snus. 50kr" is two sentences where the dot should be removed
 *
 * Should be run after TradingSynonymFilter and PriceSynonymFilter
 * @author wasd
 */
public class RelatedSentenceFilter implements MessageFilter{

    @Override
    public void filter(Message m) {
        String[] sentences = m.getMessage().split("\\.(?![0-9])");
        if(sentences.length==1)
            return;

        boolean change = false;

        for(int i=0;i<sentences.length-1;i++){
            String sentence = sentences[i];
            String nextSentence = sentences[i+1];
            if(isTradeNotPrice(sentence) && isPriceNotTrade(nextSentence)){
                if(!nextSentence.startsWith(" ")){
                    nextSentence+=" ";
                }
                sentences[i]+= nextSentence;
                sentences[i+1] = "";
                i++;
                change=true;
            }
        }

        if(!change)
            return;

        //rebuild the message with the modified sentences
        String newMessage = "";
        for(String s : sentences){
            if(s.length()==0)
                continue;
            newMessage+=s+". ";
        }
        newMessage = newMessage.substring(0, newMessage.length()-2); //remove dot+space at end

        m.setMessage(newMessage);
    }

    private boolean isTradeNotPrice(String sentence){
        return hasTrade(sentence) && !hasPrice(sentence);
    }

    private boolean isPriceNotTrade(String sentence){
        return hasPrice(sentence) && !hasTrade(sentence);
    }

    private boolean hasTrade(String sentence){
        return sentence.contains("WTB") || sentence.contains("WTS");
    }

    private Pattern price = Pattern.compile("([1-9][0-9]*)kr", Pattern.CASE_INSENSITIVE);

    private boolean hasPrice(String sentence){
        Matcher matcher = price.matcher(sentence);
        return matcher.find();
    }

}
