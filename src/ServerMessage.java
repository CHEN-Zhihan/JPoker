import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhihan on 5/3/17.
 */
abstract class ServerMessage implements Serializable {
    private ArrayList<User> users;

    abstract void execute(Client c);
    ServerMessage(ArrayList<User> users) {
        this.users = users;
    }
    ArrayList<User> getUsers() {
        return users;
    }

}


class EndMessage extends ServerMessage {
    private String winner;
    private String solution;
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
    private ArrayList<Integer> cards;
    private int gameID;
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