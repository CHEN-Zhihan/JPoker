import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by zhihan on 2/24/17.
 */
class Game {
    private User[] users;
    private HashSet<Integer> cards;
    private int target;
    private long startTime;
    private long endTime;
    Game(User[] u) {
        startTime = System.nanoTime();
        users = u;
        initCards();
    }

    private void initCards() {
        cards = new HashSet<>();
        Random rand = new Random();
        cards = new HashSet<>();
        while (cards.size() != 4) {
            int type = rand.nextInt(4) + 1;
            int value = rand.nextInt(13) + 1;
            int result = value + (value >= 10 ? type * 100 : type * 10);
            cards.add(result);
        }
        target = Calculator.getRandomResult(cards);
    }
    HashSet<Integer> getCards() {
        return cards;
    }

    int getTarget() {
        return target;
    }

    User[] getUsers() {
        return users;
    }

    double getDuration() {
        return ((double)(endTime - startTime)) / 1000000000;
    }

    void complete() {
        endTime = System.nanoTime();
    }
}
