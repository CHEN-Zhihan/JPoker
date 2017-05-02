import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;

/**
 * Created by zhihan on 2/7/17.
 */
public class DBManager extends UnicastRemoteObject implements UserManager, InfoManager {

    private Connection connection;
    DBManager() throws RemoteException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost/COMP3402", "COMP3402", "password");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            System.err.println("[ERROR] cannot establish database connection " + e);
            e.printStackTrace();
            System.exit(-1);
        }
        System.setSecurityManager(new SecurityManager());
        try {
            Naming.rebind("userManager", this);
        } catch (MalformedURLException e) {
            System.err.println("[ERROR] cannot bind loginManager: " + e);
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
                return new User(result.getInt("id"), username, numGames, numWins, totalTime);
            }
            return new User(USER_NOT_EXIST);
        } catch (SQLException e) {
            System.err.println("Error login: " + e);
            return new User(DATABASE_ERROR);
        }
    }
    public int register(String username, char[] password) throws RemoteException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM COMP3402 WHERE username = ?");
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                System.out.println(username + " has registered.");
                return HAS_REGISTERED;
            }
            stmt = connection.prepareStatement("INSERT INTO COMP3402 (username, password, numWins, numGames, totalTime, loggedIn) "+
                    "VALUES (?, ?, 0, 0, 0, FALSE)");
            stmt.setString(1, username);
            stmt.setString(2, new String(password));
            boolean insertResult = stmt.execute();
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery("SELECT COUNT(*)");
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error register: " + e);
            return DATABASE_ERROR;
        }
    }
    public void logout(String username) throws RemoteException {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE COMP3402 SET loggedIn = FALSE WHERE username = ?");
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logout: " + e);
        }
    }
    public int getRank(String username) throws RemoteException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(numWins) FROM COMP3402 WHERE numWins < (SELECT numWins FROM COMP3402 WHERE username = ?)");
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException e) {
            System.err.println("Error getRank: " + e);
            return -1;
        }
    }
    public ArrayList<User> getAllUsers() throws RemoteException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT id, username, numWins, numGames, totalTime FROM COMP3402");
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
                result.add(new User(resultSet.getInt("id"), username, numGames, numWins, totalTime));
            }
            return result;
        } catch (SQLException e) {
            System.err.println("Error getAllUsers: " + e);
            return null;
        }
    }

    public User getUser(int i) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, username, numWins, numGames, totalTime FROM COMP3402 WHERE id = ?");
            stmt.setInt(1, i);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getInt("numGames"),
                    resultSet.getInt("numWins"), resultSet.getDouble("totalTime"));
        } catch (SQLException e) {
            System.err.println("[ERROR] Cannot get user with index: " + i + " " + e);
            return null;
        }
    }

    public void update(int i, double time) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE COMP3402 SET" +
                    " numWins = numWins + 1, numGames = numGames + 1, totalTime = totalTime + ? WHERE id = ?");
            stmt.setInt(2,i);
            stmt.setDouble(1,time);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Cannot update user with index: " + i + " " + e);
        }
    }
    public void update(int i) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE COMP3402 SET numGames = numGames + 1 WHERE id = ?");
            stmt.setInt(1, i);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Cannot update user with index: " + i + " " + e);
        }
    }
}
