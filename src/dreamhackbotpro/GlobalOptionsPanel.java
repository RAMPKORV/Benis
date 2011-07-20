package dreamhackbotpro;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GlobalOptionsPanel extends JPanel implements ActionListener {

    private JTextField maxActiveConversations;
    private JTextField secBetweenNewConversation;
    
    private JButton update;
    private JTextField errorField;
    
    private GridBagConstraints left = new GridBagConstraints();
    private GridBagConstraints right = new GridBagConstraints();
    
    private final Options o = Options.getInstance();

    public GlobalOptionsPanel() {
        super(new GridBagLayout());
        
        left.weightx = 0.05;
        left.gridwidth = GridBagConstraints.RELATIVE;
        left.fill = 300;
        
        right.fill = GridBagConstraints.HORIZONTAL;
        right.anchor = GridBagConstraints.NORTHWEST;
        right.weightx = 1.0;
        right.gridwidth = GridBagConstraints.REMAINDER;
        right.insets = new Insets(5, 5, 5, 5);
        
        addLabel("Max active conversations");
        maxActiveConversations = new JTextField();
        addRight(maxActiveConversations);
        
        addLabel("Seconds between new conversations");
        secBetweenNewConversation = new JTextField();
        addRight(secBetweenNewConversation);

        update = new JButton("Update");
        update.addActionListener(this);
        errorField = new JTextField();
        errorField.setEditable(false);
        add(update, left);
        add(errorField, right);
        
        //ugly coding to make stuff stick to the top
        GridBagConstraints end = new GridBagConstraints();
        end.weighty = 1;
        add(new JLabel(), end);
        
        updateOptionsDisplayed();
    }
    
    private void addLabel(String text){
        add(new JLabel(text), left);
    }
    
    private void addRight(Component comp){
        add(comp, right);
    }
    
    public void updateOptionsDisplayed(){
        maxActiveConversations.setText(String.valueOf(o.getMaxActiveConversations()));
        secBetweenNewConversation.setText(String.valueOf(o.getSecondsBetweenNewConversation()));
        errorField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int maxConversations = -1;
        try {
            maxConversations = Integer.parseInt(maxActiveConversations.getText().trim());
        } catch (NumberFormatException ex) {
            errorField.setText("Max active conversations must be a number.");
            return;
        }
        
        int newConversationWait = -1;
        try {
            newConversationWait = Integer.parseInt(secBetweenNewConversation.getText().trim());
        } catch (NumberFormatException ex) {
            errorField.setText("Seconds between new conversations must be a number.");
            return;
        }
        
        o.setMaxActiveConversations(maxConversations);
        o.setSecondsBetweenNewConversation(newConversationWait);
        errorField.setText("");
    }
}
