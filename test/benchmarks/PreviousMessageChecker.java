/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package benchmarks;

/**
 *
 * @author wasd
 */
public interface PreviousMessageChecker {
    
    public void add(String user, String message);
    
    public boolean contains(String user, String message);
    
}
