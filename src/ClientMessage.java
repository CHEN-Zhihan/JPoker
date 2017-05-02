import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
 */
public abstract class ClientMessage implements Serializable {
    protected User sender;
    abstract void execute(GameManager g);
    User getSender() {
        return sender;
    }
}

class RequestMessage extends ClientMessage {
    RequestMessage(User sender) {
        this.sender = sender;
    }
    void execute(GameManager g) {
        g.onRequest(this);
    }
}

class FinishedMessage extends ClientMessage {
    private int game;
    private String solution;
    FinishedMessage(User sender, int game, String solution) {
        this.sender = sender;
        this.game = game;
        this.solution = solution;
    }
    int getGameID() {
        return game;
    }

    String getSolution() {
        return solution;
    }

    void execute(GameManager g) {
        g.onFinish(this);
    }
}


