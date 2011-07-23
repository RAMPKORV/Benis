package dreamhackbotpro.gui;

import dreamhackbotpro.Interest;
import dreamhackbotpro.ThingInfo;
import java.awt.Graphics;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wasd
 */
public class ThingTable extends JTable{
    
    private final String[] headers = {"Name","Counter","Buyers","Sellers","Median","Range","STD"};
    private DefaultTableModel model = new DefaultTableModel();

    public ThingTable() {
        super();
        
        model = new DefaultTableModel();
        
        setModel(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void updateData(){
        List<ThingInfo> stuff = Interest.getInterestsSorted();
        
        Object[][] data = new Object[stuff.size()][headers.length];
        
        int n=0;
        boolean error = false;
        do {
            try {
                for(ThingInfo ti : stuff){
                    data[n][0] = ti.getThing();
                    data[n][1] = ti.getCounter();
                    data[n][2] = ti.getBuyers();
                    data[n][3] = ti.getSellers();
                    data[n][4] = ti.getMedian();
                    data[n][5] = String.format("%d-%d", ti.getMinPrice(), ti.getMaxPrice());
                    data[n][6] = ti.getStdDev();
                    n++;
                }
                error = false;
            } catch(ConcurrentModificationException ex) {
                data = new Object[stuff.size()][headers.length];
                n = 0;
                error = true;
            }
        } while(error);
        
        model.setDataVector(data, headers);
        
        //TODO make the first colum more wide than the other
        
    }
    
}
