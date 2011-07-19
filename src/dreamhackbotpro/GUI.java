package dreamhackbotpro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI extends JFrame implements IrcListener, ConversationsListener, ListSelectionListener{
    
    private DefaultListModel conversationData;
    private JList conversationList;
    private JTextArea channel;
    private GlobalOptionsPanel options;
    private JScrollPane textAreaScroll;
    private Map<String, JTextArea> chats = new HashMap<String, JTextArea>();
    
    public static void main(String[] args) {
        //testing
        GUI gui = new GUI();
    }

    public GUI() {
        super("Dreamhack Bot Pro");
        
        /* TODO:
         * List of users in channel
         * 
         */
        
        setBounds(300, 200, 800, 600);
        setMinimumSize(new Dimension(600, 400));
        //TODO discuss if we want a windowListener that does more stuff on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        
        conversationData = new DefaultListModel();
        
        conversationData.addElement("#channelname");
        conversationData.addElement("Options");
        for (int i = 0; i < 30; i+=2) {
            //test stuff. TODO remove
            String s = "user"+i+" - user"+(i+1);
            JTextArea chat = new JTextArea("Chat: "+s);
            chats.put(s, chat);
            conversationData.addElement(s);
        }
        
        conversationList = new JList(conversationData);
        conversationList.setSelectedIndex(0);
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.addListSelectionListener(this);
        JScrollPane listScroll = new JScrollPane(conversationList);
        
        add(listScroll, BorderLayout.WEST);
        JPanel mainMenu = new JPanel(new BorderLayout());
        
        channel = new JTextArea("Text from #channelname");
        channel.setLineWrap(true);
        channel.setEditable(false);
        
        options = new GlobalOptionsPanel();
        
        textAreaScroll = new JScrollPane(channel);
        mainMenu.add(textAreaScroll, BorderLayout.CENTER);
        
        ChatOptionsPanel optionGui = new ChatOptionsPanel();
        mainMenu.add(optionGui, BorderLayout.SOUTH);
        
        add(mainMenu, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private String timeStamp(){
        Calendar cal = Calendar.getInstance();
        return String.format("(%02d:%02d:%02d) ", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }
    
    private void appendToChannel(String s){
        channel.append('\n'+timeStamp()+s);
    }

    public void onMessage(Message m) {
        appendToChannel(m.toString());
    }

    public void onNameChange(String oldName, String newName) {
        //TODO output "oldName changed name to newName" in channel tab
        //TODO check if the user is in any chat, append namechange in chat
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPrivateMessage(Message m) {
        //do nothing. Handled in onConversationMessage
    }

    public void onQuit(String user) {
        appendToChannel("User left: "+user);
    }

    public void onConversationMessage(Message m) {
        
        JTextArea chat = chats.get(m.getFrom()+" - "+m.getTo());
        if(chat==null){
            chat = new JTextArea(m.getFrom()+" - "+m.getTo());
        }
        chat.append('\n'+timeStamp()+m.toString());
    }
    
    public void valueChanged(ListSelectionEvent e) {
        boolean enableLiveUpdate = true;
        if(e.getValueIsAdjusting() != enableLiveUpdate)
            return;
        int index = conversationList.getSelectedIndex();
        if(index==0){
            textAreaScroll.setViewportView(channel);
            return;
        }
        if(index==1){
            options.updateOptionsDisplayed();
            textAreaScroll.setViewportView(options);
            return;
        }
        JTextArea chat = chats.get(conversationList.getSelectedValue().toString());
        //presume chat is not null
        textAreaScroll.setViewportView(chat);
    }
    
    /**
     * Minor options.
     */
    private class ChatOptionsPanel extends JPanel{
        
        private JCheckBox autoscroll;

        public ChatOptionsPanel() {
            super(new FlowLayout(FlowLayout.LEFT, 5, 5));
            
            autoscroll = new JCheckBox("Autoscroll");
            add(autoscroll);            
        }
        
        public boolean isAutoScroll(){
            return autoscroll.isSelected();
        }
        
    }

}