package dreamhackbotpro;

import benchmarks.PreviousMessageChecker;
import benchmarks.UserWithTreeSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author patrik
 */
public class LogReader implements ChatObservable {

    private Set<ChatListener> listeners = new HashSet<ChatListener>();
    
    private PreviousMessageChecker pMC = new UserWithTreeSet();
    
    private static final long MILLIS_BETWEEN_MESSAGES = 0;

    public LogReader() {
    }

    @Override
    public void addChatListener(ChatListener l) {
        listeners.add(l);
    }

    @Override
    public void removeChatListener(ChatListener l) {
        listeners.remove(l);
    }
    
    public void read(final String textfile) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                            new FileInputStream(textfile), "ISO-8859-1")
                            );
                    String line = reader.readLine();
                    while (line != null) {
                        
                        if(line.length()<12){
                            //some lines are apparently empty
                            line = reader.readLine();
                            continue;
                        }

                        //presume that the format of the log is like log4000.txt
                        
                        line = line.substring(11); //remove timestamp

                        //FIXME: Some people announce trades with /me. In this chat log the format for /me is "***WASD wts snus" and it gets filtered when parsing here
                        //We should make sure that IrcHandler can handle that as a normal message.
                        int colonIndex = line.indexOf(':');
                        if (colonIndex == -1) {
                            //system message
                            line = reader.readLine();
                            continue;
                        }
                        int spaceIndex = line.indexOf(' ');
                        if (colonIndex > spaceIndex) {
                            //system message
                            line = reader.readLine();
                            continue;
                        }
                        String user = line.substring(0, colonIndex);
                        String msg = line.substring(colonIndex + 2);
                        
                        if(message(user, msg)){
                            //sleep if the message sent successfuly
                            Thread.sleep(MILLIS_BETWEEN_MESSAGES);
                        }
                        
                        line = reader.readLine();
                        
                    }
                    System.out.println("Chat log parsed successfully");
                } catch (InterruptedException e) {
                    error("Interrupted");
                } catch (IOException e) {
                    error("File not found");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * Notifies the listeners that a message has been sent
     * @return true if the message was unique and sent
     */
    private boolean message(String user, String msg){
        if(pMC.contains(user, msg))
            return false;
        pMC.add(user, msg);
        
        Message m = new Message(user, msg, new BotInfo("LogReader"));
        for(ChatListener l : listeners){
            l.onMessage(m);
        }
        return true;
    }

    private void error(String msg) {
        for(ChatListener l: listeners) {
            l.onError(msg);
        }
    }
}
