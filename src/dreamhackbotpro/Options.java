/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dreamhackbotpro;

/**
 *
 * @author wasd
 */
public class Options {
    
    private int maxActiveConversations=20;
    
    private static Options instance = null;
    
    private Options(){}
    
    public static Options getInstance(){
        if(instance==null)
            instance=new Options();
        return instance;
    }

    public int getMaxActiveConversations() {
        return maxActiveConversations;
    }

    public void setMaxActiveConversations(int maxActiveConversations) {
        this.maxActiveConversations = maxActiveConversations;
    }
    
}
