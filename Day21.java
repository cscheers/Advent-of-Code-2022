import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

public class Day21 {

    static class TreeNode {
        TreeNode(char operation, String left, String right) {
            this.operation = operation;
            this.left = left;
            this.right = right;
        }
        TreeNode(int value) {
            this.value = BigInteger.valueOf(value);
        }
        BigInteger value;
        char operation;
        String left, right;
    }
    static Map<String, TreeNode> monkeys = new HashMap<String, TreeNode>();


    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            String monkey = tokens[0].split(":")[0];
            System.out.print(monkey + ": ");
            if (tokens.length == 2) {
                int value = Integer.valueOf(tokens[1]);
                System.out.println(value);
                monkeys.put(monkey, new TreeNode(value));
            } else {
                char operation = tokens[2].charAt(0);
                String left = tokens[1];
                String right = tokens[3];
                System.out.println(left + " " + operation + " " + right);
                monkeys.put(monkey, new TreeNode(operation, left, right));
            }
            for (String token : tokens) {
//                int value = Integer.valueOf(token);
            }
        }
    }

    static BigInteger yell(String monkey) {
        TreeNode node = monkeys.get(monkey);
        if (node.value != null) {
            return node.value;
        }
        BigInteger left = yell(node.left);
        BigInteger right = yell(node.right);
        switch(node.operation) {
            case '+':
                return left.add(right);
            case '-':
                return left.subtract(right);
            case '*':
                return left.multiply(right);
            case '/':
                return left.divide(right);
        }
        return null;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 21");
        readFile(new File("data/day21.txt"));
        BigInteger y = yell("root");
        System.out.println("Root yells: " + y);
    }
}
