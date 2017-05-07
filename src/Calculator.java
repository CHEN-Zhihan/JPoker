import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

/**
 * Created by Zhihan CHEN on 4/24/17.
 * Calculator is a utility class with all methods static.
 */
final class Calculator {
    static final int ILLEGAL = -1;
    private static final HashMap<Character, Integer> map = new HashMap<>();
    private static final HashSet<Character> operator = new HashSet<>();
    private static final ArrayList<Character> ops = new ArrayList<>();
    private static ArrayList<Integer> seen;
    private static ArrayList<Integer> nums;
    private Calculator(){}
    static {
        map.put('A', 1);
        map.put('a', 1);
        map.put('J', 11);
        map.put('Q', 12);
        map.put('K', 13);
        map.put('j', 11);
        map.put('q', 12);
        map.put('k', 13);
        char[] operators = {'+', '-', '*', '/', '(', ')'};
        for (char i : operators) {
            operator.add(i);
        }
        ops.add('+');
        ops.add('-');
        ops.add('*');
        ops.add('/');
    }

    /**
     * Judge whether 4 cards can make it to 24.
     * @param cards
     * @return
     */
    static boolean solvable(ArrayList<Integer> cards) {
        nums = cardsToNums(cards);
        HashSet<ArrayList<Integer> > permuations = getPermutations(nums);
        HashSet<ArrayList<Character> > operatorPerm = getPermutations();
        for (ArrayList<Integer> n : permuations) {
            for (ArrayList<Character> op : operatorPerm) {
                if (solvable(n, op)) {
                    String sb = "((" +
                            n.get(3) +
                            op.get(0) +
                            n.get(2) +
                            ')' +
                            op.get(1) +
                            n.get(1) +
                            ')' +
                            op.get(2) +
                            n.get(0);
                    System.out.println(sb);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return whether all cards are used in expression.
     */
    static boolean allUsed() {
        Collections.sort(seen);
        Collections.sort(nums);
        return seen.equals(nums);
    }

    /**
     *
     * @param input string expression entered by user.
     * @param cards cards delivered by server.
     * @return the result of expression on success else ILLEGAL error number.
     */
    static int calculate(String input, ArrayList<Integer> cards) {
        nums = cardsToNums(cards);
        seen = new ArrayList<>();
        StringBuilder expression = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            if (Character.isDigit(input.charAt(i))) {
                int j = i + 1;
                int value;
                if (j != input.length() && Character.isDigit(input.charAt(j))) {
                    while (j != input.length() && Character.isDigit(input.charAt(j))) {
                        ++j;
                    }
                    value = Integer.parseInt(input.substring(i, j));
                    i = j - 1;
                } else {
                    value = Character.getNumericValue(input.charAt(i));
                }
                seen.add(value);
                if (!nums.contains(value)) {
                    return ILLEGAL;
                }
                expression.append(value);
            } else if (!map.containsKey(input.charAt(i)) && !operator.contains(input.charAt(i)) && input.charAt(i) != ' ') {
                return ILLEGAL;
            } else if (map.containsKey(input.charAt(i))) {
                if (!nums.contains(map.get(input.charAt(i)))) {
                    return ILLEGAL;
                }
                expression.append(map.get(input.charAt(i)));
                seen.add(map.get(input.charAt(i)));
            } else {
                expression.append(input.charAt(i));
            }
        }
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("JavaScript");
        try {
            if (engine.eval(expression.toString()) == null) {
                return ILLEGAL;
            }
            Double result;
            try {
                if (engine.eval(expression.toString()) instanceof Integer) {
                    result = ((Integer) engine.eval(expression.toString())).doubleValue();
                } else {
                    result = (Double)engine.eval(expression.toString());
                }
            } catch (ClassCastException e) {
                System.err.println("[ERROR] Error on casting result: " + e);
                e.printStackTrace();
                return ILLEGAL;
            }

            if (result != Math.floor(result)) {
                return ILLEGAL;
            }
            return result.intValue();
        } catch (ScriptException e) {
            return ILLEGAL;
        }
    }
    private static ArrayList<Integer> cardsToNums(ArrayList<Integer> cards) {
        ArrayList<Integer> nums = new ArrayList<>();
        for (Integer i : cards) {
            nums.add(i > 100 ? i % 100 : i % 10);
        }
        return nums;
    }

    private static boolean solvable(ArrayList<Integer> num, ArrayList<Character> ops) {
        Stack<Double> stack = new Stack<>();
        for (Integer i: num) {
            stack.push(i.doubleValue());
        }
        for (Character i : ops) {
            Double left = stack.pop();
            Double right = stack.pop();
            switch (i) {
                case '+': {
                    stack.push(left + right);
                    break;
                }case '-': {
                    stack.push(left - right);
                    break;
                }case '*': {
                    stack.push(left * right);
                    break;
                }case '/': {
                    if (right == 0) {
                        return false;
                    }
                    stack.push(left / right);
                    break;
                }
            }
        }
        return stack.peek() == Math.floor(stack.peek()) && stack.peek().intValue() == 24;
    }

    private static HashSet<ArrayList<Integer> > getPermutations(ArrayList<Integer> nums) {
        HashSet<ArrayList<Integer> > permutation = new HashSet<>();
        ArrayList<Integer> sub = new ArrayList<>();
        perm(sub, nums, permutation);
        return permutation;
    }

    private static HashSet<ArrayList<Character> > getPermutations() {
        HashSet<ArrayList<Character> > permutation = new HashSet<>();
        ArrayList<Character> sub = new ArrayList<>();
        permOpr(sub, ops, permutation);
        return permutation;
    }

    private static void perm(ArrayList<Integer> sub, ArrayList<Integer> a, HashSet<ArrayList<Integer> > set) {
        int L = a.size();
        if (L == 0) {
            set.add(sub);
        } else {
            for (int i = 0; i < L; i++) {
                ArrayList<Integer> ab = new ArrayList<>(sub);
                ab.add(a.get(i));
                ArrayList<Integer> bc = new ArrayList<>(a);
                bc.remove(i);
                perm(ab, bc, set);
            }
        }
    }

    private static void permOpr(ArrayList<Character> sub, ArrayList<Character> a, HashSet<ArrayList<Character> > set) {
        int L = a.size();
        if (L == 1) {
            set.add(sub);
        } else {
            for (int i = 0; i < L; i++) {
                ArrayList<Character> ab = new ArrayList<>(sub);
                ab.add(a.get(i));
                ArrayList<Character> bc = new ArrayList<>(a);
                bc.remove(i);
                permOpr(ab, bc, set);
            }
        }
    }
}