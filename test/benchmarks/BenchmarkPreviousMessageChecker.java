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
            totalTime+=benchmarkV3(new UserPlusMessageHash());
        }
        System.out.println("UserPlusMessageHash V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserPlusMessageHash());
        }
        System.out.println("UserPlusMessageHash V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserPlusMessageHash());
        }
        System.out.println("UserPlusMessageHash V3 total: "+totalTime);
        
        
        
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserWithTreeSet());
        }
        System.out.println("UserWithTreeSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserWithTreeSet());
        }
        System.out.println("UserWithTreeSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserWithTreeSet());
        }
        System.out.println("UserWithTreeSet V3 total: "+totalTime);
        
        
        
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserMapWithMessageSet());
        }
        System.out.println("UserMapWithMessageSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserMapWithMessageSet());
        }
        System.out.println("UserMapWithMessageSet V3 total: "+totalTime);
        totalTime = 0f;
        for (int i = 0; i < 10; i++) {
            totalTime+=benchmarkV3(new UserMapWithMessageSet());
        }
        System.out.println("UserMapWithMessageSet V3 total: "+totalTime);
        
    }
    
    private static float benchmarkV1(PreviousMessageChecker list){
        long start = System.nanoTime();
        
        int numUsers = 200; //how many users to test
        int numSay = 20; //how many sentences they say each
        
        //add numUsers users
        for (int i = 0; i < numUsers; i++) {
            list.add("user"+i, "hello");
        }
        
        //make them say numUsers things each. 0, 2, 4, 6 ...
        for (int i = 0; i < numUsers; i++) {
            for (int j = 0; j < numSay; j++) {
                list.add("user"+i, "I said "+(j*2));
            }
        }
        
        //check what they have said. 0, 1, 2, 3 ...
        for (int i = 0; i < numUsers; i++) {
            for (int j = 0; j < numSay*2; j++) {
                list.contains("user"+i, "I said "+j);
            }
        }
        
        long end = System.nanoTime();
        end = end-start;
        float time = (end/1000000f);
//        System.out.println(item.getClass().getSimpleName()+": benchmarkV1 time: "+time+"ms");
        return time;
    }
    
    private static float benchmarkV2(PreviousMessageChecker list){
        long start = System.nanoTime();
        
        int numUsers = 200; //how many users to test
        int numSay = 20; //how many sentences they say each
        
        //some random loops
        for (int i = 0; i < numUsers; i++) {
            for (int j = 0; j < numSay; j++) {
                gotMessage("user"+i, "I said "+j, list);
            }
        }
        for (int i = 0; i < numUsers/2; i++) {
            for (int j = numSay/2; j < numSay; j++) {
                gotMessage("user"+i, "I said "+j, list);
            }
        }
        for (int i = numUsers/2; i < numUsers; i++) {
            for (int j = 0; j < numSay/2; j++) {
                gotMessage("user"+i, "I said "+j, list);
            }
        }
        for (int i = 0; i < numUsers; i++) {
            for (int j = numSay/2; j < numSay+numSay/2; j++) {
                gotMessage("user"+i, "I said "+j, list);
            }
        }
        
        long end = System.nanoTime();
        end = end-start;
        float time = (end/1000000f);
        return time;
    }
    
    /**
     * Run through ChatLog4000. Most realistic scenario.
     */
    private static float benchmarkV3(PreviousMessageChecker list){
        long start = System.nanoTime();
        
        for(Message m : log){
            gotMessage(m.getFrom(), m.getMessage(), list);
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
