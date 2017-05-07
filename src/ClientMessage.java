import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
 */
abstract class ClientMessage implements Serializable {
    final int id;
    ClientMessage(int i) {
        id = i;
    }
    abstract void execute(GameManager g);
}

class RequestMessage extends ClientMessage {
    RequestMessage(int i) {
        super(i);
    }

    /**
     *
     * @param g
     */
    void execute(GameManager g) {
        g.onRequest(this);
    }

    /**
     *
     * @return id
     */
    int getSenderID() {
        return id;
    }
}

class FinishedMessage extends ClientMessage {
    private final int game;
    private final String username;
    private final String solution;
    FinishedMessage(int sender, String username, int game, String solution) {
        super(sender);
        this.game = game;
        this.username = username;
        this.solution = solution;
    }

    /**
     *
     * @return gameID.
     */
    int getGameID() {
        return game;
    }

    /**
     *
     * @return senderID.
     */
    int getSenderID() {
        return id;
    }

    /**
     *
     * @return solution.
     */
    String getSolution() {
        return solution;
    }

    /**
     *
     * @return username;
     */
    String getUsername() {return username;}

    /**
     *
     * @param g
     */
    void execute(GameManager g) {
        g.onFinish(this);
    }
}


