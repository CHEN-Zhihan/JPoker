import java.io.Serializable;
import java.util.*;

/**
 * Created by zhihan on 2/24/17.
 */
class Game implements Serializable {
    private final ArrayList<User> users;
    private ArrayList<Integer> cards;
    private long startTime;
    private long endTime;
    private final int id;
    private boolean ready;
    Game(User u, int id, GameManager manager) {
        startTime = System.nanoTime();
        users = new ArrayList<>();
        users.add(u);
        prepareCards();
        this.id = id;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("wake up!!!");
                ready = true;
                if (users.size() >= 2) {
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
