import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.math.BigInteger;

public class Day11 {

    static class Monkey {
        Monkey(int id) {
            this.id = id;
            items = new LinkedList<Integer>();
        }
        int id;
        Queue<Integer> items;
        char operation;
        Integer left, right;
        int divisible;
        int trueMonkey, falseMonkey;
        int inspections;
    }

    static List<Monkey> monkeys;

    static int allDivisible;

    static void readFile(File file) throws FileNotFoundException {
        monkeys = new ArrayList<Monkey>();
        Monkey monkey = null;
        Scanner fs = new Scanner(file);
        allDivisible = 1;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            if (tokens.length == 1) { // next monkey
            } else if (tokens[0].equals("Monkey")) {
                int id = Integer.valueOf(tokens[1].split(":")[0]);
                monkey = new Monkey(id);
                monkeys.add(monkey);
            } else if (tokens[2].equals("Starting")) {
                for (int i = 4; i < tokens.length; i++) {
                    int item = Integer.valueOf(tokens[i].split(",")[0]);
                    monkey.items.offer(item);
                }
            } else if (tokens[2].equals("Operation:")) {
                monkey.operation = tokens[6].charAt(0);
                if (!tokens[5].equals("old")) {
                    monkey.left = Integer.valueOf(tokens[5]);
                }
                if (!tokens[7].equals("old")) {
                    monkey.right = Integer.valueOf(tokens[7]);
                }
            } else if (tokens[2].equals("Test:")) {
                monkey.divisible = Integer.valueOf(tokens[5]);
                allDivisible *= monkey.divisible;
            } else if (tokens[4].equals("If")) {
                if (tokens[5].equals("true:")) {
                    monkey.trueMonkey = Integer.valueOf(tokens[9]);
                } else {
                    monkey.falseMonkey = Integer.valueOf(tokens[9]);
                }
            }
        }
    }

    static void round(boolean partOne) {
        for (Monkey monkey : monkeys) {
//            System.out.println("Monkey id: " + monkey.id + ", items: " + monkey.items);
//            System.out.println("Operation: " + monkey.operation + " (" + monkey.left + ", " + monkey.right + ")");
//            System.out.println("Divisible: " + monkey.divisible);
//            System.out.println("If true: " + monkey.trueMonkey);
//            System.out.println("If false: " + monkey.falseMonkey);
//            System.out.println();
            monkey.inspections += monkey.items.size();
            while (!monkey.items.isEmpty()) {
                int item = monkey.items.poll();
                int left = monkey.left == null ? item : monkey.left;
                int right = monkey.right == null ? item : monkey.right;
                BigInteger leftBig = BigInteger.valueOf(left);
                BigInteger rightBig = BigInteger.valueOf(right);
                BigInteger levelBig = monkey.operation == '+' ? leftBig.add(rightBig) : leftBig.multiply(rightBig);
                int level = levelBig.remainder(BigInteger.valueOf(allDivisible)).intValue();
//                int level = monkey.operation == '+' ? left + right : left * right;
                if (partOne) {
                    level /= 3;
                }
                int next = level % monkey.divisible == 0 ? monkey.trueMonkey : monkey.falseMonkey;
                Monkey nextMonkey = monkeys.get(next);
                nextMonkey.items.offer((int)level);
            }
        }
    }

    static void showMonkeyItems() {
        for (Monkey monkey : monkeys) {
            System.out.println("Monkey id: " + monkey.id + ", inspections: " + monkey.inspections + ", items: " + monkey.items);
        }
    }

    static void play(File file, int rounds, boolean partOne) throws FileNotFoundException {
        readFile(file);
        if (partOne) {
            System.out.println("Product of all divisors: " + allDivisible);
        }
        for (int i = 0; i < rounds; i++) {
            round(partOne);
        }
        showMonkeyItems();
        monkeys.sort((m1, m2) -> m2.inspections - m1.inspections);
        BigInteger total = BigInteger.valueOf(monkeys.get(0).inspections).multiply(
                           BigInteger.valueOf(monkeys.get(1).inspections));
        System.out.println("Total inspections: " + total);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 11");
        File file = new File("data/day11.txt");
        play(file, 20, true);
        play(file, 10000, false);
    }
}
