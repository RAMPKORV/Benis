package benchmarks;

import java.util.TreeSet;

/**
 * One list that stores the hash of username+message
 * @author wasd
 */
public class UserPlusMessageHash implements PreviousMessageChecker{
    
    private TreeSet<Integer> hashes = new TreeSet<Integer>();
    
    public UserPlusMessageHash(){
        
    }
    
    @Override
    public boolean contains(String name, String message){
        return hashes.contains(name.concat(message).hashCode());
    }

    @Override
    public void add(String name, String message) {
        int hash = name.concat(message).hashCode();
        hashes.add(hash);
    }
    
}
