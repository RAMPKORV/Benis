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
    public boolean contains(String user, String message){
        return hashes.contains(user.concat(message).hashCode());
    }

    @Override
    public void add(String user, String message) {
        int hash = user.concat(message).hashCode();
        hashes.add(hash);
    }
    
}
