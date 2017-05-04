import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhihan on 5/3/17.
 */
class GameManager {
    private Game currentGame;
    private final HashMap<Integer, Game> gameSet;
    private JMSServer jms;
    private int gameCounter;
    private InfoManager manager;
    GameManager(int port) {
        try {
            jms = new JMSServer(port, this);
        } catch (JMSException | NamingException e) {
            System.err.println("[ERROR] Cannot setup JMS Server: " +e);
            System.exit(-1);
        }
        Thread thread = new Thread(jms);
        thread.start();
        gameSet = new HashMap<>();
    }

    void setInfoManager(InfoManager m) {
        manager = m;
    }
    void onMessage(ClientMessage m) {
        m.execute(this);
    }

    void quit(int i) {
        if (currentGame != null) {
            currentGame.removeUser(i);
            if (currentGame.getUsers().size() == 0) {
                currentGame = null;
            }
        }
    }

    void onRequest(RequestMessage m) {
        System.out.println("Receive request!!!");
        User u = manager.getUser(m.getSenderID());
        if (currentGame == null) {
            currentGame = new Game(u, gameCounter++, this);
        } else {
            currentGame.addUser(u);
            if (currentGame.isReady()) {
                start();
            }
        }
    }

    void start() {
        if (currentGame != null) {
            System.out.println("Starting game!!");
            jms.send(new StartMessage(new ArrayList<>(currentGame.getCards()), currentGame.getUsers(), currentGame.getID()));
            currentGame.start();
            gameSet.put(currentGame.getID(), currentGame);
            currentGame = null;
        }
    }

    void onFinish(FinishedMessage m) {
        if (gameSet.containsKey(m.getGameID())) {
            Game game = gameSet.get(m.getGameID());
            gameSet.remove(m.getGameID());
            game.complete();
            double time = game.getDuration();
            manager.update(m.getSenderID(), time);
            for (User u:game.getUsers()) {
                if (u.getID() != m.getSenderID()) {
                    manager.update(u.getID());
                }
            }
            jms.send(new EndMessage(game.getUsers(), m.getUsername(), m.getSolution()));
        }
    }
}