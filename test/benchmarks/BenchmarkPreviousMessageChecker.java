package benchmarks;

/**
 * Benchmarking for which way is faster to filter messages that already has been said
 * @author wasd
 */
public class BenchmarkPreviousMessageChecker {
    
    public static void main(String[] args) {
        
        benchmarkV1(new UserPlusMessageHash());
        
        benchmarkV1(new UserMapWithMessageSet());

        benchmarkV1(new UserWithTreeList());
        
    }
    
    private static void benchmarkV1(PreviousMessageChecker item){
        long start = System.nanoTime();
        
        int numUsers = 1000; //how many users to test
        int numSay = 50; //how many sentences they say each
        
        //add numUsers users
        for (int i = 0; i < numUsers; i++) {
            item.add("user"+i, "hello");
        }
        
        //make them say 20 things each. 0, 2, 4, 6 ...
        for (int i = 0; i < numUsers; i++) {
            for (int j = 0; j < numSay; j++) {
                item.add("user"+i, "I said "+(j*2));
            }
        }
        
        //check what they have said. 0, 1, 2, 3 ...
        for (int i = 0; i < numUsers; i++) {
            for (int j = 0; j < numSay*2; j++) {
                item.contains("user"+i, "I said "+j);
            }
        }
        
        long end = System.nanoTime();
        end = end-start;
        System.out.println(item.getClass().getSimpleName()+": benchmarkV1 time: "+(end/1000000f)+"ms");
    }
    
    
}
