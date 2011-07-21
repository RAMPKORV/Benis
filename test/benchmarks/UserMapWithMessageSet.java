package benchmarks;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Contains a map with userHash and a separate TreeSet for each user
 * @author wasd
 */
public class UserMapWithMessageSet implements PreviousMessageChecker{
    
    private Map<Integer, TreeSet<Integer>> hashes = new HashMap<Integer, TreeSet<Integer>>();

    @Override
    public void add(String name, String message) {
        TreeSet<Integer> t = hashes.get(name.hashCode());
        if(t==null){
            t = new TreeSet<Integer>();
            hashes.put(name.hashCode(), t);
        }
        t.add(message.hashCode());
    }

    @Override
    public boolean contains(String name, String message) {
        TreeSet<Integer> t = hashes.get(name.hashCode());
        if(t==null)
            return false;
        return t.contains(message.hashCode());
    }
    
}
