import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhihan on 5/3/17.
 */
abstract class ServerMessage implements Serializable {
    private final ArrayList<User> users;

    abstract void execute(Client c);
    ServerMessage(ArrayList<User> users) {
        this.users = users;
    }
    ArrayList<User> getUsers() {
        return users;
    }

}


class EndMessage extends ServerMessage {
    private final String winner;
    private final String solution;
    EndMessage(ArrayList<User> users, String winner, String solution) {
        super(users);
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
    private final ArrayList<Integer> cards;
    private final int gameID;
    StartMessage(ArrayList<Integer> cards, ArrayList<User> users, int id) {
        super(users);
        this.cards = cards;
        gameID = id;
    }

    void execute(Client c) {
        c.onStart(this);
    }

    int getGameID() {
        return gameID;
    }

    ArrayList<Integer> getCards() {
        return cards;
    }
}