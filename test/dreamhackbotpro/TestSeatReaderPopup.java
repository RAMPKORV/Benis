package dreamhackbotpro;

/**
 *
 * @author wasd
 */
public class TestSeatReaderPopup {
    
    public static void main(String[] args) throws Exception{
        SeatReader sr = new SeatReader();
        Options.getInstance().setSeatPopupEnabled(true);
        sr.alarm(new Message(null, null));
        Thread.sleep(6000);
        System.exit(0);
    }
}
