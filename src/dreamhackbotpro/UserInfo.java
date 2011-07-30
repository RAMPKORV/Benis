/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

/**
 * Stuff that should be replaced in conversations
 * @author patrik
 */
public class UserInfo implements Comparable<UserInfo> {
    public final String nick;
    public final String ident;
    public final String host;
    public final String ip;



    public UserInfo(String nick, String ident, String host, String ip) {
        this.nick = nick;
        this.ident = ident;
        this.host = host;
        this.ip = ip;
    }

    public UserInfo(String nick) {
        this(nick, "", "", "");
    }

    public int compareTo(UserInfo t) {
        int cmp = 0;
        cmp = nick.compareTo(t.nick);
        if(cmp != 0)
            return cmp;
        cmp = ident.compareTo(t.ident);
        if(cmp != 0)
            return cmp;
        cmp = host.compareTo(t.host);
        if(cmp != 0)
            return cmp;
        cmp = ip.compareTo(t.ip);
        return cmp;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if (getClass() != obj.getClass())
           return false;
        final UserInfo other = (UserInfo) obj;
        return  nick.equals(other.nick)  &&
               ident.equals(other.ident) &&
                host.equals(other.host)  &&
                  ip.equals(other.ip);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.nick != null ? this.nick.hashCode() : 0);
        hash = 89 * hash + (this.ident != null ? this.ident.hashCode() : 0);
        hash = 89 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 89 * hash + (this.ip != null ? this.ip.hashCode() : 0);
        return hash;
    }

}
