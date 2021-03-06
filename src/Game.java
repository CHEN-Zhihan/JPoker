import java.io.Serializable;
import java.util.*;

/**
 * Created by zhihan on 2/24/17.
 * Abstraction of actual game.
 */
class Game implements Serializable {
    private final ArrayList<User> users;
    private ArrayList<Integer> cards;
    private long startTime;
    private long endTime;
    private final int id;
    private boolean ready;
    private static int counter;
    /**
     *Setup Timer. If 10 seconds passed and can start, call the GameManager to start.
     * @param u the id of first user requesting a new game.
     * @param manager GameManager
     */
    Game(User u, GameManager manager) {
        startTime = System.nanoTime();
        users = new ArrayList<>();
        users.add(u);
        prepareCards();
        this.id = counter++;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ready = true;
                System.out.println("[INFO] Wake up with " + users.size() + " manager can begin: " + manager.canBegin());
                if (users.size() >= 2 && manager.canBegin()) {
                    manager.start();
                }
            }
        }, 10000);
    }


    ArrayList<Integer> getCards() {
        return cards;
    }

    ArrayList<User> getUsers() {
        return users;
    }

    double getDuration() {
        return ((double)(endTime - startTime)) / 1000000000;
    }

    boolean isReady() {
        return ready;
    }

    int getID() {return id;}

    void complete() {
        endTime = System.nanoTime();
    }

    void addUser(User u) {
        users.add(u);
        System.out.println(users.size());
        if (users.size() == 4) {
            ready = true;
        }
    }

    void start() {
        startTime = System.nanoTime();
    }

    void removeUser(int id) {
        for (int i = 0; i != users.size(); ++i) {
            if (users.get(i).getID() == id) {
                users.remove(i);
                break;
            }
        }
    }

    private void prepareCards() {
        do {
            generateCards();
        } while (!Calculator.solvable(cards));
    }

    private void generateCards() {
        Random rand = new Random();
        HashSet<Integer> set = new HashSet<>();
        HashSet<Integer> valueSet = new HashSet<>();
        int value;
        int type;
        int result;
        while (set.size() != 4) {
            type = rand.nextInt(4) + 1;
            do {
                value = rand.nextInt(13) + 1;
            } while (valueSet.contains(value));
            valueSet.add(value);
            result = value + (value >= 10 ? type * 100 : type * 10);
            set.add(result);
        }
        cards = new ArrayList<>(set);
    }
}
