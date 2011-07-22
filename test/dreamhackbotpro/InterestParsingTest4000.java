package dreamhackbotpro;

/**
 * Checks which sentences that the bot failed to parse
 * Uncomment the stuff after line 40 in Bot.java when running
 *
 * @author wasd
 */
public class InterestParsingTest4000 {

    public static void main(String[] args){
        LogReader logReader = new LogReader();
        Bot bot = new Bot();
        logReader.addChatListener(bot);
        logReader.read("log4000.txt");
    }

}
