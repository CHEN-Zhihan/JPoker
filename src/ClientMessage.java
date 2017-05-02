import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
 */
public abstract class ClientMessage implements Serializable {
    protected int sender;
    abstract void execute(GameManager g);
    int getSender() {
        return sender;
    }
}

class RequestMessage extends ClientMessage {
    RequestMessage(int sender) {
        this.sender = sender;
    }
    void execute(GameManager g) {
        g.onRequest(this);
    }
}

class FinishedMessage extends ClientMessage {
    private int game;
    FinishedMessage(int sender, int game) {
        this.sender = sender;
        this.game = game;
    }
    int getGameID() {
        return game;
    }

    void execute(GameManager g) {
        g.onFinish(this);
    }
}


