package dreamhackbotpro;

public class ProxyFinder {
    
    private static ProxyFinder instance = null;

    private ProxyFinder(){
        //TODO: Create proxy checking thread
    }

    public static synchronized ProxyFinder getInstance(){
        if(instance==null)
            instance = new ProxyFinder();
        return instance;
    }

    public String getWorkingProxy(){
        return null;
    }


}
