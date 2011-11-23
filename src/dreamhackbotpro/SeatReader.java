package dreamhackbotpro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author patrik
 */
public class SeatReader implements ConversationsListener, ChatListener {

    private static Pattern pattern = Pattern.compile("(^|\\s)([a-d])(\\:)?[ ]*([1-9][0-9]?)([ ,]*(plats)?(\\:)?[ ]*)([1-9][0-9]?)", Pattern.CASE_INSENSITIVE);
    private boolean hasGui;
    private JFrame window;
    
    private List<SeatMentionListener> listeners = new ArrayList<SeatMentionListener>();

    public SeatReader(){
        this(true);
    }
    
    public SeatReader(boolean hasGui) {
        this.hasGui=hasGui;
    }
    
    public void alarm(Message m) {
        System.out.println("RED ALERT! FROM: "+m.getFrom()+". TO: "+m.getTo()+". MESSAGE: " + m.getMessage());
        for(SeatMentionListener sml : listeners){
            sml.onSeatMention(m);
        }
        if(hasGui && Options.getInstance().isSeatPopupEnabled()){
            popupWindow();
        }
    }
    
    private void popupWindow(){
        if(window==null){
            window = new JFrame();
            window.setUndecorated(true);
            window.setAlwaysOnTop(true);
            window.setBounds(100, 100, 400, 100);
            JPanel jp = new JPanel() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.red);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.black);
                    g.setFont(new Font(null, Font.BOLD, 42));
                    g.drawString("SEAT MENTION", 5, getHeight()/2);
                }
            };
            jp.setBounds(0, 0, 400, 100);
            window.add(jp);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        window.setVisible(true);
                        Thread.sleep(500);
                        window.setVisible(false);
                        Thread.sleep(300);
                    }
                } catch (InterruptedException ex) {
                    window.setVisible(false);
                }
            }
        }).start();
    }

    public static String getSeat(String message) {
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            if(matcher.group(5).length() < 1)
                continue;
            return matcher.group(2)+matcher.group(4)+':'+matcher.group(8);
        }
        return null;
    }

    public void scanSeats(Message m) {
        String seat = getSeat(m.getMessage());
        if(seat != null) {
            alarm(m);
        }
    }

    public void onConversationMessage(Message m) {
        scanSeats(m);
    }

    public void onMessage(Message m) {
        scanSeats(m);
    }

    public void onPrivateMessage(Message m) {}
    public void onError(String error) {}
    public void onServerMessage(String message) {}
    public void onNameChange(UserInfo olduser, UserInfo newuser) {}
    public void onQuit(UserInfo userInfo) {}
    public void onUserInfo(UserInfo ui) {}
    public void onConversationClose(Conversation c) {}
    
    public void addSeatMentionListener(SeatMentionListener sml){
        listeners.add(sml);
    }
    
}
