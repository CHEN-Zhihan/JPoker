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
    private final HashMap<Integer, Game> playerGame;
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
        playerGame = new HashMap<>();
    }

    void setInfoManager(InfoManager m) {
        manager = m;
    }
    void onMessage(ClientMessage m) {
        m.execute(this);
    }

    void quit(int i) {
        System.out.println("[INFO] " + i + " quit");
        Game g = playerGame.get(i);
        if (g != null) {
            g.removeUser(i);
            if (currentGame == g) {
                if (currentGame.getUsers().size() == 0) {
                    currentGame = null;
                }
            } else if (g.getUsers().size() == 0) {
                gameSet.remove(g.getID());
            }
            playerGame.remove(i);
        }
    }

    boolean canBegin() {
        return currentGame != null;
    }

    void onRequest(RequestMessage m) {
        System.out.println("[INFO] Received game request from " + m.getSenderID());
        User u = manager.getUser(m.getSenderID());
        boolean canStart = false;
        if (currentGame == null) {
            currentGame = new Game(u, gameCounter++, this);
        } else {
            currentGame.addUser(u);
            canStart = currentGame.isReady();
        }
        playerGame.put(m.getSenderID(), currentGame);
        System.out.println("[INFO] Add " + m.getSenderID() + " to " + currentGame.getID());
        if (canStart) {
            start();
        }
    }

    void start() {
        System.out.println("[INFO] Game " + currentGame.getID() + " start");
        jms.send(new StartMessage(new ArrayList<>(currentGame.getCards()), currentGame.getUsers(), currentGame.getID()));
        currentGame.start();
        gameSet.put(currentGame.getID(), currentGame);
        currentGame = null;
    }

    void onFinish(FinishedMessage m) {
        if (gameSet.containsKey(m.getGameID())) {
            Game game = gameSet.get(m.getGameID());
            for (User u:game.getUsers()) {
                playerGame.remove(u.getID());
            }
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

    void shutdown() {
        jms.shutdown();
    }
}