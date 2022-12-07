import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Day05 {

    static List<LinkedList<Character>> stacks;

    static void readStacks(Scanner fs) {
        while (true) {
            String line = fs.nextLine();
            if (line.charAt(1) == '1') { // Just the stack numbers, don't process this line.
                fs.nextLine(); // Read a blank line after this line
                return; 
            }
            int stackIndex = 0;
            for (int i = 1; i < line.length(); i += 4, stackIndex++) {
                if (stacks.size() <= stackIndex) {
                    stacks.add(new LinkedList<Character>());
                }
                if (line.charAt(i) != ' ') {
                    LinkedList<Character> stack = stacks.get(stackIndex);
                    stack.addFirst(line.charAt(i));
                }
            }
        }
    }

    static void readFile(File file, boolean retainOrder) throws FileNotFoundException {
        stacks = new ArrayList<LinkedList<Character>>();
        Scanner fs = new Scanner(file);
        readStacks(fs);
        while (fs.hasNextLine()) {
            String line = fs.nextLine();
            String[] tokens = line.split(" ");
            int count = Integer.valueOf(tokens[1]);
            int from = Integer.valueOf(tokens[3]);
            int to = Integer.valueOf(tokens[5]);
            LinkedList<Character> fromStack = stacks.get(from - 1);
            LinkedList<Character> toStack = stacks.get(to - 1);
            LinkedList<Character> move = new LinkedList<Character>();
            for (int i = 0; i < count; i++) {
                if (retainOrder) {
                    move.addFirst(fromStack.removeLast());
                } else {
                    move.addLast(fromStack.removeLast());
                }
            }
            toStack.addAll(move);
        }
    }

    static String getStacksTop() {
        char[] top = new char[stacks.size()];
        for (int i = 0; i < top.length; i++) {
            top[i] = stacks.get(i).getLast();
        }
        return new String(top);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 5");
        File file = new File("data/day5.txt"); 
        readFile(file, false);
        System.out.println("Move one by one order: " + getStacksTop());
        readFile(file, true);
        System.out.println("Bulk move order: " + getStacksTop());
    }
}
