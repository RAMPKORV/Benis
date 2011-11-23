package dreamhackbotpro;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFinder {
    
    private static ProxyFinder instance = null;
    
    private List<String> untested = new ArrayList<String>();
    private List<String> notWorking = new ArrayList<String>();
    private List<Proxy> working = new ArrayList<Proxy>();

    private ProxyFinder(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                while(true) {
                    //TODO: Find new potential proxies to test
                    for(String proxyToTry: untested) {
                        //TODO: Test HTTP
                        //TODO: Test SOCKS4
                        //TODO: Test SOCKS5
                        //TODO: Test other proxies
                        Thread.sleep(1000);
                    }
                    if(untested.isEmpty()) {
                        Thread.sleep(1000);
                    }
                }
                } catch(InterruptedException ex) {
                    return;
                }
            }
        }).start();
    }

    public static synchronized ProxyFinder getInstance(){
        if(instance==null)
            instance = new ProxyFinder();
        return instance;
    }

    public Proxy getWorkingProxy(){
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("202.70.52.66", 1080));
    }


}
