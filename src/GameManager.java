import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.HashMap;

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
    GameManager(int port) {
        try {
            jms = new JMSServer(port, this);
        } catch (JMSException | NamingException e) {
            System.err.println("[ERROR] Cannot setup JMS Server: " +e);
            System.exit(-1);
        }
        thread = new Thread(jms);
        thread.start();
        gameSet = new HashMap<>();
    }

    void setInfoManager(InfoManager m) {
        manager = m;
    }
    void onMessage(ClientMessage m) {
        m.execute(this);
    }

    void onRequest(RequestMessage m) {
        System.out.println("Receive request!!!");
        if (currentGame == null) {
            currentGame = new Game(m.getSender(), gameCounter++, this);
        } else {
            currentGame.addUser(m.getSender());
            if (currentGame.isReady()) {
                start();
            }
        }
    }

    void start() {
        System.out.println("Starting game!!");
        jms.send(new StartMessage(currentGame));
        gameSet.put(currentGame.getID(), currentGame);
        currentGame = null;
    }

    void onFinish(FinishedMessage m) {
        if (gameSet.containsKey(m.getGameID())) {
            Game game = gameSet.get(m.getGameID());
            gameSet.remove(m.getGameID());
            game.complete();
            double time = game.getDuration();
            manager.update(m.getSender().getID(), time);
            for (User u:game.getUsers()) {
                if (u.getID() != m.getSender().getID()) {
                    manager.update(u.getID());
                }
            }
            jms.send(new EndMessage(m.getSender().getUsername(), m.getSolution()));
        }
    }
}
