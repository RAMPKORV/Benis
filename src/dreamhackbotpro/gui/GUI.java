package dreamhackbotpro.gui;

import dreamhackbotpro.ChatListener;
import dreamhackbotpro.ConversationsListener;
import dreamhackbotpro.Message;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI extends JFrame implements ChatListener, ConversationsListener, ListSelectionListener{
    
    private DefaultListModel listData;
    private JList conversationList;
    private JTextArea channel;
    private GlobalOptionsPanel options;
    private ThingTable thingTable;
    private JScrollPane textAreaScroll;
    private Map<String, JTextArea> chats = new HashMap<String, JTextArea>();
    private ChatOptionsPanel chatOptions;
    
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
        
        listData = new DefaultListModel();
        
        listData.addElement("Main channel");
        listData.addElement("Options");
        listData.addElement("Thing table");
        for (int i = 0; i < 30; i+=2) {
            //test stuff. TODO remove
            String s = "user"+i+" - user"+(i+1);
            JTextArea chat = new JTextArea("Chat: "+s);
            chats.put(s, chat);
            listData.addElement(s);
        }
        
        conversationList = new JList(listData);
        conversationList.setSelectedIndex(0);
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.addListSelectionListener(this);
        JScrollPane listScroll = new JScrollPane(conversationList);
        
        add(listScroll, BorderLayout.WEST);
        JPanel mainMenu = new JPanel(new BorderLayout());
        
        channel = new JTextArea(" - Main channel - ");
        channel.setLineWrap(true);
        channel.setEditable(false);
        
        options = new GlobalOptionsPanel();
        thingTable = new ThingTable();
        
        textAreaScroll = new JScrollPane(channel);
        mainMenu.add(textAreaScroll, BorderLayout.CENTER);
        
        chatOptions = new ChatOptionsPanel();
        mainMenu.add(chatOptions, BorderLayout.SOUTH);
        
        add(mainMenu, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private String timeStamp(){
        Calendar cal = Calendar.getInstance();
        return String.format("(%02d:%02d:%02d) ", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }
    
    private void appendToChannel(String s){
        channel.append('\n'+timeStamp()+s);
        channel.setCaretPosition(channel.getDocument().getLength());
    }

    @Override
    public void onMessage(Message m) {
        appendToChannel(m.toString());
    }

    @Override
    public void onNameChange(String oldName, String newName) {
        //TODO check if the user is in any chat, append namechange in chat
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPrivateMessage(Message m) {
        //do nothing. Handled in onConversationMessage
    }

    @Override
    public void onQuit(String user) {
    }
    
    @Override
    public void onError(String error) {
        appendToChannel(error);
    }

    @Override
    public void onConversationMessage(Message m) {
        
        //TODO implement AutoScroll check from chatOptions
        String chatName = m.getFrom()+" - "+m.getTo();
        if(m.getFrom().compareTo(m.getTo())>0){
            chatName = m.getTo()+" - "+m.getFrom();
        }

        JTextArea chat = chats.get(chatName);
        if(chat==null){
            chat = new JTextArea(chatName);
            listData.addElement(chatName);
            chats.put(chatName, chat);
        }
        chat.append('\n'+timeStamp()+m.toString());
        chat.setCaretPosition(chat.getDocument().getLength());
    }
    
    @Override
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
        if(index==2){
            thingTable.updateData();
            textAreaScroll.setViewportView(thingTable);
            return;
        }
        JTextArea chat = chats.get(conversationList.getSelectedValue().toString());
        //presume chat is not null
        textAreaScroll.setViewportView(chat);
    }

}
