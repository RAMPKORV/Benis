package dreamhackbotpro.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author wasd
 */
public class ChatOptionsPanel extends JPanel implements ActionListener{

    private JCheckBox autoscroll;
    private JButton updateListMark;
    private GUI gui;

    public ChatOptionsPanel(GUI gui) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.gui=gui;

        autoscroll = new JCheckBox("Autoscroll");
        add(autoscroll);

        updateListMark = new JButton("Update List Mark");
        updateListMark.addActionListener(this);
        add(updateListMark);
    }
    
    public boolean isAutoScroll() {
        return autoscroll.isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==updateListMark){
            gui.updateListMark();
        }
    }
}
