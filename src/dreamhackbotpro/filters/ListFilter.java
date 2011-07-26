/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class ListFilter implements MessageFilter {
    public void filter(Message m) {
        String msg = m.getMessage();
        msg = msg.replace("och", ",");
        String[] sentences = msg.split("\\.");
        StringBuilder sb = new StringBuilder("");
        for(String sentence : sentences) {
            String[] parts = sentence.split(",");
            String type = null;
            for(String part : parts) {
                if(part.contains("WTB") && !part.contains("WTS"))
                    type = "WTB";
                if(part.contains("WTS") && !part.contains("WTB"))
                    type = "WTS";
                sb.append(type + " " + part.replace(type, " ").trim() + ". ");
            }
        }
        m.setMessage(sb.toString().trim());
    }

    public static void main(String[] args) {
        String msg = "WTB A, WTS B, C";
        Message m = new Message(msg, "Monsquaz");
        new ListFilter().filter(m);
        System.out.println(m.getMessage());
    }

}
