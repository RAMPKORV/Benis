package dreamhackbotpro.gui;

import dreamhackbotpro.Interest;
import dreamhackbotpro.ThingInfo;
import java.util.SortedSet;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wasd
 */
public class ThingTable extends JTable{
    
    private final String[] headers = {"Name","Counter","Buyers-Sellers","Median","Min-Max","STD"};
    private DefaultTableModel model = new DefaultTableModel();

    public ThingTable() {
        super();
        
        model = new DefaultTableModel();
        
        setModel(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        boolean debug=true;
        if(debug){
            new Interest("snus", 50, true);
            new Interest("snus", 60, false);
            new Interest("snus", 65, true);
            new Interest("cigg", 40, true);
            new Interest("cigg", 50, false);
        }
        
    }
    
    
    public void updateData(){
        SortedSet<ThingInfo> stuff = Interest.getInterestsSorted();
        
        Object[][] data = new Object[stuff.size()][headers.length];
        
        int n=0;
        for(ThingInfo ti : stuff){
            data[n][0] = ti.getThing();
            data[n][1] = ti.getCounter();
            data[n][2] = ti.getBuyers()+"-"+ti.getSellers();
            data[n][3] = ti.getMedian();
            data[n][4] = ti.getMinPrice()+"-"+ti.getMaxPrice();
            data[n][5] = ti.getStdDev();
            n++;
        }
        
        model.setDataVector(data, headers);
        
    }
    
}
