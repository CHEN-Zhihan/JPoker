import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by zhihan on 5/3/17.
 */
public class GameManager {
    private Game currentGame;
    private HashMap<Integer, Game> gameSet;
    private JMSServer jms;
    private int gameCounter;
    private Thread thread;
    private InfoManager manager;
    GameManager(int port, InfoManager manager) {
        try {
            jms = new JMSServer(port, this);
        } catch (JMSException | NamingException e) {
            System.err.println("[ERROR] Cannot setup JMS Server: " +e);
            System.exit(-1);
        }
        this.manager = manager;
        thread = new Thread(jms);
        thread.start();
    }

    void onMessage(ClientMessage m) {
        m.execute(this);
    }

    void onRequest(RequestMessage m) {
        ;
    }

    void onFinish(FinishedMessage m) {
        ;
    }
}
