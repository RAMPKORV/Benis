package dreamhackbotpro;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GlobalOptionsPanel extends JPanel implements ActionListener {

    private JTextField maxActiveConversations;
    
    private JButton update;
    private JTextField errorField;
    
    private final Options o = Options.getInstance();

    public GlobalOptionsPanel() {
        super(new GridLayout(2, 2, 20, 50));
        
        add(new JLabel("Max active conversations"));

        maxActiveConversations = new JTextField();
        add(maxActiveConversations);

        update = new JButton("Update");
        update.addActionListener(this);
        errorField = new JTextField();
        errorField.setEditable(false);
        add(update);
        add(errorField);
        
        updateOptionsDisplayed();
    }
    
    public void updateOptionsDisplayed(){
        maxActiveConversations.setText(String.valueOf(o.getMaxActiveConversations()));
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
        o.setMaxActiveConversations(maxConversations);
    }
}
