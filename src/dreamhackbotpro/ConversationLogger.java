package dreamhackbotpro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

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

    @Override
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
            log.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    public String getName(String n1, String n2) {
        n1 = n1.replaceAll("[^a-zA-Z0-9åäöÅÄÖ]", "-");
        n2 = n2.replaceAll("[^a-zA-Z0-9åäöÅÄÖ]", "-");
        if (n1.compareTo(n2) < 0) {
            return n1 + "_" + n2 + ".txt";
        } else {
            return n2 + "_" + n1 + ".txt";
        }
    }

    @Override
    public void onConversationInactive(Conversation c) {
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
        
        //remove the BufferedWriter
        logs.remove(filename);
    }
    
    /**
     * Close all files on shutdown
     */
    public void onShutdown(){
        //TODO call this when the GUI has been closed
        for(Entry<String, BufferedWriter> e: logs.entrySet()){
            try {
                e.getValue().close();
            } catch (Exception ex) {
                
            }
        }
        System.exit(0);
    }
    
}
