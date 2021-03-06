/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package benchmarks;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class UserWithTreeSet implements PreviousMessageChecker {
    Map<String,SortedSet<Integer>> map = new HashMap<String,SortedSet<Integer>>();

    public void add(String name, String message) {
        SortedSet<Integer> set = map.get(name);
        if(set == null) {
            set = new TreeSet<Integer>();
            map.put(name, set);
        }
        set.add(message.hashCode());
    }

    public boolean contains(String name, String message) {
        SortedSet<Integer> set = map.get(name);
        if(set == null)
            return false;
        return set.contains(message.hashCode());
    }



}
