import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhihan on 5/3/17.
 */
public abstract class ServerMessage implements Serializable {
    abstract void execute(Client c);
}


class EndMessage extends ServerMessage {
    private String winner;
    private String solution;
    private int gameID;
    EndMessage(String winner, String solution, int id) {
        this.winner = winner;
        this.solution = solution;
        this.gameID = id;
    }

    int getGameID() {
        return gameID;
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
    private ArrayList<Integer> cards;
    private ArrayList<User> users;
    private int game;

    StartMessage(ArrayList<Integer> cards, ArrayList<User> users, int id) {
        this.cards = cards;
        this.users = users;
        this.game = id;
    }

    void execute(Client c) {
        c.onStart(this);
    }

    int getGameID() {
        return game;
    }

    ArrayList<Integer> getCards() {
        return cards;
    }

    ArrayList<User> getUsers() {
        return users;
    }
}