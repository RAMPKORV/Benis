package dreamhackbotpro.gui;

import dreamhackbotpro.ChatListener;
import dreamhackbotpro.Conversation;
import dreamhackbotpro.ConversationsListener;
import dreamhackbotpro.Message;
import dreamhackbotpro.SeatMentionListener;
import dreamhackbotpro.UserInfo;
import dreamhackbotpro.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI extends JFrame implements ChatListener, ConversationsListener,
        ListSelectionListener, ListCellRenderer, SeatMentionListener {

    private static final int NUMBER_OF_FLASHES = 12;
    private static final long DELAY_BETWEEN_FLASHES = 250;
    private DefaultListModel listData;
    private JList conversationList;
    private JTextArea channel;
    private JTextArea errors;
    private JTextArea server;
    private GlobalOptionsPanel options;
    private ThingTable thingTable;
    private JScrollPane textAreaScroll;
    private Map<String, JTextArea> chats = new HashMap<String, JTextArea>();
    private volatile Map<String, Integer> unread = new HashMap<String, Integer>();
    private ChatOptionsPanel chatOptions;
    private ListCellRenderer renderer = new DefaultListCellRenderer();
    private Set<String> seatMentions = new HashSet<String>();
    private static final String LISTMARK = " -------------------- ";

    public GUI() {
        super("Dreamhack Bot Pro");

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
        listData.addElement(LISTMARK);

        conversationList = new JList(listData);
        conversationList.setCellRenderer(this);
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

    private void appendTo(String name, JTextArea area, String s) {
        area.append('\n' + Utils.timeStamp() + s);
        addUnread(name);
        if (chatOptions.isAutoScroll()) {
            area.setCaretPosition(area.getDocument().getLength());
        }
    }

    @Override
    public void onMessage(Message m) {
        appendTo("Main channel", channel, m.toString());
    }

    @Override
    public void onNameChange(UserInfo olduser, UserInfo newuser) {
        //TODO check if the user is in any chat, append namechange in chat
    }

    @Override
    public void onPrivateMessage(Message m) {
        //do nothing. Handled in onConversationMessage
    }

    @Override
    public void onQuit(UserInfo userInfo) {
    }

    @Override
    public void onError(String error) {
        appendTo("Errors", errors, error);
    }

    @Override
    public void onConversationMessage(Message m) {

        String chatName = getChatName(m.getFrom().nick, m.getTo().nick);

        JTextArea chat = chats.get(chatName);

        if (chat == null) {
            chat = new JTextArea(chatName);
            chat.setLineWrap(true);
            chat.setEditable(false);
            chats.put(chatName, chat);
        } else {
            if(Utils.countChar(chat.getText(), '\n')>1){
                listData.removeElement(chatName);
                listData.addElement(chatName);
            }
        }
        appendTo(chatName, chat, m.toString());
    }

    /**
     * 
     * @return the name of the chat-tab that belongs to Message m
     */
    private String getChatName(String u1, String u2) {
        if (u1.compareTo(u2) > 0) {
            return u1 + " - " + u2;
        } else {
            return u2 + " - " + u1;
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        boolean enableLiveUpdate = true;
        if (e.getValueIsAdjusting() != enableLiveUpdate) {
            return;
        }
        int index = conversationList.getSelectedIndex();
        
        try {
            removeUnread(conversationList.getSelectedValue().toString());
        } catch (Exception ex) {
        }

        switch (index) {
            case 0:
                textAreaScroll.setViewportView(channel);
                return;

            case 1:
                options.updateOptionsDisplayed();
                textAreaScroll.setViewportView(options);
                return;

            case 2:
                thingTable.updateData();
                textAreaScroll.setViewportView(thingTable);
                return;

            case 3:
                textAreaScroll.setViewportView(errors);
                return;

            case 4:
                textAreaScroll.setViewportView(server);
                return;
        }

        String listValue = conversationList.getSelectedValue().toString();
        if (listValue.equals(LISTMARK)) {
            return;
        }
        JTextArea chat = chats.get(listValue);
        //presume chat is not null
        textAreaScroll.setViewportView(chat);
    }

    public void updateListMark() {
        listData.removeElement(LISTMARK);
        listData.addElement(LISTMARK);
    }

    @Override
    public void onServerMessage(String message) {
        appendTo("Server", server, message);
    }
    private static Color myRed = new Color(225, 125, 125);

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String chatName = value.toString();
        if (!isSelected && !cellHasFocus) {            
            if (seatMentions.contains(chatName)) {
                c.setBackground(Color.CYAN);                
            } else {
                Integer flashesLeft = unread.get(chatName);
                if (flashesLeft != null) {
                    c.setBackground(flashesLeft % 2 == 0 ? Color.PINK : myRed);
                }
            }
        } else {
            seatMentions.remove(chatName);
        }
        return c;
    }

    private void addUnread(String chatName) {
        if(chatName==null)
            return;
        if (conversationList.getSelectedValue() != null) {
            if (!conversationList.getSelectedValue().equals(chatName)) {
                unread.put(chatName, NUMBER_OF_FLASHES);
            }
            repaintThread();
        }
    }

    private void removeUnread(String chatName) {
        unread.remove(chatName);
    }
    private Thread flashThread = null;
    private Runnable repainter = new Runnable() {

        public void run() {
            int counter = 0;
            do {
                conversationList.repaint();
                Set<String> keys = unread.keySet();
                for (String s : keys) {
                    int flashesLeft = unread.get(s);
                    if (flashesLeft > 0) {
                        counter++;
                        flashesLeft--;
                    }
                    unread.put(s, flashesLeft);
                }
                try {
                    Thread.sleep(DELAY_BETWEEN_FLASHES);
                } catch (InterruptedException ex) {
                    return;
                }
            } while (counter > 0);
        }
    };

    private void repaintThread() {
        if (flashThread == null || !flashThread.isAlive()) {
            flashThread = new Thread(repainter);
            flashThread.start();
        }
    }

    @Override
    public void onUserInfo(UserInfo ui) {
        // Ignore for now
    }

    @Override
    public void onConversationInactive(Conversation c) {
        //TODO change tab color to grey
        String chatName = getChatName(c.getBuyer().getName(), c.getSeller().getName());
        JTextArea chat = chats.get(chatName);
        if (chat == null) {
            //probably shouldnt happen
            return;
        }
        
        //only append CLOSED to tabs that are beyond "hello" stage
        if(Utils.countChar(chat.getText(), '\n')>1){
            //null so that the tab wont flash
            appendTo(null, chat, "CLOSED");
        }
    }

    @Override
    public void onSeatMention(Message m) {
        String chatName = getChatName(m.getFrom().nick, m.getTo().nick);
        JTextArea chat = chats.get(chatName);
        if (chat == null) {
            //will happen if someone manually contacts the bot with a seat
            System.out.println("Stupid seat mention: " + m);
            return;
        }
        seatMentions.add(chatName);
    }

    @Override
    public void onBotNickChange(String newName) {
        //TODO append newName somewhere
    }
}
