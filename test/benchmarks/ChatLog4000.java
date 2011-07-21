package benchmarks;

import dreamhackbotpro.Message;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wasd
 */
public class ChatLog4000 {

    private static List<Message> messages;

    public static void main(String[] args) {
        List<Message> log = null;
        try {
            log = getLog();
        } catch (Exception e) {
            System.out.println("Error: "+e.getLocalizedMessage());
            return;
        }
        
        for (Message m : log) {
            System.out.println(m.toString());
        }
    }

    public static List<Message> getLog() throws IOException {
        if (messages != null) {
            return messages;
        }
        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                            new FileInputStream("log4000.txt"), "ISO-8859-1")
                            );
        messages = new ArrayList<Message>(4000);
        
        String line = reader.readLine();
        while (line != null) {
            
            line = line.substring(11); //remove timestamp
            
            //FIXME: Some people announce trades with /me. In this chat log the format for /me is "***WASD wts snus" and it gets filtered when parsing here
            //We should make sure that IrcHandler can handle that as a normal message.
            int colonIndex = line.indexOf(':');
            if(colonIndex==-1){
                //system message
                line = reader.readLine();
                continue;
            }
            int spaceIndex=line.indexOf(' ');
            if(colonIndex>spaceIndex){
                //system message
                line = reader.readLine();
                continue;
            }
            String name = line.substring(0, colonIndex);
            String msg = line.substring(colonIndex+2);
            
            messages.add(new Message(name, msg));
            
            line = reader.readLine();
        }

        return messages;
    }
}
