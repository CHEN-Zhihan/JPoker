import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by zhihan on 2/6/17.
 */
public abstract class User implements Serializable, Comparable<User>{
    private String username;
    private int numGames;
    private int numWins;
    private double totalTime;
    private static final long serialVersionUID = 8367908553994431734L;
    User(String username) {
        this.username = username;
        numGames = 0;
        numWins = 0;
        totalTime = 0;
    }

    User(User another) {
        this.numGames = another.getNumGames();
        this.numWins = another.getNumWins();
        this.totalTime = another.getTotalTime();
        this.username = another.getUsername();
    }


    double getTotalTime() {
        return this.totalTime;
    }

    int getNumGames() {
        return numGames;
    }

    int getNumWins() {
        return numWins;
    }

    double getAverageTime() {
        return totalTime / numGames;
    }

    String getUsername() {return username; }


    public int compareTo(User another) {
        if (numWins < another.getNumWins()) {
            return -1;
        } else if (numWins == another.getNumWins()) {
            return 0;
        } else {
            return 1;
        }
    }

}

class CorrectUser extends User {
    CorrectUser(String name) {
        super(name);
    }
    CorrectUser(CorrectUser another) {
        super(another);
    }
}

class NotExistUser extends User{
    NotExistUser() {
        super("NotExist");
    }
}

class IncorrectUser extends User {
    IncorrectUser() {
        super("Incorrect");
    }
}

class LoggedInUser extends User {
    LoggedInUser() {
        super("LoggedIn");
    }
}

class ExistUser extends User {
    ExistUser() {
        super("Exist");
    }
}