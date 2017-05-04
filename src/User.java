import java.io.Serializable;

/**
 * Created by zhihan on 2/6/17.
 */
public class User implements Serializable, Comparable<User> {
    private final int id;
    private final String username;
    private final int numGames;
    private final int numWins;
    private final double totalTime;
    private static final long serialVersionUID = 8367908553994431734L;
    User (int id, String username, int numGames, int numWins, double totalTime) {
        this.id = id;
        this.username = username;
        this.numGames = numGames;
        this.numWins = numWins;
        this.totalTime = totalTime;
    }

    int getNumGames() {
        return numGames;
    }

    int getNumWins() {
        return numWins;
    }

    double getAverageTime() {
        return numGames>0? totalTime / numGames :-1;
    }

    String getUsername() {return username; }
    public int compareTo(User another) {
        if (numWins < another.getNumWins()) {
            return 1;
        } else if (numWins == another.getNumWins()) {
            return 0;
        } else {
            return -1;
        }
    }
    int getID() {
        return id;
    }
}