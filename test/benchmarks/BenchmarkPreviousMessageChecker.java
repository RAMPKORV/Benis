package benchmarks;

import dreamhackbotpro.Message;
import java.util.List;

/**
 * Benchmarking for which way is faster to filter messages that already has been said
 * @author wasd
 */
public class BenchmarkPreviousMessageChecker {
    
    private static List<Message> log;
    
    public static void main(String[] args) {
        
        float totalTime = 0f;
        
        try {
            log = ChatLog4000.getLog();
        } catch (Exception e) {
            //file probably not found
            System.out.println("Error: "+e.getMessage());
            return;
        }
        
        System.out.println(" - Please note: The benchmark that runs first is extra slow - ");
        
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserPlusMessageHash());
        }
        System.out.println("UserPlusMessageHash V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserPlusMessageHash());
        }
        System.out.println("UserPlusMessageHash V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserPlusMessageHash());
        }
        System.out.println("UserPlusMessageHash V3 total: "+totalTime);
        
        
        
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserWithTreeSet());
        }
        System.out.println("UserWithTreeSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserWithTreeSet());
        }
        System.out.println("UserWithTreeSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserWithTreeSet());
        }
        System.out.println("UserWithTreeSet V3 total: "+totalTime);
        
        
        
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserMapWithMessageSet());
        }
        System.out.println("UserMapWithMessageSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserMapWithMessageSet());
        }
        System.out.println("UserMapWithMessageSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmark(new UserMapWithMessageSet());
        }
        System.out.println("UserMapWithMessageSet V3 total: "+totalTime);
        
    }
    
    /**
     * Run through ChatLog4000. Most realistic scenario.
     */
    private static float benchmark(PreviousMessageChecker list){
        long start = System.nanoTime();
        
        for(Message m : log){
            gotMessage(m.getFrom().nick, m.getMessage(), list);
        }
        
        long end = System.nanoTime();
        end = end-start;
        float time = (end/1000000f);
        return time;
    }
    
    /**
     * This is what it will look like when we use it
     */
    private static void gotMessage(String user, String message, PreviousMessageChecker list){
        if(list.contains(user, message))
            return;
        list.add(user, message);
    }
    
    
}
