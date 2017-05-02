import java.io.Serializable;

/**
 * Created by zhihan on 5/3/17.
 */
public abstract class ServerMessage implements Serializable {
    abstract void execute(Client c);
}


class EndMessage extends ServerMessage {
    private String winner;
    private String solution;
    EndMessage(String winner, String solution) {
        this.winner = winner;
        this.solution = solution;
    }

    String getWinner() {
        return winner;
    }

    String getSolution() {
        return solution;
    }
    void execute(Client c) {
        c.onEnd(this);
    }
}

class StartMessage extends ServerMessage {
    private Game game;

    StartMessage(Game g) {
        game = g;
    }

    void execute(Client c) {
        c.onStart(this);
    }

    Game getGame() {
        return game;
    }

}