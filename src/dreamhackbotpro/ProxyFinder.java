package dreamhackbotpro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyFinder {

    private static ProxyFinder instance = null;
    private List<String> untested = new ArrayList<String>();
    private List<String> notWorking = new ArrayList<String>();
    private Stack<Proxy> working = new Stack<Proxy>();
    private static final String[] proxyLists = {
        "http://www.textproxylists.com/proxy.php?allproxy"
    };
    private Pattern proxyPattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\:[0-9]{1,5}");

    private ProxyFinder() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        for (String proxyList : proxyLists) {
                            URL u;
                            InputStream is = null;
                            try {
                                u = new URL(proxyList);
                                is = u.openStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader bis = new BufferedReader(isr);
                                String s;
                                while ((s = bis.readLine()) != null) {
                                    Matcher m = proxyPattern.matcher(s);
                                    while (m.find()) {
                                        String match = m.group();
                                        if (!working.contains(match) && !notWorking.contains(match)) {
                                            untested.add(match);
                                        }
                                    }
                                }
                            } catch (MalformedURLException ex) {
                            } catch (IOException ex) {
                            } finally {
                                try {
                                    is.close();
                                } catch (IOException ex) {
                                }
                            }
                        }
                        boolean isWorking = false;
                        for (String proxyToTry : untested) {
                            String[] parts = proxyToTry.split(":");
                            if (parts.length < 2) {
                                continue;
                            } 
                            String host = parts[0];
                            try {
                                int port = Integer.parseInt(parts[1]);
                                URL url = new URL("irc://"+host+":"+port);
                                Proxy toTry = null;
                                Proxy.Type[] proxyTypes = {Proxy.Type.HTTP, Proxy.Type.SOCKS, Proxy.Type.DIRECT};
                                for (Proxy.Type type : proxyTypes) {
                                    toTry = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                                    try {
                                        url.openConnection(toTry);
                                        System.out.println("PROXY WORKED");
                                        isWorking = true;
                                        untested.remove(proxyToTry);
                                        working.add(toTry);
                                        break;
                                    } catch (IOException ex) {
                                        System.out.println("PROXY DID NOT WORK");
                                    }
                                }
                                if (!isWorking) {
                                    notWorking.add(proxyToTry);
                                    untested.remove(proxyToTry);
                                }
                                Thread.sleep(1000);
                            } catch (MalformedURLException ex) {
                                System.out.println("MalformedURLException");
                            } catch (NumberFormatException ex) {
                                System.out.println("NumberFormatException");
                            }
                        }
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }).start();
    }

    public static synchronized ProxyFinder getInstance() {
        if (instance == null) {
            instance = new ProxyFinder();
        }
        return instance;
    }

    public Proxy getWorkingProxy() {
        if (working.isEmpty()) {
            return null;
        }
        return working.pop();
    }
}
