/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class GreetingTester {
    public static void main(String[] args) {
        System.out.println("BUYING");
        for(int i = 0; i < 100; i++) {
            System.out.println(Greeting.getGreeting("snus", 50, true));
        }
        System.out.println("SELLING");
        for(int i = 0; i < 100; i++) {
            System.out.println(Greeting.getGreeting("snus", 50, false));
        }
    }
}
