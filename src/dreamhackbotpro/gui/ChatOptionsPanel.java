package dreamhackbotpro.gui;

import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author wasd
 */
public class ChatOptionsPanel extends JPanel {

    private JCheckBox autoscroll;

    public ChatOptionsPanel() {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));

        autoscroll = new JCheckBox("Autoscroll");
        add(autoscroll);
    }
    
    public boolean isAutoScroll() {
        return autoscroll.isSelected();
    }
}
