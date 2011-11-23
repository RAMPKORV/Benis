package dreamhackbotpro;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyFinder {

    private static ProxyFinder instance = null;
    private List<String> untested = new ArrayList<String>();
    private List<String> notWorking = new ArrayList<String>();
    private Stack<Proxy> working = new Stack<Proxy>();

    private ProxyFinder() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        //TODO: Find new potential proxies to test
                        boolean isWorking = false;
                        for (String proxyToTry : untested) {
                            String[] parts = proxyToTry.split(":");
                            String host = parts[0];
                            try {
                                int port = Integer.parseInt(parts[1]);
                                URL url = new URL(host + ":" + port);
                                Proxy toTry = null;
                                Proxy.Type[] proxyTypes = {Proxy.Type.HTTP, Proxy.Type.SOCKS, Proxy.Type.DIRECT};
                                for (Proxy.Type type : proxyTypes) {
                                    toTry = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                                    try {
                                        url.openConnection(toTry);
                                        isWorking = true;
                                        untested.remove(proxyToTry);
                                        working.add(toTry);
                                        break;
                                    } catch (IOException ex) {
                                    }
                                }
                                if(!isWorking) {
                                    notWorking.add(proxyToTry);
                                    untested.remove(proxyToTry);
                                }
                                Thread.sleep(1000);
                            } catch (MalformedURLException ex) {
                            } catch (NumberFormatException ex) {
                            }
                        }
                        if (untested.isEmpty()) {
                            Thread.sleep(1000);
                        }
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
