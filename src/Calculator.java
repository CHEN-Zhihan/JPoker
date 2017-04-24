import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.function.DoubleToIntFunction;

/**
 * Created by zhihan on 4/24/17.
 */
class Calculator {
    public static final int ILLEGAL = -1;
    private static HashMap<Character, Integer> map = new HashMap<>();
    private static HashSet<Character> operator = new HashSet<>();
    private static ArrayList<Integer> seen;
    private static ArrayList<Integer> nums;
    static {
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
    }
    static int getRandomResult(HashSet<Integer> cards) {
        System.out.println("Calling getRandomResult");

        nums = cardsToNums(cards);
        Random rand = new Random();
        Double result = 0.0;
        Stack<Double> stack = new Stack<>();
        for (Integer i: nums) {
            stack.push(i.doubleValue());
        }
        for (int i = 0; i != 3; ++i) {
            int seed = rand.nextInt(4) + 1;
            Double left = stack.pop();
            Double right = stack.pop();
            switch (seed) {
                case 1: {
                    System.out.println(left + " + " + right);
                    stack.push(left + right);
                    break;
                }case 2: {
                    System.out.println(left + " - " + right);
                    stack.push(left - right);
                    break;
                }case 3: {
                    System.out.println(left + " * " + right);
                    stack.push(left * right);
                    break;
                }case 4: {
                    if (right == 0) {
                        return getRandomResult(cards);
                    }
                    System.out.println(left + " / " + right);
                    stack.push(left / right);
                }
            }
        }
        if (stack.peek() == Math.floor(stack.peek()) && stack.peek() > 0) {
            return stack.peek().intValue();
        }

        return getRandomResult(cards);
    }

    private static ArrayList<Integer> cardsToNums(Set<Integer> cards) {
        ArrayList<Integer> nums = new ArrayList<>();
        for (Integer i : cards) {
            nums.add(i > 100 ? i % 100 : i % 10);
        }
        return nums;
    }

    static int calculate(String input, Set<Integer> cards) {
        ArrayList<Integer> nums = cardsToNums(cards);
        seen = new ArrayList<>();
        StringBuilder expression = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            if (Character.isDigit(input.charAt(i))) {
                int j = i + 1;
                int value = 0;
                if (j != input.length() && Character.isDigit(input.charAt(j))) {
                    if (input.charAt(j) != '0') {
                        return ILLEGAL;
                    } else {
                        value = 10;
                    }
                } else {
                    value = Character.getNumericValue(input.charAt(i));
                }
                seen.add(value);
                if (!nums.contains(value)) {
                    return ILLEGAL;
                }
                expression.append(value);
            } else if (!map.containsKey(input.charAt(i)) && !operator.contains(input.charAt(i))) {
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
            Double result = null;
            try {
                if (engine.eval(expression.toString()) instanceof Integer) {
                    result = ((Integer) engine.eval(expression.toString())).doubleValue();
                } else {
                    result = (Double)engine.eval(expression.toString());
                }
            } catch (ClassCastException e) {
                System.err.println(e);
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

    static boolean allUsed() {
        Collections.sort(seen);
        Collections.sort(nums);
        return seen.equals(nums);
    }
}