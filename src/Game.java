import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by zhihan on 2/24/17.
 */
class Game {
    private ArrayList<User> users;
    private HashSet<Integer> cards;
    private long startTime;
    private long endTime;
    private GameManager manager;
    private int id;
    Game(User u, int id, GameManager manager) {
        startTime = System.nanoTime();
        users = new ArrayList<>();
        users.add(u);
        prepareCards();
        this.manager = manager;
        this.id = id;
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

    int getID() {return id;}

    void complete() {
        endTime = System.nanoTime();
    }

    void addUser(User u) {
        users.add(u);
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
