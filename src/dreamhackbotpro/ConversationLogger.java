package dreamhackbotpro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConversationLogger implements ConversationsListener {

    private HashMap<String, BufferedWriter> logs = new HashMap<String, BufferedWriter>();
    private File folder;

    public ConversationLogger() {
        /*
         * - create folder if not exists
         * - one file per conversation
         * - filename: user1+"_"+user2+".txt"
         * - where user1 is before user2 in lexical order
         *
         * - add a new line in the right file onConversationMessage
         */

        folder = new File("dreamhackbot_logs");
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    public void onConversationMessage(Message m) {
        String filename = getName(m.getFrom().nick, m.getTo().nick);
        BufferedWriter log = logs.get(filename);

        try {
            if (log == null) {
                //make new
                log = new BufferedWriter(new FileWriter(folder.getAbsolutePath()
                        +System.getProperty("file.separator")+filename, true));
                logs.put(filename, log);
            }
            log.write(Utils.timeStamp() + m.getFrom().nick + ": " + m.getMessage() + '\n');
        } catch (IOException ex) {
        }

    }

    private String getName(String n1, String n2) {
        //TODO make tests
        String name = null;
        if (n1.compareTo(n2) < 0) {
            name = n1 + "_" + n2;
        } else {
            name = n2 + "_" + n1;
        }
        name = name.replaceAll("[!a-zA-Z0-9åäöÅÄÖ]", "-")+".txt";
        return name;
    }

    public void onConversationClose(Conversation c) {
        String filename = getName(c.getBuyer().getName(), c.getSeller().getName());
        BufferedWriter log = logs.get(filename);
        if(log==null){
            //should not happen
            return;
        }
        try {
            log.close();
        } catch (IOException ex) {
        }
    }
}
