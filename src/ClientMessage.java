import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
 */
public abstract class ClientMessage implements Serializable {
    abstract void execute(GameManager g);

}

class RequestMessage extends ClientMessage {
    private User sender;
    RequestMessage(User sender) {
        this.sender = sender;
    }
    void execute(GameManager g) {
        g.onRequest(this);
    }
    User getSender() {
        return sender;
    }
}

class FinishedMessage extends ClientMessage {
    private int game;
    private String solution;
    private int senderID;
    private String name;
    FinishedMessage(int sender, String name, int game, String solution) {
        this.senderID = sender;
        this.game = game;
        this.solution = solution;
        this.name = name;
    }
    int getGameID() {
        return game;
    }

    int getSenderID() {
        return senderID;
    }

    String getSolution() {
        return solution;
    }
    String getName() {
        return name;
    }

    void execute(GameManager g) {
        g.onFinish(this);
    }
}


