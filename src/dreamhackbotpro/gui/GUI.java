package dreamhackbotpro.gui;

import dreamhackbotpro.ChatListener;
import dreamhackbotpro.ConversationsListener;
import dreamhackbotpro.Message;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
    private JTextArea errors;
    private JTextArea server;
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
        listData.addElement("Errors");
        listData.addElement("Server");
        listData.addElement(" ----- ");
        
        conversationList = new JList(listData);
        conversationList.setSelectedIndex(0);
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.addListSelectionListener(this);
        JScrollPane listScroll = new JScrollPane(conversationList);
        
        add(listScroll, BorderLayout.WEST);
        JPanel mainMenu = new JPanel(new BorderLayout());

        Font f = new Font("Monospaced", Font.PLAIN, 12);
        channel = new JTextArea(" - Main channel - ");
        channel.setFont(f);
        channel.setLineWrap(true);
        channel.setEditable(false);
        errors = new JTextArea(" - Errors - ");
        errors.setFont(f);
        errors.setLineWrap(true);
        errors.setEditable(false);
        server = new JTextArea(" - Server - ");
        server.setFont(f);
        server.setLineWrap(true);
        server.setEditable(false);
        
        options = new GlobalOptionsPanel();
        thingTable = new ThingTable();
        
        textAreaScroll = new JScrollPane(channel);
        mainMenu.add(textAreaScroll, BorderLayout.CENTER);
        
        chatOptions = new ChatOptionsPanel(this);
        mainMenu.add(chatOptions, BorderLayout.SOUTH);
        
        add(mainMenu, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private String timeStamp(){
        Calendar cal = Calendar.getInstance();
        return String.format("(%02d:%02d:%02d) ", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }
    
    private void appendTo(JTextArea area, String s){
        area.append('\n'+timeStamp()+s);
        if(chatOptions.isAutoScroll())
            area.setCaretPosition(area.getDocument().getLength());
    }

    @Override
    public void onMessage(Message m) {
        appendTo(channel, m.toString());
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
        appendTo(errors, error);
    }

    @Override
    public void onConversationMessage(Message m) {

        String chatName = m.getFrom()+" - "+m.getTo();
        if(m.getFrom().compareTo(m.getTo())>0){
            chatName = m.getTo()+" - "+m.getFrom();
        }

        JTextArea chat = chats.get(chatName);
        if(chat==null){
            chat = new JTextArea(chatName);
            chat.setLineWrap(true);
            chat.setEditable(false);
            listData.addElement(chatName);
            chats.put(chatName, chat);
        }
        else{
            listData.removeElement(chatName);
            listData.addElement(chatName);
        }
        chat.append('\n'+timeStamp()+m.toString());
        if(chatOptions.isAutoScroll())
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
        if(index==3){
            textAreaScroll.setViewportView(errors);
            return;
        }
        if(index==4){
            textAreaScroll.setViewportView(server);
            return;
        }
        String listValue = conversationList.getSelectedValue().toString();
        if(listValue.equals(" ----- "))
            return;
        JTextArea chat = chats.get(listValue);
        //presume chat is not null
        textAreaScroll.setViewportView(chat);
    }

    public void updateListMark(){
        listData.removeElement(" ----- ");
        listData.addElement(" ----- ");
    }

    public void onServerMessage(String message) {
        appendTo(server, message);
    }

}
