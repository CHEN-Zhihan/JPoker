import java.io.Serializable;
import java.util.*;

/**
 * Created by zhihan on 2/24/17.
 */
class Game implements Serializable {
    private ArrayList<User> users;
    private HashSet<Integer> cards;
    private long startTime;
    private long endTime;
    private int id;
    private boolean ready;
    private transient Timer timer = new Timer();
    Game(User u, int id, GameManager manager) {
        startTime = System.nanoTime();
        users = new ArrayList<>();
        users.add(u);
        prepareCards();
        this.id = id;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("wake up!!!");
                ready = true;
                if (users.size() >= 2) {
                    manager.start();
                }
            }
        }, 1000);
    }


    HashSet<Integer> getCards() {
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

    private void prepareCards() {
        do {
            generateCards();
        } while (!Calculator.solvable(cards));
    }

    private void generateCards() {
        cards = new HashSet<>();
        Random rand = new Random();
        cards = new HashSet<>();
        HashSet<Integer> valueSet = new HashSet<>();
        int value = 0;
        int type = 0;
        int result = 0;
        while (cards.size() != 4) {
            type = rand.nextInt(4) + 1;
            do {
                value = rand.nextInt(13) + 1;
            } while (valueSet.contains(value));
            valueSet.add(value);
            result = value + (value >= 10 ? type * 100 : type * 10);
            cards.add(result);
        }
    }
}
