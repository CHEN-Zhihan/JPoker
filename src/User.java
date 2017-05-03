import java.io.Serializable;

/**
 * Created by zhihan on 2/6/17.
 */
public class User implements Serializable, Comparable<User> {
    private int id;
    private String username;
    private int numGames;
    private int numWins;
    private double totalTime;
    private static final long serialVersionUID = 8367908553994431734L;
    private int validFlag;
    User(int id, String username) {
        this.id = id;
        this.username = username;
        numGames = 0;
        numWins = 0;
        totalTime = 0;
        validFlag = 0;
    }
    User(int validFlag) {
        this.validFlag = validFlag;
    }
    User (int id, String username, int numGames, int numWins, double totalTime) {
        this(id, username);
        this.numGames = numGames;
        this.numWins = numWins;
        this.totalTime = totalTime;
        this.validFlag = UserManager.VALID;
    }
    int getValidFlag() {return validFlag;}
    double getTotalTime() {
        return totalTime;
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