/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 * Filters comma separated lists
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
            String type = "";
            for(String part : parts) {
                if(part.contains("WTB") && !part.contains("WTS"))
                    type = "WTB ";
                if(part.contains("WTS") && !part.contains("WTB"))
                    type = "WTS ";
                sb.append(". " + type + part.replace(type, "").trim());
            }
        }
        if(msg.endsWith("."))
            sb.append(".");
        m.setMessage(sb.toString().trim().substring(2));
    }
}
