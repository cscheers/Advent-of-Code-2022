import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day03 {

    static int getItemOffset(char c) {
        return Character.isLowerCase(c) ? c - 'a' : c - 'A' + 26;
    }

    static void markFirst(String items, int to, boolean[] used) {
        for (int i = 0; i < to; i++) {
            used[getItemOffset(items.charAt(i))] = true;
        }
    }

    static int checkSecond(String items, boolean[] used) {
        for (int i = items.length() / 2; i < items.length(); i++) {
            int offset = getItemOffset(items.charAt(i));
            if (used[offset]) {
                return offset + 1;
            }
        }
        return 0;
    }

    static int getPriority(String items) {
        boolean[] used = new boolean[52];
        markFirst(items, items.length() / 2, used);
        return checkSecond(items, used);
    }

    static void intersectSecond(String items, boolean[] used, boolean[] intersection) {
        for (int i = 0; i < items.length(); i++) {
            int offset = getItemOffset(items.charAt(i));
            if (used[offset]) {
                intersection[offset] = true;
            }
        }
    }

    static int intersectThird(String items, boolean[] intersection) {
        for (int i = 0; i < items.length(); i++) {
            int offset = getItemOffset(items.charAt(i));
            if (intersection[offset]) {
                return offset + 1;
            }
        }
        return 0;
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int totalOne = 0, totalTwo = 0;
        boolean[] used = null, intersection = null;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            totalOne += getPriority(line);
            if (used == null) {
                used = new boolean[52];
                markFirst(line, line.length(), used);
            } else if (intersection == null) {
                intersection = new boolean[52];
                intersectSecond(line, used, intersection);
            } else {
                totalTwo += intersectThird(line, intersection);
                used = null;
                intersection = null;
            }
        }
        System.out.println("Total priorities part one: " + totalOne);
        System.out.println("Total priorities part two: " + totalTwo);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 3");
        readFile(new File("data/day3.txt"));
    }
}
