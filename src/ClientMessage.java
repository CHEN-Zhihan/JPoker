import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
 */
abstract class ClientMessage implements Serializable {
    int id;
    ClientMessage(int i) {
        id = i;
    }
    abstract void execute(GameManager g);

}

class RequestMessage extends ClientMessage {
    RequestMessage(int i) {
        super(i);
    }
    void execute(GameManager g) {
        g.onRequest(this);
    }
    int getSenderID() {
        return id;
    }
}

class FinishedMessage extends ClientMessage {
    private int game;
    private String solution;
    FinishedMessage(int sender, int game, String solution) {
        super(sender);
        this.game = game;
        this.solution = solution;
    }
    int getGameID() {
        return game;
    }

    int getSenderID() {
        return id;
    }

    String getSolution() {
        return solution;
    }

    void execute(GameManager g) {
        g.onFinish(this);
    }
}


