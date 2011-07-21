package dreamhackbotpro.gui;

import javax.swing.JTable;

/**
 *
 * @author wasd
 */
public class ThingTable extends JTable{

    public ThingTable() {
        super();
        
        String[] columnNames = {"Name","Counter","Buyers","Sellers","Median","STD"};
        
        
    }
    
    private void clearData(){
        //TODO remove everything
    }
    
    public void updateData(){
        clearData();
        //TODO update with data from Interest.getInterestsSorted()
    }
    
}
