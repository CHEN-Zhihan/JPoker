import com.sun.org.apache.regexp.internal.RE;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;

/**
 * Created by zhihan on 2/7/17.
 */
public class Server extends UnicastRemoteObject implements UserManager {

    private Connection connection;
    private HashMap<String, char[]> cache = new HashMap<>();
    Server() throws RemoteException {
        try {
            Class.forName("com.mysql.jdbc.driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost/COMP3402", "COMP3402", "password");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            System.err.println("[ERROR] cannot establish database connection " + e);
            System.exit(-1);
        }
    }
    public User login(String username, char[] password) throws RemoteException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM COMP3402 WHERE username = ?");
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                char[] passwordInDB = result.getString("password").toCharArray();
                if (!Arrays.equals(password, passwordInDB)) {
                    return new User(USER_INCORRECT_PASSWORD);
                }
                boolean hasLoggedIn = result.getBoolean("loggedIn");
                if (hasLoggedIn) {
                    return new User(USER_HAS_LOGGEDIN);
                }
                int numGames = result.getInt("numGames");
                int numWins = result.getInt("numWins");
                double totalTime = result.getDouble("totalTime");
                return new User(username, numGames, numWins, totalTime);
            }
            return new User(USER_NOT_EXIST);
        } catch (SQLException e) {
            System.err.println(e);
            return new User(DATABASE_ERROR);
        }
    }
    public int register(String username, char[] password) throws RemoteException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM COMP3402 WHERE username = ?");
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return HAS_REGISTERED;
            }
            stmt = connection.prepareStatement("INSERT INTO COMP3402 (username, password, numWins, numGames, totalTime, loggedIn) "+
                    "VALUES (?, ?, 0, 0, 0, FALSE)");
            stmt.setString(1, username);
            stmt.setString(2, password.toString());
            boolean insertResult = stmt.execute();
            if (insertResult) {
                return VALID;
            }
            return DATABASE_ERROR;
        } catch (SQLException e) {
            System.err.println(e);
            return DATABASE_ERROR;
        }
    }
    public boolean logout(String username) throws RemoteException {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE COMP3402 SET loggedIn = FALSE WHERE username = ?");
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }
    public int getRank(String username) throws RemoteException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT username, numWins, numGames, totalTime, ")
        }
    }
    public ArrayList<User> getAllUsers() throws RemoteException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT username, numWins, numGames, totalTime FROM COMP3402");
            String username = null;
            int numWins = 0;
            int numGames = 0;
            double totalTime = 0;
            ArrayList<User> result = new ArrayList<>();
            while (resultSet.next()) {
                username = resultSet.getString("username");
                numGames = resultSet.getInt("numGames");
                numWins = resultSet.getInt("numWins");
                totalTime = resultSet.getDouble("totalTime");
                result.add(new User(username, numGames, numWins, totalTime));
            }
            return result;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }
}
